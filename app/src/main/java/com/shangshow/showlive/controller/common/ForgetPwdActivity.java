package com.shangshow.showlive.controller.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

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
import com.shangshow.showlive.network.service.models.body.UserBody;


/**
 * 忘记密码
 *
 * @author
 */
public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private ClearableEditTextWithIcon forgetPwdAccount;
    private ClearableEditTextWithIcon forgetSmsCode;
    private BaseButton forgetGetSmsCode;
    private ClearableEditTextWithIcon inputNewPwd;//输入新密码
    private ClearableEditTextWithIcon inputNewPwdReconfirm;//确认密码
    private BaseButton confrimBtn;//

    //获取短信验证码接口返回
    private String smsCaptchaToken = "";

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_forget_pwd;
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
        titleBarView.initCenterTitle(getString(R.string.forget_pwd));
        forgetPwdAccount = (ClearableEditTextWithIcon) findViewById(R.id.forget_pwd_account_edittext);
        forgetSmsCode = (ClearableEditTextWithIcon) findViewById(R.id.forget_pwd_sms_code_edittext);
        forgetGetSmsCode = (BaseButton) findViewById(R.id.forget_pwd_get_sms_code);
        inputNewPwd = (ClearableEditTextWithIcon) findViewById(R.id.input_new_pwd_edittext);
        inputNewPwdReconfirm = (ClearableEditTextWithIcon) findViewById(R.id.input_new_pwd_reconfirm_edittext);

        confrimBtn = (BaseButton) findViewById(R.id.forget_pwd_confrim);


    }

    @Override
    protected void bindEven() {
        forgetPwdAccount.addTextChangedListener(watcher);
        forgetSmsCode.addTextChangedListener(watcher);
        inputNewPwd.addTextChangedListener(watcher);
        inputNewPwdReconfirm.addTextChangedListener(watcher);
        forgetGetSmsCode.setOnClickListener(this);
        confrimBtn.setEnabled(false);
        confrimBtn.setOnClickListener(this);

    }

    @Override
    protected void setView() {

    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (forgetPwdAccount.getText().length() > 0 && forgetSmsCode.getText().length() > 0 && inputNewPwd.getText().length() > 0 && inputNewPwdReconfirm.getText().length() > 0) {
                confrimBtn.setEnabled(true);
            } else {
                confrimBtn.setEnabled(false);
            }
        }
    };


    @Override
    public void onClick(View v) {
        CommonUtil.keyBoardCancel(ForgetPwdActivity.this);
        switch (v.getId()) {
            case R.id.forget_pwd_get_sms_code:
                if (VerifyUtils.checkMobileNO(ForgetPwdActivity.this, forgetPwdAccount.getText().toString())) {
                    getSmsCaptcha();
                }

                break;
            case R.id.forget_pwd_confrim:
                if (VerifyUtils.checkMobileNO(ForgetPwdActivity.this, forgetPwdAccount.getText().toString())
                        && VerifyUtils.checkSmsCodeLength(ForgetPwdActivity.this, forgetSmsCode.getText().toString())
                        && VerifyUtils.checkPassword(ForgetPwdActivity.this, inputNewPwd.getText().toString())
                        && VerifyUtils.checkPassword(ForgetPwdActivity.this, inputNewPwdReconfirm.getText().toString())
                        && VerifyUtils.checkTwicePasswordIsEqual(ForgetPwdActivity.this, inputNewPwd.getText().toString(), inputNewPwdReconfirm.getText().toString())) {

                    restPwd();

                }
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
        userBody.userMobile = forgetPwdAccount.getText().toString();
        userBody.op = MConstants.SMSCAPTCHA_OP_FORGET;
        userBody.type = "0";
        userModel.smsCaptcha(userBody, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                smsCaptchaToken = s;
                new VcodeUtil().setViewToVcode(forgetGetSmsCode);
                showToast(getResources().getString(R.string.sms_code_send));
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }


    /**
     * 重置密码
     */
    public void restPwd() {
        UserBody userBody = new UserBody();
        userBody.userMobile = forgetPwdAccount.getText().toString();
        userBody.captcha = forgetSmsCode.getText().toString();
        userBody.userPwd = inputNewPwd.getText().toString();
        userBody.confirmPwd = inputNewPwdReconfirm.getText().toString();
        userBody.captchaToken = smsCaptchaToken;
        userBody.op = MConstants.SMSCAPTCHA_OP_REGISTER;
        userBody.type = "0";
        userModel.restPwd(userBody, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                finish();
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


