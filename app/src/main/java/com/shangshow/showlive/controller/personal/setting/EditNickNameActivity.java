package com.shangshow.showlive.controller.personal.setting;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.common.utils.VerifyUtils;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.netease.nim.uikit.model.UserInfo;


/**
 * 编辑昵称
 *
 * @author
 */
public class EditNickNameActivity extends BaseActivity {
    public static String key_NICKNAME;
    private UserModel userModel;
    private String currNickName = "";
    private ClearableEditTextWithIcon nickNameEdit;//输入新密码
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (nickNameEdit.getText().length() > 0) {
                titleBarView.setBtnRightEnabled(true);
            } else {
                titleBarView.setBtnRightEnabled(false);
            }
        }
    };

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_edit_nickname;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        if (getIntent().hasExtra(key_NICKNAME)) {
            currNickName = getIntent().getExtras().getString(key_NICKNAME);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(getString(R.string.nickname));
        titleBarView.initRight(getString(R.string.complete), 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VerifyUtils.checkNickName(EditNickNameActivity.this, nickNameEdit.getText().toString())) {
                    updateUserInfo();
                }
            }
        });
        nickNameEdit = (ClearableEditTextWithIcon) findViewById(R.id.update_nick_name_edit);
    }

    @Override
    protected void bindEven() {
        nickNameEdit.addTextChangedListener(watcher);
    }

    @Override
    protected void setView() {
        nickNameEdit.setText(currNickName);
    }

    /**
     * 更新用户信息（nickname）
     */
    public void updateUserInfo() {
        UserInfo updateUserInfo = new UserInfo();
        updateUserInfo.userName = nickNameEdit.getText().toString();
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
