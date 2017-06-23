package com.shangshow.showlive.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseFragment;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.widget.TitleBarView;
import com.shangshow.showlive.common.widget.viewpager.CustomViewPager;
import com.shangshow.showlive.controller.adapter.TabFragmentAdapter;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.message.MessageActivity;
import com.shangshow.showlive.controller.search.HomeSearchActivity;
import com.shaojun.widget.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    public TitleBarView titleBarView;
    private Context mContext;
    private SlidingTabLayout slidingTabLayout;
    private String[] mTitles = {"全球热播", "最新关注"};
    private List<String> titles = new ArrayList<>();

    private CustomViewPager customViewPager;
    private List<BasePageFragment> fragments;// Tab页面列表
    private FragmentHomeHot fragmentHomeHot;
    private FragmentHomeAttention fragmentHomeAttention;

    public static FragmentHome newInstance(String title) {
        FragmentHome f = new FragmentHome();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    protected void initWidget(View rootView) {

        title = getArguments().getString("title");
        titleBarView = (TitleBarView) rootView.findViewById(R.id.home_title_layout);
        titleBarView.initLeft("", R.mipmap.icon_sys_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CacheCenter.getInstance().isLogin()) {
                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        titleBarView.initRight("", R.mipmap.icon_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, HomeSearchActivity.class));
            }
        });
        titleBarView.initCenterTitle(R.string.app_name);
        titleBarView.initCenterTitle(R.string.app_name_detail);
        //设置字体颜色
        titleBarView.getCenterTitle().setTextColor(getResources().getColor(R.color.a1_color));

        slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.home_SlidingTabLayout);

        customViewPager = (CustomViewPager) rootView.findViewById(R.id.my_bill_viewpager);

        //titles
        for (String title : mTitles) {
            titles.add(title);
        }
        fragments = new ArrayList<BasePageFragment>();
        fragmentHomeHot = FragmentHomeHot.newInstance("热门");
        fragmentHomeAttention = FragmentHomeAttention.newInstance("关注");
        fragments.add(fragmentHomeHot);
        fragments.add(fragmentHomeAttention);

        customViewPager.setAdapter(new TabFragmentAdapter(getChildFragmentManager(),
                fragments, titles));
        customViewPager.setIsScrollable(true);
        customViewPager.setCurrentItem(0);
//        customViewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        slidingTabLayout.setViewPager(customViewPager);

    }

    @Override
    protected void bindEven() {


    }

    @Override
    protected void setView() {

    }

    @Override
    public void onRefresh() {

    }
}
