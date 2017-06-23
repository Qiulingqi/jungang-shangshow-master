package com.shangshow.showlive.controller.liveshow;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.netease.nim.uikit.cache.SimpleCallback;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.model.UserInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.permission.MPermission;
import com.shangshow.showlive.common.permission.MPermissionUtil;
import com.shangshow.showlive.common.permission.annotation.OnMPermissionDenied;
import com.shangshow.showlive.common.permission.annotation.OnMPermissionGranted;
import com.shangshow.showlive.common.permission.annotation.OnMPermissionNeverAskAgain;
import com.shangshow.showlive.common.utils.DateUtils;
import com.shangshow.showlive.common.utils.MessageUtils;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.controller.liveshow.chatroom.helper.ChatRoomMemberCache;
import com.shangshow.showlive.controller.liveshow.live.LivePlayer;
import com.shangshow.showlive.controller.liveshow.live.LiveSurfaceView;
import com.shangshow.showlive.controller.liveshow.live.OpenGLSurfaceView;
import com.shangshow.showlive.controller.liveshow.widget.Gift;
import com.shangshow.showlive.controller.liveshow.widget.GiftCache;
import com.shangshow.showlive.controller.liveshow.widget.GiftConstant;
import com.shangshow.showlive.controller.liveshow.widget.GiftType;
import com.shangshow.showlive.controller.liveshow.widget.LiveAnchorWidget;
import com.shangshow.showlive.controller.liveshow.widget.LiveWidgetOperate;
import com.shangshow.showlive.controller.liveshow.widget.MixAudioDialog;
import com.shangshow.showlive.controller.liveshow.widget.NetWorkInfoDialog;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.LiveInfo;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shangshow.showlive.network.service.models.body.VideoOffLiveBody;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 直播端
 */
//  直播界面  收到礼物 发送消息

public class LiveAnchorActivity extends LiveBaseActivity implements LivePlayer.ActivityProxy {

    private static final String TAG = "LiveAnchorActivity";
    private final static String EXTRA_LIVE_INFO = "liveInfo";
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    /***********************
     * 录音摄像头权限申请
     *******************************/

    // 权限控制
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE};
    // constant
    private static final String EXTRA_MODE = "EXTRA_MODE";
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    //直播视图
    public LiveSurfaceView liveView;
    //直播相关widget
    public LiveAnchorWidget liveAnchorWidget;
    private OpenGLSurfaceView filterSurfaceView;
    // state
    private boolean disconnected = false; // 是否断网（断网重连用）
    private boolean isStartLive = false; // 是否开始直播推流
    // 推流参数
    private LivePlayer livePlayer;
    // data
    private List<Gift> giftList = new ArrayList<>(); // 礼物列表数据
    private String tempMessageAcc = "";
    private Queue<String> accountQueue = new LinkedList<String>();// 列表数据
    private LiveInfo liveInfo;
    private UserInfo currUser;
    // 推流地址
    private String mp3Path;
    // 推流参数
    private boolean m_tryToStopLivestreaming = false;
    private boolean mIsFilterMode = false;
    private MixAudioDialog mixAudioDialog;
    private NetWorkInfoDialog netWorkInfoDialog;
    private UserModel userModel;


    public  void getBackMainActivity(){
        Intent intent = new Intent(LiveAnchorActivity.this, LaunchLiveActivity.class);
        intent.putExtra("upload_type",0);
        startActivity(intent);
        finish();
    }

  //  干嘛用？？？？
    public static void start(Context context, LiveInfo liveInfo) {
        Intent intent = new Intent();
        intent.setClass(context, LiveAnchorActivity.class);
        intent.putExtra(EXTRA_LIVE_INFO, liveInfo);
        intent.putExtra(EXTRA_ROOM_ID, String.valueOf(liveInfo.roomId));
        intent.putExtra(EXTRA_URL, liveInfo.pushUrl);
        intent.putExtra(EXTRA_MODE, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liveInfo = (LiveInfo) getIntent().getSerializableExtra(EXTRA_LIVE_INFO);
        //  获取用户头像  结束直播后设置直播信息背景
        /*UserInfo object4XmlDB = XmlDB.getObject4XmlDB(MConstants.KEY_USER, UserInfo.class);
        Picasso.with(context).load(object4XmlDB.avatarUrl).into(liveLogo);*/
        userModel = new UserModel(this);
        url = liveInfo.pushUrl;
        requestLivePermission(); // 请求权限
        setFilterMode();

        copyMP3();

        loadGift();
        liveView = (LiveSurfaceView) findViewById(R.id.live_view);
        filterSurfaceView = (OpenGLSurfaceView) findViewById(R.id.filter_surface_view);
        liveAnchorWidget = findView(R.id.live_anchor_widget);
        liveAnchorWidget.setLiveInfo(liveInfo);
        liveAnchorWidget.setLiveAnchorOperateInterface(new widgetOperateClickListener());
        liveAnchorWidget.setMessageModules(this, roomId);
//        liveAnchorWidget.setPrivateLetterModules(this,roomId);

        MessageUtils.incomingMessage(new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                liveAnchorWidget.onPrivateMessage(imMessages);
            }
        });

    }

    private void setFilterMode() {
//        normalImageMode.setImageResource(R.drawable.ic_normal_mode);
//        normalText.setTextColor(getResources().getColor(R.color.color_black_CCffffff));
//        normalLayout.setBackgroundResource(R.drawable.ic_round_hole);
//
//        filterImageMode.setImageResource(R.drawable.ic_filter_pressed);
//        filterText.setTextColor(getResources().getColor(R.color.color_black_2e2625));
//        filterLayout.setBackgroundResource(R.drawable.ic_solid_round);

        mIsFilterMode = true;
    }

    //处理伴音MP3文件
    private void copyMP3() {

        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("mixAudio");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        File mp3AppFileDirectory = getExternalFilesDir(null);
        if (mp3AppFileDirectory == null) {
            mp3AppFileDirectory = getFilesDir();
        }

        for (String filename : files) {
            try {
                InputStream in = assetManager.open("mixAudio/" + filename);
                File outFile = new File(mp3AppFileDirectory, filename);
                mp3Path = mp3AppFileDirectory.toString();
                if (!outFile.exists()) {
                    FileOutputStream out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                Log.e("tag", "Failed to copy MP3 file", e);
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_live_anchor;
    }

    @Override
    public Activity getActivity() {
        return LiveAnchorActivity.this;
    }

    @Override
    public void onLiveStart() {

    }

    @Override
    public void onInitFailed() {
        m_tryToStopLivestreaming = true;
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LivePlayer.STREAM_FINISH));
        finish();
    }

    @Override
    public void onNetWorkBroken() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LivePlayer.STREAM_FINISH));
        Toast.makeText(LiveAnchorActivity.this, R.string.net_broken, Toast.LENGTH_SHORT).show();
        resetLivePlayer();
    }

    private void resetLivePlayer() {
        // 释放资源
        if (livePlayer != null) {
            livePlayer.tryStop();
            livePlayer.resetLive();
            livePlayer.unInitMixAudio();
        }
    }

    @Override
    public void onFinished() {
//        finish();
    }

    private void showNetworkInfoDialog() {
        if (this.isFinishing()) {
            return;
        }
        netWorkInfoDialog = new NetWorkInfoDialog(this);
//        netWorkInfoDialog.showAsDropDown(closeBtn);
    }

    private void dismissNetworkInfoDialog() {
        try {
            if (netWorkInfoDialog != null) {
                netWorkInfoDialog.dismiss();
                netWorkInfoDialog = null;
            }
        } catch (Exception e) {

        }
    }

    private void showMixAudioDialog() {
        if (this.isFinishing()) {
            return;
        }
        mixAudioDialog = new MixAudioDialog(this);
        mixAudioDialog.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void dismissMixAudioDialog() {
        try {
            if (mixAudioDialog != null) {
                mixAudioDialog.dismiss();
                mixAudioDialog = null;
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 恢复直播
        if (livePlayer != null) {
            livePlayer.onActivityResume();
        }

        if (liveAnchorWidget != null) {
            liveAnchorWidget.onResume();
        }
    }

    protected void onPause() {
        dismissNetworkInfoDialog();
        dismissMixAudioDialog();
        super.onPause();
        // 暂停直播
        if (livePlayer != null) {
            livePlayer.onActivityPause(m_tryToStopLivestreaming);
        }
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        if (livePlayer != null) {
            livePlayer.resetLive();
            livePlayer.unInitMixAudio();
        }
        giftList.clear();
        super.onDestroy();
        if (liveAnchorWidget != null) {
            liveAnchorWidget.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // 退出聊天室
    private void logoutChatRoom() {

        CustomDialogHelper.TwoButtonDialog(this, "", getString(R.string.finish_confirm), getString(R.string.confirm), getString(R.string.cancel), new CustomDialogHelper.OnDialogActionListener() {
            @Override
            public void doCancelAction() {

            }

            @Override
            public void doPositiveAction() {
                isStartLive = false;
                liveAnchorWidget.changeLiveState(MConstants.STATE_LIVE_END);
                // 释放资源
                if (livePlayer != null) {
                    livePlayer.resetLive();
                    livePlayer.unInitMixAudio();
                }
            }
        });
    }

    @Override
    protected void enterChatRoomSuccess() {
        // 初始化推流
        if (disconnected) {
            // 如果网络不通
            Toast.makeText(getActivity(), R.string.net_broken, Toast.LENGTH_SHORT).show();

            return;
        }
        initLivePlayer();
        livePlayer.startStopLive();
        livePlayer.initMixAudio(mp3Path);
//        requestLivePermission(); // 请求权限
    }

    private void initLivePlayer() {
        if (mIsFilterMode) {
            livePlayer = new LivePlayer(filterSurfaceView, url, this, mIsFilterMode);
            filterSurfaceView.setVisibility(View.VISIBLE);
            liveAnchorWidget.changeLiveState(MConstants.STATE_LIVE_LIVING);
//            initSeekBar();
        } else {
            livePlayer = new LivePlayer(liveView, url, this, mIsFilterMode);
            liveView.setVisibility(View.VISIBLE);
            liveAnchorWidget.changeLiveState(MConstants.STATE_LIVE_LIVING);
        }
    }

    @Override
    protected void enterChatRoomFailed() {
        liveAnchorWidget.changeLiveState(MConstants.STATE_LIVE_ERROR);
    }

    protected void updateUI(ChatRoomInfo roomInfo) {
        currUser = CacheCenter.getInstance().getCurrUser();
        liveAnchorWidget.updateUI(roomInfo, currUser);
        //IM
        ChatRoomMember roomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, roomInfo.getCreator());
        if (roomMember != null) {
            masterNick = roomMember.getNick();
            liveAnchorWidget.updateUINickName(TextUtils.isEmpty(masterNick) ? roomInfo.getCreator() : masterNick);
        }

        Timer timer = new Timer();
        //开始一个定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ChatRoomMemberCache.getInstance().fetchRoomMembers(roomId, MemberQueryType.GUEST, 0, 10, new SimpleCallback<List<ChatRoomMember>>() {
                    @Override
                    public void onResult(boolean success, List<ChatRoomMember> result) {
                        if (success && result != null && result.size() > 0) {
                            liveAnchorWidget.updateUIGuest(result);
                        }
                    }
                });
            }
        }, MConstants.FETCH_ONLINE_PEOPLE_COUNTS_DELTA, MConstants.FETCH_ONLINE_PEOPLE_COUNTS_DELTA);
    }

    // 取出缓存的礼物
    private void loadGift() {
        Map gifts = GiftCache.getInstance().getGift(roomId);
        if (gifts == null) {
            return;
        }
        Iterator<Map.Entry<Integer, Integer>> it = gifts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            int type = entry.getKey();
            int count = entry.getValue();
            giftList.add(new Gift(GiftType.typeOfValue(type), GiftConstant.titles[type], count, GiftConstant.images[type]));
        }
        liveAnchorWidget.setGiftData(giftList);
    }

    /**
     * 收到礼物
     *
     * @param message
     */
    public void receivGift(ChatRoomMessage message) {
        super.receivGift(message);
//        if (!updateGiftCount(giftInfoAttachment.getCount())) {
//            giftList.add(new Gift(type, GiftConstant.titles[type.getValue()], 1, GiftConstant.images[type.getValue()]));
//        }
//        GiftCache.getInstance().saveGift(roomId, type.getValue());

        //收到礼物
        if (accountQueue.contains(message.getFromAccount())) {
            liveAnchorWidget.showGiftAnimation(message, true);
        } else {
            liveAnchorWidget.showGiftAnimation(message, false);
        }
        tempMessageAcc = message.getFromAccount();
        if (accountQueue.size() == 10) {
            accountQueue.poll();
        } else {
            accountQueue.offer(tempMessageAcc);
        }
    }

    @Override
    public void receivLove() {
        super.receivLove();
        liveAnchorWidget.addHeart();
    }

    @Override
    protected void receivMessage(List<ChatRoomMessage> messages) {
        super.receivMessage(messages);
        liveAnchorWidget.onIncomingMessage(messages);
    }

    @Override
    public void receivDanmu(ChatRoomMessage message) {
        super.receivDanmu(message);
        liveAnchorWidget.showDanmu(message);
    }

    @Override
    protected void receiveIMMessage(List<IMMessage> messages) {
        super.receiveIMMessage(messages);
        liveAnchorWidget.onIncomingIMMessage(messages);
    }

    @Override
    protected void receiveContactList(List<RecentContact> messages) {
        super.receiveContactList(messages);
        liveAnchorWidget.recentContactList(messages);
    }

    // 更新收到礼物的数量
    private boolean updateGiftCount(GiftType type) {
//        for (Gift gift : giftList) {
//            if (type == gift.getGiftType()) {
//                gift.setCount(gift.getCount() + 1);
//                return true;
//            }
//        }
        return false;
    }

    /**
     * ********************************** 断网重连处理 **********************************
     */

    // 网络连接成功
    protected void onConnected() {
        if (disconnected == false) {
            return;
        }

        LogUtil.i(TAG, "live on connected");
        if (livePlayer != null) {
            livePlayer.restartLive();
        }

        disconnected = false;
    }

    // 网络断开
    protected void onDisconnected() {
        LogUtil.i(TAG, "live on disconnected");

        if (livePlayer != null) {
            livePlayer.stopLive();
        }
        disconnected = true;
    }

    private void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(LIVE_PERMISSION_REQUEST_CODE)
                .permissions(LIVE_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
        Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
        isStartLive = true;
        liveAnchorWidget.changeLiveState(MConstants.STATE_LIVE_LIVING);
//        if (livePlayer.startStopLive()) {
//
//        }
    }

    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, LIVE_PERMISSIONS);
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
    }

    private class widgetOperateClickListener implements LiveWidgetOperate.LiveAnchorOperateInterface {


//        @Override
//        public void onCLickStartLive() {
//            if (disconnected) {
//                // 如果网络不通
//                Toast.makeText(getActivity(), R.string.net_broken, Toast.LENGTH_SHORT).show();
//                return;
//            }
//            liveAnchorWidget.changeLiveState(MConstants.STATE_LIVE_START);
//            requestLivePermission(); // 请求权限
//        }

        private UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                if (platform.name().equals("WEIXIN_FAVORITE")) {
                    Toast.makeText(LiveAnchorActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LiveAnchorActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(LiveAnchorActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(LiveAnchorActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            }
        };

        @Override
        public void onClickBackClose() {
            if (isStartLive && livePlayer != null) {
                livePlayer.tryStop();
                logoutChatRoom();
            } else {
                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                clearChatRoom();
            }
        }

        @Override
        public void onClickFinish(VideoOffLiveBody videoOffLiveBody) {
            videoOffLiveBody.cid = liveInfo.cid;
            videoOffLiveBody.videoId = liveInfo.videoId;
            videoOffLiveBody.videoRoomId = liveInfo.videoRoomId;
            userModel.offLive("VIDEO-CLOSE", videoOffLiveBody, new Callback<String>() {
                @Override
                public void onSuccess(String s) {
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    clearChatRoom();
                }

                @Override
                public void onFailure(int resultCode, String message) {
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    clearChatRoom();
                }
            });
        }

        @Override
        public void onClickSaveVideo(VideoOffLiveBody videoOffLiveBody) {
            videoOffLiveBody.cid = liveInfo.cid;
            videoOffLiveBody.videoId = liveInfo.videoId;
            videoOffLiveBody.videoRoomId = liveInfo.videoRoomId;
            userModel.offLive("VIDEO-SAVE", videoOffLiveBody, new Callback<String>() {
                @Override
                public void onSuccess(String s) {
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    clearChatRoom();
                }

                @Override
                public void onFailure(int resultCode, String message) {
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    clearChatRoom();
                }
            });
        }

        @Override
        public void onClickSwitchCamera() {
            if (livePlayer != null) {
                livePlayer.switchCamera();
            }
        }

        @Override
        public void onClickLike() {

        }

        @Override
        public void onClickReplyPrivateLetter(UserInfo userInfo, long contactId) {
            UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
            if(currentUser.userId != userInfo.userId){
                liveAnchorWidget.showReplyPrivateLetterLayout(userInfo);
            }else{
                userModel.getUserInfo(contactId, new Callback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo info) {
                        liveAnchorWidget.showReplyPrivateLetterLayout(info);
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                }, false);
            }
        }

        @Override
        public void onClickSendPrivateLetter(UserInfo userInfo) {
            liveAnchorWidget.sendPrivateLetter(userInfo);
        }

        @Override
        public void onClickPrivateLetter() {
            liveAnchorWidget.showPrivateLetterLayout();
            liveAnchorWidget.autoGetPrivates();
        }

        @Override
        public void onClickShowPrivateLetter(final List<PrivateLetter> privateLetters) {
            NIMClient.getService(MsgService.class).queryRecentContacts()
                    .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                        @Override
                        public void onResult(int code, final List<RecentContact> recents, Throwable e) {
                            // recents参数即为最近联系人列表（最近会话列表）
                            if(recents != null){
                                liveAnchorWidget.clearPrivates();
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
                                                liveAnchorWidget.addPrivates();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int resultCode, String message) {

                                        }
                                    }, false);
                                    i++;
                                }
                            }
                            liveAnchorWidget.refreshPrivate();
                        }
                    });
        }

        @Override
        public void onClickShare() {
            new ShareAction(LiveAnchorActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                    .withText("竟然这么多人在围观，#" + currUser.userName + "#的直播间发生了什么？速来商秀围观，围观地址：" + url)
                    .withMedia(new UMImage(LiveAnchorActivity.this, liveInfo.logoUrl))
                    .withTargetUrl(url)
                    .setCallback(umShareListener)
                    .open();
        }

        @Override
        public void onClickGood() {
            liveAnchorWidget.showGoodLayout();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
