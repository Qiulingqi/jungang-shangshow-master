package com.shangshow.showlive.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.BaseApplication;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.fragment.FragmentFavourite;
import com.shangshow.showlive.controller.fragment.FragmentHome;
import com.shangshow.showlive.controller.fragment.FragmentMerchant;
import com.shangshow.showlive.controller.fragment.FragmentSuperStar;
import com.shangshow.showlive.controller.fragment.TabEntity;
import com.shangshow.showlive.controller.liveshow.LaunchLiveActivity;
import com.shangshow.showlive.controller.personal.account.PersonCenterActivity;
import com.shangshow.showlive.widget.MainPopWin;
import com.shaojun.widget.tablayout.CommonTabLayout;
import com.shaojun.widget.tablayout.listener.CustomTabEntity;
import com.shaojun.widget.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"主页", "星尚", "", "星咖", "星品"};
    private int[] mIconUnselectIds = {R.mipmap.icon_tab_home_n, R.mipmap.icon_tab_favourite_n, 0, R.mipmap.icon_tab_superstar_n, R.mipmap.icon_tab_merchant_n};
    private int[] mIconSelectIds = {R.mipmap.icon_tab_home_p, R.mipmap.icon_tab_favourite_p, 0, R.mipmap.icon_tab_superstar_p, R.mipmap.icon_tab_merchant_p};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private CommonTabLayout mainTabLayout;
    private ImageView iv_main_center;

    private int currIndex = -1;
    private Fragment currentFragment;
    private int currentPosition = -1;

    //解决fragment重叠的问题【但是有弊端】
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //这里不用调用super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(BaseApplication.getInstance().getCurrentIndex() == MConstants.MENU_PERSONAL) {
//            if (!CacheCenter.getInstance().isLogin()) {
//                switchTab(MConstants.MENU_HOME);
//                return;
//            }
//        }
        switchTab(BaseApplication.getInstance().getCurrentIndex());
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(false);
//        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
//            public void onEvent(StatusCode status) {
//                // 判断在线状态，如果为被其他端踢掉，做登出操作
//                Log.i("json", "status:" + status);
//            }
//        }, true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.inShow(false);

        mainTabLayout = (CommonTabLayout) findViewById(R.id.mainTabLayout);
        iv_main_center = (ImageView) findViewById(R.id.iv_main_center);

        //tabs
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mFragments.add(FragmentHome.newInstance("主页"));
        mFragments.add(FragmentFavourite.newInstance("星尚"));
        mFragments.add(FragmentFavourite.newInstance("星尚"));
        mFragments.add(FragmentSuperStar.newInstance("星咖"));
        mFragments.add(FragmentMerchant.newInstance("星品"));
//        mFragments.add(FragmentPersonal.newInstance("个人"));

        mainTabLayout.setTabData(mTabEntities);
        mainTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
//                Log.i("json", position + "");
//                if(position == MConstants.MENU_PERSONAL){
//                    if (!CacheCenter.getInstance().isLogin()) {
//                        startActivity(LoginActivity.class);
//                        return;
//                    }
//                }
                if(position == 2){
                    currentPosition = position;
                    showPopFormBottom();
                    return;
                }
                switchTab(position);
            }

            @Override
            public void onTabReselect(int position) {
//                if (position == 0) {
//                    mainTabLayout.showMsg(0, new Random().nextInt(100) + 1);
//                }
            }
        });

//        //两位数
//        mainTabLayout.showMsg(0, 55);
//        mainTabLayout.setMsgMargin(0, -5, 5);

    }

    @Override
    protected void bindEven() {
        iv_main_center.setOnClickListener(this);
    }

    @Override
    protected void setView() {

    }

    private void switchTab(int currIndex) {
        BaseApplication.getInstance().setCurrentIndex(currIndex);
        mainTabLayout.setCurrentTab(currIndex);
        switchFragment(mFragments.get(currIndex));
    }

    /**
     * @param targetFragment
     */
    private void switchFragment(Fragment targetFragment) {
        if (targetFragment == null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        //第一次使用switchFragment()时currentFragment为null，所以要判断一下
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        //如已添加过，则show就行了
        if (targetFragment.isAdded()) {
            transaction.show(targetFragment);
        } else {
            transaction.add(R.id.content_frame, targetFragment);
        }
        transaction.commit();
        currentFragment = targetFragment;
    }

    long last_back_time = 0;

    @Override
    public void onBackPressed() {
        //		LONG_DELAY = 3500; // 3.5 seconds
        //		SHORT_DELAY = 2000; // 2 seconds
        long cur_time = System.currentTimeMillis();
        if ((cur_time - last_back_time) < 2000) {
            super.onBackPressed();
            // YtTemplate.release(this);
        } else {
            last_back_time = cur_time;
            ToastUtils.show("连续点击退出..");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_main_center:{
                showPopFormBottom();
            }
            break;
        }
    }

    //  直播  个人中心
    public void showPopFormBottom() {
        MainPopWin takePhotoPopWin = new MainPopWin(this);
        takePhotoPopWin.setOnMainPopListener(new MainPopWin.OnMainPopListener() {
            @Override
            public void video() {
                if(!CacheCenter.getInstance().isLogin()){
                    startActivity(LoginActivity.class);
                }else{
                    /**
                     * 此处  都可以开启直播
                     */
                    if (!MConstants.USER_TYPE_COMMONUSER.equals(CacheCenter.getInstance().getCurrUser().userType)) {
                     /*   CustomDialogHelper.OneButtonDialog(MainActivity.this, "提示", "请申请星尚／星咖／星品", "关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });*/
                        //开启直播
                        Intent intent = new Intent(MainActivity.this, LaunchLiveActivity.class);
                        intent.putExtra("upload_type", 0);
                        startActivity(intent);
                    }else {
                        //开启直播
                        Intent intent = new Intent(MainActivity.this, LaunchLiveActivity.class);
                        intent.putExtra("upload_type", 0);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void userCenter() {
                if(!CacheCenter.getInstance().isLogin()){
                    startActivity(LoginActivity.class);
                }else{
                    //进入个人中心
                    startActivity(PersonCenterActivity.class);
                }
            }
        });
        takePhotoPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

}
