package com.shangshow.showlive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by 1 on 2016/10/31.
 */
public class HandlerListView extends ListView implements AbsListView.OnScrollListener {
    public HandlerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                //加载更多功能的代码
                if(loadMoreListener != null){
                    loadMoreListener.loadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    private LoadMoreListener loadMoreListener;

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public interface LoadMoreListener{
        void loadMore();
    }

}
