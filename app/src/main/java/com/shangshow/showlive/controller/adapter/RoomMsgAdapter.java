package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.model.UserInfo;
import com.netease.nim.uikit.session.extension.ChatAttachment;
import com.netease.nim.uikit.session.extension.ChatRoomMsgAttachment;
import com.netease.nim.uikit.session.extension.DefaultCustomAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.shangshow.showlive.R;
import com.shangshow.showlive.controller.adapter.adapter.BaseAdapterHelper;
import com.shangshow.showlive.controller.adapter.adapter.QuickAdapter;

import java.util.List;

/**
 * Created by 1 on 2016/11/3.
 */
public class RoomMsgAdapter extends QuickAdapter<ChatRoomMessage> {

    public RoomMsgAdapter(Context context, int layoutResId, List<ChatRoomMessage> data) {
        super(context, R.layout.room_msg_item_layout, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, ChatRoomMessage item, int position) {
        if(item.getAttachment() instanceof ChatAttachment) {
            ChatAttachment chatAttachment = (ChatAttachment) item.getAttachment();
            TextView tv_username = helper.getView(R.id.tv_username);
            TextView tv_message = helper.getView(R.id.tv_message);
            tv_username.setText(chatAttachment.getSendUser().userName);
            tv_message.setText(chatAttachment.getText() + "");
        }
    }
}
