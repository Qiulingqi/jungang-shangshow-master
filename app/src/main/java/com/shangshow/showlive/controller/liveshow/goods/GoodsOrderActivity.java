package com.shangshow.showlive.controller.liveshow.goods;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.controller.personal.pay.PayOrderActivity;
import com.shangshow.showlive.controller.personal.setting.AddressEditActivity;
import com.shangshow.showlive.controller.personal.setting.AddressListActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Goods;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.UserAddress;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.requestJson.BuyProductRequest;
import com.shangshow.showlive.network.service.models.responseBody.BuyProductResponse;

import java.util.List;

public class GoodsOrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_goods_order_username, tv_goods_order_address, item_goodslist_name,
            item_goodslist_price, item_goodslist_no, item_goodslist_count;
    private BaseButton bb_goods_order_submit;
    private View rl_goods_order_address, rl_goods_order_address_add;
    private ImageView item_goodslist_image;
    private Button b_goods_order_increase, b_goods_order_decrease;

    private UserModel userModel;
    public static final String GOODSORDERACTIVITY_GOODS = "goodsorderactivity_goods";
    private Goods goods;
    private UserAddress userAddress;
    private long videoId;
    private long userId;
    private int count = 1;
    public static final int GOODS_PAY = 0x111;
    public static final int ADDRESS_EDIT = 0x222;
    public static final int ADDRESS_ADD = 0x333;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_goods_order;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        goods = (Goods) getIntent().getSerializableExtra(GOODSORDERACTIVITY_GOODS);
        userModel = new UserModel(this);
        userId = getIntent().getLongExtra("live_anchor_user_id", -1);
        videoId = getIntent().getLongExtra("live_anchor_video_id", -1);
        rl_goods_order_address = findViewById(R.id.rl_goods_order_address);
        rl_goods_order_address_add = findViewById(R.id.rl_goods_order_address_add);
        tv_goods_order_username = (TextView) findViewById(R.id.tv_goods_order_username);
        tv_goods_order_address = (TextView) findViewById(R.id.tv_goods_order_address);
        item_goodslist_name = (TextView) findViewById(R.id.item_goodslist_name);
        item_goodslist_price = (TextView) findViewById(R.id.item_goodslist_price);
        item_goodslist_no = (TextView) findViewById(R.id.item_goodslist_no);
        bb_goods_order_submit = (BaseButton) findViewById(R.id.bb_goods_order_submit);
        item_goodslist_image = (ImageView) findViewById(R.id.item_goodslist_image);
        item_goodslist_count = (TextView) findViewById(R.id.item_goodslist_count);
        b_goods_order_increase = (Button) findViewById(R.id.b_goods_order_increase);
        b_goods_order_decrease = (Button) findViewById(R.id.b_goods_order_decrease);
        getAddressList();
    }

    @Override
    protected void bindEven() {
        rl_goods_order_address.setOnClickListener(this);
        bb_goods_order_submit.setOnClickListener(this);
        rl_goods_order_address_add.setOnClickListener(this);
        b_goods_order_increase.setOnClickListener(this);
        b_goods_order_decrease.setOnClickListener(this);
    }

    @Override
    protected void setView() {
        item_goodslist_name.setText("" + goods.productName);
        item_goodslist_price.setText("" + goods.price);
        item_goodslist_no.setText("数量：" + count);
        ImageLoaderKit.getInstance().displayImage(goods.logoUrl, item_goodslist_image);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_goods_order_address:{
                Intent intent = new Intent(GoodsOrderActivity.this, AddressListActivity.class);
                intent.putExtra("checkAddress", true);
                startActivityForResult(intent, GOODS_PAY);
            }
            break;
            case R.id.rl_goods_order_address_add:{
                Intent intent = new Intent(GoodsOrderActivity.this, AddressEditActivity.class);
                intent.putExtra("isOrder", true);
                startActivityForResult(intent, ADDRESS_ADD);
            }
            break;
            case R.id.bb_goods_order_submit:{
                if(userAddress == null){
                    return;
                }
                BuyProductRequest buyProductRequest = new BuyProductRequest();
                buyProductRequest.address = userAddress.address;
                buyProductRequest.contactName = userAddress.userName;
                buyProductRequest.contactPhone = userAddress.userMobile;
                buyProductRequest.orderType = "1";
                buyProductRequest.videoUserId = videoId + "";
                buyProductRequest.videoId  = videoId + "";
                buyProductRequest.remark = "";
                userModel.videoBuy(goods.productId, buyProductRequest, new Callback<BuyProductResponse>() {
                    @Override
                    public void onSuccess(BuyProductResponse buyProductResponse) {
                        Intent intent = new Intent(GoodsOrderActivity.this, PayOrderActivity.class);
                        intent.putExtra(PayOrderActivity.PAYORDER_BUYPRODUCT, buyProductResponse);
                        startActivityForResult(intent, ADDRESS_EDIT);
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });
            }
            break;
            case R.id.b_goods_order_increase:{
                count++;
                item_goodslist_count.setText(count + "");
                item_goodslist_no.setText("数量：" + count);
            }
            break;
            case R.id.b_goods_order_decrease:{
                if(count > 0)
                    count--;
                item_goodslist_count.setText(count + "");
                item_goodslist_no.setText("数量：" + count);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == GOODS_PAY){
                userAddress = (UserAddress) data.getSerializableExtra(AddressListActivity.ADDRESS_KEY);
                if(userAddress != null){
                    tv_goods_order_address.setText(userAddress.address + "");
                    tv_goods_order_username.setText(userAddress.userName + "");
                }
            }
            if(requestCode == ADDRESS_EDIT){
                finish();
            }
        }
    }

    /**
     * 收货地址
     */
    public void getAddressList() {
        PageBody pageBody = new PageBody();
        pageBody.pageNum = MConstants.PAGE_INDEX;
        pageBody.pageSize = 100;
        pageBody.orders = "";
        userModel.addressList(pageBody, new Callback<Pager<UserAddress>>() {
            @Override
            public void onSuccess(Pager<UserAddress> userAddressPager) {
                List<UserAddress> addresses = userAddressPager.list;
                for(UserAddress address : addresses){
                    if(address.isDefault.equals(MConstants.ISDEFAULT_ADDRESS_YES)){
                        GoodsOrderActivity.this.userAddress = address;
                        tv_goods_order_address.setText(userAddress.address + "");
                        tv_goods_order_username.setText(userAddress.userName + "");
                        rl_goods_order_address.setVisibility(View.VISIBLE);
                        rl_goods_order_address_add.setVisibility(View.GONE);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        });
    }

}
