package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.common.widget.custom.DynamicHeightImageView;
import com.shangshow.showlive.network.service.models.body.YoutubeListContontBody;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 1 on 2016/11/9.
 */
public class MemberAdapter extends SuperAdapter<YoutubeListContontBody> {

    public MemberAdapter(Context context, List<YoutubeListContontBody> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, YoutubeListContontBody item) {
      /*  View livevideo_header = holder.findViewById(R.id.livevideo_header);
        livevideo_header.setVisibility(View.GONE);*/
        //  TextView liveVideoState = holder.findViewById(R.id.livevideo_state);
        DynamicHeightImageView logoImage = holder.findViewById(R.id.livevideo_logo_image);
        //  String logoUrl = item.coverUrl.trim() + "@1e_1c_0o_0l_" + BaseApplication.screenWidth / 3 + "h_" + BaseApplication.screenWidth / 3 + "w_90q.src";
        Picasso.with(mContext).load(item.coverUrl).into(logoImage); //ImageLoaderKit.getInstance().displayImage(logoUrl, logoImage);
        /**
         * 修改的
         */
       /* if("LIV".equals(item.videoStatus) && TextUtils.isEmpty(item.videoUrl)){
            liveVideoState.setVisibility(View.VISIBLE);
            liveVideoState.setText(R.string.video_state_live);
        }else if("OFF".equals(item.videoStatus)){
            liveVideoState.setVisibility(View.VISIBLE);
            liveVideoState.setText(R.string.video_state_video);
        }else{
            liveVideoState.setVisibility(View.INVISIBLE);
        }*/
//        if (!TextUtils.isEmpty(item.rtmpPullUrl) && TextUtils.isEmpty(item.videoUrl)) {
//            liveVideoState.setVisibility(View.VISIBLE);
//            liveVideoState.setText(R.string.video_state_live);
//        } else if (TextUtils.isEmpty(item.hlsPullUrl) && !TextUtils.isEmpty(item.videoUrl)) {
//            liveVideoState.setVisibility(View.VISIBLE);
//            liveVideoState.setText(R.string.video_state_video);
//        } else {
//            liveVideoState.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, YoutubeListContontBody item) {

    }
}