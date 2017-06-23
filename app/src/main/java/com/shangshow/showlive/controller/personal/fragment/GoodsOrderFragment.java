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
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
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
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

/**
 * 商品
 */
public class GoodsOrderFragment extends BasePageFragment {

    private PtrFrameLayout rlPtrFrameLayout;
    private RecyclerView goods_orders_recyclerView;
    private MyOrderListAdapter myOrderListAdapter;
    private UserModel userModel;
    private List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
    private TextView tv_order_item_status;

    public static GoodsOrderFragment newInstance(String title) {
        GoodsOrderFragment f = new GoodsOrderFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_order;
    }

    @Override
    protected void initWidget(View rootView) {

        userModel = new UserModel(getActivity());
        rlPtrFrameLayout = (PtrFrameLayout) rootView.findViewById(R.id.goods_orders_ptr_framelayout);
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
        goods_orders_recyclerView = (RecyclerView) rootView.findViewById(R.id.goods_orders_recyclerView);
        //分割线
        goods_orders_recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getResources().getColor(R.color.default_stroke_color))
                .sizeResId(R.dimen.common_activity_padding_1)
                .build());
        goods_orders_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        myOrderListAdapter = new MyOrderListAdapter(getActivity(), new ArrayList<OrderInfo>(), R.layout.item_recycler_order);
        goods_orders_recyclerView.setAdapter(myOrderListAdapter);
        myOrderListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                ToastUtils.show("wobeidianji" + position);

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
    private class MyOrderListAdapter extends SuperAdapter<OrderInfo> {
        public MyOrderListAdapter(Context context, List<OrderInfo> items, int layoutResId) {
            super(context, items, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final OrderInfo item) {

            ImageView orderImageView = holder.findViewById(R.id.item_order_image);
            TextView orderName = holder.findViewById(R.id.item_order_tv);
            TextView orderPrice = holder.findViewById(R.id.item_order_price);
            TextView orderNo = holder.findViewById(R.id.item_order_no);
            TextView orderTime = holder.findViewById(R.id.item_order_time);
            final View ll_order_item_pay = holder.findViewById(R.id.ll_order_item_pay);
            View ll_order_item_parent = holder.findViewById(R.id.ll_order_item_parent);
            tv_order_item_status = holder.findViewById(R.id.tv_order_item_status);
            ImageLoaderKit.getInstance().displayImage(item.getLogoUrl(), orderImageView, true);
            orderName.setText(item.getProductName());
            orderPrice.setText(item.getPrice() + "");
            orderNo.setText(item.getAmount() + "");
//            orderTime.setText(item.getOrderTime());
            if (TextUtils.isEmpty(item.getOrderTime()) && null != item.getOrderTime()) {
                Date date = new Date(item.getOrderTime());
                orderTime.setText(date.getHours() + ":" + date.getMinutes() + "\n\n" + date.getYear() + ":" + date.getMonth());
            }
            switch (item.getStatus()){
                case "FAL":{
                    tv_order_item_status.setText("已取消");
                    tv_order_item_status.setTextColor(getActivity().getResources().getColor(R.color.ColorD));
                }
                break;
                case "PAY":{
                    tv_order_item_status.setText("待支付");
                    tv_order_item_status.setTextColor(getActivity().getResources().getColor(R.color.ColorE));
                }
                break;
                case "PAD":{
                    tv_order_item_status.setText("已支付");
                    tv_order_item_status.setTextColor(getActivity().getResources().getColor(R.color.ColorD));
                }
                break;
            }
            ll_order_item_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String status = item.getStatus();
                    if ("FAL".equals(status)){
                       // ll_order_item_pay.setVisibility(View.GONE);
                    }else {
                        ToastUtils.show("-" + tv_order_item_status.getText().toString()
                        );
                        ll_order_item_pay.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, OrderInfo item) {

        }


    }

    /**
     * 获取账单列表
     * @param type
     */
    private void getOrderList(final int type) {
        OrderPageBody pageBody = new OrderPageBody();
        pageBody.pageNum = MConstants.PAGE_INDEX;
        pageBody.pageSize = MConstants.PAGE_SIZE;
        pageBody.setOrderType("2");
        userModel.getOrderList(pageBody, new Callback<Pager<OrderInfo>>() {
            @Override
            public void onSuccess(Pager<OrderInfo> orderInfoPager) {
                List<OrderInfo> orderInfoList = orderInfoPager.list;
                if(type == MConstants.DATA_4_REFRESH) {
                    orderInfos.clear();
                    orderInfos.addAll(orderInfoList);
                    myOrderListAdapter.replaceAll(orderInfos);
                }else{
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
