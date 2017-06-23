package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

public class AddressBody implements Serializable {

    public long userAddressId;//	用户地址编号
    public String userName;//收货人姓名
    public String userMobile;//收货人手机号
    public String address;//收货地址
    public String isDefault;//是否是默认

    public AddressBody() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}