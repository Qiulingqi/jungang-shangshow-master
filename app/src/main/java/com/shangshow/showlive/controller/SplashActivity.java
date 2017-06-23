package com.shangshow.showlive.controller;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.XmlDB;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.network.service.models.AdsInfo;
import com.shaojun.utils.log.Logger;


public class SplashActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private UserModel userModel;
    private DisplayImageOptions options;
    private ImageView imageView;
    private VideoView videoView;
    private LinearLayout skipBtn;
    private RelativeLayout adsinfoLinkLayout;
    private TextView currentdowntumer;
    private TextView adMessage;

    private RelativeLayout bottomLayout;//底部试图，显示商秀相关

    private AdsInfo adsInfo;

    private String defaultUrl = "http://img4q.duitang.com/uploads/item/201404/14/20140414004026_H4Q8R.jpeg";
    private Button splash_button_skip;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_splash;
    }

   /* @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(false);
        userModel = new UserModel(this);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.common_bg_color)
                .showImageForEmptyUri(R.color.common_bg_color)
                .showImageOnFail(R.color.common_bg_color)
                .displayer(new BitmapDisplayer() {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        imageAware.setImageBitmap(bitmap);
                    }
                })
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }*/


    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.inShow(false);
        splash_button_skip = (Button) findViewById(R.id.splash_button_skip);
        splash_button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipAndLeaveOut();
            }
        });
        currentdowntumer = (TextView) findViewById(R.id.currentdowntimer);
        videoView = (VideoView) this.findViewById(R.id.splash_video);
        imageView = (ImageView) findViewById(R.id.splash_image);
        adsinfoLinkLayout = (RelativeLayout) findViewById(R.id.splash_adsinfo_link_layout);
        adMessage = (TextView) findViewById(R.id.ad_message);
        bottomLayout = (RelativeLayout) findViewById(R.id.splash_bottom_layout);
        skipBtn = (LinearLayout) findViewById(R.id.splash_skip_btn);
        //隐藏
        adsinfoLinkLayout.setVisibility(View.GONE);
        //  bottomLayout.setVisibility(View.GONE);
        String s = "android.resource://" + getPackageName() + "/" + R.raw.lastspalsh;
        // 播放动画   三十秒后进入下一个界面
        playVideo(Uri.parse(s));
        //倒计时准备
        countDownTimer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skipAndLeaveOut();
                finish();
            }
        }, 30000);

    }

    @Override
    protected void bindEven() {
        videoView.setOnClickListener(this);
        skipBtn.setOnClickListener(this);
        adsinfoLinkLayout.setOnClickListener(this);
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome_video);
//        playVideo(uri);
    }


    @Override
    protected void setView() {
        //获取广告
        // obtainAds();
    }


    public void playVideo(Uri uri) {
        videoView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);

        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        videoView.setOnErrorListener(this);
        videoView.setOnCompletionListener(this);
        //下面android:resource://是固定的，+包名，不是.Java文件package名称，R.raw.movie_1是id名称
        videoView.setVideoURI(uri);
        videoView.start();
    }

/*    public void showImage(String url) {
        videoView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);

        ImageLoader.getInstance().displayImage(url, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                fade();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                fade();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_video:
                break;
            case R.id.splash_adsinfo_link_layout:
                if (adsInfo != null && adsInfo.actionType == MConstants.AD_ACTIONTYPE_WEB) {
                    CommonUtil.startCommonBrowserForPage(this, adsInfo.name, adsInfo.action);
                } else {
                    Logger.d("adsInfo is null 或者 actionType不对");
                }
                break;
            default:
                break;

        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // fade();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        showToast("视频播放 onError");
        String url = defaultUrl;
        //  showImage(url);
        return true;
    }



/*    *//**
     * 获取广告
     *//*
    private void obtainAds() {
        userModel.adsInfoList(MConstants.ADS_NO_SPLASH, new Callback<List<AdsInfo>>() {
            @Override
            public void onSuccess(List<AdsInfo> adsInfos) {
                if (adsInfos != null && adsInfos.size() > 0) {
                    //取广告列表第一个
                    adsInfo = adsInfos.get(0);
                    adsinfoLinkLayout.setVisibility(View.VISIBLE);
                    adMessage.setText(adsInfo.name);
                    bottomLayout.setVisibility(View.VISIBLE);
                    AdsInfo adsInfo = adsInfos.get(0);
                    Logger.i("获取到首页广告图片地址为" + adsInfo.resource);
                    String url = "";
                    if (adsInfo.resource.contains("http")) {
                        url = adsInfo.resource;
                    } else {
                        url = defaultUrl;
                    }
                    showImage(url);
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
                String url = defaultUrl;
                showImage(url);
            }
        }, false);
    }*/

/*    *//**
     * 图片动画
     *//*
    private void fade() {

        AnimUtil.startScaleAnimation(5000, imageView, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                skipAndLeaveOut();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }*/

    /**
     * 离开启动页
     */
    private void skipAndLeaveOut() {
        //判断是否是第一次启动app，第一次启动app进引导页
        int flag = XmlDB.getInstance(SplashActivity.this).getInt(MConstants.key_IS_FRIST_START_APP, 0);
        if (flag == 0) {
            Intent intent = new Intent(SplashActivity.this, AppGuideActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            //  Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            if (CacheCenter.getInstance().isLogin()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private int currenttime = 30;
    CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            currenttime--;
            currentdowntumer.setText(currenttime + "");
        }

        @Override
        public void onFinish() {

        }
    };


 /*   @Override
    protected void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }*/
}
