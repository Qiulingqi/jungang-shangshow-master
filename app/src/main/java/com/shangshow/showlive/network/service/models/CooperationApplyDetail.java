package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * 用户，商家合作详情查询
 *
 */
public class CooperationApplyDetail implements Serializable {
    public long userBusinessCooperationId;
    public long userId;
    public long businessUserId;
    public String status;

    public CooperationApplyDetail() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}