package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * Created by 1 on 2017/5/19.
 */

public class PayOrderDto implements Serializable {
    public String orderId;
    public String paymentWay;
    public String clientIp;
}
