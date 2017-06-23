package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.custom.DynamicHeightImageView;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shaojun.widget.superAdapter.IMulItemViewType;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

public class HomeLiveVideoMulItemAdapter extends SuperAdapter<VideoRoom> {
    public HomeLiveVideoMulItemAdapter(Context context, List<VideoRoom> items, IMulItemViewType<VideoRoom> mulItemViewType) {
        super(context, items, mulItemViewType);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, VideoRoom item) {
        DynamicHeightImageView logoImage = holder.findViewById(R.id.livevideo_logo_image);
        ImageView userIcon = holder.findViewById(R.id.user_icon);
        TextView liveVideoState = holder.findViewById(R.id.livevideo_state);
        TextView liveVideoLaber = holder.findViewById(R.id.livevideo_laber);
        ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
        //人数
        TextView watchNumber = holder.findViewById(R.id.livevideo_info_watch_number);
        //观看或在看
        TextView watchLaber = holder.findViewById(R.id.livevideo_info_watch_number_laber);

        TextView username = holder.findViewById(R.id.livevideo_info_username);
        TextView labercode = holder.findViewById(R.id.livevideo_info_labercode);


        logoImage.setHeightRatio(MConstants.RATIO_POINT_VOIDEIMAGE);
        String logoUrl = item.logoUrl.trim() + "@1e_1c_0o_0l_400h_300w_90q.src";
        ImageLoaderKit.getInstance().displayImage(logoUrl, logoImage);
        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, "");
        //当前视频状态
        if (item.videoType.equals(MConstants.VIDEOROOM_TYPE_LIVE)) {
            liveVideoState.setVisibility(View.VISIBLE);
            liveVideoState.setText(R.string.video_state_live);
            watchLaber.setText("在看");
        } else if (item.videoType.equals(MConstants.VIDEOROOM_TYPE_VIDEO)) {
            liveVideoState.setVisibility(View.VISIBLE);
            liveVideoState.setText(R.string.video_state_video);
            watchLaber.setText("观看");
        } else {
            liveVideoState.setVisibility(View.INVISIBLE);
        }
        //观看人数
        watchNumber.setText(item.onlineUserCount + "");


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
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, VideoRoom item) {

    }
}