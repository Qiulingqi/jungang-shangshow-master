package com.shangshow.showlive.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shangshow.showlive.R;


/**
 * Created by qinwei on 16/7/18 上午10:08
 */
public class MyToolbar extends Toolbar {
    public static final int GRAVITY_LEFT = 0;
    public static final int GRAVITY_CENTER = 1;
    private int gravity = GRAVITY_CENTER;
    private TextView mToolBarTitleLabel;
    private TextView mToolbarRightLabel;
    private TextView mToolbarLeftLabel;

    public MyToolbar(Context context) {
        super(context);
        initializeView();
    }


    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    private void initializeView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_toolbar, this);
        mToolBarTitleLabel = (TextView) findViewById(R.id.mToolbarTitleLabel);
        mToolbarLeftLabel = (TextView) findViewById(R.id.mToolbarLeftLabel);
        mToolbarRightLabel = (TextView) findViewById(R.id.mToolbarRightLabel);
        mToolbarLeftLabel.setVisibility(View.GONE);
        mToolbarRightLabel.setVisibility(View.GONE);
    }

    /**
     * 设置title显示方位
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        if (gravity == GRAVITY_CENTER) {
            mToolBarTitleLabel.setText(title);
            super.setTitle("");
        } else {
            mToolBarTitleLabel.setText("");
            super.setTitle(title);
        }
    }


    /**
     * 设置文本方向
     *
     * @param gravity
     */
    public void setTextGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setRightText(String text) {
        mToolbarRightLabel.setText(text);
        if (!TextUtils.isEmpty(text)) {
            mToolbarRightLabel.setVisibility(View.VISIBLE);
        }
    }

    public void setOnRightClickListener(final OnClickListener listener) {
        mToolbarRightLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    public void setLeftText(String text) {
        mToolbarLeftLabel.setText(text);
        if (!TextUtils.isEmpty(text)) {
            mToolbarLeftLabel.setVisibility(View.VISIBLE);
        }
    }

    public void setOnLeftClickListener(final OnClickListener listener) {
        mToolbarLeftLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }


}
