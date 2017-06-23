package com.shangshow.showlive.controller.liveshow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.netease.nim.uikit.cache.SimpleCallback;
import com.netease.nim.uikit.model.UserInfo;
import com.netease.nim.uikit.session.extension.GiftInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.permission.MPermission;
import com.shangshow.showlive.common.permission.annotation.OnMPermissionDenied;
import com.shangshow.showlive.common.permission.annotation.OnMPermissionGranted;
import com.shangshow.showlive.common.utils.MessageUtils;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.controller.liveshow.chatroom.helper.ChatRoomMemberCache;
import com.shangshow.showlive.controller.liveshow.goods.LiveGoodsAudienceActivity;
import com.shangshow.showlive.controller.liveshow.video.NEVideoView;
import com.shangshow.showlive.controller.liveshow.video.VideoConstant;
import com.shangshow.showlive.controller.liveshow.video.VideoPlayer;
import com.shangshow.showlive.controller.liveshow.widget.LiveAudienceWidget;
import com.shangshow.showlive.controller.liveshow.widget.LiveWidgetOperate;
import com.shangshow.showlive.controller.personal.account.PersonalAccountListActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.share.Defaultcontent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 观众端
 */

public class LiveAudienceActivity extends LiveBaseActivity implements VideoPlayer.VideoPlayerProxy {
    private static final String TAG = LiveAudienceActivity.class.getSimpleName();
    private final static String EXTRA_VIDEO_ROOM = "videoRoom";
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    private int bufferStrategy = 0; //0:直播低延时；1:点播抗抖动
    private VideoRoom videoRoom;
    // 播放器
    private VideoPlayer videoPlayer;
    private LiveAudienceWidget liveAudienceWidget;
    private String tempMessageAcc = "";
    private Queue<String> accountQueue = new LinkedList<String>();// 列表数据
    // state
    private boolean isStartLive = false; // 推流是否开始

    private Date currentDate = new Date();
    private UserModel userModel;

    public static void start(Context context, VideoRoom videoRoom) {
        Intent intent = new Intent();
        intent.setClass(context, LiveAudienceActivity.class);
        intent.putExtra(EXTRA_VIDEO_ROOM, videoRoom);
        intent.putExtra(EXTRA_ROOM_ID, String.valueOf(videoRoom.roomId));
        intent.putExtra(EXTRA_URL, videoRoom.httpPullUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoRoom = (VideoRoom) getIntent().getSerializableExtra(EXTRA_VIDEO_ROOM);
        userModel = new UserModel(this);
        liveAudienceWidget = (LiveAudienceWidget) findViewById(R.id.live_audience_widget);
        liveAudienceWidget.setVideoRoom(videoRoom);
        liveAudienceWidget.setLiveAudienceInterface(new widgetOperateClickListener());
        liveAudienceWidget.setMessageModules(this, roomId);

        MessageUtils.incomingMessage(new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                liveAudienceWidget.onIncomingIMMessage(imMessages);
                liveAudienceWidget.onPrivateMessage(imMessages);
            }
        });
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_live_audience;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 恢复播放
        if (videoPlayer != null) {
            videoPlayer.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 暂停播放
        if (videoPlayer != null) {
            videoPlayer.onActivityPause();
        }
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        if (videoPlayer != null) {
            videoPlayer.resetVideo();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishLive();
    }

    // 离开聊天室
    private void logoutChatRoom() {
        CustomDialogHelper.TwoButtonDialog(this, "", getString(R.string.exit_live_confirm), getString(R.string.confirm), getString(R.string.cancel), new CustomDialogHelper.OnDialogActionListener() {
            @Override
            public void doCancelAction() {

            }

            @Override
            public void doPositiveAction() {
                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                clearChatRoom();
            }
        });
    }


    @Override
    protected void onConnected() {
    }

    @Override
    protected void onDisconnected() {
    }

    @Override
    protected void enterChatRoomSuccess() {
        requestBasicPermission(); // 申请APP基本权限
    }

    @Override
    protected void enterChatRoomFailed() {
        liveAudienceWidget.changeVideoState(MConstants.STATE_VIDEO_ERROR);
    }

    @Override
    public void receivGift(ChatRoomMessage message) {
        super.receivGift(message);
        //收到礼物
        if (accountQueue.contains(message.getFromAccount())) {
            liveAudienceWidget.showGiftAnimation(message, true);
        } else {
            liveAudienceWidget.showGiftAnimation(message, false);
        }
        tempMessageAcc = message.getFromAccount();
        if (accountQueue.size() == 10) {
            accountQueue.poll();
        } else {
            accountQueue.offer(tempMessageAcc);
        }
    }

    @Override
    public void receivDanmu(ChatRoomMessage message) {
        super.receivDanmu(message);
        //收到弹幕
        liveAudienceWidget.showDanmu(message);
    }

    @Override
    public void receivLove() {
        super.receivLove();
        liveAudienceWidget.addHeart();
    }

    @Override
    protected void receivMessage(List<ChatRoomMessage> messages) {
        super.receivMessage(messages);
        liveAudienceWidget.onIncomingMessage(messages);
    }

    @Override
    protected void receiveIMMessage(List<IMMessage> messages) {
        super.receiveIMMessage(messages);
        liveAudienceWidget.onIncomingIMMessage(messages);
    }

    @Override
    protected void receiveContactList(List<RecentContact> messages) {
        super.receiveContactList(messages);
        liveAudienceWidget.recentContactList(messages);
    }

    /**
     * 轮询人数信息
     */
    @Override
    protected void updateUI(final ChatRoomInfo roomInfo) {

        liveAudienceWidget.updateUI(roomInfo, videoRoom);
        //IM
        ChatRoomMemberCache.getInstance().fetchMember(roomId, roomInfo.getCreator(), new SimpleCallback<ChatRoomMember>() {
            @Override
            public void onResult(boolean success, ChatRoomMember result) {
                if (success) {
                    masterNick = result.getNick();
                    liveAudienceWidget.updateUINickName(TextUtils.isEmpty(masterNick) ? result.getAccount() : masterNick);
                }
            }
        });

        ChatRoomMemberCache.getInstance().fetchRoomMembers(roomId, MemberQueryType.GUEST, 0, 10, new SimpleCallback<List<ChatRoomMember>>() {
            @Override
            public void onResult(boolean success, List<ChatRoomMember> result) {
                if (success && result != null && result.size() > 0) {
                    liveAudienceWidget.updateUIGuest(result);
                }
            }
        });
    }

    /**
     * 申请权限成功就开始播放
     */

    private void initAudienceParam() {

        NEVideoView videoView = findView(R.id.video_view);

        videoPlayer = new VideoPlayer(LiveAudienceActivity.this, videoView, null, url,

                bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FILL_SCALE);

        videoPlayer.openVideo();
    }

    private void finishLive() {
        if (isStartLive) {
            logoutChatRoom();
        } else {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
    }

    @Override
    public boolean isDisconnected() {
        return false;
    }


    /****************************
     * 播放器状态回调
     *****************************/

    @Override
    public void onError() {
        liveAudienceWidget.changeVideoState(MConstants.STATE_VIDEO_ERROR);
    }

    @Override
    public void onCompletion() {
//        isStartLive = false;
//        liveAudienceWidget.changeVideoState(MConstants.STATE_VIDEO_COMPLETE);
    }

    int twice = 1;
    @Override
    public void stopLive() {
//        Date date = new Date();
//        long time = date.getTime() - currentDate.getTime();
//        twice++;
//        if(time / 1000 > 20 || twice > 2) {
//        }
        isStartLive = false;
        liveAudienceWidget.changeVideoState(MConstants.STATE_VIDEO_STOP);
    }

    @Override
    public void onPrepared() {
        isStartLive = true;
        liveAudienceWidget.changeVideoState(MConstants.STATE_VIDEO_LIVING);
    }

    /**
     * 基本权限管理
     */
    private void requestBasicPermission() {
        MPermission.with(LiveAudienceActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.READ_PHONE_STATE)
                .request();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        initAudienceParam();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        finish();
    }








    //分享我的直播链接列表    已完成事项  后边无代码
    private class widgetOperateClickListener implements LiveWidgetOperate.LiveAudienceInterface {

        private UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                if (platform.name().equals("WEIXIN_FAVORITE")) {
                    Toast.makeText(LiveAudienceActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LiveAudienceActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(LiveAudienceActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(LiveAudienceActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            }
        };
        @Override
        public void onClickShare() {
            new ShareAction(LiveAudienceActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                    .withTitle(Defaultcontent.title)
                    .withText("竟然这么多人在围观，#" + videoRoom.userName + "#的直播间发生了什么？速来商秀围观，围观地址：" + videoRoom.videoUrl)
                    .withMedia(new UMImage(LiveAudienceActivity.this, videoRoom.logoUrl))
                    .withTargetUrl(videoRoom.videoUrl)
                    .setCallback(umShareListener)
                    .open();
        }

        @Override
        public void onClickGood() {
            liveAudienceWidget.showGoodLayout();
        }

        @Override
        public void onClickSendGift(GiftInfo giftInfo, int count) {
            liveAudienceWidget.sendGift(giftInfo, count);
        }

        @Override
        public void onClickFinish() {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }

        @Override
        public void onClickBackClose() {
            finishLive();
        }

        @Override
        public void onClickLike() {

            liveAudienceWidget.sendLike();
        }

        @Override
        public void onClickGift() {
            liveAudienceWidget.showGiftLayout(true);
//            liveAudienceWidget.sendGift();
        }

        @Override
        public void onClickMessage() {
            liveAudienceWidget.showMessageLayout();
        }

        @Override
        public void onClickPrivateLetter() {
            liveAudienceWidget.showPrivateLetterLayout();
        }

        @Override
        public void onClickReplyPrivateLetter(UserInfo userInfo) {
            liveAudienceWidget.showReplyPrivateLetterLayout(userInfo);
        }

        @Override
        public void onClickSendChatMessage(ChatRoomMessage chatRoomMessage) {
//            liveAudienceWidget.SendChatRoomMessage(chatRoomMessage);
        }

        @Override
        public void onClickSendPrivateLetter(UserInfo userInfo) {
            liveAudienceWidget.sendPrivateLetter(userInfo);
        }

        @Override
        public void onClickAddMoney() {
            startActivity(new Intent(context, PersonalAccountListActivity.class));
        }

        @Override
        public void onClickShop() {
            Intent intent = new Intent(LiveAudienceActivity.this, LiveGoodsAudienceActivity.class);
            intent.putExtra("live_anchor_video_id", videoRoom.videoId);
            intent.putExtra("live_anchor_user_id", videoRoom.userId);
            startActivity(intent);
        }

    }

}
