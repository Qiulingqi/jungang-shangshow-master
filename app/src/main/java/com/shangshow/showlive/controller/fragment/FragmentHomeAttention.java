package com.shangshow.showlive.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.controller.adapter.HomeLiveVideoSingleAdapter;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.liveshow.video.PlayVideoActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页-主页-关注
 */
public class FragmentHomeAttention extends BasePageFragment {
    private UserModel userModel;
    private Context mContext;
    private RLPtrFrameLayout rlPtrFrameLayout;
    private HomeLiveVideoSingleAdapter homeLiveVideoSingleAdapter;

    private long currPage = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_home_attention;
    }

    public static FragmentHomeAttention newInstance(String title) {
        FragmentHomeAttention f = new FragmentHomeAttention();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

    boolean isLazy = false;
    @Override
    public void lazyLoad() {
        isLazy = true;
        rlPtrFrameLayout.autoRefresh();
    }

    @Override
    protected void initWidget(View rootView) {
        userModel = new UserModel(mContext);
        rlPtrFrameLayout = (RLPtrFrameLayout) rootView.findViewById(R.id.home_attention_ptr_framentlayout);
        rlPtrFrameLayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                if(CacheCenter.getInstance().isLogin()) {
                    loadHomeHotVideoRoom(MConstants.DATA_4_REFRESH);
                }else{
                    rlPtrFrameLayout.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
                if(CacheCenter.getInstance().isLogin()) {
                    loadHomeHotVideoRoom(MConstants.DATA_4_LOADMORE);
                }else{
                    rlPtrFrameLayout.refreshComplete();
                }
            }
        });

        /**
         * 首页-关注直播列表
         */
        homeLiveVideoSingleAdapter = new HomeLiveVideoSingleAdapter(mContext, new ArrayList<VideoRoom>(), R.layout.item_recycler_common_livevideo_list);
        //+分割线
        rlPtrFrameLayout.getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.transparent))
                .sizeResId(R.dimen.common_activity_padding_10)
                .build());
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

    @Override
    protected void bindEven() {
    }

    @Override
    protected void setView() {

    }

    /**
     * 加载首页直播间
     */
    private void loadHomeHotVideoRoom(final long loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currPage);
        userModel.getUserFriedsVideoList(pageBody, new Callback<List<VideoRoom>>() {
            @Override
            public void onSuccess(List<VideoRoom> videoRooms) {
                if (loadType == MConstants.DATA_4_REFRESH) {
                    homeLiveVideoSingleAdapter.clear();
                    homeLiveVideoSingleAdapter.replaceAll(videoRooms);
                    currPage++;
                    rlPtrFrameLayout.refreshComplete();
                } else {
                    homeLiveVideoSingleAdapter.addAll(videoRooms);
                    if (videoRooms.size() < MConstants.PAGE_SIZE) {
                        //nodata
                        rlPtrFrameLayout.loadMoreComplete();
                    } else {
                        currPage++;
                        rlPtrFrameLayout.loadMoreComplete();
                    }
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
                homeLiveVideoSingleAdapter.clear();
                if (loadType == MConstants.DATA_4_REFRESH) {
                    rlPtrFrameLayout.refreshComplete();
                } else if (loadType == MConstants.DATA_4_LOADMORE) {
                    rlPtrFrameLayout.changeLoadMoreState(LoadMoreFooterView.LOAD_MORE_STATE_ERROR);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLazy) {
            if (!CacheCenter.getInstance().isLogin()) {
                homeLiveVideoSingleAdapter.clear();
            } else {
                rlPtrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlPtrFrameLayout.autoRefresh();
                    }
                }, MConstants.DELAYED);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }

}
