package com.netease.nim.uikit.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.model.UserInfo;

/**
 * 礼物附件
 * Created by LornTao on 2016/8/17.
 */
public class GiftInfoAttachment extends CustomAttachment {
    private final String KEY_PRESENT = "present";
    private final String KEY_COUNT = "count";
    private final String KEY_SEND_USER = "send_user";

    private GiftInfo giftInfo;
    private int count;
    private UserInfo sendUser;

    GiftInfoAttachment() {
        super(CustomAttachmentType.Gift);
    }

    public GiftInfoAttachment(GiftInfo giftInfo, int count, UserInfo sendUser) {
        this();
        this.giftInfo = giftInfo;
        this.count = count;
        this.sendUser = sendUser;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.giftInfo = data.getObject(KEY_PRESENT, GiftInfo.class);
        this.count = data.getIntValue(KEY_COUNT);
        this.sendUser = data.getObject(KEY_SEND_USER, UserInfo.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_PRESENT, giftInfo);
        data.put(KEY_COUNT, count);
        data.put(KEY_SEND_USER, sendUser);
        return data;
    }

    public GiftInfo getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(GiftInfo giftInfo) {
        this.giftInfo = giftInfo;
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
