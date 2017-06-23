package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * Created by taolong on 16/8/23.
 */
public class VideoStatistic implements Serializable {
    private long totalWatchCount;
    private long totalGaveAmount;
    private long totalGaveCount;

    public long getTotalWatchCount() {
        return totalWatchCount;
    }

    public void setTotalWatchCount(long totalWatchCount) {
        this.totalWatchCount = totalWatchCount;
    }

    public long getTotalGaveAmount() {
        return totalGaveAmount;
    }

    public void setTotalGaveAmount(long totalGaveAmount) {
        this.totalGaveAmount = totalGaveAmount;
    }

    public long getTotalGaveCount() {
        return totalGaveCount;
    }

    public void setTotalGaveCount(long totalGaveCount) {
        this.totalGaveCount = totalGaveCount;
    }
}
