package com.shangshow.showlive.controller.member;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.PlayVideoCommint;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.controller.adapter.MemberAdapter;
import com.shangshow.showlive.model.LiveModel;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.body.YoutubeListBody;
import com.shangshow.showlive.network.service.models.body.YoutubeListContontBody;
import com.shangshow.showlive.network.service.models.responseBody.PGCVideoInfo;
import com.shangshow.showlive.widget.SpaceItemDecoration;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class BunchActivity extends BaseActivity {

    private String currTitle;
    private LiveModel liveModel;
    private RLPtrFrameLayout bunch_ptr_framelayout;
    private MemberAdapter memberAdapter;
    private List<PGCVideoInfo> videoRooms = new ArrayList<>();
    private long currPage = 1;
    private UserModel userModel;
    private List<YoutubeListContontBody> data = new ArrayList<>();

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_bunch;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        currTitle = getIntent().getStringExtra("currTitle");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        userModel = new UserModel(this);
        liveModel = new LiveModel(this);
        titleBarView.initCenterTitle(currTitle);
        bunch_ptr_framelayout = (RLPtrFrameLayout) findViewById(R.id.bunch_ptr_framelayout);
        bunch_ptr_framelayout.getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .sizeResId(R.dimen.common_activity_padding_10)
                .build());
        memberAdapter = new MemberAdapter(this, data, R.layout.item_commit_video);
        bunch_ptr_framelayout.setAdapter(memberAdapter, true);
        // 设置显示多列
        RecyclerView recyclerView = bunch_ptr_framelayout.getRecyclerView();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp05)));
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void bindEven() {
        bunch_ptr_framelayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                data.clear();
                memberAdapter.clear();
                loadFavouriteVideoRoom(MConstants.DATA_4_REFRESH);
                bunch_ptr_framelayout.loadMoreComplete();
            }

            @Override
            public void onLoadMore() {
                data.clear();
                memberAdapter.clear();
                loadFavouriteVideoRoom(MConstants.DATA_4_LOADMORE);
                bunch_ptr_framelayout.loadMoreComplete();
            }

        });
        memberAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {

                if (!"".equals(memberAdapter.getItem(position).OSSUrl)) {
                    Intent intent = new Intent(BunchActivity.this, PlayVideoCommint.class);
                    intent.putExtra("videourl", memberAdapter.getItem(position).OSSUrl);
                    System.out.println("------------拿到得链接     " + memberAdapter.getItem(position).OSSUrl);
                    startActivity(intent);
                } else {
                    ToastUtils.show("视频源失效");
                }
//                if (!CommonUtil.isClickSoFast(MConstants.DELAYED)) {
//                    if (CacheCenter.getInstance().isLogin()) {
//                        VideoRoom videoRoom = memberAdapter.getItem(position);
//                        LiveAudienceActivity.start(BunchActivity.this, videoRoom);
//                    } else {
//                        startActivity(new Intent(BunchActivity.this, LoginActivity.class));
//                    }
//                }
            }
        });
    }

    @Override
    protected void setView() {
        bunch_ptr_framelayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                bunch_ptr_framelayout.autoRefresh();
            }
        }, MConstants.DELAYED);
    }

    /**
     * 加载星尚直播间
     */
    private void loadFavouriteVideoRoom(final long loadType) {
       /* PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currPage);
        liveModel.getVideoRoomList(MConstants.USER_TYPE_FAVOURITE, pageBody, new Callback<Pager<VideoRoom>>() {
            @Override
            public void onSuccess(Pager<VideoRoom> pager) {
                currPage = pager.pageNum;
                List<VideoRoom> videoRooms = pager.list;

                if (loadType == MConstants.DATA_4_REFRESH) {
                    memberAdapter.replaceAll(videoRooms);
                    currPage++;
                    bunch_ptr_framelayout.refreshComplete();

                } else {
                    memberAdapter.addAll(videoRooms);
                    if (videoRooms.size() < MConstants.PAGE_SIZE) {
                        //nodata
                        bunch_ptr_framelayout.loadMoreComplete();
                    } else {
                        currPage++;
                        bunch_ptr_framelayout.loadMoreComplete();
                    }
                }
            }*/

  /*          @Override
            public void onFailure(int resultCode, String message) {
                if (loadType == MConstants.DATA_4_REFRESH) {
                    bunch_ptr_framelayout.refreshComplete();
                } else if (loadType == MConstants.DATA_4_LOADMORE) {
                    bunch_ptr_framelayout.changeLoadMoreState(LoadMoreFooterView.LOAD_MORE_STATE_ERROR);
                }
            }
        }, false);*/

/*        UserModel userModel = new UserModel(this);
        final PGCVideoInfo pgcVideoInfo = new PGCVideoInfo();
        pgcVideoInfo.videoType = "多元";
        userModel.gettuijianList(pgcVideoInfo, new Callback<Pager<PGCVideoInfo>>() {
            @Override
            public void onSuccess(Pager<PGCVideoInfo> pgcVideoInfoPager) {
              //  ArrayList<PGCVideoInfo> list = pgcVideoInfoPager.list;
                currPage = pgcVideoInfoPager.pageNum;
                List<PGCVideoInfo> videoRooms = pgcVideoInfoPager.list;

                if (loadType == MConstants.DATA_4_REFRESH) {
                    memberAdapter.replaceAll(videoRooms);
                    currPage++;
                    bunch_ptr_framelayout.refreshComplete();

                } else {
                    memberAdapter.addAll(videoRooms);
                    if (videoRooms.size() < MConstants.PAGE_SIZE) {
                        //nodata
                        bunch_ptr_framelayout.loadMoreComplete();
                    } else {
                        currPage++;
                        bunch_ptr_framelayout.loadMoreComplete();
                    }
                }
            }
            @Override
            public void onFailure(int resultCode, String message) {

            }
        });*/

        userModel.getYoutubeList(currTitle, new Callback<YoutubeListBody>() {
            @Override
            public void onSuccess(YoutubeListBody youtubeListBody) {
                List<YoutubeListContontBody> list = youtubeListBody.list;
                data.addAll(list);
                memberAdapter.addAll(list);
                memberAdapter.notifyDataSetHasChanged();
                bunch_ptr_framelayout.loadMoreComplete();
                bunch_ptr_framelayout.refreshComplete();
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        });

    }

}
