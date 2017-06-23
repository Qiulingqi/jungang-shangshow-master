package com.shangshow.showlive.controller.search;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseListActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.controller.adapter.SearchAdpter;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.member.UserInfoActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.responseBody.EditFriendBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索
 */
//  首页- 搜索 - 模糊查询。
public class SearchActivity extends BaseListActivity implements OnClickListener {

    private UserModel userModel;
    private SearchAdpter searchAdpter;
    private String searchContent;
    private long currPage = 1;
    private final static String EXTRA_VIDEO_ROOM = "videoRoom";
    private VideoRoom videoRoom1;
    private VideoRoom videoRoom11;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        userModel = new UserModel(this);
        searchContent = getIntent().getStringExtra("searchContent");
        titleBarView.initCenterTitle(R.string.search);

        searchAdpter = new SearchAdpter(this, R.layout.item_recycler_friend, new ArrayList<UserInfo>());
        rlptr_framelayout.setAdapter(searchAdpter, false);
    }

    @Override
    protected void bindEven() {

        searchAdpter.setOnItemClickListener(new SearchAdpter.OnItemClickListener() {
            @Override
            public void attention(UserInfo userInfo) {
                if (userInfo.isFriend) {
                    cancleAttention(userInfo.userId);
                } else {
                    attentionUser(userInfo.userId);
                }
                userInfo.isFriend = !userInfo.isFriend;
                searchAdpter.notifyDataSetChanged();
            }

            @Override
            public void onItemClick(UserInfo userInfo, int position) {
                if (0 == userInfo.roomId){
                    // 查看个人信息
                    Intent intent = new Intent(SearchActivity.this, UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.key_USERINFO, userInfo);
                    startActivity(intent);
                }else {
                    // 当前主播正在直播
                   /* Intent intent = new Intent(SearchActivity.this, LiveAudienceActivity.class);
                    intent.putExtra(EXTRA_VIDEO_ROOM, userInfo);
                    startActivity(intent);
*/
                  /*  VideoRoom videoRoom = new VideoRoom();
                    videoRoom.roomId = userInfo.roomId;
                    videoRoom.userName = userInfo.userName;
                    // videoRoom.logoUrl = userInfo.videoId;
                    // videoRoom.videoUrl = userInfo.videoUrl
                    // videoRoom.logoUrl = userInfo.logoUrl
                    videoRoom.userId = userInfo.userId;
                    videoRoom.avatarUrl = userInfo.avatarUrl;
                    videoRoom.userType = userInfo.userType;
                    videoRoom.httpPullUrl = userInfo.httpPullUrl;*/
                    long roomId = userInfo.roomId;
                    userModel.getSingeoVideoRoom(roomId, new Callback<VideoRoom>() {
                        @Override
                        public void onSuccess(VideoRoom videoRoom) {

                            videoRoom11 = videoRoom;

                        }

                        @Override
                        public void onFailure(int resultCode, String message) {

                        }
                    });
                    LiveAudienceActivity.start(SearchActivity.this, videoRoom11);
                }

            }
        });
    }

    @Override
    protected void setView() {
        rlptr_framelayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                rlptr_framelayout.autoRefresh();
            }
        }, MConstants.DELAYED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onRefresh() {
        getHotUserList(MConstants.DATA_4_REFRESH);
    }

    @Override
    public void onLoadMore() {
        getHotUserList(MConstants.DATA_4_LOADMORE);
    }

    public void getHotUserList(final int loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currPage);
        pageBody.keyWord = searchContent;
        userModel.getHotUserList(pageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                List<UserInfo> userInfos = userInfoPager.list;
                if (loadType == MConstants.DATA_4_REFRESH) {
                    searchAdpter.replaceAll(userInfos);
                    currPage++;
                    rlptr_framelayout.refreshComplete();
                } else {
                    currPage++;
                    searchAdpter.addAll(userInfos);
                    if (userInfos.size() < MConstants.PAGE_SIZE) {
                        //nodata
                        rlptr_framelayout.loadMoreComplete();
                    } else {
                        rlptr_framelayout.loadMoreComplete();
                    }
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
                rlptr_framelayout.refreshComplete();
            }
        });
    }

    /**
     * 关注
     *
     * @param userId
     */
    private void attentionUser(final long userId) {
        userModel.addFriend(userId, new Callback<EditFriendBody>() {
            @Override
            public void onSuccess(EditFriendBody editFriendBody) {
                rlptr_framelayout.autoRefresh();
            }

            @Override
            public void onFailure(int resultCode, String message) {
                rlptr_framelayout.autoRefresh();
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
                rlptr_framelayout.autoRefresh();
            }

            @Override
            public void onFailure(int resultCode, String message) {
                rlptr_framelayout.autoRefresh();
            }
        });
    }

}
