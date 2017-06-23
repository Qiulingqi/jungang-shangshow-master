package com.shangshow.showlive.controller.personal.goods;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.internal.LinkedTreeMap;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.controller.liveshow.goods.LiveGoodsAdapter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.http.Response;
import com.shangshow.showlive.network.service.models.Goods;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.CooperationPageBody;
import com.shangshow.showlive.network.service.models.requestJson.UserGetList;
import com.shangshow.showlive.widget.SwipeListView;
import com.shaojun.widget.superAdapter.divider.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Super-me on 9/19/16.
 */
//  添加商品  只有商家才可以添加商品 网红可以查看  普通用户只能申请~~~~
public class MyGoodsActivity extends BaseActivity implements View.OnClickListener {

    private PtrHTFrameLayout ptrFrameLayout;
    private SwipeListView recyclerView;
    private RecyclerView cooperateMerchantRecyclerView;
    UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
    private LiveGoodsAdapter goodsListAdapter;
    private CooperateMerchantAdapter cooperateMerchantAdapter;
    private ArrayList<Goods> mDatas = new ArrayList<Goods>();
    private ArrayList<UserInfo> mUserDatas = new ArrayList<UserInfo>();
    private UserModel userModel;
    private long currPage = 1;
    private long currCooperatePage = 1;
    private long userId;
    private UserInfo userInfo;
    private View mygoodsAddGoodsa;
    private View mygoodsTop;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_mygoods;
    }

    @Override
    protected void bindEven() {

        userId = getIntent().getLongExtra("live_anchor_user_id", -1);
        userInfo = CacheCenter.getInstance().getCurrUser();
        if (userId == -1) {
            userId = userInfo.userId;
        }
        if (!userInfo.userType.equals(MConstants.USER_TYPE_MERCHANT)) {
            mygoodsAddGoodsa.setVisibility(View.GONE);
            mygoodsTop.setVisibility(View.VISIBLE);
        } else {
            mygoodsAddGoodsa.setVisibility(View.VISIBLE);
            mygoodsTop.setVisibility(View.GONE);
            loadCooperateData(MConstants.DATA_4_REFRESH);
        }
        cooperateMerchantAdapter.setOnItemClickListener(new CooperateMerchantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                getGoods(MConstants.DATA_4_REFRESH, mUserDatas.get(position).userId);
            }
        });
        goodsListAdapter.setOnItemListener(new LiveGoodsAdapter.OnItemListener() {
            @Override
            public void delete(int position) {
                userModel.delProducts(mDatas.get(position).productId, new Callback<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        ptrFrameLayout.autoRefresh(true);
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });
            }

            @Override
            public void onItemClick(int position) {
                HashMap<String, String> map = new HashMap<>();
                map.put("productsId", mDatas.get(position).productId + "");
                map.put("userId", "" + ptrFrameLayout.getTag());
                startActivity(EditGoodsActivity.class, map);
            }
        });
        ptrFrameLayout.autoRefresh(true);
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        userModel = new UserModel(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(getString(R.string.title_live_goods));
        ptrFrameLayout = (PtrHTFrameLayout) findViewById(R.id.live_goods_ptr_framelayout);
        CommonUtil.SetPtrRefreshConfig(this, ptrFrameLayout, MConstants.REFRESH_HEADER_WHITE);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!userInfo.userType.equals(MConstants.USER_TYPE_MERCHANT)) {
                    loadCooperateData(MConstants.DATA_4_REFRESH);
                } else {
                    getGoods(MConstants.DATA_4_REFRESH, userId);
                }
            }
        });
        cooperateMerchantRecyclerView = (RecyclerView) findViewById(R.id.mygoods_cooperate_merchant);
        //添加分割线
        cooperateMerchantRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cooperateMerchantAdapter = new CooperateMerchantAdapter(this, mUserDatas, R.layout.item_recycler_mygoods_cooperate_merchant);
        //添加分割线
        cooperateMerchantRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                .color(this.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        cooperateMerchantRecyclerView.setAdapter(cooperateMerchantAdapter);

        recyclerView = (SwipeListView) findViewById(R.id.live_goods_recyclerView);
        recyclerView.setRightViewWidth(MConstants.USER_TYPE_MERCHANT.equals(currentUser.userType) ? 80 : 0);
        goodsListAdapter = new LiveGoodsAdapter(recyclerView.getRightViewWidth(), this, mDatas, R.layout.item_recycler_goodslist);
        recyclerView.setAdapter(goodsListAdapter);

        mygoodsAddGoodsa = findViewById(R.id.mygoods_add_goods_layout);
        mygoodsTop = findViewById(R.id.mygoods_top_layout);

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
        ptrFrameLayout.setTag("" + userId);
        userModel.getProducts(userGetList, new Callback<Response<Object>>() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                try {
                    if (objectResponse != null) {
                        LinkedTreeMap map = (LinkedTreeMap) objectResponse.result;
                        List<Object> objectList = (List<Object>) map.get("list");
                        List<Goods> goodses = new ArrayList<Goods>();
                        for (Object object : objectList) {
                            LinkedTreeMap objectMap = (LinkedTreeMap) object;
                            Goods goods = new Goods();
                            double id = Double.parseDouble(objectMap.get("productId") + "");
                            goods.productId = (long) id;
                            goods.status = objectMap.get("status") + "";
                            goods.price = objectMap.get("price") + "";
                            goods.logoUrl = objectMap.get("logoUrl") + "";
                            goods.productName = objectMap.get("productName") + "";
                            goods.brief = objectMap.get("brief") + "";
                            goodses.add(goods);
                        }
                        if (loadType == MConstants.DATA_4_REFRESH) {
                            goodsListAdapter.replaceAll(goodses);
                            mDatas.clear();
                        } else {
                            goodsListAdapter.addAll(goodses);
                        }
                        mDatas.addAll(goodses);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrFrameLayout.refreshComplete();
                        }
                    }, MConstants.DELAYED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }
        });

    }

    public void loadCooperateData(final int loadType) {
        CooperationPageBody cooperationPageBody = new CooperationPageBody();
        //如果刷新，请求第一页
        if (loadType == MConstants.DATA_4_REFRESH) {
            cooperationPageBody.pageNum = MConstants.PAGE_INDEX;
        } else {
            cooperationPageBody.pageNum = currPage;
        }
        cooperationPageBody.pageSize = MConstants.PAGE_SIZE;
        cooperationPageBody.orders = "";
        cooperationPageBody.status = "EBL";
        userModel.getUserCooperationList(cooperationPageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                currPage = userInfoPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    mUserDatas.clear();
                    List<UserInfo> loadUserInfo = userInfoPager.list;
                    for (UserInfo userInfo : loadUserInfo) {
                        if (userInfo.userId != currentUser.userId && userInfo.applayUserId != 0) {
                            mUserDatas.add(userInfo);
                        }
                    }
                    cooperateMerchantAdapter.replaceAll(mUserDatas);
                } else {
                    List<UserInfo> loadUserInfo = userInfoPager.list;
                    for (UserInfo userInfo : loadUserInfo) {
                        if (userInfo.userId != currentUser.userId && userInfo.applayUserId != 0) {
                            mUserDatas.add(userInfo);
                        }
                    }
                    cooperateMerchantAdapter.addAll(mUserDatas);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 300);
                cooperateMerchantAdapter.notifyDataSetChanged();
                if (mUserDatas.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getGoods(MConstants.DATA_4_REFRESH, mUserDatas.get(0).userId);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 300);
            }
        });
    }

    @Override
    protected void setView() {
        mygoodsAddGoodsa.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mygoods_add_goods_layout: {
//                startActivity(EditGoodsActivity.class);
                Intent intent = new Intent(this, EditGoodsActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((CacheCenter.getInstance().getCurrUser().userId + "").equals(ptrFrameLayout.getTag() + "")) {
            ptrFrameLayout.autoRefresh(true);
        }
    }
}
