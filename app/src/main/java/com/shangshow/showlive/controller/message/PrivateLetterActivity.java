package com.shangshow.showlive.controller.message;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.netease.nim.demo.avchat.AVChatProfile;
import com.netease.nim.demo.avchat.activity.AVChatActivity;
import com.netease.nim.demo.session.SessionHelper;
import com.netease.nim.demo.team.TeamCreateHelper;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.contact_selector.activity.ContactSelectActivity;
import com.netease.nim.demo.main.model.Extras;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 */
public class PrivateLetterActivity extends BaseActivity implements OnClickListener {
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;
    public static String TAG = PrivateLetterActivity.class.getSimpleName();
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;

    private PtrFrameLayout privateLetterPtrFramentlayout;
    private RecyclerView privateLetterRecyclerView;
    private PrivateLetterRecyclerAdapter privateLetterRecyclerAdapter;
    private List<PrivateLetter> privateLetterList;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_message;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        onParseIntent();

        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {
                DialogMaker.dismissProgressDialog();
            }
        });

        Log.i(TAG, "sync completed = " + syncCompleted);
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(PrivateLetterActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.message);
        titleBarView.initRight("好友", 0, new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        privateLetterPtrFramentlayout = (PtrHTFrameLayout) findViewById(R.id.my_message_ptr_framelayout);
        CommonUtil.SetPtrRefreshConfig(PrivateLetterActivity.this, privateLetterPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        privateLetterPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getPrivateLetterMessages(MConstants.DATA_4_REFRESH);
            }
        });
        privateLetterRecyclerView = (RecyclerView) findViewById(R.id.my_message_recyclerView);
        privateLetterRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(PrivateLetterActivity.this)
                .color(getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        privateLetterRecyclerView.setLayoutManager(new LinearLayoutManager(PrivateLetterActivity.this, LinearLayout.VERTICAL, false));

        privateLetterRecyclerAdapter = new PrivateLetterRecyclerAdapter(PrivateLetterActivity.this, privateLetterList, R.layout.item_recycler_private_letter);
        privateLetterRecyclerView.setAdapter(privateLetterRecyclerAdapter);

    }

    private void getPrivateLetterMessages(int data4Refresh) {

    }

    @Override
    protected void bindEven() {
    }

    @Override
    protected void setView() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

    }

    private void onParseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            switch (message.getSessionType()) {
                case P2P:
                    SessionHelper.startP2PSession(this, message.getSessionId());
                    break;
                case Team:
                    SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
                default:
                    break;
            }
        } /*else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
            return;
        }*/ else if (intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
            if (AVChatProfile.getInstance().isAVChatting()) {
                Intent localIntent = new Intent();
                localIntent.setClass(this, AVChatActivity.class);
                startActivity(localIntent);
            }
        } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P)) {
            Intent data = intent.getParcelableExtra(Extras.EXTRA_DATA);
            String account = data.getStringExtra(Extras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    TeamCreateHelper.createNormalTeam(PrivateLetterActivity.this, selected, false, null);
                } else {
                    Toast.makeText(PrivateLetterActivity.this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                TeamCreateHelper.createAdvancedTeam(PrivateLetterActivity.this, selected);
            }
        }

    }
}
