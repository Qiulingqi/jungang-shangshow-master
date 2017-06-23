package com.shangshow.showlive.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by 1 on 2016/10/24.
 */
public class ScrollSwipeListView extends SwipeListView {
    public ScrollSwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
