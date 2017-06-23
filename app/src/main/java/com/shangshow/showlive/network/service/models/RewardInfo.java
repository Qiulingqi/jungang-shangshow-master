package com.shangshow.showlive.network.service.models;

import com.netease.nim.uikit.model.UserInfo;

import java.io.Serializable;

/**
 * Created by lorntao on 16-8-8.
 */
public class RewardInfo implements Serializable {
    long amount;
    long userId;
    UserInfo userInfo;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
