package com.shangshow.showlive.controller.liveshow.goods;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.internal.LinkedTreeMap;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.controller.liveshow.adapter.MerchantGoodsAdapter;
import com.shangshow.showlive.controller.personal.goods.CooperateMerchantAdapter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.http.Response;
import com.shangshow.showlive.network.service.models.Goods;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.requestJson.UserGetList;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.divider.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lorntao on 9/19/16.
 */
//  直播中只有网红才能上架销售物品   商家只能查看合作的网红

public class LiveAnchorGoodsActivity extends BaseActivity implements View.OnClickListener {

    private TextView typeNumText;
    private RecyclerView goodsRecyclerView;
    private RecyclerView recyclerView;
    private RecyclerView cooperateMerchantRecyclerView;
//    private LiveGoodsAdapter goodsListAdapter;
    private MerchantGoodsAdapter merchantAddGoodsAdapter;
    private MerchantGoodsAdapter merchantDeleteGoodsAdapter;
//    private LiveSoldingGoodsAdapter liveSoldingGoodsAdapter;
    private CooperateMerchantAdapter cooperateMerchantAdapter;

    private BaseButton addGoodsBtn, removeGoodsBtn;
    private ArrayList<Goods> addGoods = new ArrayList<Goods>();
    private ArrayList<Goods> deleteGoods = new ArrayList<Goods>();
    private ArrayList<UserInfo> mUserDatas = new ArrayList<UserInfo>();
    private UserModel userModel;
    private long currPage = 1;
    private long currCooperatePage = 1;
    private long userId;
    private long videoId;
    private UserInfo userInfo;

    @Override
    protected int getActivityLayout() {

        return R.layout.activity_live_anchor_goods;
    }

    @Override
    protected void bindEven() {
        merchantAddGoodsAdapter.setOnItemOperaterListener(new MerchantGoodsAdapter.OnItemOperaterListener() {
            @Override
            public void onCheck(int position) {
                merchantAddGoodsAdapter.notifyDataSetChanged();
            }
        });
        merchantDeleteGoodsAdapter.setOnItemOperaterListener(new MerchantGoodsAdapter.OnItemOperaterListener() {
            @Override
            public void onCheck(int position) {
                merchantDeleteGoodsAdapter.notifyDataSetChanged();
            }
        });
        cooperateMerchantAdapter.setOnItemClickListener(new CooperateMerchantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                getGoods(MConstants.DATA_4_REFRESH, mUserDatas.get(position).userId);
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
        userId = getIntent().getLongExtra("live_anchor_user_id", -1);
        videoId = getIntent().getLongExtra("live_video_id", -1);
        userInfo = CacheCenter.getInstance().getCurrUser();
        if (userId == -1) {
            userId = userInfo.userId;
        }
        titleBarView.initCenterTitle(getString(R.string.title_live_goods));
        typeNumText = (TextView) findViewById(R.id.solding_goods_type_count);
        addGoodsBtn = (BaseButton) findViewById(R.id.add_goods_btn);
        removeGoodsBtn = (BaseButton) findViewById(R.id.remove_goods_btn);

        goodsRecyclerView = (RecyclerView) findViewById(R.id.my_solding_goods_recycler_view);
        //添加分割线
        goodsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        liveSoldingGoodsAdapter = new LiveSoldingGoodsAdapter(this, mSoldingDatas, R.layout.item_recycler_mygoods_cooperate_merchant);
        merchantDeleteGoodsAdapter = new MerchantGoodsAdapter(this, addGoods, R.layout.check_image_layout);
        goodsRecyclerView.setAdapter(merchantDeleteGoodsAdapter);

        cooperateMerchantRecyclerView = (RecyclerView) findViewById(R.id.live_cooperate_merchant_recyclerView);
        //添加分割线
        cooperateMerchantRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cooperateMerchantAdapter = new CooperateMerchantAdapter(this, mUserDatas, R.layout.item_recycler_mygoods_cooperate_merchant);
        //添加分割线
        cooperateMerchantRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                .color(this.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        cooperateMerchantRecyclerView.setAdapter(cooperateMerchantAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.live_goods_recyclerView);
        //添加分割线
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        goodsListAdapter = new LiveGoodsAdapter(80, this, mDatas, R.layout.item_recycler_goodslist);
        merchantAddGoodsAdapter = new MerchantGoodsAdapter(this, deleteGoods, R.layout.check_image_layout);
        //添加分割线
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(this.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        recyclerView.setAdapter(merchantAddGoodsAdapter);

        addGoodsBtn.setOnClickListener(this);
        removeGoodsBtn.setOnClickListener(this);

        getVideoProductList(MConstants.DATA_4_REFRESH);
        loadCooperateData(MConstants.DATA_4_REFRESH);
    }

    private void getVideoProductList(final int loadType) {
        userModel.videoProductList(videoId, new Callback<List<Goods>>() {
            @Override
            public void onSuccess(List<Goods> goodList) {
                if (goodList != null && goodList.size() > 0) {
                    if (loadType == MConstants.DATA_4_REFRESH) {
                        merchantDeleteGoodsAdapter.replaceAll(goodList);
                        deleteGoods.clear();
                    } else {
                        merchantDeleteGoodsAdapter.addAll(goodList);
                    }
                    deleteGoods.addAll(goodList);
                }else{
                    deleteGoods.clear();
                    merchantDeleteGoodsAdapter.clear();
                }
                typeNumText.setText("正在销售：" + deleteGoods.size() + "件");
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        });

    }

    private void getGoods(final int loadType, long userId) {
        UserGetList userGetList = new UserGetList();
        // 如果刷新，请求第一页
        if (loadType == MConstants.DATA_4_REFRESH) {
            userGetList.pageNum = MConstants.PAGE_INDEX;
        } else {
            userGetList.pageNum = currPage;
        }
        userGetList.pageSize = MConstants.PAGE_SIZE;
        userGetList.userId = userId;
        userModel.getProducts(userGetList, new Callback<Response<Object>>() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                try {
                    if (objectResponse != null) {
                        LinkedTreeMap map = (LinkedTreeMap) objectResponse.result;
                        List<Object> objectList = (List<Object>) map.get("list");
                        List<Goods> goodses = JSONArray.parseArray(JSON.toJSONString(objectList), Goods.class);
                        if (loadType == MConstants.DATA_4_REFRESH) {
                            merchantAddGoodsAdapter.replaceAll(goodses);
                            addGoods.clear();
                        } else {
                            merchantAddGoodsAdapter.addAll(goodses);
                        }
                        addGoods.addAll(goodses);
                    }else{
                        merchantAddGoodsAdapter.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        });

    }

    public void loadCooperateData(final int loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currCooperatePage);
        userModel.cooperationBusinessList(userInfo.userId, pageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                currCooperatePage = userInfoPager.pageNum;
                List<UserInfo> userInfos = userInfoPager.list;
                if (userInfos != null && userInfos.size() > 0) {
                    if (loadType == MConstants.DATA_4_REFRESH) {
                        mUserDatas.clear();
                        mUserDatas.addAll(userInfos);
                        cooperateMerchantAdapter.replaceAll(mUserDatas);
                    } else {
                        mUserDatas.addAll(userInfos);
                        cooperateMerchantAdapter.addAll(mUserDatas);
                    }
                    getGoods(MConstants.DATA_4_REFRESH, mUserDatas.get(0).userId);
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        }, true);
    }

    @Override
    protected void setView() {



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_goods_btn:
                addGoods();
                break;
            case R.id.remove_goods_btn:
                removeGoods();
                break;
        }
    }

    //  添加商品
    private void addGoods() {
        if(merchantAddGoodsAdapter.getCheckCount() == 0){
            showToast("请确认要添加的商品");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\"requestBody\":[");
        int count = 0;
        for(Integer productId : merchantAddGoodsAdapter.getCheckList()){
            boolean isContinue = false;
            for(Goods goods : deleteGoods){
                if(addGoods.get(productId).productId == goods.productId){
                    isContinue = true;
                    break;
                }
            }
            if(isContinue){
                continue;
            }
            builder.append("{\"productId\":" + addGoods.get(productId).productId + "},");
            count++;
        }
        if(count == 0){
            merchantAddGoodsAdapter.clearChecks();
            merchantAddGoodsAdapter.notifyDataSetChanged();
            showToast("该商品已经上架");
            return;
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]}");
        userModel.productShelves(videoId, builder.toString(), new Callback<Object>() {
            @Override
            public void onSuccess(Object o) {
                getVideoProductList(MConstants.DATA_4_REFRESH);
                merchantAddGoodsAdapter.clearChecks();
                merchantAddGoodsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    private void removeGoods() {
        if(merchantDeleteGoodsAdapter.getCheckCount() == 0){
            showToast("请确认要删除的商品");
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("{\"requestBody\":[");
        for(Integer productId : merchantDeleteGoodsAdapter.getCheckList()){
            builder.append("{\"productId\":" + deleteGoods.get(productId).productId + "},");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]}");
        userModel.productOffShelves(videoId, builder.toString(), new Callback<Object>() {
            @Override
            public void onSuccess(Object o) {
                getVideoProductList(MConstants.DATA_4_REFRESH);
                merchantDeleteGoodsAdapter.clearChecks();
                merchantDeleteGoodsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

}
