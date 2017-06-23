package com.shangshow.showlive.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RadioGroup;

/**
 * Created by chenhongxin on 2016/10/13.
 */
public class WidthHeightImageView extends ImageView {
    public WidthHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int mScreenWidth = metrics.widthPixels;
        RadioGroup.LayoutParams absLayoutParams = new RadioGroup.LayoutParams(mScreenWidth,
                mScreenWidth);
        setLayoutParams(absLayoutParams);
    }
}
