package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * Created by 1 on 2017/5/22.
 */

public class WxBody implements Serializable {
    public String appId;
    public String nonceStr;
    public String packageValue;
    public String partnerId;
    public String prepayId;
    public String sign;
    public String timeStamp;
}
