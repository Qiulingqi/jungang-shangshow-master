package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.network.service.models.body.YoutubeListContontBody;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 1 on 2017/5/18.
 */

public class HotVideoAdapter extends SuperAdapter<YoutubeListContontBody> {


    public HotVideoAdapter(Context context, List<YoutubeListContontBody> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, YoutubeListContontBody item) {
        ImageView hot_list_video = holder.findViewById(R.id.hot_list_video);
        Picasso.with(mContext).load(item.coverUrl).placeholder(R.mipmap.youtube).into(hot_list_video);
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, YoutubeListContontBody item) {
    }
}
