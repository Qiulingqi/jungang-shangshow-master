package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * jwwt用户token信息
 */
public class payload implements Serializable {

    public String sub;//存放userId
    public String iat;//

    public payload() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}