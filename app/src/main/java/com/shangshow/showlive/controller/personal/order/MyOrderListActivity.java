package com.shangshow.showlive.controller.personal.order;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.common.widget.viewpager.CustomViewPager;
import com.shangshow.showlive.controller.adapter.TabFragmentAdapter;
import com.shangshow.showlive.controller.personal.fragment.AddValueOrderFragment;
import com.shangshow.showlive.controller.personal.fragment.GoodsOrderFragment;
import com.shaojun.widget.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 */

public class MyOrderListActivity extends BaseActivity {

    private SlidingTabLayout slidingTabLayout;
    private CustomViewPager customViewPager;
    private String[] mTitles = {"商品", "充值"};
    private List<String> titles = new ArrayList<>();
    private List<BasePageFragment> fragments = new ArrayList<>();// Tab页面列表
    private AddValueOrderFragment addValueOrderFragment;
    private GoodsOrderFragment goodsOrderFragment;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_myorderlist;
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        for (String title : mTitles) {
            titles.add(title);
        }
        titleBarView.initCenterTitle(R.string.myorderlist);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.myorderlist_SlidingTabLayout);
        customViewPager = (CustomViewPager) findViewById(R.id.myorderlist_viewpager);
        goodsOrderFragment = GoodsOrderFragment.newInstance("商品");
        addValueOrderFragment = AddValueOrderFragment.newInstance("充值");
        fragments.add(goodsOrderFragment);
        fragments.add(addValueOrderFragment);

        customViewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(),
                fragments, titles));
        customViewPager.setIsScrollable(true);
        customViewPager.setCurrentItem(0);
        slidingTabLayout.setViewPager(customViewPager);
    }

    @Override
    protected void setView() {

    }
}
