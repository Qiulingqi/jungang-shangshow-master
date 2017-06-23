package com.shangshow.showlive.common.widget.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;


public class LiveVideoTypeLayout extends LinearLayout {
    public interface OnSwitchTypeListener {

        void onSwitchList();

        void onSwitchGrid();

    }

    private OnSwitchTypeListener onSwitchTypeListener;

    public void setOnSwitchTypeListener(OnSwitchTypeListener onSwitchTypeListener) {
        this.onSwitchTypeListener = onSwitchTypeListener;
    }

    private LinearLayout listLayout;
    private ImageView videos_type_list;

    private LinearLayout gridLayout;
    private ImageView videos_type_grid;


    public LiveVideoTypeLayout(Context context) {
        super(context);
        init();
    }

    public LiveVideoTypeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveVideoTypeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_videos_top_type, this);
        listLayout = (LinearLayout) findViewById(R.id.videos_type_list_layout);
        videos_type_list = (ImageView) findViewById(R.id.videos_type_list);
        gridLayout = (LinearLayout) findViewById(R.id.videos_type_grid_layout);
        videos_type_grid = (ImageView) findViewById(R.id.videos_type_grid);
        listLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isClickSoFast(MConstants.DELAYED)) {
                    return;
                }
                changeType(MConstants.RECYCLER_LINEAR);
                if (onSwitchTypeListener != null) {
                    onSwitchTypeListener.onSwitchList();
                }
            }
        });
        gridLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isClickSoFast(MConstants.DELAYED)) {
                    return;
                }
                changeType(MConstants.RECYCLER_GRID);
                if (onSwitchTypeListener != null) {
                    onSwitchTypeListener.onSwitchGrid();
                }
            }
        });
    }


    public void changeType(int type) {
        if (type == MConstants.RECYCLER_LINEAR) {
            videos_type_list.setImageResource(R.mipmap.icon_videolist_type_list_p);
            videos_type_grid.setImageResource(R.mipmap.icon_videolist_type_grid_n);
        } else if (type == MConstants.RECYCLER_GRID) {
            videos_type_list.setImageResource(R.mipmap.icon_videolist_type_list_n);
            videos_type_grid.setImageResource(R.mipmap.icon_videolist_type_grid_p);
        }


    }


}