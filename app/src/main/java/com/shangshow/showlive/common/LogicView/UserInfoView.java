package com.shangshow.showlive.common.LogicView;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.controller.personal.MyFollwersListActivity;
import com.shangshow.showlive.controller.personal.MyFriendsListActivity;
import com.shangshow.showlive.controller.personal.MyVideosListActivity;
import com.shangshow.showlive.controller.personal.applyfor.ApplyForAuthenticationActivity;
import com.netease.nim.uikit.model.UserInfo;

/**
 * 自定义会员信息试图【绑定】会员对象
 */
public class UserInfoView extends RelativeLayout implements View.OnClickListener {
    private View rootView;
    //会员头像点击、申请认证点击
    private OnClickListener onUserIconClickListener, onApplyForCLickListener;

    private UserInfo userInfo;//用户对象
    private RelativeLayout userIconLayout;
    private ImageView userIcon;
    private ImageView userTypeIcon;//用户类型区分icon
    private TextView userApplyForAttestation;
    private TextView userLaberCode;
    private TextView userID;
    private TextView userTypeValue;
    private boolean isShowAttention = true;
    private LinearLayout userAttentionLayout;//用户关注：视频、关注、粉丝
    private LinearLayout userVideosLayout;
    private TextView userVideos;
    private LinearLayout userFriendsLayout;
    private TextView userFriends;
    private LinearLayout userFollwersLayout;
    private TextView userFollwers;

    public UserInfoView(Context context) {
        super(context);
        init(context);
    }

    public UserInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UserInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_logic_user_info, this);
        userIconLayout = (RelativeLayout) findViewById(R.id.user_icon_layout);
        userIcon = (ImageView) findViewById(R.id.user_icon);
        userTypeIcon = (ImageView) findViewById(R.id.user_type_icon);
        userApplyForAttestation = (TextView) findViewById(R.id.user_apply_for_attestation);
        userLaberCode = (TextView) findViewById(R.id.user_labercode);
        userID = (TextView) findViewById(R.id.user_id_no);
        userTypeValue = (TextView) findViewById(R.id.user_type_value);
        userAttentionLayout = (LinearLayout) findViewById(R.id.user_attention_layout);
        userVideosLayout = (LinearLayout) findViewById(R.id.user_videos_layout);
        userVideos = (TextView) findViewById(R.id.user_videos);
        userFriendsLayout = (LinearLayout) findViewById(R.id.user_friends_layout);
        userFriends = (TextView) findViewById(R.id.user_friends);
        userFollwersLayout = (LinearLayout) findViewById(R.id.user_follwers_layout);
        userFollwers = (TextView) findViewById(R.id.user_follwers);
        //视频、关注、粉丝点击事件
        userVideosLayout.setOnClickListener(this);
        userFriendsLayout.setOnClickListener(this);
        userFollwersLayout.setOnClickListener(this);
        //头像点击
        userIconLayout.setOnClickListener(this);
        //申请按钮
        userApplyForAttestation.setOnClickListener(this);

//        updateView(userInfo);

        rootView.setVisibility(View.INVISIBLE);
    }

    public void updateView(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        rootView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(userInfo.avatarUrl)) {

            ((HeadImageView) userIcon).loadBuddyAvatar(userInfo.avatarUrl);
//            userIcon.loadAvatar(userInfo.avatarUrl, getResources().getDimensionPixelSize(R.dimen.user_avatar_width));
            ImageLoaderKit.getInstance().displayImage(userInfo.avatarUrl, userIcon, true);
        }
        if (!TextUtils.isEmpty(userInfo.userName)) {
            userLaberCode.setText(userInfo.userName);
        } else {
            userLaberCode.setText("");
        }
        userID.setText("" + userInfo.userId);

        //设置用户类型的icon
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, userInfo.userType);
        //设置用户类型文案
        if (!TextUtils.isEmpty(userInfo.userType)) {
            if (MConstants.USER_TYPE_COMMONUSER.equals(userInfo.userType)) {
                userApplyForAttestation.setVisibility(View.VISIBLE);
                userTypeValue.setText("普通用户");
            } else if (MConstants.USER_TYPE_FAVOURITE.equals(userInfo.userType)) {
                userApplyForAttestation.setVisibility(View.GONE);
                userTypeValue.setText("已认证:" + getResources().getString(R.string.wanghong));
            } else if (MConstants.USER_TYPE_SUPERSTAR.equals(userInfo.userType)) {
                userApplyForAttestation.setVisibility(View.GONE);
                userTypeValue.setText("已认证:" + getResources().getString(R.string.xingka));
            } else if (MConstants.USER_TYPE_MERCHANT.equals(userInfo.userType)) {
                userApplyForAttestation.setVisibility(View.GONE);
                userTypeValue.setText("已认证:" + getResources().getString(R.string.shangjia));
            }
        }
        //是否显示用户关系按钮（视频|关注|粉丝）
        if (isShowAttention) {
            userAttentionLayout.setVisibility(View.VISIBLE);
            userVideos.setText(userInfo.videos);
            userFriends.setText(userInfo.friends);
            userFollwers.setText(userInfo.follwers);
        } else {
            userAttentionLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示视频|关注|粉丝
     *
     * @param showAttention
     */
    public void setShowAttention(boolean showAttention) {
        isShowAttention = showAttention;
        updateView(userInfo);
    }

    public void setOnUserIconClickListener(OnClickListener listener) {
        this.onUserIconClickListener = listener;
        if (userIconLayout != null && onUserIconClickListener != null) {
            userIconLayout.setOnClickListener(onUserIconClickListener);
        }
    }

    public void setOnApplyForCLickListener(OnClickListener listener) {
        this.onApplyForCLickListener = listener;
        if (userApplyForAttestation != null && onApplyForCLickListener != null) {
            userApplyForAttestation.setOnClickListener(onApplyForCLickListener);
        }
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        updateView(userInfo);
    }

    public ImageView getUserIcon() {
        return userIcon;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //申请
            case R.id.user_apply_for_attestation:
                getContext().startActivity(new Intent(getContext(), ApplyForAuthenticationActivity.class));
                break;
            //头像点击
            case R.id.user_icon_layout:

                break;
            //视频
            case R.id.user_videos_layout:
                //是当前登录的用户才能查看视频列表
                if (CacheCenter.getInstance().getTokenUserId() != 0 && CacheCenter.getInstance().getTokenUserId() == userInfo.userId) {
                    Intent intent = new Intent(getContext(), MyVideosListActivity.class);
                    intent.putExtra(MyVideosListActivity.KEY_USERID, userInfo.userId);
                    getContext().startActivity(intent);
                }
                break;
            //关注
            case R.id.user_friends_layout:
                //是当前登录的用户才能查看视频列表
                if (CacheCenter.getInstance().getTokenUserId() != 0 && CacheCenter.getInstance().getTokenUserId() == userInfo.userId) {
                    getContext().startActivity(new Intent(getContext(), MyFriendsListActivity.class));
                }
                break;
            //粉丝
            case R.id.user_follwers_layout:
                if (CacheCenter.getInstance().getTokenUserId() != 0 && CacheCenter.getInstance().getTokenUserId() == userInfo.userId) {
                    getContext().startActivity(new Intent(getContext(), MyFollwersListActivity.class));
                }
                break;
        }
    }
}