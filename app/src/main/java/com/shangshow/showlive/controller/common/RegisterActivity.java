package com.shangshow.showlive.controller.common;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.VcodeUtil;
import com.shangshow.showlive.common.utils.VerifyUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.network.service.models.body.UserBody;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity implements
        OnClickListener {
    private UserModel userModel;
    private BaseButton nextStep;
    private ClearableEditTextWithIcon registerAccount;
    private ClearableEditTextWithIcon registerSmsCode;
    private BaseButton registerGetSmsCode;
    private ClearableEditTextWithIcon registerPassword;
    private TextView serviceProtocol;

    //获取短信验证码接口返回，注册接口必须字段
    private String smsCaptchaToken = "";
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (registerAccount.getText().length() > 0 && registerSmsCode.getText().length() > 0 && registerPassword.getText().length() > 0) {
                nextStep.setEnabled(true);
            } else {
                nextStep.setEnabled(false);
            }
        }
    };

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.register);
        titleBarView.initRight(getString(R.string.login), 0, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nextStep = (BaseButton) findViewById(R.id.register_next_step);

        serviceProtocol = (TextView) findViewById(R.id.register_service_protocol);
        registerAccount = (ClearableEditTextWithIcon) findViewById(R.id.register_account_edittext);
        registerSmsCode = (ClearableEditTextWithIcon) findViewById(R.id.register_sms_code_edittext);
        registerGetSmsCode = (BaseButton) findViewById(R.id.register_get_sms_code);
        registerPassword = (ClearableEditTextWithIcon) findViewById(R.id.register_password_edittext);

    }

    @Override
    protected void bindEven() {
        nextStep.setEnabled(false);
        nextStep.setOnClickListener(this);
        serviceProtocol.setOnClickListener(this);
        registerGetSmsCode.setOnClickListener(this);
        registerAccount.addTextChangedListener(watcher);
        registerSmsCode.addTextChangedListener(watcher);
        registerPassword.addTextChangedListener(watcher);
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_next_step:
                if (VerifyUtils.checkMobileNO(RegisterActivity.this, registerAccount.getText().toString())
                        && VerifyUtils.checkPassword(RegisterActivity.this, registerPassword.getText().toString())
                        && VerifyUtils.checkSmsCodeLength(RegisterActivity.this, registerSmsCode.getText().toString())) {
                    doRegister();
                }
                break;
            case R.id.register_get_sms_code:
                if (VerifyUtils.checkMobileNO(RegisterActivity.this, registerAccount.getText().toString())) {
                    getSmsCaptcha();
                }
                break;
            case R.id.register_service_protocol:
                CommonUtil.startCommonBrowserForPage(RegisterActivity.this, getString(R.string.shangxiu_service_protocol), "www.baidu.com");
                break;
            default:
                break;
        }
    }


    /**
     * 获取注册短信验证码
     */
    private void getSmsCaptcha() {
        UserBody userBody = new UserBody();
        userBody.userMobile = registerAccount.getText().toString();
        userBody.op = MConstants.SMSCAPTCHA_OP_REGISTER;
        userBody.type = "0";//暂未确定
        userModel.smsCaptcha(userBody, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                smsCaptchaToken = s;
                new VcodeUtil().setViewToVcode(registerGetSmsCode);
                showToast(getResources().getString(R.string.sms_code_send));
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 注册
     */
    private void doRegister() {
        UserBody userBody = new UserBody();
        userBody.userMobile = registerAccount.getText().toString();
        userBody.captcha = registerSmsCode.getText().toString();
        userBody.userPwd = registerPassword.getText().toString();
        userBody.captchaToken = smsCaptchaToken;
        userBody.op = MConstants.SMSCAPTCHA_OP_REGISTER;
        userBody.type = "0";
        userModel.register(userBody, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                Intent intent = new Intent(RegisterActivity.this, RegisterInfoActivity.class);
                intent.putExtra(RegisterInfoActivity.key_USERINFO, userInfo);
                startActivity(intent);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }

}
