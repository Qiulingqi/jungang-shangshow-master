package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * 视频信息
 */
public class VideoRoom implements Serializable {

    public long userId;//用户编号
    public String cid;//直播频道编号
    public long roomId;//聊天室房间编号
    public long onlineUserCount;//在线人数
    public String httpPullUrl;//http拉流地址
    public String rtmpPullUrl;//rtmp拉流地址
    public String hlsPullUrl;//hls拉流地址
    public String userType;//用户类型[1:普通用户,2:星尚,3:星咖,4:星品]
    public String logoUrl;//直播间封面图
    public String videoType;//视屏类型 LIVE：直播视频 VIDEO:上传视频
    public String videoUrl;//视频播放地址
    public String userName;//用户昵称
    public String avatarUrl;//用户头像地址
    public String labelCode;//标签
    public String lableName;
    public long videoId;
    public long videoInfoId;
    public String gender;
    public String status;
    public String ssUrl;
    public String videoStatus;

    public VideoRoom() {
        userId = 0;
        logoUrl = "";
        videoType = "";
        videoUrl = "";
        userName = "";
        avatarUrl = "";
        labelCode = "";
        videoId = -1;
        gender="U";
    }

    @Override
    public String toString() {
        return super.toString();
    }
}