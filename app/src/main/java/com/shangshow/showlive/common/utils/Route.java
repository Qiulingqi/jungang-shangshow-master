package com.shangshow.showlive.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

/**
 * 该类用于进行activity的切换
 */
public class Route {
    public static final int WITHOUT_REQUESTCODE = -1;

    /**
     * 跳转到下一个activity，intent为有特殊要求的intent，例如要设置Action、Flag等参数时，请自行设置
     *
     * @param activity
     * @param intent
     * @param requestCode
     */
    public static void nextControllerWiteAnim(View view, Activity activity, Intent intent, int requestCode) {
        //让新的Activity从一个小的范围扩大到全屏
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(view, //The View that the new activity is animating from
                        (int) view.getWidth() / 2, (int) view.getHeight() / 2, //拉伸开始的坐标
                        0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        if (requestCode == WITHOUT_REQUESTCODE) {
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        } else {
            ActivityCompat.startActivityForResult(activity, intent, requestCode, options.toBundle());
        }
    }
}
