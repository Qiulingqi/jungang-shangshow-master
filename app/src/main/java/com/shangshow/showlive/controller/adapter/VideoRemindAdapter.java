package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.network.service.models.responseBody.VideoRemind;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by 1 on 2016/11/15.
 */
public class VideoRemindAdapter extends SuperAdapter<VideoRemind> {

    public VideoRemindAdapter(Context context, List<VideoRemind> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, VideoRemind item) {
        TextView tv_videoremind_username = holder.findViewById(R.id.tv_videoremind_username);
        TextView tv_videoremind_time = holder.findViewById(R.id.tv_videoremind_time);
        TextView tv_videoremind_startvideo = holder.findViewById(R.id.tv_videoremind_startvideo);
        ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
        ImageView userIcon = holder.findViewById(R.id.user_icon);
        tv_videoremind_username.setText(item.userInfo.userName + "");
        tv_videoremind_time.setText(item.beginTime + "");
        ImageLoaderKit.getInstance().displayImage(item.userInfo.avatarUrl, userIcon, true);
        userTypeIcon.setVisibility(View.GONE);
        tv_videoremind_startvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemOperaterListener != null){
                    onItemOperaterListener.remind(layoutPosition);
                }
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, VideoRemind item) {

    }

    private OnItemOperaterListener onItemOperaterListener;

    public void setOnItemOperaterListener(OnItemOperaterListener onItemOperaterListener) {
        this.onItemOperaterListener = onItemOperaterListener;
    }

    public interface OnItemOperaterListener{
        void remind(int position);
    }

}
