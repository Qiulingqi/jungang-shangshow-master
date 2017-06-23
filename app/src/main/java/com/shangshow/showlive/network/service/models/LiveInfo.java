package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * 直播信息
 */
public class LiveInfo implements Serializable {

    public String cid;//直播频道编号
    public long roomId;//聊天室房间编号
    public String hlsPullUrl;//HLS拉流地址
    public String httpPullUrl;//HTTP拉流地址
    public String logoUrl;//直播间封面图
    public String name;//直播间标题
    public String modifyAt;//修改时间
    public String pushUrl;//推流地址
    public String rtmpPullUrl;//rtmp拉流地址
    public String status;//状态
    public long userId;//直播间编号
    public long videoId;//视屏编号
    public long videoRoomId;//直播间编号
    public String videoType;//视屏类型 LIVE-直播视频 VIDEO-上传视频

    public LiveInfo() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}