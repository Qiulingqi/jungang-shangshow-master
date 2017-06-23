package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * Created by chenhongxin on 2016/9/23.
 */
public class CooperationApply implements Serializable {

    public long userId;
    public long businessUserId;
    public String status;
    public String applayUserId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(long businessUserId) {
        this.businessUserId = businessUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
