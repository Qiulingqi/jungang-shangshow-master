package com.shangshow.showlive.controller.personal.auction;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
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
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.responseBody.EditFriendBody;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 我发起的竞拍
 */
public class LaunchAuctionFragment extends BasePageFragment {
    public static String key_USER_TYPE;
    private PtrHTFrameLayout myAuctionPtrFramentlayout;
    private List<UserInfo> userInfos;
    private String userType;
    private long currPage = 1;
    private Context mContext;
    private RecyclerView typeEarnsRecyclerView;
    private AuctionAdapter auctionAdapter;

    public static LaunchAuctionFragment newInstance(String userType) {
        LaunchAuctionFragment f = new LaunchAuctionFragment();
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
                myAuctionPtrFramentlayout.autoRefresh(false);
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

        myAuctionPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.cashearn_ptr_framentlayout);
        CommonUtil.SetPtrRefreshConfig(mContext, myAuctionPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        myAuctionPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getLaunchAuctions(MConstants.DATA_4_REFRESH);
            }
        });

        typeEarnsRecyclerView = (RecyclerView) rootView.findViewById(R.id.cash_earn_type_recyclerView);
        //添加分割线
        typeEarnsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(mContext.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        typeEarnsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        auctionAdapter = new AuctionAdapter(mContext, new ArrayList<UserInfo>(), R.layout.item_recycler_attention);
        typeEarnsRecyclerView.setAdapter(auctionAdapter);

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
    public void getLaunchAuctions(final int loadType) {
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
        Subscription subscription = apiWrapper.friends(pageBody).subscribe(new NewSubscriber<Pager<UserInfo>>(mContext, true) {
            @Override
            public void onNext(Pager<UserInfo> userInfoPager) {
                userInfos = userInfoPager.list;
                currPage = userInfoPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    auctionAdapter.replaceAll(userInfos);
                } else {
                    auctionAdapter.addAll(userInfos);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myAuctionPtrFramentlayout.refreshComplete();
                    }
                }, 500);

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myAuctionPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }
        });
        ((MyAuctionListActivity) mContext).addSubscrebe(subscription);
    }

    /**
     * 取消关注
     *
     * @param userId
     */
    public void cancelAttention(long userId) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.cancelFriend(userId).subscribe(new NewSubscriber<EditFriendBody>(mContext, true) {
            @Override
            public void onNext(EditFriendBody editFriendBody) {
                //刷新界面
                getLaunchAuctions(MConstants.DATA_4_REFRESH);
            }
        });
        ((MyAuctionListActivity) mContext).addSubscrebe(subscription);

    }

    public class AuctionAdapter extends SuperAdapter<UserInfo> {
        public AuctionAdapter(Context context, List<UserInfo> items, int layoutResId) {
            super(context, items, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final UserInfo item) {
            ImageView userIcon = holder.findViewById(R.id.user_icon);
            ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
            TextView userName = holder.findViewById(R.id.attention_user_name);
            final TextView attentionBtn = holder.findViewById(R.id.attention_btn);
            attentionBtn.setText(R.string.cancel_attention);
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
            ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
            userName.setText(item.userName);
            attentionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelAttention(item.userId);
                }
            });
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, UserInfo item) {

        }

    }

}
