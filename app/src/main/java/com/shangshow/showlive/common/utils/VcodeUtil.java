package com.shangshow.showlive.common.utils;

import android.os.Handler;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.common.widget.custom.BaseButton;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 验证码
 */
public class VcodeUtil {

    private static VcodeUtil vCodeUitl;
    private TextView view;

    private Timer timer;
    private static final int TIME_COUNT_DOWN = 1;
    private int timeCount;
    private static final int TIMER_COUNT_DOWN = 60;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TIME_COUNT_DOWN:
                    if (timeCount > 0) {
                        view.setEnabled(false);
                        view.setText(timeCount + "S");
                    } else {
                        view.setText(R.string.send);
                        view.setEnabled(true);
                        timer.cancel();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public static VcodeUtil instance() {
        synchronized (VcodeUtil.class) {
            if (null == vCodeUitl) {
                vCodeUitl = new VcodeUtil();
            }
        }
        return vCodeUitl;
    }

    public void setViewToVcode(BaseButton view) {
        this.view = view;
        startTimeCount();
    }

    public void startTimeCount() {
        timer = new Timer();
        timeCount = TIMER_COUNT_DOWN;
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                timeCount -= 1;
                handler.sendMessage(handler.obtainMessage(TIME_COUNT_DOWN));
            }
        }, 0, 1000);
    }

}
