package com.shangshow.showlive.controller.personal.cooperate;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.CooperationPageBody;
import com.shangshow.showlive.network.service.models.body.UserBusinessCooperationBody;
import com.shangshow.showlive.widget.SwipeListView;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * @author
 */
//  合作
public class MyCooperateFragment extends BasePageFragment {
    public static String key_USER_TYPE;
    private PtrHTFrameLayout myfriendsPtrFramentlayout;
    private List<UserInfo> userInfos = new ArrayList<UserInfo>();
    UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
    private long currPage = 1;
    private Context mContext;
    private SwipeListView typeFriendsRecyclerView;
    private FriendsAdapter friendsAdapter;
    private UserModel userModel;

    public static MyCooperateFragment newInstance(String userType) {
        MyCooperateFragment f = new MyCooperateFragment();
        Bundle b = new Bundle();
        b.putString(key_USER_TYPE, userType);
        f.setArguments(b);
        return f;
    }

    @Override
    public void lazyLoad() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_swipe_list_layout;
    }

    @Override
    protected void initWidget(View rootView) {
        userModel = new UserModel(getActivity());

        myfriendsPtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.myfriends_ptr_framentlayout);
        CommonUtil.SetPtrRefreshConfig(mContext, myfriendsPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        myfriendsPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getwaitingCooperate(MConstants.DATA_4_REFRESH);
            }
        });

        typeFriendsRecyclerView = (SwipeListView) rootView.findViewById(R.id.my_friends_type_recyclerView);
        typeFriendsRecyclerView.setRightViewWidth(80);
        friendsAdapter = new FriendsAdapter(typeFriendsRecyclerView.getRightViewWidth(), mContext, new ArrayList<UserInfo>(), R.layout.item_recycler_waiting_cooperate);
        typeFriendsRecyclerView.setAdapter(friendsAdapter);
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {
    }

    /**
     * 获取待合作列表
     */
    public void getwaitingCooperate(final int loadType) {
        CooperationPageBody cooperationPageBody = new CooperationPageBody();
        //如果刷新，请求第一页
        if (loadType == MConstants.DATA_4_REFRESH) {
            cooperationPageBody.pageNum = MConstants.PAGE_INDEX;
        } else {
            cooperationPageBody.pageNum = currPage;
        }
        cooperationPageBody.pageSize = MConstants.PAGE_SIZE;
        cooperationPageBody.orders = "";
        cooperationPageBody.status = "EBL";
        userModel.getUserCooperationList(cooperationPageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                currPage = userInfoPager.pageNum;
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    userInfos.clear();
                    List<UserInfo> loadUserInfo = userInfoPager.list;
                    for(UserInfo userInfo:loadUserInfo){
                        if ((userInfo.applayUserId != userInfo.userId || userInfo.applayUserId != currentUser.userId) && userInfo.applayUserId != 0) {
                            userInfos.add(userInfo);
                        }
                    }
                    friendsAdapter.replaceAll(userInfos);
                } else {
                    List<UserInfo> loadUserInfo = userInfoPager.list;
                    for(UserInfo userInfo:loadUserInfo){
                        if ((userInfo.applayUserId != userInfo.userId || userInfo.applayUserId != currentUser.userId) && userInfo.applayUserId != 0) {
                            userInfos.add(userInfo);
                        }
                    }
                    friendsAdapter.addAll(userInfos);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myfriendsPtrFramentlayout.refreshComplete();
                    }
                }, 500);
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 删除申请
     *
     * @param position
     */
    public void cancleCooperation(int position) {
        UserInfo userInfo = userInfos.get(position);
        UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
        UserBusinessCooperationBody userBusinessCooperationBody = new UserBusinessCooperationBody();
        userBusinessCooperationBody.userBusinessCooperationId = userInfo.userBusinessCooperationId + "";
        userBusinessCooperationBody.status = "DEL";
        if(currentUser.userType == MConstants.USER_TYPE_MERCHANT){
            if(userInfo.applayUserId == userInfo.userId) {
                userBusinessCooperationBody.userId = currentUser.userId + "";
                userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
            }else{
                userBusinessCooperationBody.userId = userInfo.userId + "";
                userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
            }
        }else{
            if(userInfo.applayUserId == userInfo.userId) {
                userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                userBusinessCooperationBody.businessUserId = currentUser.userId + "";
            }else{
                userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                userBusinessCooperationBody.businessUserId = userInfo.userId+ "";
            }
        }
        agreeCooperation(userBusinessCooperationBody);
    }

    /**
     * 拒绝/同意
     *
     * @param userBusinessCooperationBody
     */
    public void agreeCooperation(UserBusinessCooperationBody userBusinessCooperationBody) {
        userModel.agreeCooperation(userBusinessCooperationBody, new Callback<Object>() {
            @Override
            public void onSuccess(Object s) {

            }

            @Override
            public void onFailure(int resultCode, String message) {
                       showToast(message);
            }
        });
    }

    public class FriendsAdapter extends SuperAdapter<UserInfo> {

        int mRightWidth;

        public FriendsAdapter(int mRightWidth, Context context, List<UserInfo> items, int layoutResId) {
            super(context, items, layoutResId);
            this.mRightWidth = mRightWidth;
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final UserInfo item) {
//            ImageView userIcon = holder.findViewById(R.id.user_icon);
//            ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
//            TextView userName = holder.findViewById(R.id.attention_user_name);
//            View item_left = holder.findViewById(R.id.item_left);
//            View item_right = holder.findViewById(R.id.item_right);
//            final TextView attentionBtn = holder.findViewById(R.id.attention_btn);
//            attentionBtn.setText("取消");
//            ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
//            ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
//            userName.setText(item.userName);
//            attentionBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    cancleCooperation(layoutPosition);
//                    userInfos.remove(layoutPosition);
//                    remove(layoutPosition);
//                }
//            });
            TextView disagreeText = holder.findViewById(R.id.disagree_text_view);
            TextView agreeText = holder.findViewById(R.id.agree_text_view);
            TextView userNameText = holder.findViewById(R.id.waiting_cooperate_mark_name_tv);
            TextView timeText = holder.findViewById(R.id.waiting_cooperate_time_tv);
            HeadImageView avatarImageView = holder.findViewById(R.id.user_icon_waiting_cooperate);
            View item_left = holder.findViewById(R.id.item_left);
            View item_right = holder.findViewById(R.id.item_right);
            userNameText.setText(item.userName);
            timeText.setText(item.createAt + "");
            ImageLoaderKit.getInstance().displayImage(item.avatarUrl, avatarImageView);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            item_left.setLayoutParams(lp1);
//        Log.i("json", "mRightWidth:" + (item.applayUserId == userInfo.userId?mRightWidth / 2:mRightWidth));
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            item_right.setLayoutParams(lp2);
            disagreeText.setText("取消");
            agreeText.setVisibility(View.GONE);
            disagreeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancleCooperation(layoutPosition);
                    userInfos.remove(layoutPosition);
                    remove(layoutPosition);
                }
            });
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, UserInfo item) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myfriendsPtrFramentlayout.autoRefresh();
    }
}
