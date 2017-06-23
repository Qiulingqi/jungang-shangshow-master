package com.shangshow.showlive.base;

import com.shangshow.showlive.R;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;

/**
 * Created by Super-me on 2016/10/25.
 */
public abstract class BaseListActivity extends BaseActivity {

    protected RLPtrFrameLayout rlptr_framelayout;

    @Override
    protected void initWidget() {
        super.initWidget();
        rlptr_framelayout = (RLPtrFrameLayout) findViewById(R.id.rlptr_framelayout);
        rlptr_framelayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                BaseListActivity.this.onRefresh();
            }

            @Override
            public void onLoadMore() {
                BaseListActivity.this.onLoadMore();
            }
        });
    }

    public abstract void onRefresh();

    public abstract void onLoadMore();

}
