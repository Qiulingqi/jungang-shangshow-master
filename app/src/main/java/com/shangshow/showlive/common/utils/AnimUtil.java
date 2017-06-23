package com.shangshow.showlive.common.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class AnimUtil {

    public static void startRotateAnimation(final View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setRepeatCount(-1);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        view.startAnimation(rotateAnimation);
    }

    public static void startScaleAnimation(long time, final View view, Animation.AnimationListener AnimationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f);
        scaleAnimation.setDuration(time);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setFillBefore(false);
//        rotateAnimation.setRepeatCount(-1);
        LinearInterpolator lin = new LinearInterpolator();
        scaleAnimation.setInterpolator(lin);
//        scaleAnimation.setFillAfter(false);
        scaleAnimation.setAnimationListener(AnimationListener);
        view.startAnimation(scaleAnimation);
    }

}