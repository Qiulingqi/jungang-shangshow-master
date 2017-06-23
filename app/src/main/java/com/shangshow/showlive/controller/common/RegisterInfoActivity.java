package com.shangshow.showlive.controller.common;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.BaseApplication;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.VerifyUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.controller.common.picture.SelectPicActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.OssAuth;
import com.netease.nim.uikit.model.UserInfo;
import com.shaojun.utils.log.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 注册信息填写
 */
public class RegisterInfoActivity extends BaseActivity implements
        OnClickListener {
    public static String key_USERINFO = "userInfo";
    private UserModel userModel;
    private UserInfo userInfo;
    private ImageView userInfoIcon;
    private ClearableEditTextWithIcon userInfoNickName;
    private ClearableEditTextWithIcon userInfoID;
    private RadioGroup sexRadioGroup;
    private BaseButton registerInfoComplete;


    private String sexType = "";
    private String userAvatar;
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (userInfoNickName.getText().length() > 0) {
                registerInfoComplete.setEnabled(true);
            } else {
                registerInfoComplete.setEnabled(false);
            }
        }
    };

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_register_info;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        if (getIntent().hasExtra(key_USERINFO)) {
            userInfo = (UserInfo) getIntent().getExtras().getSerializable(key_USERINFO);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.register_info);
        userInfoIcon = (ImageView) findViewById(R.id.register_info_icon);
        registerInfoComplete = (BaseButton) findViewById(R.id.register_info_complete);
        userInfoNickName = (ClearableEditTextWithIcon) findViewById(R.id.register_info_nick_name_edittext);
        userInfoID = (ClearableEditTextWithIcon) findViewById(R.id.register_info_id_no_edittext);
        sexRadioGroup = (RadioGroup) findViewById(R.id.register_info_sex_radiogroup);
    }

    @Override
    protected void bindEven() {
        userInfoIcon.setOnClickListener(this);
        userInfoNickName.addTextChangedListener(watcher);
        registerInfoComplete.setEnabled(false);
        registerInfoComplete.setOnClickListener(this);
        sexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.register_info_sex_male:
                        sexType = MConstants.User_Sex_Type_Male;
                        break;
                    case R.id.register_info_sex_female:
                        sexType = MConstants.User_Sex_Type_Female;
                        break;
                }
            }
        });
    }

    @Override
    protected void setView() {
        if (userInfo != null) {
            userInfoID.setHint("ID:" + userInfo.userId);
            if (!TextUtils.isEmpty(userInfo.userName)) {
                userInfoNickName.setText(userInfo.userName);
            }
            if (!TextUtils.isEmpty(userInfo.gender)) {
                if (userInfo.gender.equals(MConstants.User_Sex_Type_Male)) {
                    sexRadioGroup.check(R.id.register_info_sex_male);
                } else if (userInfo.gender.equals(MConstants.User_Sex_Type_Female)) {
                    sexRadioGroup.check(R.id.register_info_sex_female);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_info_complete:
                if (TextUtils.isEmpty(userAvatar)) {
                    showToast("您还没选择头像");
                    return;
                }

                if (!VerifyUtils.checkNickName(RegisterInfoActivity.this, userInfoNickName.getText().toString())) {
                    return;
                }
                if (TextUtils.isEmpty(sexType)) {
                    showToast("请选择性别");
                    return;
                }
                updateUserInfo();

                break;

            case R.id.register_info_icon:
                CommonUtil.choicePicture(RegisterInfoActivity.this, SelectPicActivity.class, 1);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MConstants.PHOTO_REQUEST_SELECT) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    File file = null;
                    if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_CONTENT)) {
                        String imagePath = CommonUtil.getRealPathFromUri(RegisterInfoActivity.this, uri);
                        file = new File(imagePath);
                    } else if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_FILE)) {
                        try {
                            file = new File(new URI(uri.toString()));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    if (file != null) {
                        Logger.i("拍照或选择的文件路径为" + file.getPath());
                        ossAuth(file.getPath());
                    } else {
                        Logger.e("拍照或选择的文件生成为空，可能出现异常");
                    }

                }
            } else if (requestCode == MConstants.REQUESTCODE_UPLOAD) {
                if (data.hasExtra(UpLoadFileActivity.KEY_RESULT_URL)) {
                    userAvatar = data.getStringExtra(UpLoadFileActivity.KEY_RESULT_URL);
                    Logger.i("上传成功！图片地址：" + userAvatar);
                    //显示头像
                    ImageLoaderKit.getInstance().displayImage(userAvatar, userInfoIcon);
                }
            }
        }
    }

    /**
     * 上传头像
     */
    public void ossAuth(final String filePath) {
        userModel.ossAuth(MConstants.UPLOAD_TYPE_AVATAR, new Callback<OssAuth>() {
            @Override
            public void onSuccess(OssAuth ossAuth) {
                //调用上传
                Intent intent = new Intent(RegisterInfoActivity.this, UpLoadFileActivity.class);
                intent.putExtra(UpLoadFileActivity.KEY_OSSAUTH, ossAuth);
                intent.putExtra(UpLoadFileActivity.KEY_FILEPATH, filePath);
                intent.putExtra(UpLoadFileActivity.KEY_SHOW_UPLOAD_PROGRESS, false);
                startActivityForResult(intent, MConstants.REQUESTCODE_UPLOAD);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo() {

        UserInfo userInfo = new UserInfo();
        userInfo.avatarUrl = userAvatar;
        userInfo.userName = userInfoNickName.getText().toString();
        userInfo.gender = sexType;

        userModel.updateUserInfo(userInfo, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                //跳转到个人中心
                BaseApplication.getInstance().goToMainActivity(RegisterInfoActivity.this, MConstants.MENU_PERSONAL);
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
