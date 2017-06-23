package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;


public class PageBody implements Serializable {
    public long pageNum;//页码
    public long pageSize;//每页个数
    public String orders;//orders排序条件
    public String userType;
    public String keyWord;

    public PageBody() {

    }

    @Override
    public String toString() {
        return super.toString();
    }

}