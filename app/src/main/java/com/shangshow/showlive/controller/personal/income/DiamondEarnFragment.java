package com.shangshow.showlive.controller.personal.income;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.DiamondEarnModel;
import com.netease.nim.uikit.model.UserInfo;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

import rx.Subscription;

/**
 * 钻石收益
 */
public class DiamondEarnFragment extends BasePageFragment {
    public static String key_USER_TYPE;
    private PtrHTFrameLayout diamondEarnPtrFramentlayout;
    private TextView allDiamandCountText,monthDiamandCountText,rewardDiamandCountText,adDiamandCountText,auctionDiamandCountText,traditionalDiamandCountText,accountTakeDcountText;
    private List<UserInfo> userInfos;
    private String userType;
    private long currPage = 1;
    private Context mContext;
    private RecyclerView typeEarnsRecyclerView;
    private EarnsAdapter earnsAdapter;

    public static DiamondEarnFragment newInstance(String userType) {
        DiamondEarnFragment f = new DiamondEarnFragment();
        Bundle b = new Bundle();
        b.putString(key_USER_TYPE, userType);
        f.setArguments(b);
        return f;
    }

    @Override
    public void lazyLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                diamondEarnPtrFramentlayout.autoRefresh(false);
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
        return R.layout.activity_personal_diamondearn_type_fragment;
    }

    @Override
    protected void initWidget(View rootView) {
        userType = getArguments().getString(key_USER_TYPE);

        diamondEarnPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.diamondearn_ptr_framentlayout);
        allDiamandCountText=(TextView)rootView.findViewById(R.id.account_all_dcount_tv);
        monthDiamandCountText=(TextView)rootView.findViewById(R.id.account_mouth_dcount_tv);
        rewardDiamandCountText=(TextView)rootView.findViewById(R.id.account_reward_dcount_tv);
        adDiamandCountText=(TextView)rootView.findViewById(R.id.account_ads_dcount_tv);
        auctionDiamandCountText=(TextView)rootView.findViewById(R.id.account_auction_dcount_tv);
        traditionalDiamandCountText=(TextView)rootView.findViewById(R.id.account_traditional_dcount_tv);
        accountTakeDcountText=(TextView)rootView.findViewById(R.id.account_take_dcount_tv);

        CommonUtil.SetPtrRefreshConfig(mContext, diamondEarnPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        diamondEarnPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getDiamondEarn();
            }
        });

//        typeEarnsRecyclerView = (RecyclerView) rootView.findViewById(R.id.diamond_earn_type_recyclerView);
//        //添加分割线
//        typeEarnsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
//                .color(mContext.getResources().getColor(R.color.default_stroke_color))
//                .sizeResId(R.dimen.common_activity_padding_1)
//                .build());
//        typeEarnsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
//        earnsAdapter = new EarnsAdapter(mContext, new ArrayList<UserInfo>(), R.layout.item_recycler_attention);
//        typeEarnsRecyclerView.setAdapter(earnsAdapter);

    }

    @Override
    protected void bindEven() {
    }

    @Override
    protected void setView() {
    }

    /**
     * 获取钻石收益
     */
    private void getDiamondEarn() {
        long userId= CacheCenter.getInstance().getTokenUserId();
        ApiWrapper apiWrapper=new ApiWrapper();
        Subscription subscription = apiWrapper.getDiamondEarn().subscribe(new NewSubscriber<DiamondEarnModel>(mContext,true){

            @Override
            public void onNext(DiamondEarnModel diamondEarnModel) {
                allDiamandCountText.setText(diamondEarnModel.getAllAmount()+"");
                monthDiamandCountText.setText(diamondEarnModel.getMonthAmount()+"");
                rewardDiamandCountText.setText(diamondEarnModel.getVirtualAmount()+"");
                auctionDiamandCountText.setText(diamondEarnModel.getAuctionAmount()+"");
                adDiamandCountText.setText(diamondEarnModel.getAdAmount()+"");
                traditionalDiamandCountText.setText(diamondEarnModel.getMatterAmount()+"");
                accountTakeDcountText.setText(diamondEarnModel.getCashBalance()+"");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        diamondEarnPtrFramentlayout.refreshComplete();
                    }
                }, 500);

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        diamondEarnPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }
        });
        ((MyEarnListActivity) mContext).addSubscrebe(subscription);
        }

    public class EarnsAdapter extends SuperAdapter<UserInfo> {
        public EarnsAdapter(Context context, List<UserInfo> items, int layoutResId) {
            super(context, items, layoutResId);
        }


        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final UserInfo item) {
//            ImageView userIcon = holder.findViewById(R.id.user_icon);
//            ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
//            TextView userName = holder.findViewById(R.id.attention_user_name);
//            final TextView attentionBtn = holder.findViewById(R.id.attention_btn);
//            attentionBtn.setText(R.string.cancel_attention);
//            ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
//            ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
//            userName.setText(item.userName);
//            attentionBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    cancelAttention(item.userId);
//                }
//            });
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, UserInfo item) {

        }

    }
}

