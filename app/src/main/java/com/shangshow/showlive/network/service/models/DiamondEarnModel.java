package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * Created by taolong on 16/8/8.
 */
public class DiamondEarnModel implements Serializable {
    long allAmount;
    long cashBalance;
    long monthAmount;
    long virtualAmount;
    long adAmount;
    long auctionAmount;
    long matterAmount;

    public long getMatterAmount() {
        return matterAmount;
    }

    public void setMatterAmount(long matterAmount) {
        this.matterAmount = matterAmount;
    }

    public long getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(long allAmount) {
        this.allAmount = allAmount;
    }

    public long getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(long cashBalance) {
        this.cashBalance = cashBalance;
    }

    public long getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(long monthAmount) {
        this.monthAmount = monthAmount;
    }

    public long getVirtualAmount() {
        return virtualAmount;
    }

    public void setVirtualAmount(long virtualAmount) {
        this.virtualAmount = virtualAmount;
    }

    public long getAdAmount() {
        return adAmount;
    }

    public void setAdAmount(long adAmount) {
        this.adAmount = adAmount;
    }

    public long getAuctionAmount() {
        return auctionAmount;
    }

    public void setAuctionAmount(long auctionAmount) {
        this.auctionAmount = auctionAmount;
    }
}
