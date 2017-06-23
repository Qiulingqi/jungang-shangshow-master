package com.shangshow.showlive.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ScreenUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.TitleBarView;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.common.widget.viewpager.CirclePageIndicator;
import com.shangshow.showlive.common.widget.viewpager.LoopViewPager;
import com.shangshow.showlive.controller.PalyLocationVideoActivity;
import com.shangshow.showlive.controller.adapter.BannerViewPagerAdapter;
import com.shangshow.showlive.controller.adapter.HomeLiveVideoSingleAdapter;
import com.shangshow.showlive.controller.common.CommonBrowserActivity;
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
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 星品
 */
public class FragmentMerchant extends BaseFragment {
    private LiveModel liveModel;
    private UserModel userModel;
    private Context mContext;
    private TitleBarView titleBarView;
    private RLPtrFrameLayout rlPtrFrameLayout;
    private HomeLiveVideoSingleAdapter homeLiveVideoSingleAdapter;
    private String logourl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495465975490&di=be18e52a00d5fcd026bdb9d291b032d9&imgtype=0&src=http%3A%2F%2Fwww.im4s.cn%2Fupload%2F120908%2F22445_967540.jpg";
    //顶部试图
    private View topView;
    //banner广告
    private RelativeLayout bannerViewPagerLayout;
    private LoopViewPager bannerViewPager;
    private CirclePageIndicator bannerCirclePageIndicator;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private LinearLayout gridAdsLayout;
    private ArrayList<ImageView> gridAdsImages;

    private long currPage = 1;
    private ImageView xingshang_first_play;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_merchant;
    }

    public static FragmentMerchant newInstance(String title) {
        FragmentMerchant f = new FragmentMerchant();
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
        titleBarView = (TitleBarView) rootView.findViewById(R.id.merchant_title_layout);
        titleBarView.initCenterTitle(title);
//        titleBarView.initRight("列表", 0, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MemberListActivity.class);
//                intent.putExtra(MemberListActivity.key_USER_TYPE, MConstants.USER_TYPE_MERCHANT);
//                startActivity(intent);
//            }
//        });

        rlPtrFrameLayout = (RLPtrFrameLayout) rootView.findViewById(R.id.merchant_ptr_framentlayout);
        topView = getMerchantTopView();
        //解决PtrFrameLayout嵌套ViewPager滑动冲突
        rlPtrFrameLayout.setViewPager(bannerViewPager);
        rlPtrFrameLayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadMerchantVideoRoom(MConstants.DATA_4_LOADMORE);
            }
        });

        /**
         * 星尚直播列表
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
                        if("LIV".equals(videoRoom.videoStatus)){
                            LiveAudienceActivity.start(mContext, videoRoom);
                        }
                        if("OFF".equals(videoRoom.videoStatus)){
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

    //星品topView
    public View getMerchantTopView() {
        View topView = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_merchant_top, null);
        bannerViewPagerLayout = (RelativeLayout) topView.findViewById(R.id.tab_banner_viewpager_layout);
        bannerViewPager = (LoopViewPager) topView.findViewById(R.id.tab_banner_viewpager);
        bannerCirclePageIndicator = (CirclePageIndicator) topView.findViewById(R.id.tab_banner_viewpager_indicator);
        //设置banner的高度
        int width = ScreenUtil.getScreenWidth(mContext);
        bannerViewPagerLayout.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * MConstants.RATIO_POINT_BANNER)));
        gridAdsLayout = (LinearLayout) topView.findViewById(R.id.merchant_grid_ads_layout);
        //9宫格-代码布局【被逼的】
        gridAdsImages = ShangXiuUtil.merChantGridAdsView(mContext, gridAdsLayout);


        xingshang_first_play = (ImageView) topView.findViewById(R.id.xingshang_first_play);
        Picasso.with(getActivity()).load(logourl).placeholder(R.mipmap.youtube).into(xingshang_first_play);
        xingshang_first_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), PalyLocationVideoActivity.class));
            }
        });
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
            bannerViewPagerAdapter = new BannerViewPagerAdapter(mContext, dataList);
            bannerViewPager.setAdapter(bannerViewPagerAdapter);
            bannerViewPager.setBoundaryCaching(true);
            bannerCirclePageIndicator.setViewPager(bannerViewPager);
        } else {
            bannerViewPagerAdapter.changeData(dataList);
        }
    }

    /**
     * 设置9宫格格广告
     */
    List<AdsInfo> datalists;
    int i;
    public void setGridAdsView(List<AdsInfo> dataList) {
        if (dataList == null || gridAdsImages == null) {
            return;
        }
        datalists=dataList;
        for (i = 0; i < gridAdsImages.size(); i++) {
            //避免越界
            if (i <= dataList.size() - 1) {
                ImageLoaderKit.getInstance().displayImage(dataList.get(i).resource, gridAdsImages.get(i));
                gridAdsImages.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1 == datalists.get(i-1).actionType) {
                            Intent intent = new Intent(mContext, CommonBrowserActivity.class);
                            intent.putExtra(CommonBrowserActivity.TITLE_KEY, datalists.get(i-1).name);
                            intent.putExtra(CommonBrowserActivity.URL_KEY, datalists.get(i-1).action);
                            mContext.startActivity(intent);
                        }
                        if (2 == datalists.get(i-1).actionType) {

                            if (!"".equals(datalists.get(i-1).action) && datalists.get(i-1).action.length() > 16) {
                                //  截取字符串判断是哪个类型
                                String substring = datalists.get(i-1).action.substring(0, 16);
                                String substring1 = substring.substring(12, 16);
                                if ("user".equals(substring1)) {

                                    //  截取参数  用作跳转下个界面使用
                                    // String substring2 = adsInfo.action.substring(24, 31);
                                    //  String substring3 = adsInfo.action.substring(39, 42);
                                    ToastUtils.show("查看用户信息");
                                    /**
                                     * 信息不全   先注释掉
                                     */
                                /*Intent intent = new Intent(context, UserInfoActivity.class);
                                intent.putExtra(UserInfoActivity.key_USERINFO, "");
                                context.startActivity(intent);*/
                                }
                                if ("vide".equals(substring1)) {
                                    ToastUtils.show("查看用户视频");
                                }
                                if ("live".equals(substring1)) {
                                    if (datalists.get(i-1).action.length() >= 42){
                                        String substring2 = datalists.get(i-1).action.substring(24, 31);
                                        String substring3 = datalists.get(i-1).action.substring(39, 42);
                                        // 说明当前有主播正在直播
                                        if (!CommonUtil.isClickSoFast(MConstants.DELAYED)) {
                                            if (CacheCenter.getInstance().isLogin()) {
                                                Intent intent = new Intent(mContext, LiveAudienceActivity.class);
                                                VideoRoom videoRoom = new VideoRoom();
                                                videoRoom.userId = Integer.parseInt(substring2);
                                                videoRoom.roomId = Integer.parseInt(substring3);
                                                intent.putExtra("videoRoom", videoRoom);
                                                mContext.startActivity(intent);
                                            } else {
                                                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                            }
                                        }
                                        ToastUtils.show("观看当前正在进行的直播");

                                    }
                                }
                            } else {
                                ToastUtils.show("网址不合法，请重试");
                            }
                       /* Intent intent = new Intent(context, LiveAnchorActivity.class);
                        intent.putExtra(CommonBrowserActivity.TITLE_KEY, adsInfo.name);
                        intent.putExtra(CommonBrowserActivity.URL_KEY, adsInfo.action);
                        context.startActivity(intent);*/
                        }
                    }
                });
            } else {
                gridAdsImages.get(i).setImageResource(R.mipmap.icon_placeholder);
            }
        }
    }


    private void loadData() {
        loadMerchantAds();
        loadMerchantGridAds();
        loadMerchantVideoRoom(MConstants.DATA_4_REFRESH);
    }

    /**
     * 加载星品广告,轮播广告
     */
    private void loadMerchantAds() {
        userModel.adsInfoList(MConstants.ADS_NO_MERCHANT, new Callback<List<AdsInfo>>() {
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
     * 加载星品广告,9宫格广告
     */
    private void loadMerchantGridAds() {
        userModel.adsInfoList(MConstants.ADS_NO_MERCHANT_GRID, new Callback<List<AdsInfo>>() {
            @Override
            public void onSuccess(List<AdsInfo> adsInfos) {
                setGridAdsView(adsInfos);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        }, false);
    }

    /**
     * 加载星品直播间
     */
    private void loadMerchantVideoRoom(final long loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currPage);
        liveModel.getVideoRoomList(MConstants.USER_TYPE_MERCHANT, pageBody, new Callback<Pager<VideoRoom>>() {
            @Override
            public void onSuccess(Pager<VideoRoom> pager) {
                currPage = pager.pageNum;
                /*
                * List<VideoRoom> videoRooms = pager.list;

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
                * */
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
