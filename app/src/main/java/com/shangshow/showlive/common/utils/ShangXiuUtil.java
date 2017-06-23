package com.shangshow.showlive.common.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.netease.nim.uikit.cache.DemoCache;
import com.netease.nim.demo.config.preference.UserPreferences;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.network.service.models.FunctionBtn;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.utils.log.Logger;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.util.ArrayList;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @auth:jeremy
 * @see: [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ShangXiuUtil {
    /**
     * 分享弹出框
     *
     * @param context
     */
    public static void showShare(Context context) {
        ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();
        platforms.clear();
//        for (SHARE_MEDIA e : SHARE_MEDIA.values()) {
//            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())){
//                platforms.add(e.toSnsPlatform());
//            }
//        }
//        new ShareAction(context).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE)
//                .withTitle(Defaultcontent.title)
//                .withText(Defaultcontent.text+"——来自友盟分享面板")
//                .withMedia(new UMImage(context,"http://dev.umeng.com/images/tab2_1.png"))
//                .withTargetUrl("https://wsq.umeng.com/")
//                .setCallback(umShareListener)
//                .open();
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(context.getString(cn.sharesdk.onekeyshare.R.string.ssdk_oks_share));
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(context.getString(com.shangshow.showlive.R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
//
////        oks.setPlatform("Wechat");
//        // 启动分享GUI
//        oks.show(context);
    }

//    public static void wenxinLoginAuthorize(Context context, PlatformActionListener paListener) {
//        Platform wechat = ShareSDK.getPlatform(context, Wechat.NAME);
//        wechat.setPlatformActionListener(paListener);
//        wechat.authorize();
//    }
//
//
//    public static void authorize(Context context,Platform plat) {
//        if (plat == null) {
////            popupOthers();
//            return;
//        }
//        //判断指定平台是否已经完成授权
//        if (plat.isAuthValid()) {
//            String userId = plat.getDb().getUserId();
//
//            if (userId != null) {
//                doWeChatLogin(context,userId);
////                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
////                login(plat.getName(), userId, null);
//
//                return;
//            }
//        }
////        plat.setPlatformActionListener(context);
//        // true不使用SSO授权，false使用SSO授权
//        plat.SSOSetting(true);
//        //获取用户资料
//        plat.showUser(null);
//    }


    /**
     * 个人中心功能按钮
     *
     * @param context
     * @return
     */
    public static ArrayList<FunctionBtn> getFunctionBtns(Context context) {
        ArrayList<FunctionBtn> functionBtns = new ArrayList<>();
        String[] textItems = context.getResources().getStringArray(R.array.function_text);
        TypedArray ar = context.getResources().obtainTypedArray(R.array.function_images);
        int len = ar.length();
        int[] iconItems = new int[len];
        for (int i = 0; i < len; i++)
            iconItems[i] = ar.getResourceId(i, 0);
        ar.recycle();

        if (textItems.length != iconItems.length || textItems.length < iconItems.length) {
            Logger.e("functionBtns is make error!");
            return functionBtns;
        } else {
            for (int i = 0; i < textItems.length; i++) {
                FunctionBtn functionBtn = new FunctionBtn(textItems[i], iconItems[i]);
                functionBtns.add(functionBtn);
            }
        }
        return functionBtns;
    }


    /**
     * 根据用户类型显示typeIcon
     *
     * @param userTypeIcon
     * @param userType
     */
    public static void setUserTypeIcon(ImageView userTypeIcon, String userType) {
        if (!TextUtils.isEmpty(userType)) {
            if (MConstants.USER_TYPE_COMMONUSER.equals(userType)) {
                userTypeIcon.setVisibility(View.GONE);
            } else if (MConstants.USER_TYPE_FAVOURITE.equals(userType)) {
                userTypeIcon.setVisibility(View.VISIBLE);
                userTypeIcon.setImageResource(R.mipmap.icon_user_mark_favourite);
            } else if (MConstants.USER_TYPE_SUPERSTAR.equals(userType)) {
                userTypeIcon.setVisibility(View.VISIBLE);
                userTypeIcon.setImageResource(R.mipmap.icon_user_mark_superstar);
            } else if (MConstants.USER_TYPE_MERCHANT.equals(userType)) {
                userTypeIcon.setVisibility(View.VISIBLE);
                userTypeIcon.setImageResource(R.mipmap.icon_user_mark_merchant);
            }
        } else {
            userTypeIcon.setVisibility(View.GONE);
        }
    }


    /**
     * 刷新分页逻辑{如果是刷新炒作，重置页码请求}
     *
     * @param loadType
     * @param currPage
     * @return
     */
    public static PageBody refreshPagerBodey(long loadType, long currPage) {
        PageBody pageBody = new PageBody();
        //如果刷新，请求第一页
        if (loadType == MConstants.DATA_4_REFRESH) {
            pageBody.pageNum = MConstants.PAGE_INDEX;
        } else {
            pageBody.pageNum = currPage;
        }
        pageBody.pageSize = MConstants.PAGE_SIZE;
        pageBody.orders = "";
        return pageBody;
    }


    /**
     * 代码动态化布局
     * 不要问我这个方法是干什么的，也不要问为什么这么写。。。
     *
     * @param context
     * @param gridAdsLayout
     * @return
     */
    public static ArrayList<ImageView> merChantGridAdsView(Context context, LinearLayout gridAdsLayout) {
        ArrayList<ImageView> adsImages = new ArrayList<>();
        int width = ScreenUtil.getScreenWidth(context);

        LinearLayout layoutA = new LinearLayout(context);
        layoutA.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutA.setOrientation(LinearLayout.HORIZONTAL);
        //7:5   8:5
        int useWidth = width - MConstants.MERCHANT_GRID_ADS_GAP;
        int banner1Width = (int) (useWidth * (7 / 15f));
        int banner2Width = (int) (useWidth * (8 / 15f));
        int banner1Height = (int) (banner1Width * 5 / 7f);
        int banner2Height = (int) (banner2Width * 5 / 8f);
        ImageView banner1 = new ImageView(context);
        banner1.setScaleType(ImageView.ScaleType.FIT_XY);
        banner1.setLayoutParams(new LinearLayout.LayoutParams(banner1Width, banner1Height));
        ImageView banner2 = new ImageView(context);
        banner2.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams banner2Params = new LinearLayout.LayoutParams(banner2Width, banner2Height);
        banner2Params.setMargins(MConstants.MERCHANT_GRID_ADS_GAP, 0, 0, 0);
        banner2.setLayoutParams(banner2Params);

        //+++++++++++++++++++++++++++++++++
        LinearLayout layoutB = new LinearLayout(context);
        LinearLayout.LayoutParams layoutBParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutBParams.setMargins(0, MConstants.MERCHANT_GRID_ADS_GAP, 0, 0);
        layoutB.setLayoutParams(layoutBParams);

        //左右布局 2:3   3:3
        int banner3Width = (int) (useWidth * (2 / 5f));
        int layoutB1Width = (int) (useWidth * (3 / 5f));
        int banner3Height = (int) (banner3Width * 3 / 2f);
        int layoutB1Height = layoutB1Width;
        ImageView banner3 = new ImageView(context);
        banner3.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams banner3Params = new LinearLayout.LayoutParams(banner3Width, banner3Height);
        banner3.setLayoutParams(banner3Params);


        LinearLayout layoutB1 = new LinearLayout(context);
        LinearLayout.LayoutParams layoutB1Params = new LinearLayout.LayoutParams(layoutB1Width, layoutB1Height);
        layoutB1.setOrientation(LinearLayout.VERTICAL);
        layoutB1Params.setMargins(MConstants.MERCHANT_GRID_ADS_GAP, 0, 0, 0);
        layoutB1.setLayoutParams(layoutB1Params);
        //layoutB1下面布局可用宽度[去掉上下之间间隙]
        int useLayoutB1Height = layoutB1Height - MConstants.MERCHANT_GRID_ADS_GAP;
        //上下布局 200:260
        int layoutB2Height = (int) (useLayoutB1Height * 10 / 23f);
        int banner6Height = (int) (useLayoutB1Height * 13 / 23f);
        LinearLayout layoutB2 = new LinearLayout(context);
        LinearLayout.LayoutParams layoutB2Params = new LinearLayout.LayoutParams(layoutB1Width, layoutB2Height);
        layoutB2.setOrientation(LinearLayout.HORIZONTAL);
        layoutB2.setLayoutParams(layoutB2Params);

        //layoutB1下面布局可用宽度[去掉左右之间间隙]
        int useLayoutB1Width = layoutB1Width - MConstants.MERCHANT_GRID_ADS_GAP;
        //左右布局，2张banner图  4：4   5：4
        int banner4Width = (int) (useLayoutB1Width * (4 / 9f));
        int banner5Width = (int) (useLayoutB1Width * (5 / 9f));
        int banner4Height = banner4Width;
        int banner5Height = (int) (banner5Width * 4 / 5f);

        ImageView banner4 = new ImageView(context);
        banner4.setScaleType(ImageView.ScaleType.FIT_XY);
        banner4.setLayoutParams(new LinearLayout.LayoutParams(banner4Width, banner4Height));

        ImageView banner5 = new ImageView(context);
        banner5.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams banner5Params = new LinearLayout.LayoutParams(banner5Width, banner5Height);
        banner5Params.setMargins(MConstants.MERCHANT_GRID_ADS_GAP, 0, 0, 0);
        banner5.setLayoutParams(banner5Params);

        ImageView banner6 = new ImageView(context);
        banner6.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams banner6Params = new LinearLayout.LayoutParams(layoutB1Width, banner6Height);
        banner6Params.setMargins(0, MConstants.MERCHANT_GRID_ADS_GAP, 0, 0);
        banner6.setLayoutParams(banner6Params);

        // TODO: 16/7/24 banner1
        layoutA.addView(banner1);
        // TODO: 16/7/24 banner2
        layoutA.addView(banner2);
        gridAdsLayout.addView(layoutA);

        // TODO: 16/7/24 banner3
        layoutB.addView(banner3);

        // TODO: 16/7/24 banner4
        layoutB2.addView(banner4);
        // TODO: 16/7/24 banner5
        layoutB2.addView(banner5);
        layoutB1.addView(layoutB2);
        // TODO: 16/7/24 banner6
        layoutB1.addView(banner6);
        layoutB.addView(layoutB1);
        gridAdsLayout.addView(layoutB);


        //返回banner列表
        adsImages.add(banner1);
        adsImages.add(banner2);
        adsImages.add(banner3);
        adsImages.add(banner4);
        adsImages.add(banner5);
        adsImages.add(banner6);

        return adsImages;

    }


    /**
     * 登陆云信服务【IM】
     *
     * @param account
     */
    public static void loginNim(final long account) {
        final String userId = String.valueOf(account);
        final String token = CacheCenter.getInstance().getNimToken();
        NIMClient.getService(AuthService.class).login(new LoginInfo(userId, token))
                .setCallback(new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        Logger.i("loginNim login success！");
                        CacheCenter.getInstance().setNimAccount(userId);
                        // 初始化消息提醒
                        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                        // 初始化免打扰
                        NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 302 || code == 404) {
                            ToastUtils.show("登录nim失败: " + code + CacheCenter.getInstance().getContext().getString(R.string.login_failed));
                        } else {
                            ToastUtils.show("登录nim失败: " + code);
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        exception.printStackTrace();
                    }
                });
    }

    /**
     * 云信服务【IM】
     * 添加／关注 好友
     *
     * @param account
     */

    public static void doAddFriend(final Context context, String account, String msg, boolean addDirectly) {
        if (!NetworkUtil.isNetAvailable(context)) {
            Toast.makeText(context, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(account) && account.equals(DemoCache.getAccount())) {
            Toast.makeText(context, "不能加自己为好友", Toast.LENGTH_SHORT).show();
            return;
        }
        final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD : VerifyType.VERIFY_REQUEST;
        DialogMaker.showProgressDialog(context, "", true);
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        if (VerifyType.DIRECT_ADD == verifyType) {
                            Toast.makeText(context, "添加好友成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 408) {
                            Toast.makeText(context, R.string.network_is_not_available, Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "on failed:" + code, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }

    public static void onRemoveFriend(final Context context, final String account) {
        if (!NetworkUtil.isNetAvailable(context)) {
            Toast.makeText(context, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(context, context.getString(R.string.remove_friend),
                context.getString(R.string.remove_friend_tip), true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(context, "", true);
                        NIMClient.getService(FriendService.class).deleteFriend(account).setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                DialogMaker.dismissProgressDialog();
                                Toast.makeText(context, R.string.remove_friend_success, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(int code) {
                                DialogMaker.dismissProgressDialog();
                                if (code == 408) {
                                    Toast.makeText(context, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "on failed:" + code, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onException(Throwable exception) {
                                DialogMaker.dismissProgressDialog();
                            }
                        });
                    }
                });
    }

    public static void addFriendInNim(final long account) {
        final VerifyType verifyType = VerifyType.VERIFY_REQUEST; // 发起好友验证请求
        String msg = "好友请求附言";
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account + "", verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });


    }


    /**
     * 云信服务【IM】
     * 接收系统消息
     */
    public static void receiveSystemMessage() {
//        List<SystemMessage> temps = NIMClient.getService(SystemMessageService.class)
//                .querySystemMessagesBlock(offset, limit); // 参数offset为当前已经查了offset条，limit为要继续查询limit条。
//
    }

}
