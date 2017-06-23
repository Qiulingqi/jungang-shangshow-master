package com.shangshow.showlive.controller.personal.applyfor;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.controller.common.UpLoadFileActivity;
import com.shangshow.showlive.controller.common.picture.SelectPicActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.LabelInfo;
import com.shangshow.showlive.network.service.models.OssAuth;
import com.shangshow.showlive.network.service.models.body.MerchantApplyBody;
import com.shaojun.utils.log.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 申请认证星品
 */
public class ApplyForMerchantActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private ClearableEditTextWithIcon companyName;//公司名
    private ClearableEditTextWithIcon linkman;//联系人
    private ClearableEditTextWithIcon mobile;//手机号
    private ClearableEditTextWithIcon industry;//行业
    private ClearableEditTextWithIcon bankcard;//银行卡
    private ClearableEditTextWithIcon person;//持卡人姓名
    private ClearableEditTextWithIcon tellphine;//持卡人联系方式
    private TextView tv_apply_change;
    private RelativeLayout businessLicenseImageLayout;//营业执照
    private ImageView businessLicenseImage;//营业执照
    private GridView recyclerView;
    private BaseButton commit;
    //上传图片返回
    private String licenseImageUrl;

    private LabelListAdapter labelListAdapter;
    private List<LabelInfo> labelInfoList = new ArrayList<LabelInfo>();
//    private int pageNo = 1;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_apply_for_merchant;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.title_apply_for_merchant);

        companyName = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_merchant_company_name);
        linkman = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_merchant_linkman);
        mobile = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_merchant_mobile);
        industry = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_merchant_industry);
        // 新增的选项
        bankcard = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_bankcard_industry);
        person = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_person_industry);
        tellphine = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_tellohone_industry);


        recyclerView = (GridView) findViewById(R.id.mark_description_recycler_view);
        tv_apply_change = (TextView) findViewById(R.id.tv_apply_change);
        recyclerView.setNumColumns(5);
        labelListAdapter = new LabelListAdapter(ApplyForMerchantActivity.this, R.layout.item_gridview_label, labelInfoList);
        recyclerView.setAdapter(labelListAdapter);
        getHotLabelList();
        businessLicenseImageLayout = (RelativeLayout) findViewById(R.id.apply_for_merchant_business_license_image_layout);
        businessLicenseImage = (ImageView) findViewById(R.id.apply_for_merchant_business_license_image);
        commit = (BaseButton) findViewById(R.id.apply_for_merchant_commit);
    }

    @Override
    protected void bindEven() {
        commit.setEnabled(true);
        commit.setOnClickListener(this);
        tv_apply_change.setOnClickListener(this);
        businessLicenseImageLayout.setOnClickListener(this);
        labelListAdapter.setOnLableChangeListener(new LabelListAdapter.OnLableChangeListener() {
            @Override
            public void onChange(int position) {
                labelListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_for_merchant_business_license_image_layout:
                CommonUtil.choicePicture(ApplyForMerchantActivity.this, SelectPicActivity.class, 2);
                break;
            case R.id.apply_for_merchant_commit:
                if (!(companyName.getText().length() > 0 && linkman.getText().length() > 0 && mobile.getText().toString().length() > 0 && industry.getText().length() > 0)) {
                    showToast("请填写完整信息");
                    return;
                }
                if (TextUtils.isEmpty(licenseImageUrl)) {
                    showToast(getString(R.string.please_upload_business_license));
                    return;
                }
                // TODO: 16/7/28 校验
                applyForMerchant();
                break;
            case R.id.tv_apply_change: {
//                pageNo++;
                getHotLabelList();
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MConstants.PHOTO_REQUEST_SELECT) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    File file = null;
                    if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_CONTENT)) {
                        String imagePath = CommonUtil.getRealPathFromUri(ApplyForMerchantActivity.this, uri);
                        file = new File(imagePath);
                    } else if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_FILE)) {
                        try {
                            file = new File(new URI(uri.toString()));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    if (file != null) {
                        Logger.i("拍照或选择的文件路径为" + file.getPath());
                        ossAuth(file.getPath());
                    } else {
                        Logger.e("拍照或选择的文件生成为空，可能出现异常");
                    }

                }
            } else if (requestCode == MConstants.REQUESTCODE_UPLOAD) {
                if (data.hasExtra(UpLoadFileActivity.KEY_RESULT_URL)) {
                    licenseImageUrl = data.getStringExtra(UpLoadFileActivity.KEY_RESULT_URL);
                    Logger.i("上传成功！图片地址：" + licenseImageUrl);
                    //显示头像
                    ImageLoaderKit.getInstance().displayImage(licenseImageUrl, businessLicenseImage);
                }
            }
        }
    }

    private void getHotLabelList() {
//        BasePageBody pageBody = new BasePageBody();
//        pageBody.pageNum = pageNo;
//        pageBody.pageSize = MConstants.PAGE_SIZE;
//        userModel.getHotLabelList(pageBody, new Callback<Pager<LabelInfo>>() {
//            @Override
//            public void onSuccess(Pager<LabelInfo> labelInfoPager) {
//                if(labelInfoPager.list.size() > 0) {
//                    labelInfoList.clear();
//                    labelInfoList.addAll(labelInfoPager.list);
//                    labelListAdapter.addAll(labelInfoList);
//                }else{
//                    showToast("没有更多标签");
//                }
//            }
//
//            @Override
//            public void onFailure(int resultCode, String message) {
//
//            }
//        });
    }

    /**
     * 上传头像
     */
    public void ossAuth(final String filePath) {

        userModel.ossAuth(MConstants.UPLOAD_TYPE_AVATAR, new Callback<OssAuth>() {
            @Override
            public void onSuccess(OssAuth ossAuth) {
                //调用上传
                Intent intent = new Intent(ApplyForMerchantActivity.this, UpLoadFileActivity.class);
                intent.putExtra(UpLoadFileActivity.KEY_OSSAUTH, ossAuth);
                intent.putExtra(UpLoadFileActivity.KEY_FILEPATH, filePath);
                intent.putExtra(UpLoadFileActivity.KEY_SHOW_UPLOAD_PROGRESS, false);
                startActivityForResult(intent, MConstants.REQUESTCODE_UPLOAD);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }


    /**
     * 申请成为商家
     */
    private void applyForMerchant() {

        MerchantApplyBody merchantApplyBody = new MerchantApplyBody();
        merchantApplyBody.userId = CacheCenter.getInstance().getTokenUserId();
        merchantApplyBody.name = companyName.getText().toString();
        merchantApplyBody.userName = linkman.getText().toString();
        merchantApplyBody.userMobile = mobile.getText().toString();
        merchantApplyBody.industry = industry.getText().toString();
        merchantApplyBody.bankCard = bankcard.getText().toString();
        merchantApplyBody.bankCardName = person.getText().toString();
        merchantApplyBody.contact = tellphine.getText().toString();
//        merchantApplyBody.labelCode = labelInfoList.get(labelListAdapter.checkPosition).getLabelName();
        merchantApplyBody.labelCode = "";
        merchantApplyBody.creditCardImg = licenseImageUrl;
        userModel.businessApply(merchantApplyBody, new Callback<MerchantApplyBody>() {
            @Override
            public void onSuccess(MerchantApplyBody merchantApplyBody) {
                CustomDialogHelper.OneButtonDialog(ApplyForMerchantActivity.this, getString(R.string.prompt), getString(R.string.apply_for_authentication_success), getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }

}
