package com.shangshow.showlive.controller.personal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
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
 * @author
 */
public class MyFriendsFragment extends BasePageFragment {
    public static String key_USER_TYPE;
    private UserModel userModel;
    private PtrHTFrameLayout myfriendsPtrFramentlayout;
    private String userType;
    private long currPage = 1;
    private Context mContext;
    private RecyclerView typeFriendsRecyclerView;
    private FriendsAdapter friendsAdapter;

    public static MyFriendsFragment newInstance(String userType) {
        MyFriendsFragment f = new MyFriendsFragment();
        Bundle b = new Bundle();
        b.putString(key_USER_TYPE, userType);
        f.setArguments(b);
        return f;
    }

    @Override
    public void lazyLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myfriendsPtrFramentlayout.autoRefresh(false);
            }
        }, 300);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_myfriends_type_fragment;
    }

    @Override
    protected void initWidget(View rootView) {
        userModel = new UserModel(mContext);
        userType = getArguments().getString(key_USER_TYPE);

        myfriendsPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.myfriends_ptr_framentlayout);
        CommonUtil.SetPtrRefreshConfig(mContext, myfriendsPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        myfriendsPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMyFriends(MConstants.DATA_4_REFRESH);
            }
        });

        typeFriendsRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_friends_type_recyclerView);
        //添加分割线
        typeFriendsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(mContext.getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        typeFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        friendsAdapter = new FriendsAdapter(mContext, new ArrayList<UserInfo>(), R.layout.item_recycler_friend);
        typeFriendsRecyclerView.setAdapter(friendsAdapter);
    }

    @Override
    protected void bindEven() {
    }

    @Override
    protected void setView() {
    }

    /**
     * 获取我的关注列表
     */
    public void getMyFriends(final int loadType) {
        PageBody pageBody = new PageBody();
        //如果刷新，请求第一页
        if (loadType == MConstants.DATA_4_REFRESH) {
            pageBody.pageNum = MConstants.PAGE_INDEX;
        } else {
            pageBody.pageNum = currPage;
        }
        pageBody.pageSize = MConstants.PAGE_SIZE;
        pageBody.orders = "";
        pageBody.userType = userType;
        userModel.friends(pageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myfriendsPtrFramentlayout.refreshComplete();
                    }
                }, 500);
                List<UserInfo> userInfos = userInfoPager.list;
                if(userInfos != null && userInfos.size() == 0){
                    friendsAdapter.clear();
                    return;
                }
                currPage = userInfoPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    friendsAdapter.replaceAll(userInfos);
                } else {
                    friendsAdapter.addAll(userInfos);
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myfriendsPtrFramentlayout.refreshComplete();
                    }
                }, 500);
            }
        });
    }

    /**
     * 取消关注
     *
     * @param userId
     */
    public void cancelAttention(long userId) {
        userModel.cancelFriend(userId, new Callback<EditFriendBody>() {
            @Override
            public void onSuccess(EditFriendBody editFriendBody) {
                //刷新界面
                getMyFriends(MConstants.DATA_4_REFRESH);
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

    public class FriendsAdapter extends SuperAdapter<UserInfo> {
        public FriendsAdapter(Context context, List<UserInfo> items, int layoutResId) {
            super(context, items, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final UserInfo item) {
            ImageView userIcon = holder.findViewById(R.id.user_icon_friend);
            ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon_friend);
            TextView userName = holder.findViewById(R.id.friend_mark_name_tv);
            final TextView attentionBtn = holder.findViewById(R.id.item_friend_cancel_attention);
            attentionBtn.setText(R.string.cancel_attention);
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
            ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
            userName.setText(item.userName);
            attentionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelAttention(item.userId);
                }
            });
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, UserInfo item) {

        }
    }

}
