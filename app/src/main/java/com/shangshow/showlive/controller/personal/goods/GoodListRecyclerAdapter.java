package com.shangshow.showlive.controller.personal.goods;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.shangshow.showlive.R;
import com.shaojun.widget.superAdapter.IMulItemViewType;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by lorntao on 16-8-14.
 */
public class GoodListRecyclerAdapter extends SuperAdapter<String> {

    public GoodListRecyclerAdapter(Context context, List<String> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    public GoodListRecyclerAdapter(Context context, List<String> items, IMulItemViewType<String> mulItemViewType) {
        super(context, items, mulItemViewType);
    }

    @Override
    public SuperViewHolder onCreate(View convertView, ViewGroup parent, int viewType) {
        return super.onCreate(convertView, parent, viewType);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, String item) {
        HeadImageView userIcon = (HeadImageView) holder.findViewById(R.id.user_icon);
        ImageView userTypeIcon = (ImageView) holder.findViewById(R.id.user_type_icon);
//        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
//        ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, String item) {

    }

    @Override
    public void add(int location, String item) {
        super.add(location, item);
    }

    @Override
    public void addAll(int location, List<String> items) {
        super.addAll(location, items);
    }

    @Override
    public void removeAll(List<String> items) {
        super.removeAll(items);
    }

    @Override
    public void retainAll(List<String> items) {
        super.retainAll(items);
    }

    @Override
    public boolean containsAll(List<String> items) {
        return super.containsAll(items);
    }
}
