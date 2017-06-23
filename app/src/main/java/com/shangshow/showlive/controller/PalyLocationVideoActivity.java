package com.shangshow.showlive.controller;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;

public class PalyLocationVideoActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, View.OnClickListener {

    private VideoView videoView;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_paly_location_video;
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {
        videoView = (VideoView) findViewById(R.id.xinghsang_paly_top);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.xingpinfirst;
        playVideo(Uri.parse(uri));
     /*   xinghsang_paly_top.setVideoURI(Uri.parse((uri)));
        xinghsang_paly_top.setMediaController(new MediaController(PalyLocationVideoActivity.this));
        xinghsang_paly_top.requestFocus();
        //监听播完了重播
        xinghsang_paly_top.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                xinghsang_paly_top.start();
            }
        });

        xinghsang_paly_top.start();*/
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    public void playVideo(Uri uri) {


        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        videoView.setOnErrorListener(this);
        videoView.setOnCompletionListener(this);
        //下面android:resource://是固定的，+包名，不是.Java文件package名称，R.raw.movie_1是id名称
        videoView.setVideoURI(uri);
        videoView.start();
    }
}
