package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

public class RestPwdBody implements Serializable {

    public String userMobile;//	用户名
    public String userPwd;//密码
    public String captcha;//验证码
    public String op;//操作取值见:获取验证码接口
    public String type;//	类型：0
    public String captchaToken;//验证码返回token

    public RestPwdBody() {

    }


    @Override
    public String toString() {
        return super.toString();
    }
}