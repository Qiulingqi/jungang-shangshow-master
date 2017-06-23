package com.shangshow.showlive.controller.personal.goods;

import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Goods;

/**
 * Created by taolong on 16/8/3.
 */
public class GoodsInfoActivity extends BaseActivity {

    TextView tv_apgi_title;
    TextView tv_apgi_price;
    TextView tv_apgi_content;
    ImageView iv_apgi_logo;
    UserModel userModel;
    String productsId;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_goods_info;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        userModel = new UserModel(this);

        productsId = getIntent().getStringExtra("productsId");
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {
        tv_apgi_title = (TextView) findViewById(R.id.tv_apgi_title);
        tv_apgi_price = (TextView) findViewById(R.id.tv_apgi_price);
        tv_apgi_content = (TextView) findViewById(R.id.tv_apgi_content);
        iv_apgi_logo = (ImageView) findViewById(R.id.iv_apgi_logo);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        userModel.getProductsInfo(productsId, new Callback<Goods>() {
            @Override
            public void onSuccess(Goods goods) {
                ImageLoaderKit.getInstance().displayImage(goods.logoUrl, iv_apgi_logo);
                tv_apgi_title.setText(goods.productName);
                tv_apgi_price.setText("￥" + goods.price + "");
                tv_apgi_content.setText(goods.brief);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }
}
