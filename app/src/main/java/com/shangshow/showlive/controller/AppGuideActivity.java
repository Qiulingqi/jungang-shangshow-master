package com.shangshow.showlive.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.XmlDB;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.viewpager.CirclePageIndicator;
import com.shangshow.showlive.controller.common.LoginActivity;

/**
 * 引导页
 */
public class AppGuideActivity extends BaseActivity {
    private ViewPager mGuideViewPager;
    private CirclePageIndicator circlePageIndicator;

    private int[] images = new int[]{R.mipmap.guide_01, R.mipmap.guide_02, R.mipmap.guide_03};
    private int[] guideTypes = new int[]{R.string.guide_type01, R.string.guide_type02, R.string.guide_type03};
    private int[] guideTypeDescribes = new int[]{R.string.guide_type01_describe, R.string.guide_type02_describe, R.string.guide_type03_describe};

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_app_guide;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.inShow(false);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.rg_guide);
        mGuideViewPager = (ViewPager) findViewById(R.id.viewpager_guide);
        mGuideViewPager.setAdapter(new GuideAdapter());
        circlePageIndicator.setViewPager(mGuideViewPager);
        setSwipeBackEnable(false);
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {

    }

    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(AppGuideActivity.this).inflate(R.layout.item_viewpager_app_guide, null);
            TextView guideType = (TextView) view.findViewById(R.id.guide_type);
            TextView guideTypeDescribe = (TextView) view.findViewById(R.id.guide_type_describe);
            ImageView imageView = (ImageView) view.findViewById(R.id.guide_image_view);
            BaseButton button = (BaseButton) view.findViewById(R.id.btn_start);

            guideType.setText(guideTypes[position]);
            guideTypeDescribe.setText(guideTypeDescribes[position]);
            imageView.setImageResource(images[position]);

            if (position == images.length - 1) {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XmlDB.getInstance(AppGuideActivity.this).saveInt(MConstants.key_IS_FRIST_START_APP, 1);
                       // startActivity(new Intent(AppGuideActivity.this, MainActivity.class));
                        startActivity(new Intent(AppGuideActivity.this, LoginActivity.class));
                        AppGuideActivity.this.finish();
                    }
                });
            } else {
                button.setVisibility(View.GONE);
            }
//          return super.instantiateItem(container, position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            View view = (View) object;
            ImageView imageView = (ImageView) view.findViewById(R.id.guide_image_view);
            imageView.setImageBitmap(null);
            container.removeView(view);
        }
    }
}
