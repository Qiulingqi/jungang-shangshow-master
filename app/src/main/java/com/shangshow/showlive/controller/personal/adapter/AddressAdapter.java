package com.shangshow.showlive.controller.personal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.widget.AndroidSwipeLayout.RecyclerSwipeAdapter;
import com.shangshow.showlive.controller.adapter.adapter.BaseAdapterHelper;
import com.shangshow.showlive.controller.adapter.adapter.QuickAdapter;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.UserAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2016/10/20.
 */
public class AddressAdapter extends QuickAdapter<UserAddress>{

    private int mRightWidth = 0;
    public AddressAdapter(int mRightWidth, Context context, int layoutResId, List<UserAddress> data) {
        super(context, layoutResId, data);
        this.mRightWidth = mRightWidth;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, UserAddress item, final int position) {
        TextView username = helper.getView(R.id.item_address_username);
        TextView address = helper.getView(R.id.item_address_detail);
        TextView idDefault = helper.getView(R.id.item_address_isdefault);
        TextView disagree_text_view = helper.getView(R.id.disagree_text_view);
        TextView item_address_phone = helper.getView(R.id.item_address_phone);
        View item_left = helper.getView(R.id.item_left);
        View item_right = helper.getView(R.id.item_right);
        username.setText(item.userName);
        address.setText(item.address);
        item_address_phone.setText("" + item.userMobile);
        if (item.isDefault.equals(MConstants.ISDEFAULT_ADDRESS_YES)) {
            idDefault.setVisibility(View.VISIBLE);
        } else if (item.isDefault.equals(MConstants.ISDEFAULT_ADDRESS_NO)) {
            idDefault.setVisibility(View.INVISIBLE);
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        item_right.setLayoutParams(lp2);
        disagree_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null) {
                    onItemClickListener.delete(position);
                }
            }
        });
        item_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, 0, position);
                }
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void delete(int position);
        void onItemClick(View itemView, int viewType, int position);
    }

}