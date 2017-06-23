package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by 1 on 2016/10/28.
 */
public class MessageAdapter extends SuperAdapter<PrivateLetter> {

    int mRightWidth;
    public MessageAdapter(int mRightWidth, Context context, List<PrivateLetter> items, int layoutResId) {
        super(context, items, layoutResId);
        this.mRightWidth = mRightWidth;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, PrivateLetter item) {

        ImageView userIcon = holder.findViewById(R.id.user_icon);
        ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);
        TextView itemMessageName = holder.findViewById(R.id.item_message_name);
        TextView itemMessageMessage = holder.findViewById(R.id.item_message_message);
        TextView itemMessageTime = holder.findViewById(R.id.item_message_time);
        TextView tv_message_delete = holder.findViewById(R.id.tv_message_delete);
        View item_left = holder.findViewById(R.id.item_left);
        View item_right = holder.findViewById(R.id.item_right);
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.getUserInfo().userType);
        ImageLoaderKit.getInstance().displayImage(item.getUserInfo().avatarUrl, userIcon, true);
        itemMessageName.setText(item.getUserInfo().userName + "");
        itemMessageMessage.setText(item.getMessage() + "");
        itemMessageTime.setText(item.getTime() + "");
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        item_right.setLayoutParams(lp2);
        tv_message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemListener != null){
                    onItemListener.delete(layoutPosition);
                }
            }
        });
        item_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemListener != null){
                    onItemListener.onItemClick(layoutPosition);
                }
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, PrivateLetter item) {

    }

    public OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener{
        void delete(int position);
        void onItemClick(int position);
    }

}
