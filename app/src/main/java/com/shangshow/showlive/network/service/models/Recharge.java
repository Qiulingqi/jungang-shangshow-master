package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * Created by taolong on 16/8/2.
 */
public class Recharge implements Serializable {
    int rechargeInfoId;
    int dCount;
    int rechargeType;
    long amount;
    int gaveDCount;

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

    public int getGaveDCount() {
        return gaveDCount;
    }

    public void setGaveDCount(int gaveDCount) {
        this.gaveDCount = gaveDCount;
    }
}
