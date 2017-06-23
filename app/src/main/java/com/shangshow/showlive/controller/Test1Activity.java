package com.shangshow.showlive.controller;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.widget.custom.DynamicHeightImageView;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.common.widget.ultra.loadmore.RLPtrFrameLayout;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.test.MeiZhi;
import com.shangshow.showlive.network.test.TestApiWrapper;
import com.shaojun.widget.ActionSheet;
import com.shaojun.widget.superAdapter.IMulItemViewType;
import com.shaojun.widget.superAdapter.SimpleMulItemViewType;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.List;

import rx.Subscription;


public class Test1Activity extends BaseActivity implements View.OnClickListener, RLPtrFrameLayout.OnRefreshOrLoadMoreListener {

    private RLPtrFrameLayout rlPtrFrameLayout;
    private LiveVideoAdapter liveVideoAdapter;
    private long currPage = 1;

    private int type = MConstants.RECYCLER_LINEAR;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_test1;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(false);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle("测试");
        titleBarView.initLeft("接口", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionSheet();
            }
        });
        rlPtrFrameLayout = (RLPtrFrameLayout) findViewById(R.id.RLPtrFrameLayout);
        rlPtrFrameLayout.setOnRefreshOrLoadMoreListener(this);


    }


    @Override
    protected void bindEven() {
    }

    @Override
    protected void setView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlPtrFrameLayout.autoRefresh();
            }
        }, MConstants.DELAYED);
    }

    public void setMeiZhiView(int loadType, List<MeiZhi> meiZhis) {
        if (liveVideoAdapter == null) {
            liveVideoAdapter = new LiveVideoAdapter(this, meiZhis, new SimpleMulItemViewType<MeiZhi>() {
                @Override
                public int getItemViewType(int position, MeiZhi item) {
                    //直接返回itemtype，或者根据数据类型判断返回显示type
                    return type;
                }

                @Override
                public int getLayoutId(int viewType) {
                    if (type == MConstants.RECYCLER_LINEAR) {
                        return R.layout.item_recycler_common_livevideo_list;
                    } else {
                        return R.layout.item_recycler_common_livevideo_grid;
                    }
                }
            });
            View topView = LayoutInflater.from(Test1Activity.this).inflate(R.layout.layout_logic_user_info, null);
            liveVideoAdapter.addHeaderView(topView);
            rlPtrFrameLayout.setAdapter(liveVideoAdapter, true);
        } else {
            if (loadType == MConstants.DATA_4_REFRESH) {
                liveVideoAdapter.replaceAll(meiZhis);
            } else {
                liveVideoAdapter.addAll(meiZhis);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData(MConstants.DATA_4_REFRESH);
    }

    @Override
    public void onLoadMore() {
        loadData(MConstants.DATA_4_LOADMORE);
    }

    public class LiveVideoAdapter extends SuperAdapter<MeiZhi> {
        public LiveVideoAdapter(Context context, List<MeiZhi> items, IMulItemViewType<MeiZhi> mulItemViewType) {
            super(context, items, mulItemViewType);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, MeiZhi item) {
            DynamicHeightImageView logoImage = holder.findViewById(R.id.livevideo_logo_image);
            ImageView userIcon = holder.findViewById(R.id.user_icon);
            TextView liveVideoState = holder.findViewById(R.id.livevideo_state);
            TextView liveVideoLaber = holder.findViewById(R.id.livevideo_laber);
            ImageView userTypeIcon = holder.findViewById(R.id.user_type_icon);


            ImageLoaderKit.getInstance().displayImage(item.url, logoImage);
            ImageLoaderKit.getInstance().displayImage(item.url, userIcon, true);
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, "");
//            //直播状态
//            if (item.videoType.equals(MConstants.VIDEOROOM_TYPE_LIVE)) {
//                liveVideoState.setVisibility(View.VISIBLE);
//                liveVideoState.setText("直播");
//            } else if (item.videoType.equals(MConstants.VIDEOROOM_TYPE_VIDEO)) {
//                liveVideoState.setVisibility(View.VISIBLE);
//                liveVideoState.setText("回播");
//            } else {
//                liveVideoState.setVisibility(View.INVISIBLE);
//            }
//            //直播标签
//            if (!TextUtils.isEmpty(item.labelCode)) {
//                liveVideoLaber.setVisibility(View.VISIBLE);
//                liveVideoLaber.setText(item.labelCode);
//            } else {
//                liveVideoLaber.setVisibility(View.INVISIBLE);
//            }
            //观看人数
            holder.setText(R.id.livevideo_info_watch_number, 11 + "人观看");

        }

        @Override
        public void noHolder(View convertView, int layoutPosition, MeiZhi item) {

        }
    }

    private void loadData(final int loadType) {
        if (loadType == MConstants.DATA_4_REFRESH) {
            currPage = MConstants.PAGE_INDEX;
        }
        TestApiWrapper testApiWrapper = new TestApiWrapper();
        Subscription subscription = testApiWrapper.getMeiZhiList(currPage).subscribe(new NewSubscriber<List<MeiZhi>>(Test1Activity.this, false) {
            @Override
            public void onNext(List<MeiZhi> meiZhiList) {
                currPage++;
                setMeiZhiView(loadType, meiZhiList);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlPtrFrameLayout.refreshComplete();
                    }
                }, 500);

            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                if (loadType == MConstants.DATA_4_REFRESH) {
                    rlPtrFrameLayout.refreshComplete();
                } else if (loadType == MConstants.DATA_4_LOADMORE) {
                    rlPtrFrameLayout.changeLoadMoreState(LoadMoreFooterView.LOAD_MORE_STATE_ERROR);
                }

            }
        });
        addSubscrebe(subscription);
    }


    public void showActionSheet() {
        setTheme(R.style.ActionSheetStyleiOS7);
        ActionSheet.createBuilder(Test1Activity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消(Cancel)")
                .setOtherButtonTitles("列表", "9宫格", "瀑布流", "btn1", "btn2", "btn3")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                rlPtrFrameLayout.switchType(MConstants.RECYCLER_LINEAR);
                                break;
                            case 1:
                                rlPtrFrameLayout.switchType(MConstants.RECYCLER_GRID);

                                break;
                            case 2:
                                rlPtrFrameLayout.switchType(MConstants.RECYCLER_STAGGERED_GRID);

                                break;

                            case 3:

                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

}
