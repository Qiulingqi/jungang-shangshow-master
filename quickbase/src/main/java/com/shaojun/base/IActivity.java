/*
 * Copyright (C),2015-2015. 城家酒店管理有限公司
 * FileName: ReboundScrollView.java
 * Author:   jeremy
 * Date:     2015-09-07 17:17:06
 * Description: //模块目的、功能描述
 * History: //修改记录 修改人姓名 修改时间 版本号 描述
 * <jeremy>  <2015-09-07 17:17:06> <version>   <desc>
 */
package com.shaojun.base;

import android.content.Context;
import android.os.Bundle;

import com.netease.nim.uikit.common.activity.UI;

public abstract class IActivity extends UI {
    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
