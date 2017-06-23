package com.shangshow.showlive.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangshow.showlive.R;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shaojun.base.IFragment;

import java.util.ArrayList;
import java.util.Random;


@SuppressLint("NewApi")
public abstract class BaseFragment extends IFragment {
    public String title;
    private ArrayList<Integer> mBackgroundColors;
    private Random mRandom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.ColorA);
        mBackgroundColors.add(R.color.ColorB);
        mBackgroundColors.add(R.color.ColorC);
        mBackgroundColors.add(R.color.ColorD);
        mBackgroundColors.add(R.color.ColorE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 随机背景色
     */
    public void randomBgColor(View layout) {
        int position = mRandom.nextInt(mBackgroundColors.size());
        // 随机颜色
        int backgroundIndex = position >= mBackgroundColors.size() ? position
                % mBackgroundColors.size() : position;
        layout.setBackgroundResource(mBackgroundColors.get(backgroundIndex));
    }

    public void showToast(String text) {
        this.showToast(text, 0);
    }

    public void showToast(String text, int duration) {
        ToastUtils.show(text, duration);
    }

}
