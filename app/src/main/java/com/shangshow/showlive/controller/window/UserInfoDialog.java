package com.shangshow.showlive.controller.window;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shangshow.showlive.R;
import com.shangshow.showlive.common.LogicView.UserInfoView;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.common.utils.ScreenUtil;


/**
 * Create custom Dialog windows for your application Custom dialogs rely on
 * custom layouts wich allow you to create and use your own look & feel.
 * <p>
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 *
 * @author antoine vianey
 */
public class UserInfoDialog extends Dialog {
    private Context mContext;
    private View rootView;
    private UserInfo userInfo;
    private UserInfoView userInfoView;
    private int mResourceId;

    public UserInfoDialog(Context context, int resourceId, int style) {
        super(context, style);
        this.mContext = context;
        if (-1 != resourceId) {
            setContentView(resourceId);
            this.mResourceId = resourceId;
        }
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); //
        ScreenUtil.getScreenWidth(mContext);
        p.height = (int) (ScreenUtil.getScreenHeight(mContext) * 0.4); //
        p.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.5); //
        dialogWindow.setAttributes(p);
    }

    public UserInfoDialog(Context context, int style) {
        this(context, -1, style);
        this.mContext = context;
        mResourceId = R.layout.window_user_info;
    }

    public UserInfoDialog(Context context) {
        this(context, R.style.Dialog);
        this.mContext = context;
        mResourceId = R.layout.window_user_info;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mResourceId);
        try {
            userInfoView = (UserInfoView) findViewById(R.id.memberview);
            userInfoView.setUserInfo(userInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}