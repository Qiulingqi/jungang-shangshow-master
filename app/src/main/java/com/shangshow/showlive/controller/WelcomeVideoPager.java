package com.shangshow.showlive.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseApplication;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.entry.PingMuKuanGao;

public class WelcomeVideoPager extends Activity {

    private VideoView vv;
    private TextView phoneLoginregis;
    private ImageView weiXinLogin;
    private int i = 0;
    private int TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_video_pager);
        //  先判断是否登录
        handler.postDelayed(runnable, TIME); //每隔1s执行
        initView();
    }
    private void initView() {
        // 获取屏幕的宽高  设置图片的大小
        PingMuKuanGao pingMuKuanGao = BaseApplication.getPingMuKuanGao();
        int wight = pingMuKuanGao.wight;
        int height = pingMuKuanGao.height;
        //初始化动图
        vv = (VideoView) findViewById(R.id.firstGIfView);
        // 设置图片资源
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.ss_login_video;
        vv.setVideoURI(Uri.parse(uri));
        //


     //   vv.start();
/*        firstGIfView.setGifImage(R.mipmap.firstgifview);
        firstGIfView.setShowDimension(wight,height);
        firstGIfView.setGifImageType(GifView.GifImageType.COVER);*/
        //其他控件
        phoneLoginregis = (TextView) findViewById(R.id.PhoneLoginregis);
        phoneLoginregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeVideoPager.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        weiXinLogin = (ImageView) findViewById(R.id.WeiXinLogin);
        weiXinLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show("微信登录");
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, TIME);
                vv.start();
                System.out.println("do...");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
}
