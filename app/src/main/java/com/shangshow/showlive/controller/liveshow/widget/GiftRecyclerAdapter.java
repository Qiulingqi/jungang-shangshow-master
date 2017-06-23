package com.shangshow.showlive.controller.liveshow.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.netease.nim.uikit.session.extension.GiftInfo;
import com.shaojun.widget.superAdapter.IMulItemViewType;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by lorntao on 16-8-14.
 */
//  礼物列表
public class GiftRecyclerAdapter extends SuperAdapter<GiftInfo> {

    public GiftRecyclerAdapter(Context context, List<GiftInfo> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    public GiftRecyclerAdapter(Context context, List<GiftInfo> items, IMulItemViewType<GiftInfo> mulItemViewType) {
        super(context, items, mulItemViewType);
    }

    @Override
    public SuperViewHolder onCreate(View convertView, ViewGroup parent, int viewType) {
        return super.onCreate(convertView, parent, viewType);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, GiftInfo item) {
        ImageView giftIcon = (ImageView) holder.findViewById(R.id.icon_gift);
        TextView giftName = (TextView) holder.findViewById(R.id.gift_name_tv);
        TextView giftPrice = (TextView) holder.findViewById(R.id.gift_price_tv);
        ImageLoaderKit.getInstance().displayImage(item.getLogoUrl(), giftIcon, true);
        giftName.setText(item.getProductName());
        giftPrice.setText(item.getdCount()+"");
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, GiftInfo item) {

    }

    @Override
    public void add(int location, GiftInfo item) {
        super.add(location, item);
    }

    @Override
    public void addAll(int location, List<GiftInfo> items) {
        super.addAll(location, items);
    }

    @Override
    public void removeAll(List<GiftInfo> items) {
        super.removeAll(items);
    }

    @Override
    public void retainAll(List<GiftInfo> items) {
        super.retainAll(items);
    }

    @Override
    public boolean containsAll(List<GiftInfo> items) {
        return super.containsAll(items);
    }
}
