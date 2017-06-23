package com.netease.nim.uikit.session.extension;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.model.UserInfo;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class DefaultCustomAttachment extends CustomAttachment {

//    private final String KEY_TEXT = "text";
//    private final String KEY_SEND_USER = "send_user";
//
//    private String text;
//    private UserInfo sendUser;
//
//    DefaultCustomAttachment() {
//        super(CustomAttachmentType.ChatRoom);
//    }
//
//    public DefaultCustomAttachment(UserInfo sendUser, String text) {
//        this();
//        this.sendUser = sendUser;
//        this.text = text;
//    }
//
//    @Override
//    protected void parseData(JSONObject data) {
////        content = data.toJSONString();
//        this.text = data.getString(KEY_TEXT);
//        this.sendUser = data.getObject(KEY_SEND_USER, UserInfo.class);
//    }
//
//    @Override
//    protected JSONObject packData() {
//        JSONObject jsonObject = new JSONObject();
//        Log.i("json", "text:" + text);
//        jsonObject.put(KEY_TEXT, text);
//        jsonObject.put(KEY_SEND_USER, sendUser);
//        Log.i("json", "text:" + sendUser);
//        return jsonObject;
//    }
//
//    public UserInfo getSendUser() {
//        return sendUser;
//    }
//
//    public String getText() {
//        return text;
//    }

    private final String KEY_PRESENT = "present";
    private final String KEY_COUNT = "count";
    private final String KEY_SEND_USER = "send_user";

    private GiftInfo giftInfo;
    private int count;
    private UserInfo sendUser;

    DefaultCustomAttachment() {
        super(CustomAttachmentType.ChatRoom);
    }

    public DefaultCustomAttachment(int count, UserInfo sendUser) {
        this();
        this.count = count;
        this.sendUser = sendUser;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.count = data.getIntValue(KEY_COUNT);
        this.sendUser = data.getObject(KEY_SEND_USER, UserInfo.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_COUNT, count);
        data.put(KEY_SEND_USER, sendUser);
        return data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UserInfo getSendUser() {
        return sendUser;
    }

    public void setSendUser(UserInfo sendUser) {
        this.sendUser = sendUser;
    }

}
