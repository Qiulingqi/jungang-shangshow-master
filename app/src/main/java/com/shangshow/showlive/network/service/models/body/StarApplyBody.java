package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * 星咖申请
 */
public class StarApplyBody implements Serializable {
    public long userApplyStarId;
    public long userId;//用户编号
    public String userName;//星咖姓名
    public String labelCode;//标签
    public String startAgent;//	经纪人
    public String userMobile;//手机号
    public String remark;//备注

    public StarApplyBody() {

    }

    @Override
    public String toString() {
        return super.toString();
    }

}