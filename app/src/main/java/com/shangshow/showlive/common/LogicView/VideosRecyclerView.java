package com.shangshow.showlive.common.LogicView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.network.test.MeiZhi;
import com.shaojun.widget.superAdapter.IMulItemViewType;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.SimpleMulItemViewType;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 逻辑视图，直播列表，可以切换显示视图（list/Grid）
 */
public class VideosRecyclerView extends RecyclerView {
    private LiveVideoAdapter liveVideoAdapter;
    //当前直播列表显示的样式
    private int currType = MConstants.RECYCLER_LINEAR;

    public VideosRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public VideosRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideosRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        liveVideoAdapter = new LiveVideoAdapter(getContext(), new ArrayList<MeiZhi>(), new SimpleMulItemViewType<MeiZhi>() {
            @Override
            public int getItemViewType(int position, MeiZhi item) {
                //直接返回itemtype，或者根据数据类型判断返回显示type
                return currType;
            }

            @Override
            public int getLayoutId(int viewType) {
                if (currType == MConstants.RECYCLER_LINEAR) {
                    return R.layout.item_recycler_common_livevideo_list;
                } else {
                    return R.layout.item_recycler_common_livevideo_grid;
                }
            }
        });
        switchType(currType);

        this.setAdapter(liveVideoAdapter);
//        this.addOnScrollListener(new RecyclerScrollPauseLoadListener());
    }


    public LiveVideoAdapter getLiveVideoAdapter() {
        if (liveVideoAdapter != null) {
            return liveVideoAdapter;
        } else {
            return null;
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        if (itemClickListener != null && liveVideoAdapter != null) {
            liveVideoAdapter.setOnItemClickListener(itemClickListener);
        }
    }

    /**
     * 指定按照一种方式显示
     *
     * @param liveVideoData
     * @param showType
     */
    public void setLiveVideoData(List<MeiZhi> liveVideoData, int showType) {

        switchType(showType);
        if (liveVideoAdapter != null) {
            liveVideoAdapter.addAll(liveVideoData);
        }

    }

    /**
     * +数据
     *
     * @param liveVideoData
     */
    public void addAll(List<MeiZhi> liveVideoData) {
        if (liveVideoAdapter != null) {
            liveVideoAdapter.addAll(liveVideoData);
        }
    }

    /**
     * 替换数据
     *
     * @param liveVideoData
     */
    public void replaceAll(List<MeiZhi> liveVideoData) {
        if (liveVideoAdapter != null) {
            liveVideoAdapter.replaceAll(liveVideoData);
        }
    }

    /**
     * itemData
     *
     * @param position
     * @return
     */
    public MeiZhi getItemData(int position) {
        if (liveVideoAdapter != null) {
            return liveVideoAdapter.getItem(position);
        } else {
            return null;
        }
    }


    public void switchType(int showType) {
        currType = showType;
        if (currType == MConstants.RECYCLER_LINEAR) {
            this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        } else {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return liveVideoAdapter.isHeaderView(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
            this.setLayoutManager(gridLayoutManager);
        }
    }


    public class LiveVideoAdapter extends SuperAdapter<MeiZhi> {
        public LiveVideoAdapter(Context context, List<MeiZhi> items, IMulItemViewType<MeiZhi> mulItemViewType) {
            super(context, items, mulItemViewType);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, MeiZhi item) {

//            SimpleDraweeView image = holder.findViewById(R.id.livevideo_image);
//            SimpleDraweeView icon_image = holder.findViewById(R.id.livevideo_info_member_image);
//            ImageLoaderKit.getInstance().displayImage(Uri.parse(item.url), image);
//            ImageLoaderKit.getInstance().displayImage(Uri.parse(item.url), icon_image);

//            ImageView image = holder.findViewById(R.id.livevideo_logo_image);
//            ImageView icon_image = holder.findViewById(R.id.livevideo_info_member_image);
//            ImageLoaderKit.getInstance().displayImage(item.url, image);
//            ImageLoaderKit.getInstance().displayImage(item.url, icon_image, true);
//            holder.setText(R.id.livevideo_info_watch_number, 11 + "人观看");
//
//            JLog.d("onBind" + viewType);
//            //当前列表显示的模式（list|grid）
//            if (viewType == MConstants.RECYCLER_LINEAR) {
//                holder.setText(R.id.livevideo_state, item.desc + "直播中list");
//
//            } else {
//                holder.setText(R.id.livevideo_state, item.desc + "直播中grid");
//
//            }
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, MeiZhi item) {

        }
    }
}