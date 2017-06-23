package com.shangshow.showlive.common.widget.tag;


import com.shaojun.utils.log.Logger;

/**
 * Created by kaede on 2016/4/11.
 */
class LogUtil {

    public static final String PREFIX = "[kaede]";

    public static void v(String TAG, String msg) {
        if (!Constants.DEBUG) return;
        Logger.v(TAG, PREFIX + msg);
    }

    public static void d(String TAG, String msg) {
        if (!Constants.DEBUG) return;
        Logger.d(TAG, PREFIX + msg);
    }

    public static void i(String TAG, String msg) {
        Logger.i(TAG, PREFIX + msg);
    }

    public static void w(String TAG, String msg) {
        Logger.w(TAG, PREFIX + msg);
    }

    public static void e(String TAG, String msg) {
        Logger.e(TAG, PREFIX + msg);
    }
}
