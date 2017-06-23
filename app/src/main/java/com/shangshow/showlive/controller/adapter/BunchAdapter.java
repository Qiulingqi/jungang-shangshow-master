package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.model.ImageInfo;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by 1 on 2016/11/9.
 */
public class BunchAdapter extends SuperAdapter<ImageInfo> {

    public BunchAdapter(Context context, List<ImageInfo> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, ImageInfo item) {

        ImageView iv_image_info_title = holder.findViewById(R.id.iv_image_info_title);
        ImageLoaderKit.getInstance().displayImage(item.headerUrl, iv_image_info_title);
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, ImageInfo item) {

    }
}