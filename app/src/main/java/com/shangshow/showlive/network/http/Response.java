package com.shangshow.showlive.network.http;


import com.shangshow.showlive.base.MConstants;

/**
 * 公共返回
 */
public class Response<T> {

    public String resultCode;
    public T result;//商秀接口返回
    public String errorMsg;


    public T results;//妹纸图返回数据

    public boolean isSuccess() {
        if (resultCode != null) {
            return resultCode.equals(MConstants.SUCCESS_CODE);
        } else {
            return true;
        }
    }
}
