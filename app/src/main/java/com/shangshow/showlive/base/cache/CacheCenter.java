package com.shangshow.showlive.base.cache;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.netease.nim.uikit.cache.DemoCache;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.XmlDB;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.network.service.models.body.payload;
import com.shaojun.utils.log.LogLevel;
import com.shaojun.utils.log.Logger;

import io.jsonwebtoken.impl.Base64UrlCodec;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @auth:jeremy
 * @see: [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class CacheCenter {
    private static String TAG = "shangxiu";
    private static Context mContext;
    private static UserInfo user;
    //nim
    private static String nimAccount;
    private static String nimToken;

    private static NimUserInfo userInfo;

    private static StatusBarNotificationConfig notificationConfig;

    private CacheCenter() {

    }

    public static CacheCenter getInstance() {
        return UserCenterHolder.INSTANCE;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void clear() {
        nimAccount = null;
        userInfo = null;
        user = null;
    }

    public static String getNimAccount() {
        String nimAccount = XmlDB.getInstance(mContext).getString(MConstants.KEY_USER_NIM_ACCOUNT, "");
        return nimAccount;
    }

    public static void setNimAccount(String nimAccount) {
        CacheCenter.nimAccount = nimAccount;
        NimUIKit.setAccount(nimAccount);
        XmlDB.getInstance(mContext).saveString(MConstants.KEY_USER_NIM_ACCOUNT, nimAccount);
        DemoCache.setAccount(nimAccount);
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        CacheCenter.notificationConfig = notificationConfig;
        DemoCache.setNotificationConfig(notificationConfig);
    }

    public static String getNimToken() {
        String nimToken = XmlDB.getInstance(mContext).getString(MConstants.KEY_USER_NIM_TOKEN, "");
        return nimToken;
    }

    public static void setNimToken(String nimToken) {
        CacheCenter.nimToken = nimToken;
        XmlDB.getInstance(mContext).saveString(MConstants.KEY_USER_NIM_TOKEN, nimToken);
    }

    public static NimUserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = NIMClient.getService(UserService.class).getUserInfo(nimAccount);
        }
        return userInfo;
    }

    public void init(Context mContext) {
        this.mContext = mContext;
        Logger.init(TAG)                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(2);                // default 0
    }

    /**
     * 解析token获取userid
     *
     * @return
     */
    public long getTokenUserId() {
        long userId = 0;
        String token = XmlDB.getInstance(mContext).getString(MConstants.KEY_USER_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            return userId;
        }
        String[] tokens = token.split("\\.");
        if (tokens.length == 3) {
            byte[] a = Base64UrlCodec.BASE64URL.decode(tokens[1]);
            String tokenUserInfos = new String(a);
            payload payload = new Gson().fromJson(tokenUserInfos, payload.class);
            userId = Long.parseLong(payload.sub);
        }
        return userId;
    }

    /**
     * @return
     */
    public String getUserToken() {
        return XmlDB.getInstance(mContext).getString(MConstants.KEY_USER_TOKEN, "");
    }

    /**
     * 设置user Token信息
     *
     * @param token
     */
    public void setUserToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            XmlDB.getInstance(mContext).saveString(MConstants.KEY_USER_TOKEN, token);
        } else {
            XmlDB.getInstance(mContext).removeKey(MConstants.KEY_USER);
        }
    }

    public UserInfo getCurrUser() {
        if (user != null) {
            return user;
        } else {
            UserInfo userInfo = XmlDB.getInstance(mContext).getObject4XmlDB(MConstants.KEY_USER, UserInfo.class);
            return userInfo;
        }
    }

    public void setCurrUser(UserInfo userInfo) {
        if (null != userInfo) {
            this.user = userInfo;
            XmlDB.getInstance(mContext).saveObject2XmlDB(MConstants.KEY_USER, userInfo);
        } else {
            XmlDB.getInstance(mContext).removeKey(MConstants.KEY_USER);
        }
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        String token = XmlDB.getInstance(mContext).getString(MConstants.KEY_USER_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            return false;
        } else {
            return true;
        }
    }

    public void loginOutResetUser() {
        XmlDB.getInstance(mContext).saveString(MConstants.KEY_USER_TOKEN, "");
        XmlDB.getInstance(mContext).saveObject2XmlDB(MConstants.KEY_USER, null);
        //
        XmlDB.getInstance(mContext).saveString(MConstants.KEY_USER_NIM_TOKEN, "");
        XmlDB.getInstance(mContext).saveString(MConstants.KEY_USER_NIM_ACCOUNT, "");

        NIMClient.getService(AuthService.class).logout();

    }

    private static class UserCenterHolder {
        static final CacheCenter INSTANCE = new CacheCenter();
    }


}
