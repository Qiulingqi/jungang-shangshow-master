package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.network.service.models.body.HotMoreListContontBody;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 1 on 2017/5/18.
 */

public class HotmannlerAdapter extends SuperAdapter<HotMoreListContontBody> {


    public HotmannlerAdapter(Context context, List<HotMoreListContontBody> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, HotMoreListContontBody item) {
        ImageView iv_image_info_title = holder.findViewById(R.id.iv_image_info_title);
        Picasso.with(mContext).load(item.imgUrl).placeholder(R.mipmap.youtube).into(iv_image_info_title);
        ImageView titleimage = holder.findViewById(R.id.titleimage);

        Picasso.with(mContext).load(item.logoUrl).placeholder(R.mipmap.youtube).into(titleimage);
        TextView title1 = holder.findViewById(R.id.text1);
        title1.setText(item.title);
        TextView title2 = holder.findViewById(R.id.text2);
        title2.setText(item.content);
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, HotMoreListContontBody item) {

    }

}
