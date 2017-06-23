package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by 1 on 2016/10/25.
 */
public class SearchAdpter extends SuperAdapter<UserInfo> {

    public SearchAdpter(Context context, int layoutResId, List<UserInfo> items) {
        super(context, items, layoutResId);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final UserInfo item) {
        View ll_friend_parent = holder.findViewById(R.id.ll_friend_parent);
        ImageView userIcon = holder.findViewById(R.id.user_icon_friend);
        ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon_friend);
        TextView userName = holder.findViewById(R.id.friend_mark_name_tv);
        final TextView attentionBtn = holder.findViewById(R.id.item_friend_cancel_attention);
        if(item.isFriend) {
            attentionBtn.setText(R.string.cancel_attention);
        }else{
            attentionBtn.setText(R.string.attention);
        }
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
        userName.setText(item.userName);
        attentionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.attention(item);
                }
            }
        });
        ll_friend_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(item, layoutPosition);
                }
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, UserInfo item) {

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void attention(UserInfo userInfo);
        void onItemClick(UserInfo userInfo, int position);
    }

}