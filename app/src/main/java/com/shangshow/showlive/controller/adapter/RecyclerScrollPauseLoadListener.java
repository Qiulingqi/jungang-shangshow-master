package com.shangshow.showlive.controller.adapter;

import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 滑动时不加载图片
 */
public class RecyclerScrollPauseLoadListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                //正在滑动
                ImageLoader.getInstance().pause();
//                Glide.with(recyclerView.getContext()).pauseRequests();
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                //滑动停止
                ImageLoader.getInstance().resume();
//                Glide.with(recyclerView.getContext()).resumeRequests();
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

}