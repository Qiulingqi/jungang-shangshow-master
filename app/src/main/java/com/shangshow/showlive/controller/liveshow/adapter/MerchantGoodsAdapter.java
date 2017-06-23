package com.shangshow.showlive.controller.liveshow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.network.service.models.Goods;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Super-me on 2016/11/7.
 */

//  添加删除商品的是适配器

public class MerchantGoodsAdapter extends SuperAdapter<Goods> {


    private Map<Integer, Boolean> checkMap = new HashMap<Integer, Boolean>();

    public MerchantGoodsAdapter(Context context, List<Goods> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, Goods item) {
        // 初始化布局

        ImageView goodsImage = holder.findViewById(R.id.item_check_image);
        View check_ll_parent = holder.findViewById(R.id.check_ll_parent);

        //  设置商品图像
        ImageLoaderKit.getInstance().displayImage(item.logoUrl, goodsImage);

        if(checkMap.containsKey(layoutPosition)){
            if(checkMap.get(layoutPosition)){
                check_ll_parent.setBackgroundColor(mContext.getResources().getColor(R.color.ColorD));
            }else{
                check_ll_parent.setBackgroundColor(Color.parseColor("#00000000"));
            }
        }else{
            check_ll_parent.setBackgroundColor(Color.parseColor("#00000000"));
        }
        goodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkMap.containsKey(layoutPosition)){
                    checkMap.put(layoutPosition, !checkMap.get(layoutPosition));
                }else{
                    checkMap.put(layoutPosition, true);
                }
                if(onItemOperaterListener != null){

                    onItemOperaterListener.onCheck(layoutPosition);
                }
            }
        });
    }

    @Override
    public void noHolder(View convertView, int layoutPosition, Goods item) {

    }


    public int getCheckCount(){
        int size = 0;
        for(Map.Entry<Integer, Boolean> entry : checkMap.entrySet()){
            if(entry.getValue()){
                size++;
            }
        }
        return size;
    }

    public void clearChecks(){
        checkMap.clear();
    }

    public List<Integer> getCheckList(){
        List<Integer> integers = new ArrayList<Integer>();
        for(Map.Entry<Integer, Boolean> entry : checkMap.entrySet()) {
            if (entry.getValue()) {
                integers.add(entry.getKey());
            }
        }
        return integers;
    }

    private OnItemOperaterListener  onItemOperaterListener;

    public void setOnItemOperaterListener(OnItemOperaterListener onItemOperaterListener) {
        this.onItemOperaterListener = onItemOperaterListener;
    }

    public interface OnItemOperaterListener{
        void onCheck(int position);
    }

}