package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * 首页热门推荐
 */
public class HomeHotRecommendAdapter extends SuperAdapter<UserInfo> {

    public HomeHotRecommendAdapter(Context context, List<UserInfo> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, UserInfo item) {
        ImageView userIcon = holder.findViewById(R.id.user_icon);
        ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
        holder.setText(R.id.recommend_user_laber, item.userName);

    }

    @Override
    public void noHolder(View convertView, int layoutPosition, UserInfo item) {

    }
}