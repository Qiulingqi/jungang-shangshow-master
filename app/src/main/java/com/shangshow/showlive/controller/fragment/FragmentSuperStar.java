package com.shangshow.showlive.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ScreenUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.TitleBarView;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.common.widget.viewpager.CirclePageIndicator;
import com.shangshow.showlive.common.widget.viewpager.LoopViewPager;
import com.shangshow.showlive.controller.adapter.BannerViewPagerAdapter;
import com.shangshow.showlive.controller.adapter.HomeLiveVideoSingleAdapter;
import com.shangshow.showlive.controller.adapter.VideoRemindAdapter;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.liveshow.video.PlayVideoActivity;
import com.shangshow.showlive.model.LiveModel;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.AdsInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.responseBody.VideoRemind;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 星咖（超级巨星）
 */

public class FragmentSuperStar extends BaseFragment {
    private LiveModel liveModel;
    private UserModel userModel;
    private TitleBarView titleBarView;
    private Context mContext;
    private RLPtrFrameLayout rlPtrFrameLayout;
    private HomeLiveVideoSingleAdapter homeLiveVideoSingleAdapter;
    private View topView;
    //banner广告
    private RelativeLayout bannerViewPagerLayout;
    private RecyclerView rv_superstar_top_video;
    private LoopViewPager bannerViewPager;
    private CirclePageIndicator bannerCirclePageIndicator;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private VideoRemindAdapter videoRemindAdapter;
    private ImageView iv_videoremind_content, iv_videoremind_video;

    private long currPage = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_super_star;
    }

    public static FragmentSuperStar newInstance(String title) {
        FragmentSuperStar f = new FragmentSuperStar();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

    @Override
    protected void initWidget(View rootView) {
        liveModel = new LiveModel(mContext);
        userModel = new UserModel(mContext);
        title = getArguments().getString("title");
        titleBarView = (TitleBarView) rootView.findViewById(R.id.super_star_title_layout);
        titleBarView.initCenterTitle(title);
//        titleBarView.initRight("列表", 0, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MemberListActivity.class);
//                intent.putExtra(MemberListActivity.key_USER_TYPE, MConstants.USER_TYPE_SUPERSTAR);
//                startActivity(intent);
//            }
//        });

        rlPtrFrameLayout = (RLPtrFrameLayout) rootView.findViewById(R.id.merchant_ptr_framentlayout);
        topView = getSuperStarTopView();
        //解决PtrFrameLayout嵌套ViewPager滑动冲突
        rlPtrFrameLayout.setViewPager(bannerViewPager);
        rlPtrFrameLayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadSuperStarVideoRoom(MConstants.DATA_4_LOADMORE);
            }
        });

        /**
         * 星咖直播列表
         */
        homeLiveVideoSingleAdapter = new HomeLiveVideoSingleAdapter(mContext, new ArrayList<VideoRoom>(), R.layout.item_recycler_common_livevideo_list);
        //+分割线
        rlPtrFrameLayout.getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.transparent))
                .sizeResId(R.dimen.common_activity_padding_10)
                .build());
        homeLiveVideoSingleAdapter.addHeaderView(topView);
        rlPtrFrameLayout.setAdapter(homeLiveVideoSingleAdapter, true);
        homeLiveVideoSingleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                if (!CommonUtil.isClickSoFast(MConstants.DELAYED)) {
                    if (CacheCenter.getInstance().isLogin()) {
                        VideoRoom videoRoom = homeLiveVideoSingleAdapter.getItem(position);
                        if ("LIV".equals(videoRoom.videoStatus)) {
                            LiveAudienceActivity.start(mContext, videoRoom);
                        }
                        if ("OFF".equals(videoRoom.videoStatus)) {
                            Intent intent = new Intent(mContext, PlayVideoActivity.class);
                            intent.putExtra("uri", videoRoom.videoUrl + "");
                            startActivity(intent);
                        }
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }
            }
        });


    }

    //星咖topView
    public View getSuperStarTopView() {

        View topView = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_superstar_top, null);
        bannerViewPagerLayout = (RelativeLayout) topView.findViewById(R.id.tab_banner_viewpager_layout);
        rv_superstar_top_video = (RecyclerView) topView.findViewById(R.id.rv_superstar_top_video);
        bannerViewPager = (LoopViewPager) topView.findViewById(R.id.tab_banner_viewpager);
        iv_videoremind_content = (ImageView) topView.findViewById(R.id.iv_videoremind_content);
        iv_videoremind_video = (ImageView) topView.findViewById(R.id.iv_videoremind_video);
        bannerCirclePageIndicator = (CirclePageIndicator) topView.findViewById(R.id.tab_banner_viewpager_indicator);
        rv_superstar_top_video.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        //设置banner的高度
        int width = ScreenUtil.getScreenWidth(mContext);
        bannerViewPagerLayout.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * MConstants.RATIO_POINT_BANNER)));
        iv_videoremind_content.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * MConstants.RATIO_POINT_BANNER)));
        iv_videoremind_video.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * MConstants.RATIO_POINT_BANNER)));
        return topView;
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlPtrFrameLayout.autoRefresh();
            }
        }, MConstants.DELAYED);
    }


    /**
     * 设置广告位视图
     */
    public void setBannerAdsView(List<AdsInfo> dataList) {
        if (dataList == null) {
            return;
        }
        if (bannerViewPagerAdapter == null) {
            bannerViewPagerAdapter = new BannerViewPagerAdapter(
                    mContext, dataList);
            bannerViewPager.setAdapter(bannerViewPagerAdapter);
            bannerViewPager.setBoundaryCaching(true);
            bannerCirclePageIndicator.setViewPager(bannerViewPager);
        } else {
            bannerViewPagerAdapter.changeData(dataList);
        }
    }

    //  设置。。。。。。
    public void setVideoRemind(final List<VideoRemind> videoReminds) {
        if (videoReminds == null) {
            return;
        }
        if (videoRemindAdapter == null) {
            videoRemindAdapter = new VideoRemindAdapter(mContext, videoReminds, R.layout.layout_superstar_videoremind_top);
            rv_superstar_top_video.setAdapter(videoRemindAdapter);
            videoRemindAdapter.setOnItemOperaterListener(new VideoRemindAdapter.OnItemOperaterListener() {
                @Override
                public void remind(int position) {
                    userModel.subscriberStar(videoReminds.get(position).userId, new Callback<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            loadVideoRemind();
                        }

                        @Override
                        public void onFailure(int resultCode, String message) {

                        }
                    });
                }
            });
        } else {
            videoRemindAdapter.replaceAll(videoReminds);
        }
    }

    //  数据源准备  加载 显示试图
    private void loadData() {
        // 加载 "" 开播提醒
       // loadVideoRemind();
        //   最顶部轮播广告
        loadSuperStarAds();
        //  加载明星直播间
        loadSuperStarVideoRoom(MConstants.DATA_4_REFRESH);
        }

    private void loadVideoRemind() {


        userModel.getVSInfoList(new Callback<List<VideoRemind>>() {
            @Override
            public void onSuccess(List<VideoRemind> videoReminds) {
                setVideoRemind(videoReminds);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 加载星咖广告,轮播广告
     */
    //   最顶部轮播广告
    private void loadSuperStarAds() {
        userModel.adsInfoList(MConstants.ADS_NO_SUPERSTAR, new Callback<List<AdsInfo>>() {
            @Override
            public void onSuccess(List<AdsInfo> adsInfos) {
                setBannerAdsView(adsInfos);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        }, false);
    }

    /**
     * 加载星咖直播间
     */
    private void loadSuperStarVideoRoom(final long loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currPage);
        liveModel.getVideoRoomList(MConstants.USER_TYPE_SUPERSTAR, pageBody, new Callback<Pager<VideoRoom>>() {
            @Override
            public void onSuccess(Pager<VideoRoom> pager) {
                currPage = pager.pageNum;
                List<VideoRoom> videoRooms = pager.list;

                if (loadType == MConstants.DATA_4_REFRESH) {
                    homeLiveVideoSingleAdapter.replaceAll(videoRooms);
                    currPage++;
                    rlPtrFrameLayout.refreshComplete();

                } else {
                    currPage++;
                    homeLiveVideoSingleAdapter.addAll(videoRooms);
                    if (videoRooms.size() < MConstants.PAGE_SIZE) {
                        //nodata
                        rlPtrFrameLayout.loadMoreComplete();
                    } else {
                        rlPtrFrameLayout.loadMoreComplete();
                    }
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
                if (loadType == MConstants.DATA_4_REFRESH) {
                    rlPtrFrameLayout.refreshComplete();
                } else if (loadType == MConstants.DATA_4_LOADMORE) {
                    rlPtrFrameLayout.changeLoadMoreState(LoadMoreFooterView.LOAD_MORE_STATE_ERROR);
                }
            }
        }, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        liveModel.unSubscribe();
        userModel.unSubscribe();
    }

}
