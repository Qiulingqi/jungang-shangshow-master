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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class IFragment extends Fragment {
    public Context context;
    public View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(), null);
        setRootView(root);
        return getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget(getRootView());
        bindEven();
        setView();
    }
    protected abstract int getLayoutId();

    protected abstract void initWidget(View rootView);

    protected abstract void bindEven();

    protected abstract void setView();

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public void showToast(String text) {
        this.showToast(text, 0);
    }

    public void showToast(String text, int duration) {
        Toast.makeText(getActivity(), text + "", duration).show();
    }

}
