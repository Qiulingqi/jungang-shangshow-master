package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseApplication;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.custom.DynamicHeightImageView;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.HashMap;
import java.util.List;

public class MyLiveVideoSingleAdapter extends SuperAdapter<VideoRoom> {

    public boolean isChoose = false;
    private HashMap<Integer, Boolean> chooseMap = new HashMap<>();
    private OnItemClickListener onItemClickListener;
    private UserInfo currentUser;

    public MyLiveVideoSingleAdapter(Context context, List<VideoRoom> items, int layoutResId, UserInfo currentUser) {
        super(context, items, layoutResId);
        this.currentUser = currentUser;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, VideoRoom item) {
        DynamicHeightImageView logoImage = holder.findViewById(R.id.livevideo_logo_image);
        ImageView userIcon = holder.findViewById(R.id.user_icon);
        TextView liveVideoState = holder.findViewById(R.id.livevideo_state);
        TextView liveVideoLaber = holder.findViewById(R.id.livevideo_laber);
        ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
        //人数
        TextView watchNumber = holder.findViewById(R.id.livevideo_info_watch_number);
        TextView liveDesc = holder.findViewById(R.id.live_video_info_desc);
        //观看或在看
        TextView watchLaber = holder.findViewById(R.id.livevideo_info_watch_number_laber);

        TextView username = holder.findViewById(R.id.livevideo_info_username);
        TextView labercode = holder.findViewById(R.id.livevideo_info_labercode);
        ImageView iv_livevideo_item_select = holder.findViewById(R.id.iv_livevideo_item_select);
        View fl_livevideo_item_parent = holder.findViewById(R.id.fl_livevideo_item_parent);
        View ll_function = holder.findViewById(R.id.ll_function);
        ll_function.setVisibility(View.GONE);

        String constrainURL = "";

        if("LIVE".equals(item.status) && !TextUtils.isEmpty(item.videoUrl)){
            constrainURL = item.videoUrl;
        }else if("EBL".equals(item.status)) {
            constrainURL = item.ssUrl;
            String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
            // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI5amNhekmnXc7", "kjUf0WVCLBbpbIvKX3JYwhKuescvKq");
            OSS oss = new OSSClient(mContext, endpoint, credentialProvider);
            constrainURL = oss.presignPublicObjectURL("video-showlive", constrainURL + "");
        }
        item.videoUrl = constrainURL;

        if (isChoose) {
            iv_livevideo_item_select.setVisibility(View.VISIBLE);
        } else {
            iv_livevideo_item_select.setVisibility(View.GONE);
        }
        logoImage.setHeightRatio(MConstants.RATIO_POINT_VOIDEIMAGE);
        String logoUrl = item.logoUrl.trim() + "@1e_1c_0o_0l_" + BaseApplication.screenWidth + "h_" + BaseApplication.screenWidth + "w_90q.src";
        ImageLoaderKit.getInstance().displayImage(logoUrl, logoImage);
        ImageLoaderKit.getInstance().displayImage(currentUser.avatarUrl, userIcon, true);
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, currentUser.userType);
        liveVideoState.setVisibility(View.GONE);
        watchLaber.setText("已看");
        //当前视频状态
//        if (item.videoType.equals(MConstants.VIDEOROOM_TYPE_LIVE)) {
//            liveVideoState.setVisibility(View.VISIBLE);
//            liveVideoState.setText(R.string.video_state_live);
//            watchLaber.setText("在看");
//        } else if (item.videoType.equals(MConstants.VIDEOROOM_TYPE_VIDEO)) {
//            liveVideoState.setVisibility(View.VISIBLE);
//            liveVideoState.setText(R.string.video_state_video);
//            watchLaber.setText("观看");
//        } else {
//            liveVideoState.setVisibility(View.INVISIBLE);
//        }
        if(chooseMap.containsKey(layoutPosition)){
            boolean choose = chooseMap.get(layoutPosition);
            if(choose){
                iv_livevideo_item_select.setBackgroundResource(R.mipmap.circle_sle);
            }else{
                iv_livevideo_item_select.setBackgroundResource(R.mipmap.circle_dis);
            }
        }else{
            iv_livevideo_item_select.setBackgroundResource(R.mipmap.circle_dis);
        }

        if (item.gender.equals("M")) {
            liveDesc.setText("胡润网红新锐榜-优选");
            liveDesc.setTextColor(mContext.getResources().getColor(R.color.color_yellow_FAEC55));
        } else if (item.gender.equals("F")) {
            liveDesc.setText("胡润网红新锐榜-入围");
            liveDesc.setTextColor(mContext.getResources().getColor(R.color.color_green_desc));
        } else {
            liveDesc.setText("");
        }

        //用户名称
        if (!TextUtils.isEmpty(currentUser.userName)) {
            username.setVisibility(View.VISIBLE);
            username.setText(currentUser.userName);
        } else {
            username.setVisibility(View.INVISIBLE);
        }
        //用户标签
        if (!TextUtils.isEmpty(currentUser.labelCode)) {
            labercode.setVisibility(View.VISIBLE);
            labercode.setText(currentUser.labelCode);
        } else {
            labercode.setVisibility(View.INVISIBLE);
        }
        if(!MConstants.USER_TYPE_FAVOURITE.equals(currentUser.userType)){
            labercode.setVisibility(View.GONE);
        }else{
            labercode.setVisibility(View.VISIBLE);
        }
        fl_livevideo_item_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isChoose) {
                    chooseMap.put(layoutPosition, chooseMap.containsKey(layoutPosition) ? !chooseMap.get(layoutPosition) : true);
                }
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(layoutPosition);
                }
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, VideoRoom item) {

    }

    public HashMap<Integer, Boolean> getChooseMap() {
        return chooseMap;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}