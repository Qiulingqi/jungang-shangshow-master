package com.shangshow.showlive.controller.personal.reward;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.RewardInfo;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 我的打赏
 */

public class MyRewardFragment extends BasePageFragment {
    public static String key_USER_TYPE;
    private PtrHTFrameLayout myRewardsPtrFramentlayout;
    private TextView myRewardCountText;
    private List<RewardInfo> rewardInfoList;
    private String userType;
    private long currPage = 1;
    private Context mContext;
    private RecyclerView typeRewardsRecyclerView;
    private RewardsAdapter rewardsAdapter;

    public static MyRewardFragment newInstance(String userType) {
        MyRewardFragment f = new MyRewardFragment();
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
                myRewardsPtrFramentlayout.autoRefresh(false);
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
        return R.layout.activity_personal_myreward_type_fragment;
    }

    @Override
    protected void initWidget(View rootView) {

        userType = getArguments().getString(key_USER_TYPE);

        myRewardsPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.myreward_ptr_framentlayout);
        myRewardCountText=(TextView)rootView.findViewById(R.id.account_myreward_dcount_tv) ;
        getMyRewardAmounts();
        CommonUtil.SetPtrRefreshConfig(mContext, myRewardsPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        myRewardsPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMyRewards(MConstants.DATA_4_REFRESH);
            }
        });

        typeRewardsRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_rewards_type_recyclerView);
        //添加分割线
        typeRewardsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(mContext.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        typeRewardsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        rewardsAdapter = new RewardsAdapter(mContext, new ArrayList<RewardInfo>(), R.layout.item_recycler_myreward);
        typeRewardsRecyclerView.setAdapter(rewardsAdapter);

    }

    @Override
    protected void bindEven() {
    }

    @Override
    protected void setView() {
    }

    /**
     * 获取我的关注列表
     */
    public void getMyRewards(final int loadType) {
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
        Subscription subscription = apiWrapper.getMyRewardList(pageBody).subscribe(new NewSubscriber<Pager<RewardInfo>>(mContext, true) {
            @Override
            public void onNext(Pager<RewardInfo> rewardInfoPager) {
                rewardInfoList = rewardInfoPager.list;
                currPage = rewardInfoPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    rewardsAdapter.replaceAll(rewardInfoList);
                } else {
                    rewardsAdapter.addAll(rewardInfoList);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myRewardsPtrFramentlayout.refreshComplete();
                    }
                }, 500);

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myRewardsPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }
        });
        ((MyRewardListActivity) mContext).addSubscrebe(subscription);
    }

    /**
     *
     */
    public void getMyRewardAmounts() {
        ApiWrapper apiWrapper = new ApiWrapper();
        long userId= CacheCenter.getInstance().getTokenUserId();
        Subscription subscription = apiWrapper.getMyRewardAmounts().subscribe(new NewSubscriber<Long>(mContext, true) {
            @Override
            public void onNext(Long amount) {
                myRewardCountText.setText(amount+"");
            }
        });
        ((MyRewardListActivity) mContext).addSubscrebe(subscription);

    }

    public class RewardsAdapter extends SuperAdapter<RewardInfo> {
        public RewardsAdapter(Context context, List<RewardInfo> items, int layoutResId) {
            super(context, items, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final RewardInfo item) {
            HeadImageView avatarImageView = holder.findViewById(R.id.user_icon_myreward);
            ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon_myreward);
            TextView labelText = holder.findViewById(R.id.myreward_mark_name_tv);
            TextView dcountText = holder.findViewById(R.id.myreward_dcount_tv);
            labelText.setText(item.getUserInfo().userName);
            dcountText.setText(item.getAmount() + "");
            ImageLoaderKit.getInstance().displayImage(item.getUserInfo().avatarUrl, avatarImageView, true);
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.getUserInfo().userType);
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, RewardInfo item) {

        }

    }
}
