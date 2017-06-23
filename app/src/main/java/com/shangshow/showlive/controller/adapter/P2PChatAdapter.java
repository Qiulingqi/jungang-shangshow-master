package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.controller.adapter.adapter.BaseAdapterHelper;
import com.shangshow.showlive.controller.adapter.adapter.MultiItemTypeSupport;
import com.shangshow.showlive.controller.adapter.adapter.QuickAdapter;
import com.shangshow.showlive.network.service.models.PrivateLetter;

import java.util.List;

/**
 * Created by Super-me on 2016/10/30.
 */
public class P2PChatAdapter extends QuickAdapter<PrivateLetter> {

    UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
    UserInfo userInfo;

    public P2PChatAdapter(Context context, List<PrivateLetter> data, final UserInfo currentUser, final UserInfo userInfo) {
        super(context, data, new MultiItemTypeSupport<PrivateLetter>() {
            @Override
            public int getLayoutId(int position, PrivateLetter privateLetter) {
                if((currentUser.userId + "").equals(privateLetter.getAccount())){
                    return R.layout.chat_message_right_layout;
                }
                return R.layout.chat_message_left_layout;
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int postion, PrivateLetter privateLetter) {
                if((currentUser.userId + "").equals(privateLetter.getAccount())){
                    return 1;
                }
                return 2;
            }
        });
        this.userInfo = userInfo;
        this.currentUser = currentUser;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, PrivateLetter item, int position) {
        ImageView userIcon = helper.getView(R.id.user_icon);
        ImageView userTypeIcon = helper.getView(R.id.user_type_icon);
        TextView tv_chat_name = helper.getView(R.id.tv_chat_name);
        TextView tv_chat_message = helper.getView(R.id.tv_chat_message);
        TextView tv_chat_time = helper.getView(R.id.tv_chat_time);
        if((currentUser.userId + "").equals(item.getAccount())){
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, currentUser.userType);
            ImageLoaderKit.getInstance().displayImage(currentUser.avatarUrl, userIcon, true);
            tv_chat_name.setText(currentUser.userName + "");
        }else{
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, userInfo.userType);
            ImageLoaderKit.getInstance().displayImage(userInfo.avatarUrl, userIcon, true);
            tv_chat_name.setText(userInfo.userName + "");
        }
        tv_chat_message.setText(item.getMessage() + "");
        tv_chat_time.setText(item.getTime() + "");
    }
}
