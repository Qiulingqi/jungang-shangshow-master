package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

public class OssAuthCredentials implements Serializable {


    public String securityToken;//token
    public String accessKeySecret;//	临时Secret
    public String accessKeyId;//临时id
    public String expiration;//过期时间


    public OssAuthCredentials() {

    }


    @Override
    public String toString() {
        return super.toString();
    }
}