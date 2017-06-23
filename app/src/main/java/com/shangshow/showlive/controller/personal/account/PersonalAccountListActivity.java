package com.shangshow.showlive.controller.personal.account;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.controller.personal.pay.PayOrderActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.Recharge;
import com.shangshow.showlive.network.service.models.UserAddress;
import com.shangshow.showlive.network.service.models.requestJson.BuyProductRequest;
import com.shangshow.showlive.network.service.models.responseBody.BuyProductResponse;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 我的订单
 */

/**
 * 账户明细  以及选择充值
 */
public class PersonalAccountListActivity extends BaseActivity {
    private PtrFrameLayout accountPtrFrameLayout;
    private RecyclerView accountRecyclerView;
    private AccountListAdapter accountListAdapter;
    private TextView diamondCountText;
    private List<Recharge> rechargeList = new ArrayList<Recharge>();
    private UserAddress userAddress;
    private UserModel userModel;
    private int positionss = 0;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_account;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        userModel = new UserModel(this);
        titleBarView.initCenterTitle(R.string.personal_account);
        diamondCountText = (TextView) findViewById(R.id.account_dcount_tv);
        getAccountBalance();
//        UserInfo userInfo = CacheCenter.getInstance().getCurrUser();
//        diamondCountText.setText("88888);
        accountPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.personal_account_recharge);
        CommonUtil.SetPtrRefreshConfig(this, accountPtrFrameLayout, MConstants.REFRESH_HEADER_WHITE);
        accountPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getRechargeList();
            }

        });
        accountRecyclerView = (RecyclerView) findViewById(R.id.personal_account_recharge_recyclerView);
        //分割线
        accountRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        accountListAdapter = new AccountListAdapter(this, new ArrayList<Recharge>(), R.layout.item_recycler_recharge);
        accountRecyclerView.setAdapter(accountListAdapter);
        accountListAdapter.setOnItemClickListener(new OnItemClickListener() {

            private long amount;

            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                amount = rechargeList.get(position).getAmount();
                positionss = position;
                long productId = 200;
                BuyProductRequest buyProductRequest = new BuyProductRequest();
                buyProductRequest.address = "";
                buyProductRequest.contactName = "";
                buyProductRequest.contactPhone = "";
                buyProductRequest.orderType = "1";
                buyProductRequest.videoUserId = "";
                buyProductRequest.videoId = "";
                buyProductRequest.remark = "";
                userModel.chongzhixiubiBuy(productId, buyProductRequest, new Callback<BuyProductResponse>() {
                    @Override
                    public void onSuccess(BuyProductResponse buyProductResponse) {

                        initperform(buyProductResponse.orderNo,amount,buyProductResponse.orderId);
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });

            }
        });
    }

    private void initperform(String orderNo,long amount,String orderId) {
        Intent intent = new Intent(PersonalAccountListActivity.this, PayOrderActivity.class);
        intent.putExtra("jiushiwo",amount + "");
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("orderId",orderId);
        startActivity(intent);

    }

    private class AccountListAdapter extends SuperAdapter<Recharge> {

        public AccountListAdapter(Context context, List<Recharge> items, int layoutResId) {
            super(context, items, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Recharge recharge) {
            TextView dcountText = holder.findViewById(R.id.dcount_tv);
            ImageView giveDcountImage = holder.findViewById(R.id.give_dcount_iv);
            TextView giveDcountText = holder.findViewById(R.id.give_dcount_tv);
            TextView amountText = holder.findViewById(R.id.amount_tv);
            dcountText.setText(recharge.getdCount() + "");
            int giveDcount = recharge.getGaveDCount();
            if (giveDcount != 0) {
                giveDcountText.setText("+" + giveDcount);
                giveDcountImage.setVisibility(View.VISIBLE);
            } else {
                giveDcountImage.setVisibility(View.INVISIBLE);
                giveDcountText.setVisibility(View.INVISIBLE);
            }
            amountText.setText("¥" + recharge.getAmount());
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, Recharge item) {

        }

    }


    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                accountPtrFrameLayout.autoRefresh();
            }
        }, MConstants.DELAYED);

    }

    private void getAccountBalance() {
       /* ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getRewardMeAmounts().subscribe(new NewSubscriber<Long>(this, true) {
            @Override
            public void onNext(Long amount) {
                ToastUtils.show(amount.toString());


                diamondCountText.setText(amount + "");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        accountPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        accountPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }
        });
        addSubscrebe(subscription);*/
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getAccountBalance().subscribe(new NewSubscriber<Long>(PersonalAccountListActivity.this, false) {
            @Override
            public void onNext(Long amount) {
                diamondCountText.setText(amount + "");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        accountPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        accountPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }
        });
        this.addSubscrebe(subscription);


    }

    /**
     * 获取账单列表
     */
    private void getRechargeList() {
        ApiWrapper apiWrapper = new ApiWrapper();
//        PageBody pageBody = new PageBody();
//        pageBody.pageNum = MConstants.PAGE_INDEX;
//        pageBody.pageSize = MConstants.PAGE_SIZE;
//        pageBody.orders = "";
        Subscription subscription = apiWrapper.rechargeList().subscribe(new NewSubscriber<List<Recharge>>(this, true) {
            @Override
            public void onNext(List<Recharge> rechargeList) {
                PersonalAccountListActivity.this.rechargeList.addAll(rechargeList);
                accountListAdapter.replaceAll(rechargeList);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        accountPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        accountPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }
        });
        addSubscrebe(subscription);
    }

}


