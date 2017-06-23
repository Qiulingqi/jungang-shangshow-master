package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseApplication;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.animate.Rotate3dAnimation;
import com.shangshow.showlive.common.utils.ScreenUtil;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.custom.DynamicHeightImageView;
import com.shangshow.showlive.controller.member.UserInfoActivity;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

public class HomeLiveVideoSingleAdapter extends SuperAdapter<VideoRoom> {


    public HomeLiveVideoSingleAdapter(Context context, List<VideoRoom> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final VideoRoom item) {
        DynamicHeightImageView logoImage = holder.findViewById(R.id.livevideo_logo_image);
        ImageView userIcon = holder.findViewById(R.id.user_icon);
        TextView liveVideoState = holder.findViewById(R.id.livevideo_state);
        final ImageView iv_live_status = holder.findViewById(R.id.iv_live_status);
        TextView liveVideoLaber = holder.findViewById(R.id.livevideo_laber);
        ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
        //人数
        TextView watchNumber = holder.findViewById(R.id.livevideo_info_watch_number);

        TextView liveDesc = holder.findViewById(R.id.live_video_info_desc);
        //观看或在看
        TextView watchLaber = holder.findViewById(R.id.livevideo_info_watch_number_laber);

        TextView username = holder.findViewById(R.id.livevideo_info_username);
        TextView labercode = holder.findViewById(R.id.livevideo_info_labercode);
//        logoImage.setHeightRatio(MConstants.RATIO_POINT_VOIDEIMAGE);
        String logoUrl = item.logoUrl.trim() + "@1e_1c_0o_0l_" + BaseApplication.screenWidth + "h_" + BaseApplication.screenWidth + "w_90q.src";
        ImageLoaderKit.getInstance().displayImage(logoUrl, logoImage);
        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, "");
        //当前视频状态
        if("LIV".equals(item.videoStatus) && TextUtils.isEmpty(item.videoUrl)){
            iv_live_status.setVisibility(View.VISIBLE);
            liveVideoState.setVisibility(View.GONE);
            final Rotate3dAnimation rotation = new Rotate3dAnimation(0, 360,
                    ScreenUtil.dip2px(mContext, 39) / 2, ScreenUtil.dip2px(mContext, 18) / 2, 0, true);
            rotation.setDuration(2500);
            rotation.setRepeatCount(-1);
            rotation.setInterpolator(new AccelerateInterpolator());
            iv_live_status.startAnimation(rotation);
        }else if("OFF".equals(item.videoStatus)){
            iv_live_status.setVisibility(View.GONE);
            liveVideoState.setVisibility(View.VISIBLE);
            liveVideoState.setText(R.string.video_state_video);
        }else{
            iv_live_status.setVisibility(View.VISIBLE);
            /**
             * 让LIVE动起来
             *
             */
            final Rotate3dAnimation rotation = new Rotate3dAnimation(0, 360,
                    ScreenUtil.dip2px(mContext, 39) / 2, ScreenUtil.dip2px(mContext, 18) / 2, 0, true);
            rotation.setDuration(2500);
            rotation.setRepeatCount(-1);
            rotation.setInterpolator(new AccelerateInterpolator());
            iv_live_status.startAnimation(rotation);
            liveVideoState.setVisibility(View.GONE);
        }
//        if (!TextUtils.isEmpty(item.rtmpPullUrl) && TextUtils.isEmpty(item.videoUrl)) {
//            liveVideoState.setVisibility(View.GONE);
//            liveVideoState.setText(R.string.video_state_live);
//            watchLaber.setText("在看");
//            iv_live_status.setVisibility(View.VISIBLE);
//            final Rotate3dAnimation rotation = new Rotate3dAnimation(0, 360,
//                    ScreenUtil.dip2px(mContext, 39) / 2, ScreenUtil.dip2px(mContext, 18) / 2, 0, true);
//            rotation.setDuration(2500);
//            rotation.setRepeatCount(-1);
//            rotation.setInterpolator(new AccelerateInterpolator());
//            iv_live_status.startAnimation(rotation);
//        } else if (TextUtils.isEmpty(item.hlsPullUrl) && !TextUtils.isEmpty(item.videoUrl)) {
//            liveVideoState.setVisibility(View.VISIBLE);
//            liveVideoState.setText(R.string.video_state_video);
//            watchLaber.setText("观看");
//            iv_live_status.setVisibility(View.GONE);
//        } else {
//            liveVideoState.setVisibility(View.INVISIBLE);
//            iv_live_status.setVisibility(View.GONE);
//        }
//        if (item.videoType.equals(MC_onstants.VIDEOROOM_TYPE_LIVE)) {
//            liveVideoState.setVisibility(View.VISIBLE);
//            liveVideoState.setText(R.string.video_state_live);
//            watchLaber.setText("在看");
//        } else if (item.videoType.equals(MConstants.VIDEOROOM_TYPE_VIDEO)) {
//            liveVideoState.setVisibility(View.VISIBLE);
//            liveVideoState.setText(R.string.videostate_video);
//            watchLaber.setText("观看");
//        } else {
//            liveVideoState.setVisibility(View.INVISIBLE);
//        }
        //观看人数
        watchNumber.setText(item.onlineUserCount + "");
        if(MConstants.USER_TYPE_FAVOURITE.equals(item.userType)) {
            liveDesc.setVisibility(View.VISIBLE);
            if (item.gender.equals("M")) {
                liveDesc.setText("胡润网红新锐榜-优选");
                liveDesc.setTextColor(mContext.getResources().getColor(R.color.color_yellow_FAEC55));
            } else if (item.gender.equals("F")) {
                liveDesc.setText("胡润网红新锐榜-入围");
                liveDesc.setTextColor(mContext.getResources().getColor(R.color.color_red_desc));
            } else {
                liveDesc.setText("");
            }
        }else{
            liveDesc.setVisibility(View.INVISIBLE);
        }

        //用户名称
        if (!TextUtils.isEmpty(item.userName)) {
            username.setVisibility(View.VISIBLE);
            username.setText(item.userName);
        } else {
            username.setVisibility(View.INVISIBLE);
        }
        //用户标签
        if (!TextUtils.isEmpty(item.labelCode)) {
            labercode.setVisibility(View.VISIBLE);
            labercode.setText(item.labelCode);
        } else {
            labercode.setVisibility(View.INVISIBLE);
        }
        if(!MConstants.USER_TYPE_FAVOURITE.equals(item.userType)){
            labercode.setVisibility(View.GONE);
        }else{
            labercode.setVisibility(View.VISIBLE);
        }
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UserInfo userInfo = homeHotRecommendAdapter.getItem(position);
                UserInfo userInfo = new UserInfo();
                userInfo.labelCode = item.labelCode;
                userInfo.userId = item.userId;
                userInfo.userName = item.userName;
                userInfo.userType = item.userType;
//                userInfo.friends = item
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.key_USERINFO, userInfo);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, VideoRoom item) {

    }
}