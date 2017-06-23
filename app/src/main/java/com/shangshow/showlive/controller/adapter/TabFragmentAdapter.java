package com.shangshow.showlive.controller.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shangshow.showlive.base.BasePageFragment;

import java.util.List;

/**
 * tab+fragment适配器
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<BasePageFragment> list_fragment;                         //fragment列表
    private List<String> titlest;                         //fragment列表

    public TabFragmentAdapter(FragmentManager fm, List<BasePageFragment> list_fragment, List<String> titlest) {
        super(fm);
        this.titlest = titlest;
        this.list_fragment = list_fragment;
    }

    @Override
    public BasePageFragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return titlest.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return titlest.get(position);
    }
}