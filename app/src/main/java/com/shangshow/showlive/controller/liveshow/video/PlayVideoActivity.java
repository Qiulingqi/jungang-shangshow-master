package com.shangshow.showlive.controller.liveshow.video;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import com.shangshow.showlive.R;
import com.shangshow.showlive.common.utils.CustomVideoView;

public class PlayVideoActivity extends Activity {
    private CustomVideoView zhubohuiboshipin;
    private String uri;

    // private MyVideoView pay_video_video;
  //  private String uri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play_video);
        uri = getIntent().getStringExtra("uri");
        /**
         * 我自己写的
         *
         */
        zhubohuiboshipin = (CustomVideoView) findViewById(R.id.zhubohuiboshipin);
        //设置播放加载路径
        zhubohuiboshipin.setVideoURI(Uri.parse(uri));
        //播放
        zhubohuiboshipin.start();
        //循环播放
        zhubohuiboshipin.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                zhubohuiboshipin.start();
            }
        });

/*
        uri = getIntent().getStringExtra("uri");
       // uri = "http://pdc.oss-cn-qingdao.aliyuncs.com/%E5%BC%A0%E5%8F%AF%E5%84%BF%20-%20%E7%9C%9F%E5%BF%83%E6%8D%A2%E7%9C%9F%E6%83%85.mp4?Expires=1478854025&OSSAccessKeyId=TMP.AQHHbGS9BTXQCJL8TNLgsdbRbtJlg0-BeXH8ThldrWeryEFw_1cFLMwmqqyUAAAwLAIUGrUATlK0yqmpsS8R0XM5IsGe2s4CFB-1nvuz44GpJKQTDHLCcEC_Q4Of&Signature=HlgTb1jZaoWuqXip8y3VNGvH8Vs%3D";
      //  uri = "http://v.youku.com/v_show/id_XMjUxNzM1Mzc4NA==.html?spm=a2h1n.8261147.0.0";
        pay_video_video = (MyVideoView) findViewById(R.id.pay_video_video);
        pay_video_video.setMediaController(new MediaController(this));

        pay_video_video.postDelayed(new Runnable() {
            @Override
            public void run() {
                pay_video_video.setVideoURI(Uri.parse(uri + ""));
                pay_video_video.start();
                pay_video_video.requestFocus();
            }
        }, MConstants.DELAYED);*/
    }
}
