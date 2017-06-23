package com.shangshow.showlive.controller.common;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;


/**
 * 加载网页的公共页面
 *
 * @author
 */
public class CommonBrowserActivity extends BaseActivity {

    public static final String URL_KEY = "url";
    public static final String TITLE_KEY = "title";
    private String mUrl = "";// 当前访问的页面的url
    private String mTitle = "";// 当前webview的title
    private WebView webView;
    private ProgressBar mProgressBar;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_common_browser;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        getBaseData();
        setSwipeBackEnable(true);
    }

    public void getBaseData() {
        mUrl = getIntent().getStringExtra(URL_KEY);
        mTitle = getIntent().getStringExtra(TITLE_KEY);
        if (TextUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("url can not be null !!!");
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(mTitle);
        titleBarView.initLeft(null, R.drawable.selector_title_icon_back, new leftBackClick());
        webView = (WebView) this.findViewById(R.id.activity_browser_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_browser_progressbar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }

            public void onReceivedIcon(WebView view, Bitmap icon) {
            }

            public void onReceivedTitle(WebView view, String title) {
                titleBarView.initCenterTitle(title);
            }
        });

    }

    @Override
    protected void bindEven() {
        webView.loadUrl(mUrl);
    }

    @Override
    protected void setView() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * titlebar左侧退出按钮点击事件，退出条件根据当前title的文本来决定。
     *
     * @author shaojun
     */
    class leftBackClick implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            //  点击返回键  无论什么状态都直接退出当前WebView
            finish();
            /*if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }*/
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

}
