package com.netease.nim.uikit.model;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfo implements Serializable {

    public long userId;//用户组id
    public String userName;//	用户名
    public String userType;//用户类型[1:普通用户,2:星尚,3:星咖,4:星品]
    public String gender;//性别， M： 男； F: 女
    public String avatarUrl;//	图片http地址
    public String status;//状态，正常或者冻结，DBL 不可用 EBL 正常 CKD 已验证， 则不允许删除、修改 DEL 已删除
    public String labelName;//用户标签:颜值,才艺
    public String labelCode;// 用户标签
    public String friends;//关注人数
    public String videos;//视频数量
    public String favourites;//收藏数量
    public String follwers;//粉丝人数
    public String sourceFrom;//用户来源[WX:微信,SSApp:商秀App]
    public long applayUserId; // 申请用户id
    public long userBusinessCooperationId;
    public boolean isFriend;
    public long businessUserId;
    public String modifyAt;
    public String createBy;
    public String createAt;
    public String modifyBy;
    public String address;
    public String applyTime;
    public long  roomId;
    public String httpPullUrl;//http拉流地址

    public UserInfo() {
        userName = "";
        userType = "";
        gender = "";
        avatarUrl = "";
        status = "";
        labelName = "";
        friends = "";
        videos = "";
        follwers = "";
        favourites = "";
        sourceFrom = "";
        address = "";
        applyTime = "";
    }

    @Override
    public String toString() {
        return super.toString();
    }
}