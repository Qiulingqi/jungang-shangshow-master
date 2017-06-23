package com.shangshow.showlive.controller.personal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.OrderInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.OrderPageBody;
import com.shaojun.widget.superAdapter.OnItemClickListener;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.divider.HorizontalDividerItemDecoration;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 充值
 */
public class AddValueOrderFragment extends BasePageFragment {

    private PtrFrameLayout rlPtrFrameLayout;
    private RecyclerView addvalue_orders_recyclerView;
    private UserModel userModel;
    private MyOrderListAdapter myOrderListAdapter;
    private List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();

    public static AddValueOrderFragment newInstance(String title) {
        AddValueOrderFragment f = new AddValueOrderFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_value_order;
    }

    @Override
    protected void initWidget(View rootView) {
        userModel = new UserModel(getActivity());
        rlPtrFrameLayout = (PtrFrameLayout) rootView.findViewById(R.id.addvalue_orders_ptr_framelayout);
        CommonUtil.SetPtrRefreshConfig(getActivity(), rlPtrFrameLayout, MConstants.REFRESH_HEADER_WHITE);
        rlPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getOrderList(MConstants.DATA_4_REFRESH);
            }
        });
//        rlPtrFrameLayout.setOnRefreshOrLoadMoreListener(new RLPtrFrameLayout.OnRefreshOrLoadMoreListener() {
//            @Override
//            public void onRefresh() {
//                getOrderList(MConstants.DATA_4_REFRESH);
//            }
//
//            @Override
//            public void onLoadMore() {
//                getOrderList(MConstants.DATA_4_LOADMORE);
//            }
//
//        });
        addvalue_orders_recyclerView = (RecyclerView) rootView.findViewById(R.id.addvalue_orders_recyclerView);
        //分割线
        addvalue_orders_recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        addvalue_orders_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        myOrderListAdapter = new MyOrderListAdapter(getActivity(), new ArrayList<OrderInfo>(), R.layout.item_recycler_order);
        addvalue_orders_recyclerView.setAdapter(myOrderListAdapter);
        myOrderListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {

            }
        });
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

    @Override
    public void lazyLoad() {

    }

    //  适配器
    private  class MyOrderListAdapter extends SuperAdapter<OrderInfo> {

        boolean flag = true;
        public MyOrderListAdapter(Context context, List<OrderInfo> items, int layoutResId) {
            super(context, items, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final OrderInfo item) {
            ImageView orderImageView = holder.findViewById(R.id.item_order_image);
            ImageView iv_order_item_money = holder.findViewById(R.id.iv_order_item_money);
            View ll_order_item_goods = holder.findViewById(R.id.ll_order_item_goods);
            View ll_order_item_money = holder.findViewById(R.id.ll_order_item_money);
            final View ll_order_item_pay = holder.findViewById(R.id.ll_order_item_pay);
            //  子布局 ‘
            BaseButton bb_order_pay_go = holder.findViewById(R.id.bb_order_pay_go);
            BaseButton bb_order_pay_close = holder.findViewById(R.id.bb_order_pay_close);

            View ll_order_item_parent = holder.findViewById(R.id.ll_order_item_parent);
            TextView tv_order_item_status = holder.findViewById(R.id.tv_order_item_status);
            TextView item_order_time = holder.findViewById(R.id.item_order_time);
            TextView tv_item_order_add_money = holder.findViewById(R.id.tv_item_order_add_money);
            TextView tv_item_order_bi = holder.findViewById(R.id.tv_item_order_bi);
            orderImageView.setVisibility(View.GONE);
            iv_order_item_money.setVisibility(View.VISIBLE);
            ll_order_item_goods.setVisibility(View.GONE);
            ll_order_item_money.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(item.getOrderTime()) && null != item.getOrderTime()) {
                Date date = new Date(item.getOrderTime());
                item_order_time.setText(date.getHours() + ":" + date.getMinutes() + "\n\n" + date.getYear() + ":" + date.getMonth());
            }
//            item_order_time.setText();
            tv_item_order_add_money.setText("充值：" + item.getPrice() + "￥");
            tv_item_order_bi.setText("商秀币：");
            switch (item.getStatus()) {
                case "FAL": {
                    tv_order_item_status.setText("已取消");
                    tv_order_item_status.setTextColor(getActivity().getResources().getColor(R.color.ColorD));
                }
                break;
                case "PAY": {
                    tv_order_item_status.setText("待支付");
                    tv_order_item_status.setTextColor(getActivity().getResources().getColor(R.color.ColorE));
                }
                break;
                case "PAD": {
                    tv_order_item_status.setText("已支付");
                    tv_order_item_status.setTextColor(getActivity().getResources().getColor(R.color.ColorD));
                }
                break;
            }
            ll_order_item_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String status = item.getStatus();
                    if ("FAL".equals(status)) {

                    } else {
                        if (flag){
                            ll_order_item_pay.setVisibility(View.VISIBLE);
                            flag = false;
                        }else {
                            ll_order_item_pay.setVisibility(View.GONE);
                            flag = true;
                        }
                    }
                }
            });
            // 去支付按钮点击事件
            bb_order_pay_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  int layoutPosition1 = layoutPosition;
                    String orderNo = item.getOrderNo();
                    userModel.getPaySuccessEnd(orderNo, new Callback<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            ToastUtils.show("支付成功");
                        }

                        @Override
                        public void onFailure(int resultCode, String message) {

                        }
                    });
                }
            });

            bb_order_pay_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String orderNo = item.getOrderNo();
                    userModel.getOrderListSetStatue(orderNo, new Callback<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            ToastUtils.show("取消成功");
                        }
                        @Override
                        public void onFailure(int resultCode, String message) {

                        }
                    });
                }
            });

        }

        @Override
        public void noHolder(View convertView, int layoutPosition, OrderInfo item) {

        }

        //  写两个接口  将去支付和取消支付 暴露在activity中

       /* private OnItemOperaterListener onItemOperaterListener;

        public void setOnItemOperaterListener(OnItemOperaterListener onItemOperaterListener) {
            this.onItemOperaterListener = onItemOperaterListener;
        }

        public interface OnItemOperaterListener{
            void remind(int position);
        }*/
    }

    /**
     * 获取账单列表
     *
     * @param type
     */
    private void getOrderList(final int type) {
        OrderPageBody pageBody = new OrderPageBody();
        pageBody.pageNum = MConstants.PAGE_INDEX;
        pageBody.pageSize = MConstants.PAGE_SIZE;
        pageBody.setOrderType("1");
        userModel.getOrderList(pageBody, new Callback<Pager<OrderInfo>>() {
            @Override
            public void onSuccess(Pager<OrderInfo> orderInfoPager) {
                List<OrderInfo> orderInfoList = orderInfoPager.list;
                if (type == MConstants.DATA_4_REFRESH) {
                    orderInfos.clear();
                    orderInfos.addAll(orderInfoList);
                    myOrderListAdapter.replaceAll(orderInfos);
                } else {
                    orderInfos.addAll(orderInfoList);
                    myOrderListAdapter.addAll(orderInfos);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlPtrFrameLayout.refreshComplete();
                    }
                }, MConstants.DELAYED);
            }
        });
    }

}
