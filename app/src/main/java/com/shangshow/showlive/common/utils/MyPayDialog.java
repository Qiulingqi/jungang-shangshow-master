package com.shangshow.showlive.common.utils;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by 1 on 2017/5/24.
 */

public class MyPayDialog extends Dialog {
    public MyPayDialog(Context context) {
        super(context);
    }

    public MyPayDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyPayDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
