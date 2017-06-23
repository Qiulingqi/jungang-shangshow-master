package com.shangshow.showlive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by chenhongxin on 2016/10/13.
 */
public class ListGridView extends GridView {
    public ListGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListGridView(Context context) {
        super(context);
    }

    public ListGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
