package com.shangshow.showlive.controller.personal.setting;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.VcodeUtil;
import com.shangshow.showlive.common.utils.VerifyUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.network.service.models.body.UserBody;

/**
 * 绑定手机
 */
public class BindMobileActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private UserInfo userInfo;
    private ClearableEditTextWithIcon bindmobilePhoneNum;//手机号码
    private ClearableEditTextWithIcon bindmobileAuthcode;//验证码
    private BaseButton bindmobileButton;//绑定
    private BaseButton bindmobileAuthcodeButton;//发送验证码

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
            if (bindmobilePhoneNum.getText().toString().length() > 0 && bindmobileAuthcode.getText().length() > 0) {
                bindmobileButton.setEnabled(true);
            } else {
                bindmobileButton.setEnabled(false);
            }
        }
    };

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_bindmobile;
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
        titleBarView.initCenterTitle(R.string.binding_mobile);
        bindmobilePhoneNum = (ClearableEditTextWithIcon) findViewById(R.id.bindmobile_PhoneNum);
        bindmobileAuthcode = (ClearableEditTextWithIcon) findViewById(R.id.bindmobile_authcode);
        bindmobileButton = (BaseButton) findViewById(R.id.bindmobile_bound_button);
        bindmobileAuthcodeButton = (BaseButton) findViewById(R.id.bindmobile_authcode_button);
    }

    @Override
    protected void bindEven() {
        bindmobileButton.setEnabled(false);
        bindmobileButton.setOnClickListener(this);
        bindmobileAuthcodeButton.setOnClickListener(this);
        bindmobilePhoneNum.addTextChangedListener(watcher);
        bindmobileAuthcode.addTextChangedListener(watcher);
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bindmobile_bound_button:
                //检验手机号和验证码不能为空
                if (VerifyUtils.checkMobileNO(BindMobileActivity.this, bindmobilePhoneNum.getText().toString())
                        && VerifyUtils.checkSmsCodeLength(BindMobileActivity.this, bindmobileAuthcode.getText().toString())) {
                    BindMobile();
                }
                break;
            case R.id.bindmobile_authcode_button:
                //检验手机号不能为空
                if (VerifyUtils.checkMobileNO(BindMobileActivity.this, bindmobilePhoneNum.getText().toString())) {
                    getSmsCaptcha();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取绑定短信验证码
     */
    private void getSmsCaptcha() {
        UserBody userBody = new UserBody();
        userBody.userMobile = bindmobilePhoneNum.getText().toString();
        userBody.op = MConstants.SMSCAPTCHA_OP_BINDING;
        userBody.type = "0";

        userModel.smsCaptcha(userBody, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                smsCaptchaToken = s;
                new VcodeUtil().setViewToVcode(bindmobileAuthcodeButton);
                showToast(getResources().getString(R.string.sms_code_send));
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    //绑定手机号码
    private void BindMobile() {
        UserBody userBody = new UserBody();
        userBody.userMobile = bindmobilePhoneNum.getText().toString();
        userBody.captcha = bindmobileAuthcode.getText().toString();
        userBody.captchaToken = smsCaptchaToken;
        userBody.op = MConstants.SMSCAPTCHA_OP_BINDING;
        userBody.type = "0";
        userModel.bindingMobile(userBody, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                CustomDialogHelper.OneButtonDialog(BindMobileActivity.this, getString(R.string.prompt), getString(R.string.complete), getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
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
