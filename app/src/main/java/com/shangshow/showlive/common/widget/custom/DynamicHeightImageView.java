package com.shangshow.showlive.common.widget.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shangshow.showlive.R;

/**
 * An {@link ImageView} layout that maintains a consistent width
 * to height aspect ratio.
 */
public class DynamicHeightImageView extends ImageView {

    private double mHeightRatio = 1;
    private boolean scaleToWidth = false; // this flag determines if should
    // measure height manually dependent
    // of width


    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicHeightImageView, defStyle, 0);
        int ratio_width_point = a.getInteger(R.styleable.DynamicHeightImageView_ratio_width_point_value, -1);
        int ratio_height_point = a.getInteger(R.styleable.DynamicHeightImageView_ratio_height_point_value, -1);

        if (ratio_width_point != -1 && ratio_height_point != -1) {
            mHeightRatio = ratio_height_point / ratio_width_point;
        } else {
            mHeightRatio = 0;
        }
        a.recycle();
    }

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio == 0) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            /**
             * if both width and height are set scale width first. modify in
             * future if necessary
             */

            if (widthMode == MeasureSpec.EXACTLY
                    || widthMode == MeasureSpec.AT_MOST) {
                scaleToWidth = true;
            } else if (heightMode == MeasureSpec.EXACTLY
                    || heightMode == MeasureSpec.AT_MOST) {
                scaleToWidth = false;
            } else
                throw new IllegalStateException(
                        "width or height needs to be set to match_parent or a specific dimension");

            if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0) {
                // nothing to measure
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            } else {
                if (scaleToWidth) {
                    int iw = this.getDrawable().getIntrinsicWidth();
                    int ih = this.getDrawable().getIntrinsicHeight();
                    int heightC = width * ih / iw;
                    if (height > 0)
                        if (heightC > height) {
                            // dont let hegiht be greater then set max
                            heightC = height;
                            width = heightC * iw / ih;
                        }

                    this.setScaleType(ScaleType.CENTER_CROP);
                    setMeasuredDimension(width, heightC);

                } else {
                    // need to scale to height instead
                    int marg = 0;
                    if (getParent() != null) {
                        if (getParent().getParent() != null) {
                            marg += ((RelativeLayout) getParent().getParent())
                                    .getPaddingTop();
                            marg += ((RelativeLayout) getParent().getParent())
                                    .getPaddingBottom();
                        }
                    }

                    int iw = this.getDrawable().getIntrinsicWidth();
                    int ih = this.getDrawable().getIntrinsicHeight();

                    width = height * iw / ih;
                    height -= marg;
                    setMeasuredDimension(width, height);
                }

            }
        } else if (mHeightRatio > 0.0) {
            // // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
