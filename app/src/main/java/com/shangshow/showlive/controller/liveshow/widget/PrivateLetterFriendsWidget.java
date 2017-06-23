package com.shangshow.showlive.controller.liveshow.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.controller.adapter.MyLiveVideoSingleAdapter;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.Pager;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.widget.SwipeListView;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.List;

import rx.Subscription;

/**
 * Created by taolong on 16/8/15.
 */
public class PrivateLetterFriendsWidget extends LinearLayout implements View.OnClickListener {

    private View rootView;
    private PtrHTFrameLayout privateLetterPtrFramentlayout;
    private SwipeListView privateLetterRecyclerView;
    private PrivateLetterFriendsRecyclerAdapter privateLetterFriendsRecyclerAdapter;
    private Context mContext;
    private LiveWidgetOperate.LiveAudienceInterface mLiveAudienceInterface;
//    private String userType;
    private long currPage = 1;
    private List<UserInfo> userInfos;

    public PrivateLetterFriendsWidget(Context context, LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface) {
        super(context);
        mContext = context;
        mLiveAudienceInterface = liveAudienceInterface;
        initView();
    }

    public PrivateLetterFriendsWidget(Context context, AttributeSet attrs, LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface) {
        super(context, attrs);
        mContext = context;
        mLiveAudienceInterface = liveAudienceInterface;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_live_audience_private_letter_viewpager, this);

        privateLetterPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.private_letter_ptr_framentlayout);
        CommonUtil.SetPtrRefreshConfig(mContext, privateLetterPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);

        privateLetterPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getFriends(MConstants.DATA_4_REFRESH);
            }
        });

        privateLetterRecyclerView = (SwipeListView) findViewById(R.id.private_letter_recycler);
        privateLetterRecyclerView.setRightViewWidth(0);

        privateLetterFriendsRecyclerAdapter = new PrivateLetterFriendsRecyclerAdapter(mContext, mLiveAudienceInterface, userInfos, R.layout.item_recycler_private_letter);
        privateLetterRecyclerView.setAdapter(privateLetterFriendsRecyclerAdapter);
//        getFriends(MConstants.DATA_4_REFRESH);

    }

    /**
     * 获取我的关注列表
     */
    public void getFriends(final int loadType) {
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
//        pageBody.userType = userType;
        Subscription subscription = apiWrapper.friends(pageBody).subscribe(new NewSubscriber<Pager<UserInfo>>(mContext, true) {
            @Override
            public void onNext(Pager<UserInfo> userInfoPager) {
                userInfos = userInfoPager.list;
                currPage = userInfoPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    privateLetterFriendsRecyclerAdapter.replaceAll(userInfos);
                } else {
                    privateLetterFriendsRecyclerAdapter.addAll(userInfos);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        privateLetterPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        privateLetterPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }
        });
        ((LiveAudienceActivity) mContext).addSubscrebe(subscription);
    }

    @Override
    public void onClick(View v) {

    }

    public void update(){
        privateLetterPtrFramentlayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                privateLetterPtrFramentlayout.autoRefresh();
            }
        }, MConstants.DELAYED);
    }

}
