package com.shangshow.showlive.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.model.ImageInfo;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ScreenUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.ultra.header.GapV;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.common.widget.viewpager.CirclePageIndicator;
import com.shangshow.showlive.common.widget.viewpager.LoopViewPager;
import com.shangshow.showlive.controller.adapter.BannerViewPagerAdapter;
import com.shangshow.showlive.controller.adapter.BunchAdapter;
import com.shangshow.showlive.controller.adapter.HomeHotRecommendAdapter;
import com.shangshow.showlive.controller.adapter.HomeLiveVideoSingleAdapter;
import com.shangshow.showlive.controller.adapter.RecyclerScrollPauseLoadListener;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.hotvideoexcel.HotMannler;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.liveshow.video.PlayVideoActivity;
import com.shangshow.showlive.controller.member.BunchActivity;
import com.shangshow.showlive.controller.member.UserInfoActivity;
import com.shangshow.showlive.model.LiveModel;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.AdsInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.HomeHotFirstBody;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.divider.VerticalDividerItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页-主页-热门
 */
public class FragmentHomeHot extends BasePageFragment implements View.OnClickListener {

    private LiveModel liveModel;
    private UserModel userModel;
    private Context mContext;
    private RLPtrFrameLayout rlPtrFrameLayout;
    private HomeLiveVideoSingleAdapter homeLiveVideoSingleAdapter;
    //顶部试图
    private View topView;
    //banner广告
    private RelativeLayout bannerViewPagerLayout;
    private LoopViewPager bannerViewPager;
    private CirclePageIndicator bannerCirclePageIndicator;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    //推荐用户列表
    private RecyclerView recommendRecyclerView;
    private HomeHotRecommendAdapter homeHotRecommendAdapter;
    // 点播
    private RecyclerView rv_home_top_bunch;
    private BunchAdapter bunchAdapter;
    private List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();

    private long currVideoPage = 1;//直播列表分页
    private long currRecommendPage = 1;//推荐列表分页
    private ImageView hot_video;
    private TextView more_subscription_channel;
    private TextView texttitle1;
    private TextView texttitle2;
    private ImageView titleImage;
    private HomeHotFirstBody homeHotFirstBodys;

    public static FragmentHomeHot newInstance(String title) {
        FragmentHomeHot f = new FragmentHomeHot();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_home_hot;
    }

    @Override
    public void lazyLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlPtrFrameLayout.autoRefresh();
            }
        }, MConstants.DELAYED);
    }

    @Override
    protected void initWidget(View rootView) {
        liveModel = new LiveModel(mContext);
        userModel = new UserModel(mContext);
        rlPtrFrameLayout = (RLPtrFrameLayout) rootView.findViewById(R.id.home_hot_ptr_framentlayout);
        topView = getHomeHotTopView();
        //解决PtrFrameLayout嵌套ViewPager滑动冲突
        rlPtrFrameLayout.setViewPager(bannerViewPager);
        rlPtrFrameLayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadHomeHotVideoRoom(MConstants.DATA_4_LOADMORE);
            }
        });

        /**
         * 首页-热门直播列表
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
                        //    ToastUtils.show("" + videoRoom.logoUrl);
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

    //首页热门topView
    public View getHomeHotTopView() {
        View topView = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_home_hot_top, null);
        bannerViewPagerLayout = (RelativeLayout) topView.findViewById(R.id.tab_banner_viewpager_layout);
        bannerViewPager = (LoopViewPager) topView.findViewById(R.id.tab_banner_viewpager);
        bannerCirclePageIndicator = (CirclePageIndicator) topView.findViewById(R.id.tab_banner_viewpager_indicator);

        //设置banner的高度
        int width = ScreenUtil.getScreenWidth(mContext);
        bannerViewPagerLayout.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * MConstants.RATIO_POINT_BANNER)));

        recommendRecyclerView = (RecyclerView) topView.findViewById(R.id.home_hot_recommend_RecyclerView);
        rv_home_top_bunch = (RecyclerView) topView.findViewById(R.id.rv_home_top_bunch);
        // 新增UI界面
        hot_video = (ImageView) topView.findViewById(R.id.Hot_video);
        hot_video.setOnClickListener(this);
        // title
        texttitle1 = (TextView) topView.findViewById(R.id.text1);
        texttitle2 = (TextView) topView.findViewById(R.id.text2);
        //titieimage
        titleImage = (ImageView) topView.findViewById(R.id.ImageView1);
        more_subscription_channel = (TextView) topView.findViewById(R.id.More_subscription_channel);
        more_subscription_channel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        more_subscription_channel.setOnClickListener(this);

        return topView;
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {

    }

    /**
     * 设置广告位视图
     */
    public void setBannerView(List<AdsInfo> dataList) {
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

    /**
     * 推荐列表
     *
     * @param loadType
     * @param dataList
     */
    /**
     * 查看个人详情
     *
     * @param loadType
     * @param dataList
     */
    public void setRecommendView(long loadType, List<UserInfo> dataList) {
        if (dataList == null) {
            return;
        }
        if (homeHotRecommendAdapter == null) {
            //getBunch();
            homeHotRecommendAdapter = new HomeHotRecommendAdapter(mContext, dataList, R.layout.item_recycler_home_hot_recommend);
            bunchAdapter = new BunchAdapter(mContext, imageInfos, R.layout.image_info_layout);
            recommendRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
            rv_home_top_bunch.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
            //+竖直的分割线
            recommendRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(mContext)
                    .color(getResources().getColor(R.color.transparent))
                    .sizeResId(R.dimen.common_activity_padding_10)
                    .build());
            homeHotRecommendAdapter.addHeaderView(new GapV(mContext, 1f));
            homeHotRecommendAdapter.addFooterView(new GapV(mContext, 1f));
            recommendRecyclerView.setAdapter(homeHotRecommendAdapter);
            rv_home_top_bunch.setAdapter(bunchAdapter);
            recommendRecyclerView.addOnScrollListener(new RecyclerScrollPauseLoadListener());

            homeHotRecommendAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int viewType, int position) {

                    UserInfo userInfo = homeHotRecommendAdapter.getItem(position);
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.key_USERINFO, userInfo);
                    startActivity(intent);
                }
            });
            bunchAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int viewType, int position) {
                    Intent intent = new Intent(getActivity(), BunchActivity.class);
                    intent.putExtra("currTitle", imageInfos.get(position).videoType + "");
                    startActivity(intent);
                }
            });
        } else {
            //刷新时
            if (loadType == MConstants.DATA_4_REFRESH) {
                homeHotRecommendAdapter.replaceAll(dataList);
            } else {
                homeHotRecommendAdapter.addAll(dataList);
            }
        }
    }

    private void getBunch() {
//        imageInfos.add(new ImageInfo(R.drawable.technology, "科技科学"));
//        imageInfos.add(new ImageInfo(R.drawable.business_information, "商业前沿"));
//        imageInfos.add(new ImageInfo(R.drawable.fashion, "时尚娱乐"));
//        imageInfos.add(new ImageInfo(R.drawable.home, "生活家居"));
//        imageInfos.add(new ImageInfo(R.drawable.artist, "艺术文化"));
//        imageInfos.add(new ImageInfo(R.drawable.movie, "电影"));
//        imageInfos.add(new ImageInfo(R.drawable.design, "设计"));
//        imageInfos.add(new ImageInfo(R.drawable.tourism, "旅行"));
        // imageInfos.add(new ImageInfo(R.drawable.fitness, "健身"));
        userModel.getPGCVideoType(new Callback<List<ImageInfo>>() {
            @Override
            public void onSuccess(List<ImageInfo> datas) {
                imageInfos.addAll(datas);
                bunchAdapter.addAll(datas);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                System.out.println("this is result"+message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videos_type_list_layout:
                rlPtrFrameLayout.switchType(MConstants.RECYCLER_LINEAR);
                break;
            case R.id.videos_type_grid_layout:
                rlPtrFrameLayout.switchType(MConstants.RECYCLER_GRID);
                break;
            case R.id.Hot_video: {
                Intent intent = new Intent(getActivity(), BunchActivity.class);
                intent.putExtra("currTitle", homeHotFirstBodys.pgcVideoTypesId + "");
                startActivity(intent);
            }
            break;
            case R.id.More_subscription_channel: {
                startActivity(new Intent(getActivity(), HotMannler.class));
            }
            break;
        }
    }

    private void loadData() {

        int a = 5;
        int b= 6;
        a=b;

        lpdeHomeHot();
        loadHomeHotAds();
        loadHomeRecommend(MConstants.DATA_4_REFRESH);
        loadHomeHotVideoRoom(MConstants.DATA_4_REFRESH);
    }

    private void lpdeHomeHot() {
        userModel.getHomeHot(new Callback<HomeHotFirstBody>() {
            @Override
            public void onSuccess(HomeHotFirstBody homeHotFirstBody) {
                homeHotFirstBodys = homeHotFirstBody;
                setInitView(homeHotFirstBody);

            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    private void setInitView(HomeHotFirstBody homeHotFirstBody) {

        texttitle1.setText(homeHotFirstBody.title);
        texttitle2.setText(homeHotFirstBody.content);
        Picasso.with(getActivity()).load(homeHotFirstBody.logoUrl).placeholder(R.mipmap.youtube).into(titleImage);
        Picasso.with(getActivity()).load(homeHotFirstBody.imgUrl).placeholder(R.mipmap.youtube).into(hot_video);
    }


    /**
     * 加载首页广告
     */
    private void loadHomeHotAds() {
        userModel.adsInfoList(MConstants.ADS_NO_HOMEHOT, new Callback<List<AdsInfo>>() {
            @Override
            public void onSuccess(List<AdsInfo> adsInfos) {
                setBannerView(adsInfos);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        }, false);
    }

    /**
     * 加载推荐列表
     *
     * @param loadType
     */
    private void loadHomeRecommend(final long loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currRecommendPage);
        userModel.getRecomUserList(pageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                currRecommendPage = userInfoPager.pageNum;
                List<UserInfo> userInfos = userInfoPager.list;
                //  查看个人详情
                setRecommendView(loadType, userInfos);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 加载首页直播间
     */
    private void loadHomeHotVideoRoom(final long loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currVideoPage);
        liveModel.getVideoRoomList(MConstants.USER_TYPE_NULL_0, pageBody, new Callback<Pager<VideoRoom>>() {
            @Override
            public void onSuccess(Pager<VideoRoom> pager) {
                currVideoPage = pager.pageNum;
                List<VideoRoom> videoRooms = pager.list;
                if (loadType == MConstants.DATA_4_REFRESH) {
                    homeLiveVideoSingleAdapter.replaceAll(videoRooms);
                    currVideoPage++;
                    rlPtrFrameLayout.refreshComplete();
                } else {
                    currVideoPage++;
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
