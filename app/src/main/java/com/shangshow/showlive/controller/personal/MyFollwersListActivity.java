package com.shangshow.showlive.controller.personal;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Pager;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.responseBody.EditFriendBody;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的粉丝列表
 *
 * @author
 */
public class MyFollwersListActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private PtrFrameLayout myFollwersPtrFrameLayout;
    private RecyclerView follwersRecyclerView;
    private FollwersAdapter follwersAdapter;
    private TextView attentionBtn;
    private long currPage = 1;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_myfollwers;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(getString(R.string.fans));
        myFollwersPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.my_follwers_ptr_framelayout);
        CommonUtil.SetPtrRefreshConfig(this, myFollwersPtrFrameLayout, MConstants.REFRESH_HEADER_WHITE);
        myFollwersPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMyFollwers(MConstants.DATA_4_REFRESH);
            }
        });
        follwersRecyclerView = (RecyclerView) findViewById(R.id.my_follwers_recyclerView);
        //添加分割线
        follwersRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        follwersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        follwersAdapter = new FollwersAdapter(this, new ArrayList<UserInfo>(), R.layout.item_recycler_flower);
        follwersRecyclerView.setAdapter(follwersAdapter);
    }


    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myFollwersPtrFrameLayout.autoRefresh(false);
            }
        }, MConstants.DELAYED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }

    }

    /**
     * 获取我的粉丝
     */
    public void getMyFollwers(final int loadType) {
        PageBody pageBody = new PageBody();
        //如果刷新，请求第一页
        if (loadType == MConstants.DATA_4_REFRESH) {
            pageBody.pageNum = MConstants.PAGE_INDEX;
        } else {
            pageBody.pageNum = currPage;
        }
        pageBody.pageSize = MConstants.PAGE_SIZE;
        pageBody.orders = "";
        userModel.follwers(pageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                List<UserInfo> userInfos = userInfoPager.list;
                currPage = userInfoPager.pageNum;
                if (loadType == MConstants.DATA_4_REFRESH) {
                    follwersAdapter.replaceAll(userInfos);
                } else {
                    follwersAdapter.addAll(userInfos);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myFollwersPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myFollwersPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }
        });

    }

    /**
     * 添加关注
     *
     * @param user
     */
    public void addFriend(final UserInfo user) {
        userModel.addFriend(user.userId, new Callback<EditFriendBody>() {
            @Override
            public void onSuccess(EditFriendBody editFriendBody) {
                attentionBtn.setText(R.string.has_attention);
                attentionBtn.setEnabled(false);
                showToast("已成功添加关注");
                ShangXiuUtil.doAddFriend(MyFollwersListActivity.this, user.userName, "请求添加好友", false);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }

    public class FollwersAdapter extends SuperAdapter<UserInfo> {
        public FollwersAdapter(Context context, List<UserInfo> items, int layoutResId) {
            super(context, items, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final UserInfo item) {
            ImageView userIcon = holder.findViewById(R.id.user_icon_flower);
            ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon_flower);
            TextView userName = holder.findViewById(R.id.attention_user_name);
            attentionBtn = holder.findViewById(R.id.item_flower_attention);
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
            attentionBtn.setText(R.string.attention);
            ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
            userName.setText(item.userName);
            attentionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriend(item);
                }
            });

        }

        @Override
        public void noHolder(View convertView, int layoutPosition, UserInfo item) {

        }
    }


}
