package com.shangshow.showlive.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.shangshow.showlive.base.cache.CacheCenter;

public class ToastUtils {
    private static Toast toast;

    public static void show(final String msg) {
        showMessage(CacheCenter.getInstance().getContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void show(final int msg) {
        showMessage(CacheCenter.getInstance().getContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void show(final String msg, final int duration) {
        if (duration == 0) {
            showMessage(CacheCenter.getInstance().getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            showMessage(CacheCenter.getInstance().getContext(), msg, duration);
        }
    }


    public static void showMessage(final Context act, final String msg,
                                   final int len) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(act, msg, len);
        toast.show();

    }

    public static void showMessage(final Context act, final int msg,
                                   final int len) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(act, msg, len);
        toast.show();
    }

}
