package com.shangshow.showlive.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shangshow.showlive.R;

public class Tenants extends Activity {

    private WebView the_tenants;
    private String tenants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenants);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        tenants = intent.getStringExtra("tenants");
        the_tenants = (WebView) findViewById(R.id.the_tenants);
        the_tenants.setWebViewClient(new WebViewClient());
        WebSettings settings = the_tenants.getSettings();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //启动App缓存
        settings.setAppCacheEnabled(true);
        //缩放控制
        settings.setBuiltInZoomControls(true);
        //设置webview推荐使用的窗口
        settings.setUseWideViewPort(true);
        //加载模式
        settings.setLoadWithOverviewMode(true);
        the_tenants.loadUrl(tenants);

    }
}
