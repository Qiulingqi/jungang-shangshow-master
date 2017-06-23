package com.shangshow.showlive.controller.personal.goods;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.controller.liveshow.goods.LiveAnchorGoodsActivity;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by lorntao on 9/19/16.
 */
public class CooperateMerchantAdapter extends SuperAdapter<UserInfo> {

    private Context mContext;
    public CooperateMerchantAdapter(Context context, List<UserInfo> items, int layoutResId) {
        super(context, items, layoutResId);
        mContext = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final UserInfo item) {
        ImageView goodsImage = holder.findViewById(R.id.item_cooperate_merchant_image);
        ImageLoaderKit.getInstance().displayImage(item.avatarUrl, goodsImage);
        goodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(layoutPosition);
                }
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, UserInfo item) {

    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}