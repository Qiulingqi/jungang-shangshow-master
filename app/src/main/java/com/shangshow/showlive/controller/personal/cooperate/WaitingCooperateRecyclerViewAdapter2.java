package com.shangshow.showlive.controller.personal.cooperate;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.controller.adapter.adapter.BaseAdapterHelper;
import com.shangshow.showlive.controller.adapter.adapter.QuickAdapter;

import java.util.List;

/**
 * 合作的时候     如果是自己发起的就取消
 */

public class WaitingCooperateRecyclerViewAdapter2 extends QuickAdapter<UserInfo> {

    private int mRightWidth = 0;

    public WaitingCooperateRecyclerViewAdapter2(int mRightWidth, Context context, int layoutResId, List<UserInfo> data) {
        super(context, layoutResId, data);
        this.mRightWidth = mRightWidth;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final UserInfo item, final int position) {
        TextView disagreeText = helper.getView(R.id.disagree_text_view);
        TextView userNameText = helper.getView(R.id.waiting_cooperate_mark_name_tv);
        TextView timeText = helper.getView(R.id.waiting_cooperate_time_tv);
        HeadImageView avatarImageView = helper.getView(R.id.user_icon_waiting_cooperate);
        final UserInfo userInfo = CacheCenter.getInstance().getCurrUser();
        View item_left = helper.getView(R.id.item_left);
        View item_right = helper.getView(R.id.item_right);
        userNameText.setText(item.userName);
        timeText.setText(item.createAt + "");
        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, avatarImageView);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        item_left.setLayoutParams(lp1);
        Log.i("json", "mRightWidth:" + (item.applayUserId == userInfo.userId ? mRightWidth / 2 : mRightWidth));
        //item.applayUserId == userInfo.userId ? mRightWidth / 2 :
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams( mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        item_right.setLayoutParams(lp2);
        if (item.applayUserId == userInfo.userId) {
            disagreeText.setText("取消");
        }
        disagreeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userInfo.userId != item.applayUserId) {
                    if (operater != null) {
                        operater.disaggree(position);
                    }
                } else {
                    if (operater != null) {
                        operater.cancle(position);
                    }
                }
            }
        });

    }

    private CooperaterOperater operater;

    public void setOperater(CooperaterOperater operater) {
        this.operater = operater;
    }

    public interface CooperaterOperater {

        void disaggree(int position);

        void cancle(int position);
    }

}
