package com.shangshow.showlive.controller.personal;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.custom.LiveVideoTypeLayout;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.controller.adapter.MyLiveVideoSingleAdapter;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.liveshow.video.PlayVideoActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.widget.CommonDialog;
import com.shangshow.showlive.widget.SpaceItemDecoration;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 我的视频列表
 *
 * @author
 */

public class MyVideosListActivity extends BaseActivity implements View.OnClickListener {
    public static String KEY_USERID = "userId";
    private long userId;
    private UserModel userModel;
    private RLPtrFrameLayout rlPtrFrameLayout;
    private LiveVideoTypeLayout liveVideoTypeLayout;
    private MyLiveVideoSingleAdapter myLiveVideoSingleAdapter;
    private View ll_myvideos_operater;
    private View tv_myvideos_delete;
    private View tv_myvideos_close;

    private long currPage = 1;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_myvideos;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        userId = getIntent().getLongExtra(KEY_USERID, 0);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(getString(R.string.video));
        rlPtrFrameLayout = (RLPtrFrameLayout) findViewById(R.id.live_video_RLPtrFrameLayout);
        liveVideoTypeLayout = (LiveVideoTypeLayout) findViewById(R.id.live_video_type_layout);
        rlPtrFrameLayout.setRefreshEnabled(false);
        ll_myvideos_operater = findViewById(R.id.ll_myvideos_operater);
        tv_myvideos_delete = findViewById(R.id.tv_myvideos_delete);
        tv_myvideos_close = findViewById(R.id.tv_myvideos_close);

        myLiveVideoSingleAdapter = new MyLiveVideoSingleAdapter(this, new ArrayList<VideoRoom>(), R.layout.item_recycler_common_livevideo_list, CacheCenter.getInstance().getCurrUser());
        //+分割线
        rlPtrFrameLayout.getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .sizeResId(R.dimen.common_activity_padding_10)
                .build());
        rlPtrFrameLayout.setAdapter(myLiveVideoSingleAdapter, true);

        liveVideoTypeLayout.changeType(MConstants.RECYCLER_LINEAR);

    }

    @Override
    protected void bindEven() {
        tv_myvideos_delete.setOnClickListener(this);
        tv_myvideos_close.setOnClickListener(this);
        //  切换布局
        liveVideoTypeLayout.setOnSwitchTypeListener(new LiveVideoTypeLayout.OnSwitchTypeListener() {

            SpaceItemDecoration spaceItemDecoration;

            @Override
            public void onSwitchList() {
                rlPtrFrameLayout.switchType(MConstants.RECYCLER_LINEAR);
            }

            @Override
            public void onSwitchGrid() {
                final RecyclerView recyclerView = rlPtrFrameLayout.getRecyclerView();
                final GridLayoutManager gridLayoutManager = new GridLayoutManager(MyVideosListActivity.this, 2);
                if(spaceItemDecoration == null) {
                    spaceItemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp01));
                    recyclerView.addItemDecoration(spaceItemDecoration);
                }
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        });
        // 右侧的点击事件
        titleBarView.initRight("选择", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLiveVideoSingleAdapter.isChoose = !myLiveVideoSingleAdapter.isChoose;
                ll_myvideos_operater.setVisibility(myLiveVideoSingleAdapter.isChoose?View.VISIBLE:View.GONE);
                titleBarView.rightText.setText(myLiveVideoSingleAdapter.isChoose?"完成":"编辑");
                myLiveVideoSingleAdapter.notifyDataSetChanged();
            }
        });
        // 刷新事件
        rlPtrFrameLayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                getMyVideos(MConstants.DATA_4_REFRESH);
            }

            @Override
            public void onLoadMore() {
                getMyVideos(MConstants.DATA_4_LOADMORE);
            }
        });
        //  适配器点击事件
        myLiveVideoSingleAdapter.setOnItemClickListener(new MyLiveVideoSingleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(!myLiveVideoSingleAdapter.isChoose) {
                    if (!CommonUtil.isClickSoFast(MConstants.DELAYED)) {
                        if (CacheCenter.getInstance().isLogin()) {
                            VideoRoom videoRoom = myLiveVideoSingleAdapter.getItem(position);
                            if(TextUtils.isEmpty(videoRoom.videoUrl)){
                                showToast("进入房间失败");
                                return;
                            }
                            if("LIV".equals(videoRoom.status)){
                                LiveAudienceActivity.start(context, videoRoom);
                            }
                            if("EBL".equals(videoRoom.status)){
                                Intent intent = new Intent(context, PlayVideoActivity.class);
                                intent.putExtra("uri", videoRoom.videoUrl + "");
                                startActivity(intent);
                            }
                        } else {
                            startActivity(new Intent(MyVideosListActivity.this, LoginActivity.class));
                        }
                    }
                }else{
                    myLiveVideoSingleAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void setView() {
        getMyVideos(MConstants.DATA_4_REFRESH);
    }

    //  编辑删除视频
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_myvideos_delete:{
                final List<Long> list = new ArrayList<Long>();
                for(Map.Entry<Integer, Boolean> entry : myLiveVideoSingleAdapter.getChooseMap().entrySet()){
                    if(entry.getValue()){
                        list.add(myLiveVideoSingleAdapter.getItem(entry.getKey()).videoInfoId);
                    }
                }
                if(list.size() == 0){
                    showToast("您没有选择要删除的视频");
                    return;
                }
                commonDialog.setMessage("确认删除" + list.size() + "项视频");
                commonDialog.setOnViewClickListener(new CommonDialog.OnViewClickListener() {

                    @Override
                    public void ok(View view) {
                        userModel.removeVideoListByUser(list, new Callback<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                myLiveVideoSingleAdapter.getChooseMap().clear();
                                rlPtrFrameLayout.autoRefresh();
                            }

                            @Override
                            public void onFailure(int resultCode, String message) {

                            }
                        });
                    }

                    @Override
                    public void close(View view) {

                    }
                });
                commonDialog.showDig();
            }
            break;
            case R.id.tv_myvideos_close:{
                myLiveVideoSingleAdapter.getChooseMap().clear();
                myLiveVideoSingleAdapter.isChoose = false;
                titleBarView.rightText.setText("编辑");
                ll_myvideos_operater.setVisibility(View.GONE);
                myLiveVideoSingleAdapter.notifyDataSetChanged();
            }
            break;
        }
    }

    /**
     * 获取我的视频列表
     */
    public void getMyVideos(final long loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currPage);
        userModel.getVideoListByUser(userId, pageBody, new Callback<Pager<VideoRoom>>() {
            @Override
            public void onSuccess(Pager<VideoRoom> pager) {
                currPage = pager.pageNum;
                List<VideoRoom> videoRooms = pager.list;

                if (loadType == MConstants.DATA_4_REFRESH) {
                    myLiveVideoSingleAdapter.replaceAll(videoRooms);
                    currPage++;
                    rlPtrFrameLayout.refreshComplete();

                } else {
                    currPage++;
                    myLiveVideoSingleAdapter.addAll(videoRooms);
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
        }, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }

}
