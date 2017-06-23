package com.netease.nim.uikit.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.model.UserInfo;

/**
 * 点赞附件
 * Created by hzxuwen on 2016/3/30.
 */
public class LikeAttachment extends CustomAttachment {
    private final String KEY_SEND_USER = "send_user";
    private UserInfo sendUser;

    public LikeAttachment() {
        super(CustomAttachmentType.Like);
    }

    public LikeAttachment(UserInfo sendUser) {
        this();
        this.sendUser = sendUser;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.sendUser = data.getObject(KEY_SEND_USER, UserInfo.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_SEND_USER, sendUser);
        return data;
    }

    public UserInfo getSendUser() {
        return sendUser;
    }

    public void setSendUser(UserInfo sendUser) {
        this.sendUser = sendUser;
    }
}
