package com.shangshow.showlive.controller.personal.income;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.Goods;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 现金收益
 */

public class CashEarnFragment extends BasePageFragment {
    public static String key_USER_TYPE;
    private PtrHTFrameLayout cashEarnsPtrFramentlayout;
    private TextView monthCashCountText;
    private List<UserInfo> userInfos;
    private String userType;
    private long currPage = 1;
    private Context mContext;
    private RecyclerView typeEarnsRecyclerView;
    private EarnsAdapter earnsAdapter;

    public static CashEarnFragment newInstance(String userType) {
        CashEarnFragment f = new CashEarnFragment();
        Bundle b = new Bundle();
        b.putString(key_USER_TYPE, userType);
        f.setArguments(b);
        return f;
    }

    @Override
    public void lazyLoad() {
//        myfriendsPtrFramentlayout.autoRefresh(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cashEarnsPtrFramentlayout.autoRefresh(false);
            }
        }, 300);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_cashearn_type_fragment;
    }


    @Override
    protected void initWidget(View rootView) {
        userType = getArguments().getString(key_USER_TYPE);
        cashEarnsPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.cashearn_ptr_framentlayout);
        monthCashCountText=(TextView)rootView.findViewById(R.id.account_month_cashcount_tv);
        getCashEarnAmounts();
        CommonUtil.SetPtrRefreshConfig(mContext, cashEarnsPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        cashEarnsPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getCashEarnInfo(MConstants.DATA_4_REFRESH);
            }
        });

        typeEarnsRecyclerView = (RecyclerView) rootView.findViewById(R.id.cash_earn_type_recyclerView);
        //添加分割线
        typeEarnsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(mContext.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        typeEarnsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        earnsAdapter = new EarnsAdapter(mContext, new ArrayList<Goods>(), R.layout.item_recycler_exchange_list);
        typeEarnsRecyclerView.setAdapter(earnsAdapter);

    }

    @Override
    protected void bindEven() {
    }

    @Override
    protected void setView() {
    }

    private void getCashEarnAmounts() {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getCashEarnAmounts().subscribe(new NewSubscriber<Long>(mContext,true){
            @Override
            public void onNext(Long amount) {
                monthCashCountText.setText(amount+"");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cashEarnsPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cashEarnsPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }
        });
        ((MyEarnListActivity) mContext).addSubscrebe(subscription);
    }

    /**
     *
     */
    public void getCashEarnInfo(final int loadType) {
        ApiWrapper apiWrapper = new ApiWrapper();
        PageBody pageBody = new PageBody();
        //如果刷新，请求第一页
        if (loadType == MConstants.DATA_4_REFRESH) {
            pageBody.pageNum = MConstants.PAGE_INDEX;
        } else {
            pageBody.pageNum = currPage;
        }
        pageBody.pageSize = MConstants.PAGE_SIZE;
        pageBody.orders = "";
        pageBody.userType = userType;
        Subscription subscription = apiWrapper.getCashEarnInfo(pageBody).subscribe(new NewSubscriber<Pager<Goods>>(mContext, true) {
            @Override
            public void onNext(Pager<Goods> goodsPager) {
//                userInfos = userInfoPager.list;
                currPage = goodsPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    earnsAdapter.replaceAll(goodsPager.list);
                } else {
                    earnsAdapter.addAll(goodsPager.list);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cashEarnsPtrFramentlayout.refreshComplete();
                    }
                }, 500);

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cashEarnsPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }
        });
        ((MyEarnListActivity) mContext).addSubscrebe(subscription);
    }

    public class EarnsAdapter extends SuperAdapter<Goods> {
        public EarnsAdapter(Context context, List<Goods> items, int layoutResId) {
            super(context, items, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final Goods item) {
            ImageView goodImage = holder.findViewById(R.id.item_goods_image);
            TextView goodsNameText = holder.findViewById(R.id.item_goods_name_tv);
            TextView goodsPriceText = holder.findViewById(R.id.item_goods_price);
            TextView goodsNoText = holder.findViewById(R.id.item_goods_no);
            if (null != item.productName && null != item.price && 0 != item.buyCount && null !=item.logoUrl){
                ImageLoaderKit.getInstance().displayImage(item.logoUrl, goodImage, true);
                goodsNameText.setText(item.productName);
                goodsPriceText.setText(item.price + "");
                goodsNoText.setText(item.buyCount);
            }

        }

        @Override
        public void noHolder(View convertView, int layoutPosition, Goods item) {

        }

    }


}
