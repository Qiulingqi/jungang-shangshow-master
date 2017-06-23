package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * 商品实体类
 */
public class Goods implements Serializable {

    public long productId;//商品Id
    public String productName;//商品名称
    public String logoUrl;//商品图片
    public String price;//商品价格
    public String brief;//	商品描述
    public String status;//商品状态
    public int buyCount;

    public Goods() {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}