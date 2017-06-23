package com.shangshow.showlive.controller.liveshow.goods;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.network.service.models.Goods;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

/**
 * Created by lorntao on 9/19/16.
 */
public class LiveGoodsAdapter extends SuperAdapter<Goods> {

    private int mRightWidth = 0;

    private String information;

    public LiveGoodsAdapter(int mRightWidth, Context context, List<Goods> items, int layoutResId) {
        super(context, items, layoutResId);
        this.mRightWidth = mRightWidth;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, Goods item) {
        ImageView goodsImage = holder.findViewById(R.id.item_goodslist_image);
        TextView productNameText = holder.findViewById(R.id.item_goodslist_name);
        TextView priceText = holder.findViewById(R.id.item_goodslist_price);
        TextView productNoText = holder.findViewById(R.id.item_goodslist_no);
        BaseButton item_goodslist_check = holder.findViewById(R.id.item_goodslist_check);
        TextView tv_goodslist_delete = holder.findViewById(R.id.tv_goodslist_delete);
        View item_left = holder.findViewById(R.id.item_left);
        View item_right = holder.findViewById(R.id.item_right);
        productNameText.setText(item.productName);
        priceText.setText("￥" + item.price + "");
        productNoText.setText("销量：" + item.buyCount + "");
        ImageLoaderKit.getInstance().displayImage(item.logoUrl, goodsImage);
        item_goodslist_check.setEnabled(false);
        if(!TextUtils.isEmpty(information)){
            item_goodslist_check.setText(information + "");
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        item_right.setLayoutParams(lp2);
        tv_goodslist_delete.setOnClickListener(new View.OnClickListener() {
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
    public void noHolder(View convertView, int layoutPosition, Goods item) {

    }

    public OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener{
        void delete(int position);
        void onItemClick(int position);
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}