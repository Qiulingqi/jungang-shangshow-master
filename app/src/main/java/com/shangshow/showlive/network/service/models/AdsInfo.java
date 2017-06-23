package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * 广告信息
 */
public class AdsInfo implements Serializable {

    public long adsInfoId;//广告编号
    public long posationId;//广告位模块编号
    public String name;//用户类型[1:普通用户,2:星尚,3:星咖,4:星品]
    public long actionType;//广告类型1-外链 2-内链
    public String action;//广告点击事件
    public String resource;//广告图片地址
    public String weight;//排序权重
    public String remark;//广告描述
    public String status;//广告状态 EBL-可用 DBL-不可用

    public AdsInfo() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}