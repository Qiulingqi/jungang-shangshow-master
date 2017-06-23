package com.shangshow.showlive.controller.personal.account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.LogicView.UserInfoView;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.FileHelper;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.controller.adapter.FunctionBtnGridAdapter;
import com.shangshow.showlive.controller.liveshow.LaunchLiveActivity;
import com.shangshow.showlive.controller.personal.cooperate.MyCooperateActivity;
import com.shangshow.showlive.controller.personal.goods.MyGoodsActivity;
import com.shangshow.showlive.controller.personal.income.MyEarnListActivity;
import com.shangshow.showlive.controller.personal.order.MyOrderListActivity;
import com.shangshow.showlive.controller.personal.reward.MyRewardListActivity;
import com.shangshow.showlive.controller.personal.setting.SettingActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;

import java.io.File;

/**
 * Created by Super-me on 2016/10/26.
 */
//  个人中心
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    private UserModel userModel;
    private Context mContext;
    private PtrFrameLayout homePersonalPtrFrameLayout;
    private View loginedLayout;
    private UserInfoView userInfoView;
    private GridView functionGridView;//功能按钮
    private TextView personalCreateLive;//发起直播
    private TextView personalUploadVideo;//上传视频

    @Override
    protected int getActivityLayout() {
        return R.layout.fragment_tab_personal;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        this.mContext = this;
        userModel = new UserModel(mContext);
//        titleBarView = (TitleBarView) findViewById(R.id.personal_title_layout);
        titleBarView.initCenterTitle(R.string.person_center);
        titleBarView.initRight("", R.mipmap.icon_setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intent);
            }
        });
        homePersonalPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.home_personal_ptr_framentlayout);
        CommonUtil.SetPtrRefreshConfig(mContext, homePersonalPtrFrameLayout, MConstants.REFRESH_HEADER_WHITE);
        homePersonalPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateUserInfo();
            }
        });
        /*gotoLogin = (Button) rootView.findViewById(R.id.personal_goto_login);*/
        loginedLayout = findViewById(R.id.personal_logined_layout);
        /*unloginLayout = rootView.findViewById(R.id.personal_unlogin_layout);*/
        loginedLayout.setVisibility(View.GONE);
        /*unloginLayout.setVisibility(View.GONE);*/
        //用户信息
        userInfoView = (UserInfoView) findViewById(R.id.personal_memberinfo);
        userInfoView.setOnUserIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SettingActivity.class));
            }
        });
        personalCreateLive = (TextView) findViewById(R.id.personal_create_live);
        personalUploadVideo = (TextView) findViewById(R.id.personal_upload_video);
        functionGridView = (GridView) findViewById(R.id.personal_function_gridview);
    }

    @Override
    protected void bindEven() {
        personalCreateLive.setOnClickListener(this);
        personalUploadVideo.setOnClickListener(this);
    }

    @Override
    protected void setView() {

    }


    private void setLoginStateView(boolean isLogin, UserInfo userInfo) {
        if (isLogin) {
            loginedLayout.setVisibility(View.VISIBLE);
            /*unloginLayout.setVisibility(View.GONE);*/

            //功能按钮
            functionGridView.setAdapter(new FunctionBtnGridAdapter(mContext, ShangXiuUtil.getFunctionBtns(mContext)));
            functionGridView.setOnItemClickListener(this);
            userInfoView.setUserInfo(userInfo);
        } else {
            loginedLayout.setVisibility(View.GONE);
           /* unloginLayout.setVisibility(View.VISIBLE);*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loginOut();
    }

    public void loginOut() {
        if (CacheCenter.getInstance().isLogin()) {
            titleBarView.setBtnRightEnabled(true);
            UserInfo userInfo = CacheCenter.getInstance().getCurrUser();
            if (userInfo != null) {
                setLoginStateView(true, userInfo);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homePersonalPtrFrameLayout.autoRefresh(false);
                    }
                }, MConstants.DELAYED);
            }
            updateUserInfo();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.personal_goto_login:
//                mContext.startActivity(new Intent(mContext, LoginActivity.class));
//                break;
            case R.id.personal_create_live: {
                Intent intent = new Intent(mContext, LaunchLiveActivity.class);
                intent.putExtra("upload_type", 0);
                startActivity(intent);
            }
            break;
            case R.id.personal_upload_video: {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
                Intent wrapperIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapperIntent, MConstants.FILE_REQUEST_SELECT);

             /*   Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);*/

            }
            break;
        }
    }

    /**
     * 获取用户信息
     */
    public void updateUserInfo() {
        long userId = CacheCenter.getInstance().getTokenUserId();
        userModel.getUserInfo(userId, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                homePersonalPtrFrameLayout.refreshComplete();
                setLoginStateView(true, userInfo);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                homePersonalPtrFrameLayout.refreshComplete();
            }
        }, false, false);
    }

    //    当前是普通用户登陆：商品、合作  普通用户 不能点   点击“您不是星品、星尚或星咖”
//    当前是 星尚，星咖用户登陆：点击星品进来，列出的是星品列表对应的商品列表
//    当前是星品用户登陆：点击星品进来，列出的是星品自己的商品列表，且可以新增商品
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        UserInfo userInfo = CacheCenter.getInstance().getCurrUser();
        switch (position) {
            case 0://账户
                startActivity(new Intent(mContext, PersonalAccountListActivity.class));
                break;
            case 1://收益
                startActivity(new Intent(mContext, MyEarnListActivity.class));
                break;
            case 2://打赏
                startActivity(new Intent(mContext, MyRewardListActivity.class));
                break;
            case 3://商品
                if (!userInfo.userType.equals(MConstants.USER_TYPE_COMMONUSER)) {
                    intent = new Intent(mContext, MyGoodsActivity.class);
//                intent.putExtra(MyGoods1Activity.KEY_USERTYPE, MyGoods1Activity.VALUE_MERCHANT);
                    startActivity(intent);
                } else {
                    ToastUtils.show("请申请网红/商家/星咖");
                }
//                if (!userInfo.userType.equals(MConstants.USER_TYPE_COMMONUSER)) {
//                    //星咖和星尚
//                    if (userInfo.userType.equals(MConstants.USER_TYPE_SUPERSTAR) || userInfo.userType.equals(MConstants.USER_TYPE_FAVOURITE)) {
//                        intent = new Intent(mContext, MyGoods1Activity.class);
//                        intent.putExtra(MyGoods1Activity.KEY_USERTYPE, MyGoods1Activity.VALUE_FAVOURITE_OR_SUPERSTAR);
//                        startActivity(intent);
//                    } else if (userInfo.userType.equals(MConstants.USER_TYPE_MERCHANT)) {
//                        intent = new Intent(mContext, MyGoods1Activity.class);
//                        intent.putExtra(MyGoods1Activity.KEY_USERTYPE, MyGoods1Activity.VALUE_MERCHANT);
//                        startActivity(intent);
//                    }
//                } else {
//                    ToastUtils.show("您不是星品、星尚或星咖!");
//                }
                break;
            case 4://公益
                CustomDialogHelper.OneButtonDialog(mContext, "提示", "敬请期待", "关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                break;
            case 5://竞拍
                CustomDialogHelper.OneButtonDialog(mContext, "提示", "敬请期待", "关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                break;
            case 6://合作
                if (!userInfo.userType.equals(MConstants.USER_TYPE_COMMONUSER)) {
                    startActivity(new Intent(mContext, MyCooperateActivity.class));
                } else {

                    ToastUtils.show("请申请网红/商家/星咖");
                }
                break;
            case 7://订单
                startActivity(new Intent(mContext, MyOrderListActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.FILE_REQUEST_SELECT) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri == null) {
                    return;
                }
                try {
                    if (uri.toString().contains("/images/")) {
                        showToast("请选择视频");
                        return;
                    }
                    String filePath = FileHelper.getRealFilePath(PersonCenterActivity.this, uri);
                    File file = new File(filePath);
                    String path = file.getAbsolutePath();
                    Intent intent = new Intent(mContext, LaunchLiveActivity.class);
                    intent.putExtra("upload_type", 1);
                    intent.putExtra("uploadPath", path + "");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(e.getMessage() + "");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }

}
