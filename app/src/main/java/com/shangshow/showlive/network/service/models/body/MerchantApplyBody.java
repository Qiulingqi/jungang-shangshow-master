package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * 申请星尚请求
 */
public class MerchantApplyBody implements Serializable {
    public long userApplyBusinessId;
    public long userId;//用户编号
    public String name;//公司名称
    public String userName;//联系人
    public String userMobile;//手机号码
    public String address;//地址
    public String status;//状态
    public String labelCode;//用户标签
    public String industry;//行业
    public String auditType;//证件类型 1.三证合一（一照一码）2.三证合一 3.三证分离
    public String creditCard;//统一社会信息代码（字母大写）
    public String creditCardImg;//营业执照图片
    public String bussinessNo;//营业执照号
    public String businessImage;//营业执照
    public String bankCard; // 银行卡卡号
    public String bankCardName;// 持卡人姓名
    public String contact; // 持卡人联系方式

    public MerchantApplyBody() {

    }

    @Override
    public String toString() {
        return super.toString();
    }

}