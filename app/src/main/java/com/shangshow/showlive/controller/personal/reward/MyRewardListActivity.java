package com.shangshow.showlive.controller.personal.reward;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
 * 打赏
 */
public class MyRewardListActivity extends BaseActivity implements View.OnClickListener {
    private List<BasePageFragment> fragments;                   //定义要装fragment的列表
    private List<String> titles;                                //定义要装fragment的列表
    private String[] mTitles = {"我的打赏", "打赏我的"};

    private View customTitleView;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_myrewardlist;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterCustom(createRewardTitle());
        viewPager = (ViewPager) findViewById(R.id.my_reward_viewpager);
        initViewPager();
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {

    }

    public void initViewPager() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        for (String title : mTitles) {
            titles.add(title);
        }
        fragments.add(MyRewardFragment.newInstance(MConstants.MY_REWARD));
        fragments.add(RewardMeFragment.newInstance(MConstants.REWARD_ME));

        viewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), fragments, titles));
        viewPager.setOffscreenPageLimit(2);
        slidingTabLayout.setViewPager(viewPager);
    }

    public View createRewardTitle() {
        if (customTitleView == null) {
            customTitleView = LayoutInflater.from(this).inflate(R.layout.layout_title_myreward_list, null);
            slidingTabLayout = (SlidingTabLayout) customTitleView.findViewById(R.id.my_reward_slidingTabLayout);
        }
        return customTitleView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
