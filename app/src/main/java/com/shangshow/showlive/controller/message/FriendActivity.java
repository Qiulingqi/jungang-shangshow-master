package com.shangshow.showlive.controller.message;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.controller.adapter.ChatFriendAdapter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.widget.superAdapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends BaseActivity {

    private UserModel userModel;
    RLPtrFrameLayout rlfl_message_f;
    ChatFriendAdapter chatFriendAdapter;
    private long currPage = 1;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_friend;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle("好友");
        userModel = new UserModel(this);
        rlfl_message_f = (RLPtrFrameLayout) findViewById(R.id.rlfl_message_f);
        rlfl_message_f.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
            @Override
            public void onRefresh() {
                getMyFriends(MConstants.DATA_4_REFRESH);
            }

            @Override
            public void onLoadMore() {
                getMyFriends(MConstants.DATA_4_LOADMORE);
            }
        });
        chatFriendAdapter = new ChatFriendAdapter(this, new ArrayList<UserInfo>(), R.layout.item_recycler_friend);
        rlfl_message_f.setAdapter(chatFriendAdapter, true);
    }

    /**
     * 获取我的关注列表
     */
    public void getMyFriends(final int loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currPage);
        userModel.friends(pageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                List<UserInfo> userInfos = userInfoPager.list;
                currPage = userInfoPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    chatFriendAdapter.replaceAll(userInfos);
                } else {
                    chatFriendAdapter.addAll(userInfos);
                }
                currPage++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlfl_message_f.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlfl_message_f.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }
        });
    }

    @Override
    protected void bindEven() {
        chatFriendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                String userId = chatFriendAdapter.getItem(position).userId + "";
                Intent intent = new Intent(FriendActivity.this, P2PChatActivity.class);
                intent.putExtra("contactId", userId);
                startActivity(intent);
            }
        });
        chatFriendAdapter.setOnItemOperatorListener(new ChatFriendAdapter.OnItemOperatorListener() {
            @Override
            public void attention(int position) {
                String userId = chatFriendAdapter.getItem(position).userId + "";
                Intent intent = new Intent(FriendActivity.this, P2PChatActivity.class);
                intent.putExtra("contactId", userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setView() {
        rlfl_message_f.postDelayed(new Runnable() {
            @Override
            public void run() {
                rlfl_message_f.autoRefresh();
            }
        }, MConstants.DELAYED);
    }
}
