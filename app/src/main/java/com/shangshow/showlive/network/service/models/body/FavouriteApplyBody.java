package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * 申请星尚请求
 */
public class FavouriteApplyBody implements Serializable {
//    public long userApplyFavouriteId;
    public long userId;//用户编号
    public String userName;//用户真实姓名
    public String idCard;//用户身份证号码
    public String userMobile;//用户手机号
//    public String status;//状态
    public String addres;//用户居住地址
    public String labelCode;//标签
    public String idCardUserImg;//手持身份证照
    public String idCardBackImg;//身份证背面照
    public String idCardFrontImg;//身份证正面照

    public FavouriteApplyBody() {

    }

    @Override
    public String toString() {
        return super.toString();
    }

}