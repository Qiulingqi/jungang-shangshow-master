package com.shangshow.showlive.controller.liveshow.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shaojun.widget.superAdapter.IMulItemViewType;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by lorntao on 16-8-14.
 */
public class PrivateLetterFriendsRecyclerAdapter extends SuperAdapter<UserInfo> {
    private LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface;
    private UserModel userModel;
    public PrivateLetterFriendsRecyclerAdapter(Context context, LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface, List<UserInfo> items, int layoutResId) {
        super(context, items, layoutResId);
        this.liveAudienceInterface=liveAudienceInterface;
        userModel = new UserModel(context);
    }

    public PrivateLetterFriendsRecyclerAdapter(Context context, LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface, List<UserInfo> items, IMulItemViewType<UserInfo> mulItemViewType) {
        super(context, items, mulItemViewType);
        this.liveAudienceInterface=liveAudienceInterface;
        userModel = new UserModel(context);
    }

    @Override
    public SuperViewHolder onCreate(View convertView, ViewGroup parent, int viewType) {
        return super.onCreate(convertView, parent, viewType);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final UserInfo item) {
        HeadImageView userIcon = (HeadImageView) holder.findViewById(R.id.user_icon);
        ImageView userTypeIcon = (ImageView) holder.findViewById(R.id.user_type_icon);
        TextView userNameText = (TextView) holder.findViewById(R.id.private_letter_mark_name_tv);
        TextView messageText = (TextView) holder.findViewById(R.id.live_private_letter_context);
        BaseButton replyMessageBtn = (BaseButton) holder.findViewById(R.id.item_private_letter_reply_btn);
        replyMessageBtn.setText(R.string.launch_btn);
        messageText.setVisibility(View.GONE);
        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, userIcon, true);
        ShangXiuUtil.setUserTypeIcon(userTypeIcon, item.userType);
        userNameText.setText(item.userName);

        replyMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
                if(currentUser.userId != item.userId){
                    liveAudienceInterface.onClickReplyPrivateLetter(item);
                }else{
                    ToastUtils.show("自己与自己不能聊天");
                }
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, UserInfo item) {

    }

    @Override
    public void add(int location, UserInfo item) {
        super.add(location, item);
    }

    @Override
    public void addAll(int location, List<UserInfo> items) {
        super.addAll(location, items);
    }

    @Override
    public void removeAll(List<UserInfo> items) {
        super.removeAll(items);
    }

    @Override
    public void retainAll(List<UserInfo> items) {
        super.retainAll(items);
    }

    @Override
    public boolean containsAll(List<UserInfo> items) {
        return super.containsAll(items);
    }
}
