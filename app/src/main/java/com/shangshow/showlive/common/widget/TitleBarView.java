package com.shangshow.showlive.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;

/**
 * 标题栏view
 */
public class TitleBarView extends RelativeLayout {
    public static String BTN_LEFT = "left";
    public static String BTN_RIGHT = "right";
    public View mRootView;
    // title相关
    public RelativeLayout rootLayout;
    public RelativeLayout leftLayout, centerLayout;
    public LinearLayout centerCustomLayout;
    public RelativeLayout rightLayout;
    public LinearLayout rightCustomLayout;
    public ImageView leftIcon, rightIcon;
    public TextView leftText, centerTitle, rightText;

    private View centerCustomView = null;
    private Context mContext;

    public TitleBarView(Context context) {
        super(context);
        init(context);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        this.mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.common_activity_titlebar, this);
        rootLayout = (RelativeLayout) findViewById(R.id.common_activity_titlebar_root_layout);
        centerLayout = (RelativeLayout) findViewById(R.id.common_activity_titlebar_center_layout);
        centerCustomLayout = (LinearLayout) findViewById(R.id.common_activity_titlebar_center_custom_layout);
        leftLayout = (RelativeLayout) findViewById(R.id.common_activity_titlebar_left_layout);
        rightLayout = (RelativeLayout) findViewById(R.id.common_activity_titlebar_right_layout);
        rightCustomLayout = (LinearLayout) findViewById(R.id.common_activity_titlebar_right_custom_layout);
        leftIcon = (ImageView) findViewById(R.id.common_activity_titlebar_left_image);
        rightIcon = (ImageView) findViewById(R.id.common_activity_titlebar_right_image);
        leftText = (TextView) findViewById(R.id.common_activity_titlebar_left_tv);
        centerTitle = (TextView) findViewById(R.id.common_activity_titlebar_center_tv);
        rightText = (TextView) findViewById(R.id.common_activity_titlebar_right_tv);


        mRootView.setVisibility(View.GONE);

        initializeState();
    }

    public void initializeState() {
        // 为左边按钮设置默认监听器 逻辑为finish掉当前activity
        centerLayout.setOnClickListener(null);
        leftLayout.setVisibility(View.VISIBLE);
        leftLayout.setOnClickListener(new defaultLeftClickListener());
        leftIcon.setOnClickListener(new defaultLeftClickListener());
        // 右边自定布局默认为空
        rightLayout.setVisibility(View.VISIBLE);
        rightCustomLayout.removeAllViews();
        rightCustomLayout.setVisibility(View.GONE);
        // 中间自定义布局
        centerCustomLayout.removeAllViews();
        centerCustomLayout.setVisibility(View.GONE);
    }


    public class defaultLeftClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.common_activity_titlebar_left_image:
                case R.id.common_activity_titlebar_left_layout:{
//                    ((Activity)getContext()).finish();
                }
                break;
            }
        }
    }


    public void initTitleBarOption(String leftText, int leftIconRes,
                                   View.OnClickListener leftClickListener, String rightText,
                                   int rightIconRes, View.OnClickListener rightClickListener,
                                   String title, View.OnClickListener centerTitleListener) {
        mRootView.setVisibility(View.VISIBLE);
        initLeft(leftText, leftIconRes, leftClickListener);
        initRight(rightText, rightIconRes, rightClickListener);
        initCenterTitle(title, centerTitleListener);
    }


    public void inShow(Boolean isShow) {
        if (isShow) {
            mRootView.setVisibility(View.VISIBLE);
        } else {
            mRootView.setVisibility(View.GONE);

        }
    }

    /**
     * @param text
     * @param iconResId
     * @param onClickListener
     * @return
     */
    public TitleBarView initLeft(String text, Integer iconResId,
                                 OnClickListener onClickListener) {
        mRootView.setVisibility(View.VISIBLE);
        if (text != null) {
            leftText.setVisibility(View.VISIBLE);
            leftText.setText(text);
        } else {
            leftText.setVisibility(View.GONE);
        }

        if (iconResId != null && iconResId != 0) {
            leftIcon.setVisibility(View.VISIBLE);
            leftIcon.setImageResource(iconResId);
        } else {
            leftIcon.setVisibility(View.GONE);
        }

        if (onClickListener != null) {
            leftIcon.setEnabled(true);
            leftLayout.setEnabled(true);
            leftIcon.setOnClickListener(onClickListener);
            leftLayout.setOnClickListener(onClickListener);
        } else {
            leftIcon.setEnabled(false);
            leftLayout.setEnabled(false);
        }
        return this;
    }


    public void setBtnRightEnabled(boolean isEnabled) {
        rightIcon.setEnabled(isEnabled);
        rightLayout.setEnabled(isEnabled);
        if (isEnabled) {
            rightLayout.setAlpha(1.0f);
        } else {
            rightLayout.setAlpha(0.6f);
        }
    }


    /**
     * @param text
     * @param iconResId
     * @param onClickListener
     * @return
     */
    public TitleBarView initRight(String text, Integer iconResId,
                                  OnClickListener onClickListener) {
        mRootView.setVisibility(View.VISIBLE);
        if (text != null) {
            rightText.setVisibility(View.VISIBLE);
            rightText.setText(text);
        } else {
            rightText.setVisibility(View.GONE);
        }

        if (iconResId != null && iconResId != 0) {
            rightIcon.setVisibility(View.VISIBLE);
            rightIcon.setImageResource(iconResId);

        } else {
            rightIcon.setVisibility(View.GONE);

        }
        if (onClickListener != null) {
            rightIcon.setEnabled(true);
            rightLayout.setEnabled(true);
            rightIcon.setOnClickListener(onClickListener);
            rightLayout.setOnClickListener(onClickListener);
        } else {
            rightIcon.setEnabled(false);
            rightLayout.setEnabled(false);
        }
        return this;
    }

    /**
     * 右边菜单自定义试图
     *
     * @param customView
     * @return
     */
    public TitleBarView initRightCustom(View customView) {
        rightIcon.setVisibility(View.GONE);
        rightText.setVisibility(View.GONE);
        if (customView != null) {
            rightCustomLayout.setVisibility(View.VISIBLE);
            rightCustomLayout.addView(customView);
        } else {
            rightCustomLayout.setVisibility(View.GONE);
            rightCustomLayout.removeAllViews();
        }

        return this;
    }

    /**
     * title
     *
     * @param text
     * @param onClickListener
     * @return
     */
    public TitleBarView initCenterTitle(String text,
                                        OnClickListener onClickListener) {
        mRootView.setVisibility(View.VISIBLE);
        if (text != null) {
            centerTitle.setVisibility(View.VISIBLE);
            initCenterTitle(text);
        } else {
            centerTitle.setVisibility(View.GONE);
        }

        if (onClickListener != null) {
            centerLayout.setOnClickListener(onClickListener);
        } else {
            centerLayout.setOnClickListener(null);
        }
        return this;
    }

    public TitleBarView initCenterTitle(String title) {
        mRootView.setVisibility(View.VISIBLE);
        if (centerTitle != null) {
            centerTitle.setText(title);
        }
        return this;
    }

    public TitleBarView initCenterTitle(int resId) {
        return initCenterTitle(mContext.getString(resId));
    }

    /**
     * 中间菜单自定义试图
     *
     * @param customView
     * @param needLeft   是否显示左边按钮
     * @param needRight  是否显示右边按钮
     * @return
     */
    public TitleBarView initCenterCustom(View customView, boolean needLeft, boolean needRight) {
        hideLeft(!needLeft);
        hideRight(!needRight);
        initCenterCustom(customView);
        return this;
    }

    /**
     * 自定义中间title的view
     *
     * @param customView
     * @return
     */
    public TitleBarView initCenterCustom(View customView) {
        mRootView.setVisibility(View.VISIBLE);
        centerTitle.setVisibility(View.GONE);
        centerCustomView = customView;
        if (centerCustomView != null) {
            centerCustomLayout.setVisibility(View.VISIBLE);
            centerCustomLayout.removeAllViews();
            customView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            centerCustomLayout.addView(customView);
        } else {
            centerCustomLayout.setVisibility(View.GONE);
            centerCustomLayout.removeAllViews();
        }
        return this;
    }


    /**
     * 隐藏右边按钮布局
     *
     * @return
     */
    public TitleBarView hideRight(boolean flag) {
        if (flag) {
            rightLayout.setVisibility(View.GONE);
        } else {
            rightLayout.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 隐藏左边按钮布局
     *
     * @return
     */
    public TitleBarView hideLeft(boolean flag) {
        if (flag) {
            leftLayout.setVisibility(View.GONE);
        } else {
            leftLayout.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置标题背景
     *
     * @param resId 背景id -1去掉背景
     * @return
     */
    public TitleBarView setTitleBgResource(int resId) {
        if (resId != -1) {
            rootLayout.setBackgroundResource(resId);
        } else {
            rootLayout.setBackgroundDrawable(null);
        }
        return this;
    }

    public RelativeLayout getCenterLayout() {
        return centerLayout;
    }

    public void setCenterLayout(RelativeLayout centerLayout) {
        this.centerLayout = centerLayout;
    }

    public RelativeLayout getLeftLayout() {
        return leftLayout;
    }

    public void setLeftLayout(RelativeLayout leftLayout) {
        this.leftLayout = leftLayout;
    }

    public RelativeLayout getRightLayout() {
        return rightLayout;
    }

    public void setRightLayout(RelativeLayout rightLayout) {
        this.rightLayout = rightLayout;
    }

    public ImageView getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(ImageView leftIcon) {
        this.leftIcon = leftIcon;
    }

    public ImageView getRightIcon() {
        return rightIcon;
    }

    public void setRightIcon(ImageView rightIcon) {
        this.rightIcon = rightIcon;
    }

    public TextView getLeftText() {
        return leftText;
    }

    public void setLeftText(TextView leftText) {
        this.leftText = leftText;
    }

    public TextView getCenterTitle() {
        return centerTitle;
    }

    public void setCenterTitle(TextView centerTitle) {
        this.centerTitle = centerTitle;
    }

    public TextView getRightText() {
        return rightText;
    }

    public void setRightText(TextView rightText) {
        this.rightText = rightText;
    }

}
