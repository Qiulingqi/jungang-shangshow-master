package com.shangshow.showlive.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 1 on 2016/10/19.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int margin;

    public SpaceItemDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, 0);
    }
}
