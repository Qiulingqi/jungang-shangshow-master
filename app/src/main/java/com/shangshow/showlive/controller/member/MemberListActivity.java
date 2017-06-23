package com.shangshow.showlive.controller.member;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.controller.adapter.HomeLiveVideoSingleAdapter;
import com.shangshow.showlive.controller.adapter.MemberAdapter;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.liveshow.video.PlayVideoActivity;
import com.shangshow.showlive.model.LiveModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.widget.SpaceItemDecoration;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员列表页面【星尚，星咖，星品】
 */
/*下拉加载的时候  会不停的加载最后一条  在ListView里边
 */
public class MemberListActivity extends BaseActivity implements
        OnClickListener {
    private LiveModel liveModel;
    public static String key_USER_TYPE = "userType";
    private String memberType;
    private String currTitle;
    private RLPtrFrameLayout my_message_ptr_framelayout;
    private MemberAdapter memberAdapter;
    private List<VideoRoom> videoRooms = new ArrayList<>();
    private long currPage = 1;
    private boolean isLastPage;
    private HomeLiveVideoSingleAdapter homeLiveVideoSingleAdapter;

    @Override
    protected int getActivityLayout() {
        return R.layout.member_list_layout;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        if (getIntent().hasExtra(key_USER_TYPE)) {
            memberType = getIntent().getStringExtra(key_USER_TYPE);
            switch (memberType) {
                case MConstants.USER_TYPE_FAVOURITE:
                    currTitle = "星尚直播";
                    break;
                case MConstants.USER_TYPE_SUPERSTAR:
                    currTitle = "星咖直播";
                    break;
                case MConstants.USER_TYPE_MERCHANT:
                    currTitle = "星品直播";
                    break;
                default:
                    currTitle = "";
                    break;
            }
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        liveModel = new LiveModel(this);
        titleBarView.initCenterTitle(currTitle);
        my_message_ptr_framelayout = (RLPtrFrameLayout) findViewById(R.id.my_message_ptr_framelayout);
        my_message_ptr_framelayout.getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .sizeResId(R.dimen.common_activity_padding_10)
                .build());
        //memberAdapter = new MemberAdapter(this, videoRooms, R.layout.memberlist_one_activity_layout);
        homeLiveVideoSingleAdapter = new HomeLiveVideoSingleAdapter(this, new ArrayList<VideoRoom>(), R.layout.item_recycler_common_livevideo_list);
        my_message_ptr_framelayout.setAdapter(homeLiveVideoSingleAdapter, true);
        // 设置显示多列
        RecyclerView recyclerView = my_message_ptr_framelayout.getRecyclerView();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp01)));
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void bindEven() {
        my_message_ptr_framelayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadFavouriteVideoRoom(MConstants.DATA_4_REFRESH);
            }

            @Override
            public void onLoadMore() {
                loadFavouriteVideoRoom(MConstants.DATA_4_LOADMORE);
            }
        });
        homeLiveVideoSingleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                if (!CommonUtil.isClickSoFast(MConstants.DELAYED)) {
                    if (CacheCenter.getInstance().isLogin()) {
                        if (videoRooms.size() > 0) {
                            VideoRoom videoRoom = videoRooms.get(position);
                            if (TextUtils.isEmpty(videoRoom.videoUrl)) {
                                showToast("进入房间失败");
                                return;
                            }
                            if ("LIVE".equals(videoRoom.videoStatus)) {
                                LiveAudienceActivity.start(context, videoRoom);
                            }
                            if ("OFF".equals(videoRoom.videoStatus)) {
                                Intent intent = new Intent(context, PlayVideoActivity.class);
                                intent.putExtra("uri", videoRoom.videoUrl + "");
                                startActivity(intent);
                            }

                        } else {
                            ToastUtils.show("主播暂未主播信息，敬请期待");
                        }
                    } else {
                        startActivity(new Intent(MemberListActivity.this, LoginActivity.class));
                    }
                }
            }
        });
    }

    @Override
    protected void setView() {
        my_message_ptr_framelayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                my_message_ptr_framelayout.autoRefresh();
            }
        }, MConstants.DELAYED);
    }

    /**
     * 加载星尚直播间
     */
    private void loadFavouriteVideoRoom(final long loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currPage);
        liveModel.getVideoRoomList(MConstants.USER_TYPE_FAVOURITE, pageBody, new Callback<Pager<VideoRoom>>() {
            @Override
            public void onSuccess(Pager<VideoRoom> pager) {
                currPage = pager.pageNum;
                isLastPage = pager.isLastPage;
                List<VideoRoom> videoRooms = pager.list;

                if (loadType == MConstants.DATA_4_REFRESH) {
                    homeLiveVideoSingleAdapter.replaceAll(videoRooms);
                    currPage++;
                    my_message_ptr_framelayout.refreshComplete();

                } else {
                    if (true == isLastPage) {
                        my_message_ptr_framelayout.loadMoreComplete();
                        ToastUtils.show("已全部加载");
                    } else {
                        homeLiveVideoSingleAdapter.addAll(videoRooms);
                        if (videoRooms.size() < MConstants.PAGE_SIZE) {
                            //nodata
                            my_message_ptr_framelayout.loadMoreComplete();
                        } else {
                            currPage++;
                            my_message_ptr_framelayout.loadMoreComplete();
                        }
                    }

                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
                if (loadType == MConstants.DATA_4_REFRESH) {
                    my_message_ptr_framelayout.refreshComplete();
                } else if (loadType == MConstants.DATA_4_LOADMORE) {
                    my_message_ptr_framelayout.changeLoadMoreState(LoadMoreFooterView.LOAD_MORE_STATE_ERROR);
                }
            }
        }, false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

    }
}
