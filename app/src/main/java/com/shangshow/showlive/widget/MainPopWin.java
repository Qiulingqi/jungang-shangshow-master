package com.shangshow.showlive.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.shangshow.showlive.R;

/**
 * Created by 1 on 2016/10/26.
 */
public class MainPopWin extends PopupWindow implements View.OnClickListener {

    private View view, ll_main_clear, ll_main_user, ll_main_video;

    public MainPopWin(Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.main_pop_layout, null);

        ll_main_clear = view.findViewById(R.id.ll_main_clear);
        ll_main_user = view.findViewById(R.id.ll_main_user);
        ll_main_video = view.findViewById(R.id.ll_main_video);

        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.AnimBottom);
        ll_main_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ll_main_video.setOnClickListener(this);
        ll_main_user.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_video: {
                if (onMainPopListener != null) {
                    onMainPopListener.video();
                }
                dismiss();
            }
            break;
            case R.id.ll_main_user: {
                if (onMainPopListener != null) {
                    onMainPopListener.userCenter();
                }
                dismiss();
            }
            break;
        }
    }

    //  接口  外部实现
    private OnMainPopListener onMainPopListener;

    public void setOnMainPopListener(OnMainPopListener onMainPopListener) {
        this.onMainPopListener = onMainPopListener;
    }

    public interface OnMainPopListener {
        void video(); // 直播

        void userCenter(); // 个人中心
    }

}
