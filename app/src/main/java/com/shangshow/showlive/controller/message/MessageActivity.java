package com.shangshow.showlive.controller.message;

import android.content.Intent;
import android.view.View;

import com.netease.nim.uikit.model.UserInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.DateUtils;
import com.shangshow.showlive.common.utils.MessageUtils;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.controller.adapter.MessageAdapter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shangshow.showlive.widget.SwipeListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Super-me on 2016/10/27.
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener {

    private UserModel userModel;
    private PtrHTFrameLayout privateLetterPtrFramentlayout;
    private SwipeListView privateLetterRecyclerView;
    private MessageAdapter messageAdapter;
    private List<PrivateLetter> privateLetters = new ArrayList<PrivateLetter>();
    private UserInfo currentUser;
    private List<RecentContact> recents1;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_message;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        currentUser = CacheCenter.getInstance().getCurrUser();
    }

    @Override

    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.message);
        privateLetterPtrFramentlayout = (PtrHTFrameLayout) findViewById(R.id.my_message_ptr_framelayout);
        CommonUtil.SetPtrRefreshConfig(MessageActivity.this, privateLetterPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        privateLetterPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                recentlyMessage();
            }
        });
        privateLetterRecyclerView = (SwipeListView) findViewById(R.id.my_message_recyclerView);
        privateLetterRecyclerView.setRightViewWidth(80);
        messageAdapter = new MessageAdapter(privateLetterRecyclerView.getRightViewWidth(), this, privateLetters, R.layout.message_layout_item);
        privateLetterRecyclerView.setAdapter(messageAdapter);

        receiverMessage();
    }

    private void receiverMessage() {

        MessageUtils.incomingMessage(new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                if (imMessages != null) {
                    privateLetterRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            privateLetterPtrFramentlayout.autoRefresh();
                        }
                    }, MConstants.DELAYED);
                }
            }
        });
    }

    private void recentlyMessage() {
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, final List<RecentContact> recents, Throwable e) {
                        recents1 = recents;
                        // recents1参数即为最近联系人列表（最近会话列表）
                        if (recents1 != null) {
                            privateLetters.clear();
                            messageAdapter.clear();
                            int i = 0;
                            for (final RecentContact recentContact : recents1) {
                                if (!"".equals(recentContact.getFromAccount())) {
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
                                            messageAdapter.add(privateLetter);
                                        }

                                        @Override
                                        public void onFailure(int resultCode, String message) {
                                        }
                                    }, false);
                                    i++;
                                }
                            }
                        }
                        privateLetterPtrFramentlayout.refreshComplete();
                    }
                });
    }

    @Override
    protected void bindEven() {
        titleBarView.initRight("好友", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FriendActivity.class);
            }
        });
        privateLetterRecyclerView.setOnListScrollListener(new SwipeListView.OnListScrollListener() {
            @Override
            public void complete() {

            }
        });
        messageAdapter.setOnItemListener(new MessageAdapter.OnItemListener() {
            @Override
            public void delete(int position) {

              //  NIMClient.getService(MsgService.class).clearChattingHistory(privateLetters.get(position).getUserInfo().userId + "", SessionTypeEnum.P2P);
                NIMClient.getService(MsgService.class).deleteRecentContact(recents1.get(position));

                privateLetterPtrFramentlayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        privateLetterPtrFramentlayout.autoRefresh();
                    }
                }, MConstants.DELAYED);
            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MessageActivity.this, P2PChatActivity.class);
                intent.putExtra("contactId", privateLetters.get(position).getContactId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        recentlyMessage();
    }
}
