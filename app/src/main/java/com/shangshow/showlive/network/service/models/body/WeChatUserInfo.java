package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * Created by lorntao on 8/29/16.
 */
public class WeChatUserInfo implements Serializable {
    String userName;
    String gender;
    String avatarUrl;
    String openId;
//    String sc

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
