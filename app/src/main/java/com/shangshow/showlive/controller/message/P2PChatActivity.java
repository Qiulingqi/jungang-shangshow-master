package com.shangshow.showlive.controller.message;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nim.uikit.model.UserInfo;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.AndroidBug5497Workaround;
import com.shangshow.showlive.common.utils.DateUtils;
import com.shangshow.showlive.common.utils.MessageUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.controller.adapter.P2PChatAdapter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.PrivateLetter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 聊天直播间
 * 个人与个人聊天
 */
public class P2PChatActivity extends BaseActivity implements View.OnClickListener {

    ListView rv_p2p_chat_data;
    private EditText et_p2p_chat_content;
    private BaseButton bb_p2p_chat_send;
    private View rl_left_back;
    private TextView tv_chat_title;

    private UserModel userModel;
    private List<PrivateLetter> privateLetters = new ArrayList<PrivateLetter>();
    private P2PChatAdapter p2PChatAdapter;
    private UserInfo currentUser;
    private UserInfo userInfo;
    private String contactId;
    private IMMessage imMessage;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_p2_pchat;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        userModel = new UserModel(this);
        currentUser = CacheCenter.getInstance().getCurrUser();
        contactId = getIntent().getStringExtra("contactId");
        imMessage = MessageBuilder.createEmptyMessage(contactId, SessionTypeEnum.P2P, 0);
        //   无线懵逼。。。。。。。。。。。。。
        AndroidBug5497Workaround.assistActivity(this, new AndroidBug5497Workaround.OnCallBack() {
                                    @Override
                                    public void callBack() {
                                        rv_p2p_chat_data.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                rv_p2p_chat_data.setSelection(rv_p2p_chat_data.getBottom());
                                            }
                }, MConstants.DELAYED);
            }
        });
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        rv_p2p_chat_data = (ListView) findViewById(R.id.rv_p2p_chat_data);
        et_p2p_chat_content = (EditText) findViewById(R.id.et_p2p_chat_content);
        bb_p2p_chat_send = (BaseButton) findViewById(R.id.bb_p2p_chat_send);
        rl_left_back = findViewById(R.id.rl_left_back);
        tv_chat_title = (TextView) findViewById(R.id.tv_chat_title);

        titleBarView.setVisibility(View.GONE);

        //  准备数据
        loadData();
    }

    private void loadData() {
        userModel.getUserInfo(Long.parseLong(contactId), new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {

                P2PChatActivity.this.userInfo = userInfo;
                tv_chat_title.setText(userInfo.userName + "");
                p2PChatAdapter = new P2PChatAdapter(P2PChatActivity.this, privateLetters, currentUser, userInfo);
                rv_p2p_chat_data.setAdapter(p2PChatAdapter);
                MessageUtils.pullMessageHistory(imMessage, new RequestCallbackWrapper<List<IMMessage>>() {
                    @Override
                    public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {
                        if (imMessages != null) {
                            Collections.reverse(imMessages);
                            for (IMMessage imMessage : imMessages) {
                                PrivateLetter privateLetter = new PrivateLetter();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String date = format.format(new Date(imMessage.getTime()));
                                privateLetter.setTime(DateUtils.formatDisplayTime(date, true));
                                privateLetter.setContactId(imMessage.getSessionId());
                                privateLetter.setAccount(imMessage.getFromAccount());
                                privateLetter.setMessage(imMessage.getContent());
                                privateLetters.add(privateLetter);
                            }
                            p2PChatAdapter.addAll(privateLetters);
                            rv_p2p_chat_data.setSelection(rv_p2p_chat_data.getBottom());
                        }
                    }
                });
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        }, false);
    }

    @Override
    protected void bindEven() {
        bb_p2p_chat_send.setOnClickListener(this);
        rl_left_back.setOnClickListener(this);
        MessageUtils.incomingMessage(new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                if (imMessages != null) {
                    for (IMMessage imMessage : imMessages) {
                        PrivateLetter privateLetter = new PrivateLetter();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = format.format(new Date(imMessage.getTime()));
                        privateLetter.setTime(DateUtils.formatDisplayTime(date, true));
                        privateLetter.setContactId(imMessage.getSessionId());
                        privateLetter.setAccount(imMessage.getFromAccount());
                        privateLetter.setMessage(imMessage.getContent());
                        privateLetters.add(privateLetter);
                        p2PChatAdapter.add(privateLetter);
                    }
                    rv_p2p_chat_data.setSelection(rv_p2p_chat_data.getBottom());
                }
            }
        });
        et_p2p_chat_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(editable.toString())){
                    bb_p2p_chat_send.setEnabled(true);
                }else{
                    bb_p2p_chat_send.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_left_back:{
                finish();
            }
            break;
            case R.id.bb_p2p_chat_send: {
                if(userInfo == null){
                    return;
                }
                //  发送消息
                String content = et_p2p_chat_content.getText().toString();
                MessageUtils.sendMessgeP2p(contactId, content);
                et_p2p_chat_content.setText("");
                PrivateLetter privateLetter = new PrivateLetter();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = format.format(new Date());
                privateLetter.setTime(DateUtils.formatDisplayTime(date, true));
                privateLetter.setContactId(userInfo.userId + "");
                privateLetter.setAccount(currentUser.userId + "");
                privateLetter.setMessage(content);
                privateLetters.add(privateLetter);
                p2PChatAdapter.add(privateLetter);
                rv_p2p_chat_data.setSelection(rv_p2p_chat_data.getBottom());
            }
            break;
        }
    }

}
