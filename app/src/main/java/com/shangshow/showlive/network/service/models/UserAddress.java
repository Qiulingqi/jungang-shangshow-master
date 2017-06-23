package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

public class UserAddress implements Serializable {

    public long userAddressId;//
    public long userId;//
    public String userName;//	用户名
    public String userMobile;//
    public String status;//状态，
    public String address;//
    public String isDefault;//默认
    public String modifyAt;//
    public String createAt;//


    public UserAddress() {

    }


    @Override
    public String toString() {
        return super.toString();
    }
}