package com.shangshow.showlive.controller.liveshow.goods;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Goods;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lorntao on 9/19/16.
 */
public class LiveGoodsAudienceActivity extends BaseActivity implements View.OnClickListener {

    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private LiveGoodsAdapter goodsListAdapter;
    private List<Goods> goodses = new ArrayList<Goods>();
    private UserModel userModel;

    private long videoId;
    private long userId;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_live_goods;
    }

    @Override
    protected void setContentViewOption(int resId, boolean hasTitle) {
        super.setContentViewOption(resId, hasTitle);
    }

    @Override
    protected void bindEven() {
        goodsListAdapter.setOnItemListener(new LiveGoodsAdapter.OnItemListener() {
            @Override
            public void delete(int position) {


            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(LiveGoodsAudienceActivity.this, GoodsOrderActivity.class);
                intent.putExtra(GoodsOrderActivity.GOODSORDERACTIVITY_GOODS, goodses.get(position));
                intent.putExtra("live_anchor_video_id", videoId);
                intent.putExtra("live_anchor_user_id", userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        userModel = new UserModel(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        getVideoProductList(MConstants.DATA_4_REFRESH);
        userId = getIntent().getLongExtra("live_anchor_user_id", -1);
        videoId = getIntent().getLongExtra("live_anchor_video_id", -1);
        titleBarView.initCenterTitle(getString(R.string.title_live_goods));
        ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.live_goods_ptr_framelayout);

        CommonUtil.SetPtrRefreshConfig(this, ptrFrameLayout, MConstants.REFRESH_HEADER_WHITE);

        recyclerView = (RecyclerView) findViewById(R.id.live_goods_recyclerView);
        //添加分割线
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        goodsListAdapter = new LiveGoodsAdapter(80, this, goodses, R.layout.item_recycler_goodslist);
        //添加分割线
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(this.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        recyclerView.setAdapter(goodsListAdapter);
        goodsListAdapter.setInformation("购买");
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getVideoProductList(MConstants.DATA_4_REFRESH);
            }
        });

    }

    private void getVideoProductList(final int loadType) {
        userModel.videoProductList(videoId, new Callback<List<Goods>>() {
            @Override
            public void onSuccess(List<Goods> goodList) {
                if (goodList != null && goodList.size() > 0) {
                    if (loadType == MConstants.DATA_4_REFRESH) {
                        goodsListAdapter.replaceAll(goodList);
                        goodses.clear();
                    } else {
                        goodsListAdapter.addAll(goodList);
                    }
                    goodses.addAll(goodList);
                }else{
                    goodses.clear();
                    goodsListAdapter.clear();
                }
                ptrFrameLayout.refreshComplete();
              /*  initWidget();
                goodsListAdapter.notifyDataSetHasChanged();*/
            }

            @Override
            public void onFailure(int resultCode, String message) {
                ptrFrameLayout.refreshComplete();
            }
        });

    }

    @Override
    protected void setView() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh();
            }
        }, MConstants.DELAYED);

    }

    @Override
    public void onClick(View view) {

    }
}
