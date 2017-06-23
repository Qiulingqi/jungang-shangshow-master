package com.netease.nim.uikit.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.model.UserInfo;

/**
 * Created by 1 on 2016/11/4.
 */
public class ChatAttachment extends CustomAttachment {

    private final String KEY_TEXT = "text";
    private final String KEY_SEND_USER = "send_user";

    private String text;
    private UserInfo sendUser;

    ChatAttachment(){
        super(CustomAttachmentType.ChatRoom);
    }

    public ChatAttachment(UserInfo sendUser, String text) {
        this();
        this.sendUser = sendUser;
        this.text = text;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.text = data.getString(KEY_TEXT);
        this.sendUser = data.getObject(KEY_SEND_USER, UserInfo.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_TEXT, text);
        jsonObject.put(KEY_SEND_USER, sendUser);
        return jsonObject;
    }

    public String getText() {
        return text;
    }

    public UserInfo getSendUser() {
        return sendUser;
    }
}
