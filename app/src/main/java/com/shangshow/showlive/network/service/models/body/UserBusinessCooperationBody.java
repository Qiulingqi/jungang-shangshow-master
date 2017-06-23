package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * Created by chenhongxin on 2016/10/10.
 */
public class UserBusinessCooperationBody implements Serializable{
    public String userBusinessCooperationId; // 当前用户
    public String userId; // 当前用户
    public String businessUserId; // 商家用户
    public String status; // 状态 EBL-同意 DBL-拒绝
}
