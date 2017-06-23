package com.shangshow.showlive.network.service.models.responseBody;

import com.netease.nim.uikit.model.UserInfo;

import java.io.Serializable;

/**
 * 关注-取消关注
 */
public class EditFriendBody implements Serializable {

    public UserInfo friend;//被关注
    public UserInfo owner;//自己

    public EditFriendBody() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}