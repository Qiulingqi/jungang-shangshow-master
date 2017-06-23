package com.shangshow.showlive.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.shangshow.showlive.R;

public class StartLogoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_logo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
         /*           if (CacheCenter.getInstance().isLogin()) {
                        Intent intent = new Intent(StartLogoActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(StartLogoActivity.this, WelcomeVideoPager.class);
                        startActivity(intent);
                    }
*/
                    Intent intent = new Intent(StartLogoActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
