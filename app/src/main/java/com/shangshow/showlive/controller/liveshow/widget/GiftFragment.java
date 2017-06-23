package com.shangshow.showlive.controller.liveshow.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.common.widget.viewpager.CustomViewPager;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.netease.nim.uikit.session.extension.GiftInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * @author
 */
public class GiftFragment extends BasePageFragment {
    public static String key_USER_TYPE;
    private UserModel userModel;
    private PtrHTFrameLayout giftPtrFramentlayout;
    private String userType;
    private long currPage = 1;
    private Context mContext;
    private RecyclerView giftRecyclerView;
    private CustomViewPager giftViewPager;
    private GiftRecyclerAdapter giftRecyclerAdapter;

    public static GiftFragment newInstance(String userType,String giftType) {
        GiftFragment f = new GiftFragment();
        Bundle b = new Bundle();
        b.putString(key_USER_TYPE, userType);
        f.setArguments(b);
        return f;
    }

    @Override
    public void lazyLoad() {
//        giftPtrFramentlayout.autoRefresh(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                giftPtrFramentlayout.autoRefresh(false);
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
        return R.layout.widget_gift_type_fragment;
    }

    @Override
    protected void initWidget(View rootView) {
        userModel = new UserModel(mContext);
        userType = getArguments().getString(key_USER_TYPE);

        giftPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.gift_ptr_framentlayout);
        giftViewPager=(CustomViewPager)rootView.findViewById(R.id.gift_custom_view_pager);
        giftViewPager.setIsScrollable(true);
        CommonUtil.SetPtrRefreshConfig(mContext, giftPtrFramentlayout, MConstants.REFRESH_HEADER_BLACK);
        giftPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getGift(MConstants.DATA_4_REFRESH,"1");
            }
        });

        giftRecyclerView = (RecyclerView) rootView.findViewById(R.id.gift_type_recyclerView);
        //添加分割线
        giftRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(mContext.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        giftRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        giftRecyclerAdapter = new GiftRecyclerAdapter(mContext, new ArrayList<GiftInfo>(), R.layout.item_recycler_gift);
        giftRecyclerView.setAdapter(giftRecyclerAdapter);

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
    public void getGift(final int loadType,String giftType) {
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
        userModel.getLiveGaves(pageBody,giftType, new Callback<Pager<GiftInfo>>() {
            @Override
            public void onSuccess(Pager<GiftInfo> giftInfoPager) {
                List<GiftInfo> giftInfoList = giftInfoPager.list;
                currPage = giftInfoPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    giftRecyclerAdapter.replaceAll(giftInfoList);
                } else {
                    giftRecyclerAdapter.addAll(giftInfoList);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        giftPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        giftPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }
}
