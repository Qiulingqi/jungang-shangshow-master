package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * Created by taolong on 16/8/2.
 * 充值
 */
public class RechargeInfo implements Serializable{
    int rechargeInfoId;
    int dCount;
    int rechargeType;
    long amount;

    public int getRechargeInfoId() {
        return rechargeInfoId;
    }

    public void setRechargeInfoId(int rechargeInfoId) {
        this.rechargeInfoId = rechargeInfoId;
    }

    public int getdCount() {
        return dCount;
    }

    public void setdCount(int dCount) {
        this.dCount = dCount;
    }

    public int getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(int rechargeType) {
        this.rechargeType = rechargeType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
