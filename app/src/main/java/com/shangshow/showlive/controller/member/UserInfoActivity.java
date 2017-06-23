package com.shangshow.showlive.controller.member;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.LogicView.UserInfoView;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.custom.LiveVideoTypeLayout;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.common.widget.ultra.header.GapV;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.controller.adapter.HomeHotRecommendAdapter;
import com.shangshow.showlive.controller.adapter.MyLiveVideoSingleAdapter;
import com.shangshow.showlive.controller.adapter.RecyclerScrollPauseLoadListener;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.liveshow.video.PlayVideoActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.http.Response;
import com.shangshow.showlive.network.service.models.CooperationApply;
import com.shangshow.showlive.network.service.models.CooperationApplyDetail;
import com.shangshow.showlive.network.service.models.CooperationApplyInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.responseBody.EditFriendBody;
import com.shangshow.showlive.widget.CommonDialog;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.divider.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情页【星尚，星咖，星品】
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    public static String key_USERINFO = "userinfo";
    private UserModel userModel;
    private String currTitle;
    private UserInfo userInfo;
    private UserInfo user;

    private RLPtrFrameLayout rlPtrFrameLayout;
//    private HomeLiveVideoSingleAdapter homeLiveVideoSingleAdapter;
    private MyLiveVideoSingleAdapter myLiveVideoSingleAdapter;
    //顶部试图
    private View topView;
    private UserInfoView userInfoView;
    private RelativeLayout cooperateRecyclerViewLayout;
    private TextView cooperateRelation;//合作关系
    private RecyclerView cooperateRecyclerView;//合作列表
    private HomeHotRecommendAdapter homeHotRecommendAdapter;
    private LiveVideoTypeLayout liveVideoTypeLayout;

    private View userinfo_operation_layout;
    private TextView attention;//关注
    private TextView applyCooperate;//申请合作

    private long currVideoPage = 1;//用户video列表分页
    private long currCooperatePage = 1;//合作列表

    String friendStatus = "";
    boolean isAttention;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        if (getIntent().hasExtra(key_USERINFO)) {
            //   类装换异常  父类不能强转为异类
           userInfo = (UserInfo) getIntent().getSerializableExtra(key_USERINFO);
            switch (userInfo.userType) {
                case MConstants.USER_TYPE_FAVOURITE:
                    currTitle = "星尚信息";
                    break;
                case MConstants.USER_TYPE_SUPERSTAR:
                    currTitle = "星咖信息";
                    break;
                case MConstants.USER_TYPE_MERCHANT:
                    currTitle = "星品信息";
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
        user = CacheCenter.getInstance().getCurrUser();
        titleBarView.initCenterTitle(R.string.message);
        titleBarView.initCenterTitle(currTitle);
        rlPtrFrameLayout = (RLPtrFrameLayout) findViewById(R.id.userinfo_RLPtrFrameLayout);
        topView = getUserInfoTopView();
        rlPtrFrameLayout.setRefreshEnabled(false);
        rlPtrFrameLayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadUserInfoVideoList(MConstants.DATA_4_LOADMORE);
            }
        });

//        homeLiveVideoSingleAdapter = new HomeLiveVideoSingleAdapter(this, new ArrayList<VideoRoom>(), R.layout.item_recycler_common_livevideo_list);
        myLiveVideoSingleAdapter = new MyLiveVideoSingleAdapter(this, new ArrayList<VideoRoom>(), R.layout.item_recycler_common_livevideo_list, userInfo);
        //+分割线
        rlPtrFrameLayout.getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .sizeResId(R.dimen.common_activity_padding_10)
                .build());
//        homeLiveVideoSingleAdapter.addHeaderView(topView);
        myLiveVideoSingleAdapter.addHeaderView(topView);
//        rlPtrFrameLayout.setAdapter(homeLiveVideoSingleAdapter, true);
        rlPtrFrameLayout.setAdapter(myLiveVideoSingleAdapter, true);
        myLiveVideoSingleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                if (!CommonUtil.isClickSoFast(MConstants.DELAYED)) {
                    if (CacheCenter.getInstance().isLogin()) {
                        VideoRoom videoRoom = myLiveVideoSingleAdapter.getItem(position);
                        if(TextUtils.isEmpty(videoRoom.videoUrl)){
                            showToast("进入房间失败");
                            return;
                        }
                        if("LIVE".equals(videoRoom.videoStatus)){
                            LiveAudienceActivity.start(context, videoRoom);
                        }
                        if("OFF".equals(videoRoom.videoStatus)){
                            Intent intent = new Intent(context, PlayVideoActivity.class);
                            intent.putExtra("uri", videoRoom.videoUrl + "");
                            startActivity(intent);
                        }
                    } else {
                        startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                    }
                }
            }
        });

        attention = (TextView) findViewById(R.id.userinfo_attention);
        applyCooperate = (TextView) findViewById(R.id.userinfo_apply_cooperate);
        userinfo_operation_layout = findViewById(R.id.userinfo_operation_layout);

        initData();
    }

    private void initData() {
        userModel.checkIsFriend(userInfo.userId + "", new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                isAttention = aBoolean;
                if (aBoolean) {
                    attention.setText(R.string.cancel_attention);
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        });
        try {
            if (CacheCenter.getInstance().isLogin()) {
                CooperationApplyInfo cooperationApplyInfo = new CooperationApplyInfo();
                cooperationApplyInfo.businessUserId = userInfo.userId;
                userModel.cooperationApplyDetail(cooperationApplyInfo, new Callback<CooperationApplyDetail>() {
                    @Override
                    public void onSuccess(CooperationApplyDetail cooperationApplyDetail) {
//                    CooperationApplyDetail cooperationApplyDetail = cooperationApplyDetailResponse.result;
                        if (cooperationApplyDetail == null) {
                            friendStatus = "";
                        } else {
                            friendStatus = cooperationApplyDetail.status;
                            if("EBL".equals(friendStatus)){
                                applyCooperate.setText("已合作");
                                applyCooperate.setEnabled(false);
                            }
                            if("APL".equals(friendStatus)){
                                applyCooperate.setText("申请中");
                                applyCooperate.setEnabled(false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View getUserInfoTopView() {
        View topView = LayoutInflater.from(this).inflate(R.layout.layout_userinfo_top, null);
        userInfoView = (UserInfoView) topView.findViewById(R.id.userinfo_UserInfoView);
        cooperateRecyclerViewLayout = (RelativeLayout) topView.findViewById(R.id.userinfo_cooperate_RecyclerView_layout);
        cooperateRelation = (TextView) topView.findViewById(R.id.userinfo_cooperate_relation);
        cooperateRecyclerView = (RecyclerView) topView.findViewById(R.id.userinfo_cooperate_RecyclerView);
        liveVideoTypeLayout = (LiveVideoTypeLayout) topView.findViewById(R.id.userinfo_LiveVideoTypeLayout);
        liveVideoTypeLayout.changeType(MConstants.RECYCLER_LINEAR);
        liveVideoTypeLayout.setOnSwitchTypeListener(new LiveVideoTypeLayout.OnSwitchTypeListener() {
            @Override
            public void onSwitchList() {
                rlPtrFrameLayout.switchType(MConstants.RECYCLER_LINEAR);
            }

            @Override
            public void onSwitchGrid() {
                rlPtrFrameLayout.switchType(MConstants.RECYCLER_GRID);
            }
        });
        return topView;
    }

    @Override
    protected void bindEven() {
        attention.setOnClickListener(this);
        applyCooperate.setOnClickListener(this);
    }

    @Override
    protected void setView() {
        userInfoView.setUserInfo(userInfo);
        if(user.userId == userInfo.userId){
            userinfo_operation_layout.setVisibility(View.GONE);
        }
        loadData();
        updateUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userinfo_attention:
                if (CacheCenter.getInstance().isLogin()) {
                    if(!isAttention) {
                        attention(userInfo.userId);
                    }else{
                        cancleAttention(userInfo.userId);
                    }
                }else{
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.userinfo_apply_cooperate:
                if (!CacheCenter.getInstance().isLogin()) {
                    startActivity(LoginActivity.class);
                    return;
                }
                if(TextUtils.isEmpty(friendStatus)){
                    applyCooperation();
                }else {
                    switch (friendStatus){
                        case "APL":{
                            showToast("已提交申请，请勿重复申请");
                        }
                        break;
                        case "EBL":{
                            showToast("已达成合作，请勿重复申请");
                        }
                        break;
                        case "DBL":
                        case "DEL":{
                            commonDialog.setMessage("你之前的申请已被拒绝，是否重新提交审核？");
                            commonDialog.setOnViewClickListener(new CommonDialog.OnViewClickListener() {

                                @Override
                                public void ok(View view) {
                                    applyCooperation();
                                }

                                @Override
                                public void close(View view) {

                                }
                            });
                            commonDialog.showDig();
                        }
                        break;
                    }
                }
                break;
        }
    }

    /**
     * @param loadType
     * @param dataList
     */
    public void setCooperateView(long loadType, List<UserInfo> dataList) {
        if (dataList == null) {
            return;
        }
        if (homeHotRecommendAdapter == null) {
            homeHotRecommendAdapter = new HomeHotRecommendAdapter(this, dataList, R.layout.item_recycler_home_hot_recommend);
            cooperateRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
            //+竖直的分割线
            cooperateRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                    .color(getResources().getColor(R.color.transparent))
                    .sizeResId(R.dimen.common_activity_padding_10)
                    .build());
            homeHotRecommendAdapter.addHeaderView(new GapV(this, 1f));
            homeHotRecommendAdapter.addFooterView(new GapV(this, 1f));
            cooperateRecyclerView.setAdapter(homeHotRecommendAdapter);
            cooperateRecyclerView.addOnScrollListener(new RecyclerScrollPauseLoadListener());

            homeHotRecommendAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int viewType, int position) {
//                    UserInfo userInfo = homeHotRecommendAdapter.getItem(position);
//                    UserInfoDialog userInfoDialog = new UserInfoDialog(UserInfoActivity.this);
//                    userInfoDialog.setUserInfo(userInfo);
//                    userInfoDialog.show();
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
        if (homeHotRecommendAdapter.getCount() != 0) {
            cooperateRecyclerViewLayout.setVisibility(View.VISIBLE);
        } else {
            cooperateRecyclerViewLayout.setVisibility(View.GONE);
        }

    }

    private void loadData() {
        loadCooperateData(MConstants.DATA_4_REFRESH);
        loadUserInfoVideoList(MConstants.DATA_4_REFRESH);
    }

    public void loadCooperateData(final int loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currCooperatePage);
        userModel.cooperationBusinessList(userInfo.userId, pageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                currCooperatePage = userInfoPager.pageNum;
                List<UserInfo> userInfos = userInfoPager.list;
                setCooperateView(loadType, userInfos);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        }, true);
    }

    public void loadUserInfoVideoList(final int loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currVideoPage);
        userModel.getVideoListByUser(userInfo.userId, pageBody, new Callback<Pager<VideoRoom>>() {
            @Override
            public void onSuccess(Pager<VideoRoom> pager) {
                currVideoPage = pager.pageNum;
                List<VideoRoom> videoRooms = pager.list;

                if (loadType == MConstants.DATA_4_REFRESH) {
                    myLiveVideoSingleAdapter.replaceAll(videoRooms);
                    currVideoPage++;
                    rlPtrFrameLayout.refreshComplete();

                } else {
                    currVideoPage++;
                    myLiveVideoSingleAdapter.addAll(videoRooms);
                    if (videoRooms.size() < MConstants.PAGE_SIZE) {
                        //nodata
                        rlPtrFrameLayout.loadMoreComplete();
                    } else {
                        rlPtrFrameLayout.loadMoreComplete();
                    }
                }

                if (myLiveVideoSingleAdapter.getCount() != 0) {
                    liveVideoTypeLayout.setVisibility(View.VISIBLE);
                } else {
                    liveVideoTypeLayout.setVisibility(View.GONE);
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

    /**
     * 关注
     *
     * @param userId
     */
    private void attention(final long userId) {
        userModel.addFriend(userId, new Callback<EditFriendBody>() {
            @Override
            public void onSuccess(EditFriendBody editFriendBody) {
                isAttention = true;
                attention.setText(R.string.cancel_attention);
//                attention.setEnabled(false);
                // ShangXiuUtil.doAddFriend(UserInfoActivity.this, userInfo.userName, "请求添加好友", false);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 取消关注
     *
     * @param userId
     */
    private void cancleAttention(final long userId) {
        userModel.cancelFriend(userId, new Callback<EditFriendBody>() {
            @Override
            public void onSuccess(EditFriendBody editFriendBody) {
                isAttention = false;
                attention.setText(R.string.attention);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    void applyCooperation(){
        if (MConstants.USER_TYPE_COMMONUSER.equals(user.userType)) {
            CustomDialogHelper.OneButtonDialog(this, "提示", "请申请星尚／星咖／星品", "关闭", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return;
        }
//        if (MConstants.USER_TYPE_MERCHANT.equals(user.userType)) {
//            CustomDialogHelper.OneButtonDialog(this, "提示", "您是商家不能申请", "关闭", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            return;
//        }
        if (!MConstants.USER_TYPE_MERCHANT.equals(userInfo.userType)) {
            if(!MConstants.USER_TYPE_MERCHANT.equals(user.userType)){
                CustomDialogHelper.OneButtonDialog(this, "提示", "双方无法合作", "关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                return;
            }
        }else{
            if(MConstants.USER_TYPE_MERCHANT.equals(user.userType)){
                CustomDialogHelper.OneButtonDialog(this, "提示", "双方无法合作", "关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                return;
            }
        }
        CooperationApply cooperationApply = new CooperationApply();
        if(MConstants.USER_TYPE_MERCHANT.equals(user.userType)){
            cooperationApply.userId = userInfo.userId;
            cooperationApply.businessUserId = user.userId;
            cooperationApply.applayUserId = userInfo.userId + "";
        }
        if(MConstants.USER_TYPE_MERCHANT.equals(userInfo.userType)){
            cooperationApply.userId = user.userId;
            cooperationApply.businessUserId = userInfo.userId;
            cooperationApply.applayUserId = user.userId + "";
        }
        cooperationApply.status = "APL";
        Gson gson = new Gson();
        String json = gson.toJson(cooperationApply);
        userModel.cooperationApply(json, new Callback<Response<Object>>() {
            @Override
            public void onSuccess(Response<Object> stringResponse) {
                Object object = stringResponse.result;
                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;
                friendStatus = (String) linkedTreeMap.get("status");
                if("EBL".equals(friendStatus)){
                    applyCooperate.setText("已合作");
                }
                if("APL".equals(friendStatus)){
                    applyCooperate.setText("申请中");
                    applyCooperate.setEnabled(false);
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 获取用户信息
     */
    public void updateUserInfo() {
        userModel.getUserInfo(userInfo.userId, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                setLoginStateView(true, userInfo);
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        }, false);
    }

    private void setLoginStateView(boolean isLogin, UserInfo userInfo) {
        this.userInfo = userInfo;
        userInfoView.setUserInfo(userInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }

}
