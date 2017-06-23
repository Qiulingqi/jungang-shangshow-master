package com.shangshow.showlive.network.service.models;

/**
 * 个人中心功能按钮
 */
public class FunctionBtn {
    public int imageResId;
    public String btnText;

    public FunctionBtn(String btnText, int imageResId) {
        this.imageResId = imageResId;
        this.btnText = btnText;
    }

}