package com.netease.nim.uikit.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.model.UserInfo;

/**
 * 点赞附件
 * Created by hzxuwen on 2016/3/30.
 */
public class DanmuAttachment extends CustomAttachment {
    private final String KEY_SEND_USER = "send_user";
    private final String KEY_SEND_CONTENT = "text";
    private UserInfo sendUser;
    private String content;
    public DanmuAttachment() {
        super(CustomAttachmentType.Danmu);
    }

    public DanmuAttachment(UserInfo sendUser,String content) {
        this();
        this.sendUser = sendUser;
        this.content=content;
    }
    @Override
    protected void parseData(JSONObject data) {
        this.sendUser = data.getObject(KEY_SEND_USER, UserInfo.class);
        this.content = data.getString(KEY_SEND_CONTENT);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_SEND_USER, sendUser);
        data.put(KEY_SEND_CONTENT,content);
        return data;
    }

    public UserInfo getSendUser() {
        return sendUser;
    }

    public void setSendUser(UserInfo sendUser) {
        this.sendUser = sendUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
