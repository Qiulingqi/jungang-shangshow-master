package com.shangshow.showlive.controller.personal.setting;

import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.netease.nim.uikit.model.UserInfo;

/**
 * 编辑性别
 */
public class EditGenderActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    public static String key_GENDER;
    private UserModel userModel;
    private RadioGroup genderRadioGroup;
    private RadioButton genderMale; //男
    private RadioButton genderFemale; //女
    private BaseButton genderButton; //提交
    private String gender = "";

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_edit_gender;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        if (getIntent().hasExtra(key_GENDER)) {
            gender = (String) getIntent().getExtras().get(key_GENDER);
        }

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.sex);

        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_sex_group);
        genderMale = (RadioButton) findViewById(R.id.gender_male);
        genderFemale = (RadioButton) findViewById(R.id.gender_female);
        genderButton = (BaseButton) findViewById(R.id.gender_button);

    }

    @Override
    protected void bindEven() {
        genderRadioGroup.setOnCheckedChangeListener(this);
        genderButton.setOnClickListener(this);
    }

    @Override
    protected void setView() {
        if (!TextUtils.isEmpty(gender)) {
            if (gender.equals(MConstants.User_Sex_Type_Male)) {
                genderMale.setChecked(true);
            } else if (gender.equals(MConstants.User_Sex_Type_Female)) {
                genderFemale.setChecked(true);
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gender_button:
                updateUserInfo(gender);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.gender_male:
                gender = MConstants.User_Sex_Type_Male;
                break;
            case R.id.gender_female:
                gender = MConstants.User_Sex_Type_Female;
                break;
            default:
                break;
        }

    }


    /**
     * 更新用户信息（Gender）
     */
    public void updateUserInfo(String gender) {
        if (TextUtils.isEmpty(gender)) {
            showToast("请选择性别!");
            return;
        }
        UserInfo updateUserInfo = new UserInfo();
        updateUserInfo.gender = gender;
        userModel.updateUserInfo(updateUserInfo, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                setResult(RESULT_OK);
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
