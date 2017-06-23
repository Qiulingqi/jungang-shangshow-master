package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * Created by 1 on 2017/5/22.
 */

public class PayRecBody implements Serializable {

    public long amount;
    public long amountRefunded;
    public long amountSettle;
    public String app;
    public String body;
    public String channel;
    public String clientIp;
    public long created;
    public CredentialBody credential;
    public String currency;
    public Object extra;
    public String id;
    public boolean livemode;
    public Object metadata;
    public String object;
    public String orderNo;
    public boolean paid;
    public boolean refunded;
    public RefundsBody refunds;
    public String subject;
    public long timeExpire;






}
