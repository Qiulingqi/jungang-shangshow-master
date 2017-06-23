package com.shangshow.showlive.controller.personal.setting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.BaseApplication;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.LogicView.UserInfoView;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.SharedPreferencesUtils;
import com.shangshow.showlive.controller.common.AboutUsActivity;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.common.UpLoadFileActivity;
import com.shangshow.showlive.controller.common.picture.SelectPicActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.OssAuth;
import com.shaojun.utils.log.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;


public class SettingActivity extends BaseActivity implements View.OnClickListener {

    public static String key_USERINFO = "userInfo";
    private UserModel userModel;
    private UserInfo userInfo;
    private RelativeLayout about_us_layout;
    private RelativeLayout nickNameLayout;
    private RelativeLayout genderLayout;
    private RelativeLayout addressLayout;
    private RelativeLayout bindmobileLayout;
    private TextView nick_name;
    private TextView gender;
    private TextView address;
    private TextView bind_mobile;
    private UserInfoView userInfoView;
    private Button loginOut;
    //上传头像返回
    private String userAvatar;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_setting;
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
        titleBarView.initCenterTitle(R.string.setting);
        userInfoView = (UserInfoView) findViewById(R.id.setting_memberinfo);
        //不显示视频、关注、粉丝
        userInfoView.setShowAttention(false);
        about_us_layout = (RelativeLayout) findViewById(R.id.about_us_layout);
        nickNameLayout = (RelativeLayout) findViewById(R.id.setting_nick_name_layout);
        genderLayout = (RelativeLayout) findViewById(R.id.setting_gender_layout);
        addressLayout = (RelativeLayout) findViewById(R.id.setting_address_layout);
        bindmobileLayout = (RelativeLayout) findViewById(R.id.setting_bindmobile_layout);
        nick_name = (TextView) findViewById(R.id.setting_nick_name);
        gender = (TextView) findViewById(R.id.setting_gender);
        bind_mobile = (TextView) findViewById(R.id.setting_bindmobile);
        address = (TextView) findViewById(R.id.setting_address);
        loginOut = (Button) findViewById(R.id.setting_login_out);

    }

    @Override
    protected void bindEven() {
        //用户信息头像点击事件
        userInfoView.setOnUserIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.choicePicture(SettingActivity.this, SelectPicActivity.class, 1);
            }
        });
        about_us_layout.setOnClickListener(this);
        nickNameLayout.setOnClickListener(this);
        genderLayout.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        bindmobileLayout.setOnClickListener(this);
        loginOut.setOnClickListener(this);
    }

    @Override
    protected void setView() {
        userInfo = CacheCenter.getInstance().getCurrUser();
        userInfoView.setUserInfo(userInfo);
        if (userInfo != null) {
            nick_name.setText(userInfo.userName);
            if (userInfo.gender != null) {
                if (userInfo.gender.equals(MConstants.User_Sex_Type_Male)) {
                    gender.setText(MConstants.User_Sex_Type_Male_VALUE);
                } else if (userInfo.gender.equals(MConstants.User_Sex_Type_Female)) {
                    gender.setText(MConstants.User_Sex_Type_Female_VALUE);
                }
            }
        }
        if ("SSApp".equals(SharedPreferencesUtils.getParam(SettingActivity.this, "sourceFrom", ""))) {
            bindmobileLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_nick_name_layout:
                if (userInfo != null) {
                    Intent intent = new Intent(SettingActivity.this, EditNickNameActivity.class);
                    intent.putExtra(EditNickNameActivity.key_NICKNAME, userInfo.userName);
                    startActivityForResult(intent, MConstants.REQUESTCODE_UPDATE_NICKNAME);
                }
                break;
            case R.id.setting_gender_layout:
                if (userInfo != null) {
                    Intent intent = new Intent(SettingActivity.this, EditGenderActivity.class);
                    intent.putExtra(EditGenderActivity.key_GENDER, userInfo.gender);
                    startActivityForResult(intent, MConstants.REQUESTCODE_UPDATE_SEX_UPDATE);
                }
                break;
            case R.id.setting_address_layout:
                startActivity(new Intent(SettingActivity.this, AddressListActivity.class));
                break;
            case R.id.setting_bindmobile_layout:
                startActivity(new Intent(SettingActivity.this, BindMobileActivity.class));
                break;
            case R.id.about_us_layout:
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                break;
            case R.id.setting_login_out:
                //  做退出弹窗提示
                BaseApplication.getInstance().loginOut();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
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
                        String imagePath = CommonUtil.getRealPathFromUri(SettingActivity.this, uri);
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
                    String url = data.getStringExtra(UpLoadFileActivity.KEY_RESULT_URL);
                    Logger.i("上传成功！图片地址：" + url);
                    //上传成功更新用户信息
                    updateUserAvatar(url);
                }
            } else if (requestCode == MConstants.REQUESTCODE_UPDATE_NICKNAME) {
                //修改用户昵称返回
                setView();
            } else if (requestCode == MConstants.REQUESTCODE_UPDATE_SEX_UPDATE) {
                //修改用户昵称返回
                setView();
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
                Intent intent = new Intent(SettingActivity.this, UpLoadFileActivity.class);
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
     * 更新用户信息(头像)
     */
    public void updateUserAvatar(final String userAvatar) {
        UserInfo updateUserInfo = new UserInfo();
        updateUserInfo.avatarUrl = userAvatar;
        userModel.updateUserInfo(updateUserInfo, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                ImageLoaderKit.getInstance().displayImage(userAvatar, userInfoView.getUserIcon());
                showToast("修改头像成功");
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
