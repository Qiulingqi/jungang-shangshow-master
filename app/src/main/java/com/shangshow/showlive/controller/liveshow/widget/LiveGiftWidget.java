package com.shangshow.showlive.controller.liveshow.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.netease.nim.uikit.session.extension.GiftInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.viewpager.CustomViewPager;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.OrderPageBody;
import com.shangshow.showlive.network.service.models.body.RewardGiftBody;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by taolong on 16/8/15.
 */
//  赠送礼物  虚拟礼物  和   实际物品
public class LiveGiftWidget extends LinearLayout implements View.OnClickListener {

    int tempPosition = -1;
    int giftNum = 0;
    private View rootView;
    private CustomViewPager giftSelViewPager;
    private RecyclerView giftSelRecyclerView;
    private GiftRecyclerAdapter giftRecyclerAdapter;
    private Context mContext;
    private String mGiftType;
    private LiveWidgetOperate.LiveAudienceInterface mLiveAudienceInterface;
    private List<GiftInfo> giftInfoList = new ArrayList<GiftInfo>();
    private GiftInfo giftInfo;
    private int pageViewSize;
    private VideoRoom mVideoRoom;
    private RewardGiftBody rewardGiftBody;

    public LiveGiftWidget(Context context, String giftType, LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface, VideoRoom videoRoom) {
        super(context);
        mContext = context;
        mGiftType = giftType;
        mLiveAudienceInterface = liveAudienceInterface;
        mVideoRoom = videoRoom;
        initView();
    }

    public LiveGiftWidget(Context context, AttributeSet attrs, String giftType, LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface, VideoRoom videoRoom) {
        super(context, attrs);
        mContext = context;
        mGiftType = giftType;
        mLiveAudienceInterface = liveAudienceInterface;
        mVideoRoom = videoRoom;
        initView();
    }


    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_live_audience_gift_viewpager, this);
//        giftSelViewPager = (CustomViewPager) findViewById(R.id.gift_sel_view_pager);
          getGiftInfos(mGiftType);
//        giftSelViewPager.setAdapter(new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return pageViewSize;
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//
//                return false;
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
////                return super.instantiateItem(container, position);
//
//            }
//        });
//
//
//
//        giftSelViewPager.setIsScrollable(true);
//        giftSelViewPager.setCurrentItem(0);

        giftSelRecyclerView = (RecyclerView) findViewById(R.id.gift_item_recycler_view);
        //分割线
        giftSelRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
//        giftSelRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        giftSelRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        giftRecyclerAdapter = new GiftRecyclerAdapter(mContext, giftInfoList, R.layout.item_recycler_gift);
        giftSelRecyclerView.setAdapter(giftRecyclerAdapter);
        giftRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                if (tempPosition == position) {
                    giftNum++;
                } else {
                    tempPosition = position;
                    giftNum = 1;
                }
                //        Reward
                giftInfo = giftInfoList.get(position);
                rewardAnchorGift(giftInfo, giftNum);

            }
        });
//        giftSelViewPager.addView(giftSelRecyclerView);
    }

    private void rewardAnchorGift(final GiftInfo gift, final int giftNo) {

        UserModel userModel = new UserModel(this.getContext());
        rewardGiftBody = new RewardGiftBody(mVideoRoom.userId, mVideoRoom.videoId, gift.getGaveProductId());
        userModel.rewardAnchor(rewardGiftBody, new Callback<Long>() {
            @Override
            public void onSuccess(Long amount) {
                if (amount < gift.getAmount()) {
                    ToastUtils.show("余额不足");
                    return;
                }
                if (mLiveAudienceInterface != null && giftInfoList != null && giftInfoList.size() > 0) {
                    mLiveAudienceInterface.onClickSendGift(gift, giftNo);
                    if (rewardGiftBody.getGaveProductId() > 20){
                        ToastUtils.show("商品已送出，正在安排快递为您寄出。。。");
                    }
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        });
//        ApiWrapper apiWrapper = new ApiWrapper();
//        RewardGiftBody rewardGiftBody=new RewardGiftBody(mVideoRoom.userId, mVideoRoom.videoId, gift.getProductId());
//        Subscription subscription = apiWrapper.rewardAnchor(rewardGiftBody).subscribe(new NewSubscriber<Long>(mContext, false) {
//            @Override
//            public void onNext(Long amount) {
//                    if (mLiveAudienceInterface != null && giftInfoList != null && giftInfoList.size() > 0) {
//                        mLiveAudienceInterface.onClickSendGift(gift, giftNo);
//                    }
//            }
//
//            @Override
//            public void onError(ApiException e) {
//                Log.i("json", "message:" + e.getErrMessage());
//                if(e.getCode()==0) {
//                    CustomDialogHelper.TwoButtonDialog(mContext, "提示", "余额不足,亲记得充值呦", "关闭", new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    }, "去充值", new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mLiveAudienceInterface.onClickAddMoney();
//                        }
//                    });
//
//                }else {
//                    super.onError(e);
//                }
//            }
//        });
//        ((LiveAudienceActivity) mContext).addSubscrebe(subscription);
    }

    private void getGiftInfos(String giftType) {
        ApiWrapper apiWrapper = new ApiWrapper();
        OrderPageBody pageBody = new OrderPageBody();
        pageBody.pageNum = MConstants.PAGE_INDEX;
        pageBody.pageSize = MConstants.PAGE_SIZE * 100;
        Subscription subscription = apiWrapper.getLiveGaves(pageBody, giftType).subscribe(new NewSubscriber<Pager<GiftInfo>>(mContext, true) {
            @Override
            public void onNext(Pager<GiftInfo> giftInfoPager) {
                giftInfoList.clear();
                giftInfoList.addAll(giftInfoPager.list);
                pageViewSize = (int) (giftInfoPager.pages);
                giftRecyclerAdapter.replaceAll(giftInfoList);
                giftRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
            }
        });
        ((LiveAudienceActivity) mContext).addSubscrebe(subscription);
    }

    @Override
    public void onClick(View v) {

    }
}
