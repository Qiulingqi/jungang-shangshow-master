package com.shangshow.showlive.controller.liveshow.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.netease.nim.uikit.model.UserInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.DateUtils;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.controller.adapter.MessageAdapter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shangshow.showlive.widget.SwipeListView;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taolong on 16/8/15.
 */
public class PrivateLetterWidget extends LinearLayout implements View.OnClickListener {

    private UserModel userModel;
    private View rootView;
    private PtrHTFrameLayout privateLetterPtrFramentlayout;
    private SwipeListView privateLetterRecyclerView;
//    private PrivateLetterRecyclerAdapter privateLetterRecyclerAdapter;
    private MessageAdapter messageAdapter;
    private Context mContext;
    private LiveWidgetOperate.LiveAudienceInterface mLiveAudienceInterface;
//    private String userType;
    private long currPage = 1;
    private List<PrivateLetter> privateLetters;

    public PrivateLetterWidget(Context context, LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface,List<PrivateLetter> privateLetterList) {
        super(context);
        mContext = context;
        mLiveAudienceInterface = liveAudienceInterface;
        this.privateLetters = privateLetterList;
        userModel = new UserModel(context);
        initView();
    }

    public PrivateLetterWidget(Context context, AttributeSet attrs, LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface,List<PrivateLetter> privateLetterList) {
        super(context, attrs);
        mContext = context;
        mLiveAudienceInterface = liveAudienceInterface;
        this.privateLetters = privateLetterList;
        userModel = new UserModel(context);
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_live_audience_private_letter_viewpager, this);

        privateLetterPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.private_letter_ptr_framentlayout);
        CommonUtil.SetPtrRefreshConfig(mContext, privateLetterPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        privateLetterPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getPrivateLetterMessages();
            }
        });

        privateLetterRecyclerView = (SwipeListView) findViewById(R.id.private_letter_recycler);
        privateLetterRecyclerView.setRightViewWidth(80);
        messageAdapter = new MessageAdapter(privateLetterRecyclerView.getRightViewWidth(), getContext(), privateLetters, R.layout.message_layout_item);
        privateLetterRecyclerView.setAdapter(messageAdapter);

        messageAdapter.setOnItemListener(new MessageAdapter.OnItemListener() {
            @Override
            public void delete(int position) {

            }

            @Override
            public void onItemClick(int position) {
                UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
                UserInfo userInfo = privateLetters.get(position).getUserInfo();
                if(currentUser.userId != userInfo.userId){
                    mLiveAudienceInterface.onClickReplyPrivateLetter(userInfo);
                }else{
                    userModel.getUserInfo(Long.parseLong(privateLetters.get(position).getContactId()), new Callback<UserInfo>() {
                        @Override
                        public void onSuccess(UserInfo info) {
                            mLiveAudienceInterface.onClickReplyPrivateLetter(info);
                        }

                        @Override
                        public void onFailure(int resultCode, String message) {

                        }
                    }, false);
                }
            }
        });


    }

    private void getPrivateLetterMessages() {
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, final List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）
                        if(recents != null){
                            privateLetters.clear();
                            messageAdapter.clear();
                            int i = 0;
                            for (final RecentContact recentContact : recents) {
                                final String contactId = recentContact.getContactId();
                                final String account = recentContact.getFromAccount();
                                final String message = recentContact.getContent();
                                final String time = DateUtils.formatDisplayTime(recentContact.getTime());
                                final int j = i;
                                userModel.getUserInfo(Long.parseLong(account), new Callback<UserInfo>() {
                                    @Override
                                    public void onSuccess(UserInfo userInfo) {
                                        PrivateLetter privateLetter = new PrivateLetter();
                                        privateLetter.setMessage(message);
                                        privateLetter.setTime(time);
                                        privateLetter.setContactId(contactId);
                                        privateLetter.setUserInfo(userInfo);
                                        privateLetters.add(privateLetter);
                                        if(j == recents.size() - 1){
                                            privateLetterPtrFramentlayout.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messageAdapter.addAll(privateLetters);
                                                }
                                            }, MConstants.DELAYED);
                                        }
                                    }

                                    @Override
                                    public void onFailure(int resultCode, String message) {

                                    }
                                }, false);
                                i++;
                            }
                        }
                        privateLetterPtrFramentlayout.refreshComplete();
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }

    public void update(){
        privateLetterPtrFramentlayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                privateLetterPtrFramentlayout.autoRefresh();
            }
        }, MConstants.DELAYED);
    }
}
