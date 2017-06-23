package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * Created by chenhongxin on 2016/10/10.
 */
public class CooperationPageBody implements Serializable {
    public long pageNum;//页码
    public long pageSize;//每页个数
    public String orders;//orders排序条件
    public String status; // 状态
}
