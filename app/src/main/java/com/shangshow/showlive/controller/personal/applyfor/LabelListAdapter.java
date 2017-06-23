package com.shangshow.showlive.controller.personal.applyfor;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.controller.adapter.adapter.BaseAdapterHelper;
import com.shangshow.showlive.controller.adapter.adapter.QuickAdapter;
import com.shangshow.showlive.network.service.models.LabelInfo;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taolong on 16/8/8.
 */
public class LabelListAdapter extends QuickAdapter<LabelInfo> {
    LabelInfo labelInfo;
    int checkPosition = -1;

    public LabelListAdapter(Context context, int layoutResId, List<LabelInfo> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, LabelInfo item, final int position) {
        CheckBox labelText = helper.getView(R.id.label_name_tv);
        labelText.setText(item.getLabelName());
        if (checkPosition != position) {
            labelText.setChecked(false);
        }
        labelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPosition = position;
                if (onLableChangeListener != null) {
                    onLableChangeListener.onChange(position);
                }
            }
        });
    }

    private OnLableChangeListener onLableChangeListener;

    public void setOnLableChangeListener(OnLableChangeListener onLableChangeListener) {
        this.onLableChangeListener = onLableChangeListener;
    }

    public interface OnLableChangeListener {
        void onChange(int position);
    }

}
