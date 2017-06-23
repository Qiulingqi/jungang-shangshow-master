package com.shangshow.showlive.controller.personal.applyfor;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;

/**
 * 申请认证
 */

public class ApplyForAuthenticationActivity extends BaseActivity implements View.OnClickListener {
    public static int APPLY_FOR_REQUEST = 1;
    private LinearLayout merchantLayout, favouriteLayout, superstarLayout;
    private UserModel userModel;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_apply_for_authenication;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        userModel = new UserModel(this);
        setSwipeBackEnable(true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.title_apply_for_authentication);
        merchantLayout = (LinearLayout) findViewById(R.id.apply_for_merchant_layout);
        favouriteLayout = (LinearLayout) findViewById(R.id.apply_for_favourite_layout);
        superstarLayout = (LinearLayout) findViewById(R.id.apply_for_superstar_layout);
    }

    @Override
    protected void bindEven() {
        merchantLayout.setOnClickListener(this);
        favouriteLayout.setOnClickListener(this);
        superstarLayout.setOnClickListener(this);
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
        switch (v.getId()) {
            case R.id.apply_for_merchant_layout:
                userModel.businessApplyDetail(currentUser.userId, new Callback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        if (userInfo != null) {
                            if ("APL".equals(userInfo.status)) {
                                showToast("您已申请" + getResources().getString(R.string.shangjia) + "认证");
                                return;
                            }
                        }
                        startActivityForResult(new Intent(ApplyForAuthenticationActivity.this, ApplyForMerchantActivity.class), APPLY_FOR_REQUEST);
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });
                break;
            case R.id.apply_for_favourite_layout:
                userModel.favouriteApplyDetail(currentUser.userId, new Callback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        if (userInfo != null) {
                            if ("APL".equals(userInfo.status)) {
                                showToast("您已申请" + getResources().getString(R.string.wanghong) + "认证");
                                return;
                            }
                        }
                        startActivityForResult(new Intent(ApplyForAuthenticationActivity.this, ApplyForFavouriteActivity.class), APPLY_FOR_REQUEST);
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });
                break;
            case R.id.apply_for_superstar_layout:
                userModel.starApplyDetail(currentUser.userId, new Callback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        if (userInfo != null) {
                            if ("APL".equals(userInfo.status)) {
                                showToast("您已申请" + getResources().getString(R.string.xingka) + "认证");
                                return;
                            }
                        }
                        startActivityForResult(new Intent(ApplyForAuthenticationActivity.this, ApplyForSuperStarActivity.class), APPLY_FOR_REQUEST);
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
