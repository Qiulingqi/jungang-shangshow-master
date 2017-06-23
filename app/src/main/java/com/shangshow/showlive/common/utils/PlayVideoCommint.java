package com.shangshow.showlive.common.utils;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;

public class PlayVideoCommint extends BaseActivity {
    private CustomVideoView play_video_commint;
    private VideoView videoView;
    private String videourl;
    private String urls;
    private String substring;


    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle("热播视频");
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_play_video_commint;
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {
        Intent intent = getIntent();
        videourl = intent.getStringExtra("videourl");
        initView();
    }

    private void initView() {

        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI5amNhekmnXc7", "kjUf0WVCLBbpbIvKX3JYwhKuescvKq");
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
        try {
            substring = videourl.substring(51, videourl.length());
            urls = oss.presignConstrainedObjectURL("video-showlive", substring, 30 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        play_video_commint = (CustomVideoView) findViewById(R.id.play_video_commint);
        videoView = (VideoView) findViewById(R.id.play_video_commint2);
        //设置播放加载路径
        //网络视频
        String videoUrl2 = urls;
        Uri uri = Uri.parse(videoUrl2);
        videoView.setVideoURI(uri);
        //播放
        videoView.start();
        //循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

    }
}
