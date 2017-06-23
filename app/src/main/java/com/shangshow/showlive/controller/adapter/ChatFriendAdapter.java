package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.View;
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
 * Created by 1 on 2016/10/31.
 */
public class ChatFriendAdapter extends SuperAdapter<UserInfo> {
    public ChatFriendAdapter(Context context, List<UserInfo> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final UserInfo item) {
        ImageView userIcon = holder.findViewById(R.id.user_icon_friend);
        ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon_friend);
        TextView userName = holder.findViewById(R.id.friend_mark_name_tv);
        final TextView attentionBtn = holder.findViewById(R.id.item_friend_cancel_attention);
        attentionBtn.setText("发起会话");
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
        userName.setText(item.userName);
        attentionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemOperatorListener != null){
                    onItemOperatorListener.attention(layoutPosition);
                }
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, UserInfo item) {

    }

    private OnItemOperatorListener onItemOperatorListener;

    public void setOnItemOperatorListener(OnItemOperatorListener onItemOperatorListener) {
        this.onItemOperatorListener = onItemOperatorListener;
    }

    public interface OnItemOperatorListener{
        void attention(int position);
    }

}