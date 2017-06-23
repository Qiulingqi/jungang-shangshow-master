package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * Created by lorntao on 16-8-14.
 */
public class GoodsPageBody implements Serializable {
    private long userId;
    private long pageNum;//页码
    private long pageSize;//每页个数

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
}
