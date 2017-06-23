package com.shangshow.showlive.network.service.models;

import com.netease.nim.uikit.model.UserInfo;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.Serializable;

/**
 * Created by taolong on 16/8/24.
 */
public class PrivateLetter implements Serializable {

    private UserInfo userInfo;
    private String message;
    private String time;
    private String uuid;
    private String contactId;
    private String account;
    private IMMessage imMessage;

    public IMMessage getImMessage() {
        return imMessage;
    }

    public void setImMessage(IMMessage imMessage) {
        this.imMessage = imMessage;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}
