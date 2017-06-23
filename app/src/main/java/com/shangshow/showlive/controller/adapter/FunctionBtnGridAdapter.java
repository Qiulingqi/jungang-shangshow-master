package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.network.service.models.FunctionBtn;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 个人中心功能按钮适配器
 */
public class FunctionBtnGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<FunctionBtn> functionBtnList;
    private LayoutInflater mInflater;

    public FunctionBtnGridAdapter(Context mContext, ArrayList<FunctionBtn> functionBtnList) {
        this.mContext = mContext;
        this.functionBtnList = functionBtnList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return functionBtnList.size();
    }

    @Override
    public Object getItem(int position) {
        return functionBtnList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridCellHolder gridCellHolder;
        if (convertView == null) {
            gridCellHolder = new GridCellHolder();
            convertView = mInflater.inflate(R.layout.item_gridview_function_button, null);
            gridCellHolder.functionImage = (ImageView) convertView.findViewById(R.id.grid_image);
            gridCellHolder.functionText = (TextView) convertView.findViewById(R.id.grid_text);
            convertView.setTag(gridCellHolder);
        } else {
            gridCellHolder = (GridCellHolder) convertView.getTag();
        }
        FunctionBtn functionBtn = (FunctionBtn) getItem(position);
        gridCellHolder.functionImage.setImageResource(functionBtn.imageResId);
        gridCellHolder.functionText.setText(functionBtn.btnText);
        return convertView;
    }

    class GridCellHolder implements Serializable {
        ImageView functionImage;
        TextView functionText;
    }
}

