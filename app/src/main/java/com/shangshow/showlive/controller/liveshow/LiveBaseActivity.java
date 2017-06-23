package com.shangshow.showlive.controller.liveshow;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.session.extension.DanmuAttachment;
import com.netease.nim.uikit.session.extension.GiftInfoAttachment;
import com.netease.nim.uikit.session.extension.LikeAttachment;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomStatusChangeData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.controller.liveshow.chatroom.helper.ChatRoomMemberCache;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 直播端和观众端的基类
 * Created by hzxuwen on 2016/4/5.
 */
public abstract class LiveBaseActivity extends BaseActivity {

    private static final String TAG = LiveBaseActivity.class.getSimpleName();
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";


    // 聊天室信息
    protected String roomId;
    protected ChatRoomInfo roomInfo;
    protected String url; // 推流/拉流地址
    protected String masterNick; // 主播昵称

    protected List<IMMessage> imMessageList;

    /**
     * 监听消息
     */
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            ChatRoomMessage message = messages.get(0);
            if (message != null && message.getAttachment() instanceof GiftInfoAttachment) {
                // 收到礼物消息
//                GiftInfoAttachment giftInfoAttachment = ((GiftInfoAttachment) message.getAttachment());
//                message.setAttachment(giftInfoAttachment);
                receivGift(message);
            } else if (message != null && message.getAttachment() instanceof LikeAttachment) {
                // 收到点赞爱心
                receivLove();
            } else if (message != null && message.getAttachment() instanceof DanmuAttachment) {
                // 收到弹幕
                receivDanmu(message);
            }/*else if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                receivMessage(messages);
            }*/ else {
                receivMessage(messages);
            }
        }
    };


    /**
     * 监听私信
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
            if (messages == null || messages.isEmpty()) {
                return;
            }
            IMMessage message = messages.get(0);
            if (message != null && message.getAttachment() instanceof GiftInfoAttachment) {
                // 收到礼物消息
//                GiftInfoAttachment giftInfoAttachment = ((GiftInfoAttachment) message.getAttachment());
//                        receivGift( message);
            } else if (message != null && message.getAttachment() instanceof LikeAttachment) {
                // 收到点赞爱心
                //receivLove();
            } else if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
            } else if (message != null && message.getAttachment() instanceof DanmuAttachment) {
                // 通知类消息
                //receivDanmu( (ChatRoomMessage) message);
            } else {
                receiveIMMessage(messages);
            }
        }
    };

    /**
     * 监听链接状态
     */
    Observer<ChatRoomStatusChangeData> onlineStatus = new Observer<ChatRoomStatusChangeData>() {
        @Override
        public void onEvent(ChatRoomStatusChangeData chatRoomStatusChangeData) {
            if (chatRoomStatusChangeData.status == StatusCode.CONNECTING) {
                DialogMaker.updateLoadingMessage("连接中...");
            } else if (chatRoomStatusChangeData.status == StatusCode.UNLOGIN) {
                onOnlineStatusChanged(false);
                Toast.makeText(LiveBaseActivity.this, R.string.nim_status_unlogin, Toast.LENGTH_SHORT).show();
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINING) {
//                DialogMaker.updateLoadingMessage("登录中...");
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINED) {
                onOnlineStatusChanged(true);
            } else if (chatRoomStatusChangeData.status.wontAutoLogin()) {
            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
                onOnlineStatusChanged(false);
                Toast.makeText(LiveBaseActivity.this, R.string.net_broken, Toast.LENGTH_SHORT).show();
            }
            LogUtil.i(TAG, "Chat Room Online Status:" + chatRoomStatusChangeData.status.name());
        }
    };
    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            Toast.makeText(LiveBaseActivity.this, "被踢出聊天室，原因:" + chatRoomKickOutEvent.getReason(), Toast.LENGTH_SHORT).show();
            clearChatRoom();
        }
    };
    private AbortableFuture<EnterChatRoomResultData> enterRequest;
    //定时器
    private Timer timer;

    @Override
    protected void setContentViewOption(int resId) {
        setContentViewOption(resId, false, false);
        setSwipeBackEnable(false);
    }

    protected abstract int getActivityLayout(); // activity布局文件

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置当前窗体为全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(getActivityLayout());
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        parseIntent();

        // 进入聊天室
        enterRoom();

        // 注册观察者监听
        registerObservers(true);
    }

    private void parseIntent() {
        roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
        url = getIntent().getStringExtra(EXTRA_URL);
    }

    public void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
//        if (inputPanel != null && inputPanel.collapse(true)) {
//        }
//
//        if (messageListPanel != null && messageListPanel.onBackPressed()) {
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    /***************************
     * 监听
     ****************************/

    private void registerObservers(boolean register) {

        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, register);

    }

    /**************************
     * 断网重连
     ****************************/

    protected void onOnlineStatusChanged(boolean isOnline) {
        if (isOnline) {
            onConnected();
        } else {
            onDisconnected();
        }
    }

    protected abstract void onConnected(); // 网络连上

    protected abstract void onDisconnected(); // 网络断开

    /****************************
     * 布局初始化
     **************************/

    // 更新礼物列表，由子类处理
    protected void receivGift(ChatRoomMessage message) {

    }

    protected void receivDanmu(ChatRoomMessage message) {

    }

    // 收到爱心，由子类处理
    protected void receivLove() {

    }

    // 收到爱心，由子类处理
    protected void receivMessage(List<ChatRoomMessage> messages) {

    }

    // 收到私信，由子类处理
    protected void receiveIMMessage(List<IMMessage> messages) {
        imMessageList = messages;
    }

    //最近联系人列表
    protected void receiveContactList(List<RecentContact> messages) {

    }

    // 进入聊天室
    private void enterRoom() {

        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (enterRequest != null) {
                    enterRequest.abort();
                    onLoginDone();
                    finish();
                }
            }
        }).setCanceledOnTouchOutside(false);

        EnterChatRoomData data = new EnterChatRoomData(roomId);
        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData enterChatRoomResultData) {
                onLoginDone();
                roomInfo = enterChatRoomResultData.getRoomInfo();
                ChatRoomMember member = enterChatRoomResultData.getMember();
                member.setRoomId(roomInfo.getRoomId());
                ChatRoomMemberCache.getInstance().saveMyMember(member);
                enterChatRoomSuccess();
                //轮询
                fetchOnline(roomInfo);
                updateUI(roomInfo);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == ResponseCode.RES_CHATROOM_BLACKLIST) {
                    Toast.makeText(LiveBaseActivity.this, "你已被拉入黑名单，不能再进入", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LiveBaseActivity.this, "enter chat room failed, code=" + code, Toast.LENGTH_SHORT).show();
                }
                enterChatRoomFailed();
            }

            @Override
            public void onException(Throwable throwable) {
                onLoginDone();
                Toast.makeText(LiveBaseActivity.this, "enter chat room exception, e=" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                enterChatRoomFailed();
            }
        });
    }

    protected abstract void enterChatRoomSuccess();

    protected abstract void enterChatRoomFailed();

    protected abstract void updateUI(ChatRoomInfo roomInfo);

    private void fetchOnline(ChatRoomInfo roomInfo) {
        //  updateUI  被放开
        updateUI(roomInfo);
        fetchOnlineCount();
    }

    private void onLoginDone() {
        enterRequest = null;
        DialogMaker.dismissProgressDialog();
    }


    /**
     * 一分钟轮询一次在线人数
     */
    private void fetchOnlineCount() {
        if (timer == null) {
            timer = new Timer();
        }
        //开始一个定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NIMClient.getService(ChatRoomService.class).fetchRoomInfo(roomId).setCallback(new RequestCallback<ChatRoomInfo>() {
                    @Override
                    public void onSuccess(final ChatRoomInfo newRoomInfo) {
                        //更新聊天室信息
                        roomInfo = newRoomInfo;
                        // 此处做了修改  让在线人数一分钟刷新一次
                        updateUI(roomInfo);
                    }

                    @Override
                    public void onFailed(int code) {
                        LogUtil.d(TAG, "fetch room info failed:" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        LogUtil.d(TAG, "fetch room info exception:" + exception);
                    }
                });
            }
        }, MConstants.FETCH_ONLINE_PEOPLE_COUNTS_DELTA, MConstants.FETCH_ONLINE_PEOPLE_COUNTS_DELTA);
    }


}
