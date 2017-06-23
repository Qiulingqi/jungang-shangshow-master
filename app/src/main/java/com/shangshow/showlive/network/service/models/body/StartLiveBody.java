package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * 开启直播请求body
 */
public class StartLiveBody implements Serializable {


    public String name;//视屏标题
    public String logoUrl;//视屏封面图
    public String videoType;
    public long isCommonweal;//是否公益直播：1-是 0-否

    public StartLiveBody() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}