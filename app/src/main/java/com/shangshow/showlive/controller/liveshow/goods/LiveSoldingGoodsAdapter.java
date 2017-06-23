package com.shangshow.showlive.controller.liveshow.goods;

import android.content.Context;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.widget.custom.DynamicHeightImageView;
import com.shangshow.showlive.network.service.models.Goods;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by lorntao on 9/19/16.
 */
public class LiveSoldingGoodsAdapter extends SuperAdapter<Goods> {

    public LiveSoldingGoodsAdapter(Context context, List<Goods> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Goods item) {
        DynamicHeightImageView goodsImage = holder.findViewById(R.id.item_cooperate_merchant_image);
        ImageLoaderKit.getInstance().displayImage(item.logoUrl, goodsImage);
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, Goods item) {

    }
}