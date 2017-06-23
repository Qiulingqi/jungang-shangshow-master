package com.shangshow.showlive.controller.common;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.Tenants;


public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout theSocialContractLayout, privacyPolicyLayout, termsOfServiceLayout, contactUsLayout, feedbackLayout;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.about_us);
        theSocialContractLayout = (RelativeLayout) findViewById(R.id.the_social_contract_layout);
        privacyPolicyLayout = (RelativeLayout) findViewById(R.id.privacy_policy_layout);
        termsOfServiceLayout = (RelativeLayout) findViewById(R.id.terms_of_service_layout);
        contactUsLayout = (RelativeLayout) findViewById(R.id.contact_us_layout);
        feedbackLayout = (RelativeLayout) findViewById(R.id.feedback_layout);

    }

    @Override
    protected void bindEven() {
        theSocialContractLayout.setOnClickListener(this);
        privacyPolicyLayout.setOnClickListener(this);
        termsOfServiceLayout.setOnClickListener(this);
        contactUsLayout.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.the_social_contract_layout:
               // CommonUtil.startCommonBrowserForPage(AboutUsActivity.this, getString(R.string.the_social_contract), "www.baidu.com");
                startActivity(Tenants.class, "tenants", "file:///android_asset/gongyue.html");
                break;
            case R.id.privacy_policy_layout:
                CommonUtil.startCommonBrowserForPage(AboutUsActivity.this, getString(R.string.privacy_policy), "www.baidu.com");
                break;
            case R.id.terms_of_service_layout:
                CommonUtil.startCommonBrowserForPage(AboutUsActivity.this, getString(R.string.terms_of_service), "www.baidu.com");
                break;
            case R.id.contact_us_layout:
                startActivity(new Intent(AboutUsActivity.this, ContactUsActivity.class));
                break;
            case R.id.feedback_layout:
                startActivity(new Intent(AboutUsActivity.this, FeedbackActivity.class));
                break;
            default:
                break;
        }
    }

}
