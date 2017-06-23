package com.shangshow.showlive.controller.liveshow.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shaojun.widget.superAdapter.IMulItemViewType;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by lorntao on 16-8-14.
 */
public class PrivateLetterAnchorRecyclerAdapter extends SuperAdapter<PrivateLetter> {
    private LiveWidgetOperate.LiveAnchorOperateInterface liveAnchorOperateInterface;
    public PrivateLetterAnchorRecyclerAdapter(Context context, LiveWidgetOperate.LiveAnchorOperateInterface liveAnchorOperateInterface, List<PrivateLetter> items, int layoutResId) {
        super(context, items, layoutResId);
        this.liveAnchorOperateInterface=liveAnchorOperateInterface;
    }

    public PrivateLetterAnchorRecyclerAdapter(Context context, LiveWidgetOperate.LiveAnchorOperateInterface liveAnchorOperateInterface, List<PrivateLetter> items, IMulItemViewType<PrivateLetter> mulItemViewType) {
        super(context, items, mulItemViewType);
        this.liveAnchorOperateInterface=liveAnchorOperateInterface;
    }

    @Override
    public SuperViewHolder onCreate(View convertView, ViewGroup parent, int viewType) {
        return super.onCreate(convertView, parent, viewType);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final PrivateLetter item) {
        HeadImageView userIcon = (HeadImageView) holder.findViewById(R.id.user_icon);
        ImageView userTypeIcon = (ImageView) holder.findViewById(R.id.user_type_icon);
        TextView userNameText = (TextView) holder.findViewById(R.id.private_letter_mark_name_tv);
        TextView messageText = (TextView) holder.findViewById(R.id.live_private_letter_context);
        BaseButton replyMessageBtn = (BaseButton) holder.findViewById(R.id.item_private_letter_reply_btn);
        ImageLoaderKit.getInstance().displayImage(item.getUserInfo().avatarUrl, userIcon, true);
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.getUserInfo().userType);
        userNameText.setText(item.getUserInfo().userName);
        messageText.setText(item.getImMessage().getContent());
        replyMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                liveAnchorOperateInterface.onClickReplyPrivateLetter(item.getUserInfo());
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, PrivateLetter item) {

    }

    @Override
    public void add(int location, PrivateLetter item) {
        super.add(location, item);
    }

    @Override
    public void addAll(int location, List<PrivateLetter> items) {
        super.addAll(location, items);
    }

    @Override
    public void removeAll(List<PrivateLetter> items) {
        super.removeAll(items);
    }

    @Override
    public void retainAll(List<PrivateLetter> items) {
        super.retainAll(items);
    }

    @Override
    public boolean containsAll(List<PrivateLetter> items) {
        return super.containsAll(items);
    }
}
