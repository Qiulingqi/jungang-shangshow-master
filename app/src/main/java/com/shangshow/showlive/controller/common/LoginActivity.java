package com.shangshow.showlive.controller.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.demo.config.preference.Preferences;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.string.MD5;
import com.netease.nim.uikit.model.UserInfo;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseApplication;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.CustomVideoView;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.utils.SharedPreferencesUtils;
import com.shangshow.showlive.common.utils.Tenants;
import com.shangshow.showlive.common.utils.VerifyUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.body.UserBody;
import com.shangshow.showlive.network.service.models.body.WeChatUserInfo;
import com.shaojun.utils.log.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.util.Map;


/**
 * 登陆
 *
 * @author
 */
public class  LoginActivity extends Activity implements View.OnClickListener {

    private AbortableFuture<LoginInfo> loginRequest;
    private UserModel userModel;
    private ClearableEditTextWithIcon loginAccount;//账户
    private ClearableEditTextWithIcon loginPassword;//密码
    private TextView loginForgetPassword;//忘记密码
    private BaseButton loginBtn;//登陆
    private ImageView wenxinLogin;//微信登陆
    private UMShareAPI mShareAPI = null;
    private CustomVideoView vv;
    private int i = 0;
    private int TIME = 1000;
    private TextView login_regesit;
    private UMAuthListener umAuthListener2 = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
//            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthListener2);
            WeChatUserInfo userInfo = new WeChatUserInfo();
            userInfo.setUserName(data.get("screen_name"));
            if ("1".equals(data.get("gender"))) {
                userInfo.setGender("M");
            } else if ("2".equals(data.get("gender"))) {
                userInfo.setGender("F");
            } else {
                userInfo.setGender("U");
            }
            if(data.get("profile_image_url")==null){
                userInfo.setAvatarUrl("");
            }else{
                userInfo.setAvatarUrl(data.get("profile_image_url"));
            }
            userInfo.setOpenId(data.get("openid"));
            doWeChatLogin(LoginActivity.this, userInfo);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * auth callback interface
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            Log.d("user info", "user info:" + data.toString());
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthListener2);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (loginAccount.getText().length() > 0 && loginPassword.getText().length() > 0) {
                loginBtn.setEnabled(true);
            } else {
                loginBtn.setEnabled(false);
            }
        }
    };
    private TextView shangshow_fuwuxieyi;


/*    @Override
    protected int getActivityLayout() {
        return R.layout.activity_login;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userModel = new UserModel(this);
        /** init auth api**/
        mShareAPI = UMShareAPI.get(this);
        initView();
    }

    private void initView() {
        //  播放视频
       // vv = (CustomVideoView) findViewById(R.id.firstGIfView);
        // 设置图片资源
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.ss_login_video;
      //  vv.setVideoURI(Uri.parse(uri));
     //   handler.postDelayed(runnable, TIME); //每隔1s执行

        vv = (CustomVideoView) findViewById(R.id.firstGIfView);
        //设置播放加载路径
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ss_login_video));
        //播放
        vv.start();
        //循环播放
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vv.start();
            }
        });
        /**
         * 商秀服务协议
         */

        shangshow_fuwuxieyi = (TextView) findViewById(R.id.shangshow_fuwuxieyi);



        login_regesit = (TextView) findViewById(R.id.login_regesit);
        loginAccount = (ClearableEditTextWithIcon) findViewById(R.id.login_account_edittext);
        loginPassword = (ClearableEditTextWithIcon) findViewById(R.id.login_password_edittext);
        loginForgetPassword = (TextView) findViewById(R.id.login_forget_password);
        loginBtn = (BaseButton) findViewById(R.id.login_btn);
        wenxinLogin = (ImageView) findViewById(R.id.login_wenxin);

        loginAccount.addTextChangedListener(watcher);
        loginPassword.addTextChangedListener(watcher);
        loginForgetPassword.setOnClickListener(this);
        loginBtn.setEnabled(false);
        loginBtn.setOnClickListener(this);
        wenxinLogin.setOnClickListener(this);
        login_regesit.setOnClickListener(this);
        shangshow_fuwuxieyi.setOnClickListener(this);
    }

/*    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        *//** init auth api**//*
        mShareAPI = UMShareAPI.get( this );
    }*/

/*    @Override
    protected void initWidget() {
        super.initWidget();

        //  播放视频
        vv = (VideoView) findViewById(R.id.firstGIfView);
        // 设置图片资源
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.ss_login_video;
        vv.setVideoURI(Uri.parse(uri));
        handler.postDelayed(runnable, TIME); //每隔1s执行
*//*        titleBarView.initCenterTitle(getString(R.string.login));
        titleBarView.initRight(getString(R.string.register), 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });*//*
        login_regesit = (TextView) findViewById(R.id.login_regesit);
        loginAccount = (ClearableEditTextWithIcon) findViewById(R.id.login_account_edittext);
        loginPassword = (ClearableEditTextWithIcon) findViewById(R.id.login_password_edittext);
        loginForgetPassword = (TextView) findViewById(R.id.login_forget_password);
        loginBtn = (BaseButton) findViewById(R.id.login_btn);
        wenxinLogin = (ImageView) findViewById(R.id.login_wenxin);

    }*/

/*
    @Override
    protected void bindEven() {

        loginAccount.addTextChangedListener(watcher);
        loginPassword.addTextChangedListener(watcher);
        loginForgetPassword.setOnClickListener(this);
        loginBtn.setEnabled(false);
        loginBtn.setOnClickListener(this);
        wenxinLogin.setOnClickListener(this);
        login_regesit.setOnClickListener(this);

    }
*/
/*

    @Override
    protected void setView() {

    }
*/

    @Override
    public void onClick(View v) {
        CommonUtil.keyBoardCancel(LoginActivity.this);
        switch (v.getId()) {
            case R.id.login_btn:
                if (VerifyUtils.checkMobileNO(LoginActivity.this, loginAccount.getText().toString())
                        && VerifyUtils.checkPassword(LoginActivity.this, loginPassword.getText().toString())) {
                    doLogin();
                }
                break;
            case R.id.login_forget_password:
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                break;
            case R.id.login_regesit:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.shangshow_fuwuxieyi:
            //    startActivity(Tenants.class, "tenants", "file:///android_asset/gongyue.html");
                Intent intent = new Intent(LoginActivity.this
                        , Tenants.class);
                intent.putExtra("tenants","file:///android_asset/xieyi.html");
                startActivity(intent);
                break;
            case R.id.login_wenxin:
                SHARE_MEDIA platform = null;
                platform = SHARE_MEDIA.WEIXIN;

                mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);

//                ShangXiuUtil.wenxinLoginAuthorize(LoginActivity.this, new PlatformActionListener() {
//                    @Override
//                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                        ShangXiuUtil.authorize(LoginActivity.this,platform);
//                    }
//
//                    @Override
//                    public void onError(Platform platform, int i, Throwable throwable) {
//                        Logger.e("onError");
//                    }
//
//                    @Override
//                    public void onCancel(Platform platform, int i) {
//                        Logger.e("onCancel");
//                    }
//                });
                break;
            default:
                break;
        }
    }

    private void doLogin() {
        UserBody userBody = new UserBody();
        userBody.userMobile = loginAccount.getText().toString();
        userBody.userPwd = loginPassword.getText().toString();
        userModel.login(userBody, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo dto) {
                SharedPreferencesUtils.setParam(LoginActivity.this, "sourceFrom", "SSApp");
                CacheCenter.getInstance().setCurrUser(dto);
                BaseApplication.getInstance().goToMainActivity(LoginActivity.this, MConstants.MENU_HOME);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    private void doWeChatLogin(final Context context, WeChatUserInfo weChatUserInfo) {
        userModel.wechatLogin2(weChatUserInfo, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo dto) {
                SharedPreferencesUtils.setParam(LoginActivity.this, "sourceFrom", "WX");
                CacheCenter.getInstance().setCurrUser(dto);
                //登陆云信
                ShangXiuUtil.loginNim(dto.userId);
                //成功跳转
                BaseApplication.getInstance().goToMainActivity((LoginActivity) context, MConstants.MENU_PERSONAL);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                Logger.e(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
//        String appKey = "83e72492b112b014dc590df3e28278d0";
//        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
//                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);
//
//        return isDemo ? MD5.getStringMD5(password) : password;
        return MD5.getStringMD5(password);
    }

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

  /*  //  定时器   循环播放视频
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, TIME);
                vv.start();
                System.out.println("do...");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);//正常退出App
    }
}
