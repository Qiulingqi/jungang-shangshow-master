package com.shangshow.showlive.controller.liveshow.widget;

import com.netease.nim.uikit.session.extension.GiftInfo;
import com.netease.nim.uikit.model.UserInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shangshow.showlive.network.service.models.body.VideoOffLiveBody;

import java.util.List;

/**
 * <直播相关view操作管理>

 */
public class LiveWidgetOperate {

    /**
     * 直播端操作接口
     */
    public interface LiveAnchorOperateInterface {
        //返回、关闭
        void onClickBackClose();

        //直播结束
        void onClickFinish(VideoOffLiveBody videoOffLiveBody);

        //直播结束
        void onClickSaveVideo(VideoOffLiveBody videoOffLiveBody);

        //切换摄像头
        void onClickSwitchCamera();

        //点赞
        void onClickLike();

        //点击私信
        void onClickPrivateLetter();

        void onClickShowPrivateLetter(List<PrivateLetter> privateLetters);

        void onClickReplyPrivateLetter(UserInfo userInfo, long contactId);

        // 发送私信
        void onClickSendPrivateLetter(UserInfo userInfo);

        //点击分享
        void onClickShare();

        // 点击进入商品列表
        void onClickGood();
    }

    /**
     * 观众端操作接口
     */
    public interface LiveAudienceInterface {


        //直播结束
        void onClickFinish();

        //返回、关闭
        void onClickBackClose();

        //点赞
        void onClickLike();

        //点击礼物
        void onClickGift();

        //点击消息
        void onClickMessage();

        //点击私信
        void onClickPrivateLetter();

        //点击商品
        void onClickShop();

        //点击分享
        void onClickShare();

        void onClickGood();

        void onClickSendGift(GiftInfo giftInfo, int count);

        void onClickReplyPrivateLetter(UserInfo userInfo);

        void onClickSendChatMessage(ChatRoomMessage chatRoomMessage);

        void onClickSendPrivateLetter(UserInfo userInfo);

        void onClickAddMoney();

    }

}
