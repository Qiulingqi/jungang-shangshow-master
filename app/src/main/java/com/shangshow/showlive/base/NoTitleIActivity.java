package com.shangshow.showlive.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by 1 on 2016/10/30.
 */
public abstract class NoTitleIActivity extends Activity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentViewOption(getActivityLayout());
        initWidget();
        bindEven();
        setView();
    }

    protected abstract int getActivityLayout();

    protected abstract void setContentViewOption(int layoutId);

    protected abstract void initWidget();

    protected abstract void bindEven();

    protected abstract void setView();

}
