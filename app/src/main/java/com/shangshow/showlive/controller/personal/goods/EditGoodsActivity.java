package com.shangshow.showlive.controller.personal.goods;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.controller.common.UpLoadFileActivity;
import com.shangshow.showlive.controller.common.picture.SelectPicActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Goods;
import com.shangshow.showlive.network.service.models.OssAuth;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 编辑商品【添加和修改】
 */

public class EditGoodsActivity extends BaseActivity implements View.OnClickListener {
    public static String KEY_GOODS = "goods";
    //    private Goods goods;
    private RelativeLayout goodsImageLayout;//
    private ImageView goodsImage;//商品图片
    private ClearableEditTextWithIcon goodsTitle;//商品图片
    private ClearableEditTextWithIcon goodsPrice;//商品图片
    private ClearableEditTextWithIcon goodsBrief;//商品描述
    private BaseButton releaseBtn;//发布商品
    UserModel userModel;
    String productsId;
    String userId;
    private String logoUrl = "";

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_mygoods_edit;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        productsId = getIntent().getStringExtra("productsId");
        userId = getIntent().getStringExtra("userId");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        goodsImageLayout = (RelativeLayout) findViewById(R.id.edit_goods_image_layout);
        goodsImage = (ImageView) findViewById(R.id.edit_goods_image);
        goodsTitle = (ClearableEditTextWithIcon) findViewById(R.id.edit_goods_title);
        goodsPrice = (ClearableEditTextWithIcon) findViewById(R.id.edit_goods_price);
        goodsBrief = (ClearableEditTextWithIcon) findViewById(R.id.edit_goods_brief);
        releaseBtn = (BaseButton) findViewById(R.id.edit_goods_release_btn);
        UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
        if (!MConstants.USER_TYPE_MERCHANT.equals(currentUser.userType)) {
            releaseBtn.setVisibility(View.GONE);
            goodsTitle.setEnabled(false);
            goodsTitle.setInterupt(true);
            goodsPrice.setEnabled(false);
            goodsPrice.setInterupt(true);
            goodsBrief.setEnabled(false);
            goodsBrief.setInterupt(true);
            goodsImageLayout.setEnabled(false);
        }
    }

    @Override
    protected void bindEven() {
        releaseBtn.setOnClickListener(this);
        goodsImageLayout.setOnClickListener(this);
    }

    @Override
    protected void setView() {
        if (!getIntent().hasExtra("productsId")) {
            //新增
            titleBarView.initCenterTitle(getString(R.string.title_add_goods));
//            releaseBtn.setVisibility(View.VISIBLE);
            titleBarView.initRight("", 0, null);
        } else {//编辑
            titleBarView.initCenterTitle(getString(R.string.title_edit_goods_detail));
//            releaseBtn.setVisibility(View.GONE);
            releaseBtn.setText("修改");
            titleBarView.initRight("", 0, null);
            userModel.getProductsInfo(productsId, new Callback<Goods>() {
                @Override
                public void onSuccess(Goods goods) {
                    logoUrl = goods.logoUrl;
                    ImageLoaderKit.getInstance().displayImage(goods.logoUrl, goodsImage);
                    goodsTitle.setText(goods.productName);
                    goodsPrice.setText(getString(R.string.price_format, goods.price));
                    goodsBrief.setText(goods.brief);
                }

                @Override
                public void onFailure(int resultCode, String message) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_goods_release_btn:
                if (logoUrl.length() > 0 && goodsTitle.getText().toString().length() > 0 && goodsPrice.getText().toString().length() > 0 && goodsBrief.getText().toString().length() > 0) {
                    if (!getIntent().hasExtra("productsId")) {
                        addGoods();
                    } else {
                        updateGoods();
                    }
                } else {
                    showToast("请完善商品信息");
                }
                break;
            case R.id.edit_goods_image_layout: {
                CommonUtil.choicePicture(EditGoodsActivity.this, SelectPicActivity.class);
            }
            break;
        }
    }

    private void addGoods() {
        String title = goodsTitle.getText().toString();
        String price = goodsPrice.getText().toString();
        String brief = goodsBrief.getText().toString();
        Goods goods = new Goods();
        goods.brief = brief;
        goods.logoUrl = logoUrl;
        goods.price = price;
        goods.productName = title;
        userModel.addProductsInfo(goods, new Callback<Object>() {
            @Override
            public void onSuccess(Object object) {
                showToast("商品添加完成");
                startActivity(MyGoodsActivity.class);
                finish();
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.FILE_REQUEST_SELECT || requestCode == MConstants.PHOTO_REQUEST_SELECT) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri == null) {
                    return;
                }
                try {
                    File file = null;
                    if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_CONTENT)) {
                        String imagePath = CommonUtil.getRealPathFromUri(EditGoodsActivity.this, uri);
                        file = new File(imagePath);
                    } else if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_FILE)) {
                        try {
                            file = new File(new URI(uri.toString()));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    if (file != null) {
                        ossAuth(MConstants.UPLOAD_TYPE_AVATAR, file.getPath(), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == MConstants.REQUESTCODE_UPLOAD) {
            if (data.hasExtra(UpLoadFileActivity.KEY_RESULT_URL)) {
                logoUrl = data.getStringExtra(UpLoadFileActivity.KEY_RESULT_URL);
                ImageLoaderKit.getInstance().displayImage(logoUrl, goodsImage);
            }
        }
    }

    private void updateGoods() {
        String title = goodsTitle.getText().toString();
        String price = goodsPrice.getText().toString();
        price = price.substring(1, price.length());
        String brief = goodsBrief.getText().toString();
        Goods goods = new Goods();
        goods.productId = Long.parseLong(productsId);
        goods.brief = brief;
        goods.logoUrl = logoUrl;
        goods.price = price;
        goods.productName = title;
        userModel.updateProductsInfo(goods, new Callback<Goods>() {
            @Override
            public void onSuccess(Goods object) {
                showToast("商品修改完成");
                finish();
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 上传头像
     */
    public void ossAuth(String mediaType, final String filePath, final boolean isNeedProgress) {
        userModel.ossAuth(mediaType, new Callback<OssAuth>() {
            @Override
            public void onSuccess(final OssAuth ossAuth) {
                //调用上传
                Intent intent = new Intent(EditGoodsActivity.this, UpLoadFileActivity.class);
                intent.putExtra(UpLoadFileActivity.KEY_OSSAUTH, ossAuth);
                intent.putExtra(UpLoadFileActivity.KEY_FILEPATH, filePath);
                intent.putExtra(UpLoadFileActivity.KEY_SHOW_UPLOAD_PROGRESS, isNeedProgress);
                startActivityForResult(intent, MConstants.REQUESTCODE_UPLOAD);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

}
