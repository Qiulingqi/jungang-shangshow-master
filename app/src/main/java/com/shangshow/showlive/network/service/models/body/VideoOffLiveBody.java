package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

public class VideoOffLiveBody implements Serializable {

    public long videoRoomId;//直播间编号
    public long videoId;//视屏编号
    public String cid;//频道编号
    public long totalProfit;//单场直播总收益
    public long peakValue;//单场直播最大参与人数

    public VideoOffLiveBody() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}