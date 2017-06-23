package com.shangshow.showlive.network.test;

import java.io.Serializable;

/**
 * 个人中心功能按钮
 */
public class MeiZhi implements Serializable {

    public String _id;
    public String createdAt;
    public String desc;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public String used;
    public String who;

    @Override
    public String toString() {
        return super.toString();
    }

}