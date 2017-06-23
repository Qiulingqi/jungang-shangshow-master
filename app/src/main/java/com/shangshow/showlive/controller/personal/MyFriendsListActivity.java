package com.shangshow.showlive.controller.personal;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.controller.adapter.TabFragmentAdapter;
import com.shaojun.widget.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的关注列表
 *
 * @author
 */
public class MyFriendsListActivity extends BaseActivity implements View.OnClickListener {
    private List<BasePageFragment> fragments;                                //定义要装fragment的列表
    private List<String> titles;                                //定义要装fragment的列表
    private String[] mTitles = {"星尚", "星咖", "星品", "星友"};
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_myfriends;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(getString(R.string.title_attention));
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.my_friends_slidingTabLayout);
        viewPager = (ViewPager) findViewById(R.id.my_friends_viewpager);

        initViewPager();
    }

    public void initViewPager() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        for (String title : mTitles) {
            titles.add(title);
        }
        fragments.add(MyFriendsFragment.newInstance(MConstants.USER_TYPE_FAVOURITE));
        fragments.add(MyFriendsFragment.newInstance(MConstants.USER_TYPE_SUPERSTAR));
        fragments.add(MyFriendsFragment.newInstance(MConstants.USER_TYPE_MERCHANT));
        fragments.add(MyFriendsFragment.newInstance(MConstants.USER_TYPE_COMMONUSER));

        viewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), fragments, titles));
        viewPager.setOffscreenPageLimit(3);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }

    }


}
