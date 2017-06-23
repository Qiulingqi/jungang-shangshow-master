package com.shangshow.showlive.controller.liveshow.chatroom.viewholder;


import com.netease.nim.uikit.session.extension.GuessAttachment;

/**
 * Created by hzxuwen on 2016/1/20.
 */
public class ChatRoomMsgViewHolderGuess extends ChatRoomViewHolderText {

    @Override
    protected String getDisplayText() {
        GuessAttachment attachment = (GuessAttachment) message.getAttachment();

        return attachment.getValue().getDesc() + "!";
    }
}
