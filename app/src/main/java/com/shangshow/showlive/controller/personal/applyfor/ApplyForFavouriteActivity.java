package com.shangshow.showlive.controller.personal.applyfor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.shangshow.showlive.common.utils.FullyGridLayoutManager;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.controller.adapter.adapter.BaseAdapterHelper;
import com.shangshow.showlive.controller.adapter.adapter.QuickAdapter;
import com.shangshow.showlive.controller.common.UpLoadFileActivity;
import com.shangshow.showlive.controller.common.picture.SelectPicActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.LabelInfo;
import com.shangshow.showlive.network.service.models.OssAuth;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.BasePageBody;
import com.shangshow.showlive.network.service.models.body.FavouriteApplyBody;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.utils.log.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 申请认证星尚
 */
public class ApplyForFavouriteActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private ClearableEditTextWithIcon favouriteName;//星尚姓名
    private ClearableEditTextWithIcon favouriteIdCard;//身份证号
    private ClearableEditTextWithIcon favouriteMobile;//手机号码
    private ClearableEditTextWithIcon favouriteAddress;//居住地址
    private TextView tv_apply_change;
    private RelativeLayout favouriteLicenseImageLayout;
    private ImageView favouriteLicenseImage;//证件照片
    private BaseButton submit;//提交资料

    private GridView recyclerView;
    private LabelListAdapter labelListAdapter;
    private List<LabelInfo> labelInfoList = new ArrayList<LabelInfo>();
    private int pageNo = 1;

    //上传图片返回
    private String licenseImageUrl;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_apply_for_favourite;
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
        titleBarView.initCenterTitle(R.string.title_apply_for_favourite);
        favouriteName = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_favourite_Name);
        favouriteIdCard = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_favourite_IdCard);
        favouriteMobile = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_favourite_Mobile);
        favouriteAddress = (ClearableEditTextWithIcon) findViewById(R.id.apply_for_favourite_Address);
        favouriteLicenseImageLayout = (RelativeLayout) findViewById(R.id.apply_for_favourite_license_image_layout);
        favouriteLicenseImage = (ImageView) findViewById(R.id.apply_for_favourite_license_image);
        submit = (BaseButton) findViewById(R.id.apply_for_favourite_Submit);
        tv_apply_change = (TextView) findViewById(R.id.tv_apply_change);

        recyclerView = (GridView) findViewById(R.id.mark_description_recycler_view);
        recyclerView.setNumColumns(5);
        labelListAdapter = new LabelListAdapter(ApplyForFavouriteActivity.this, R.layout.item_gridview_label, labelInfoList);
        recyclerView.setAdapter(labelListAdapter);
        getHotLabelList();
    }

    @Override
    protected void bindEven() {
        submit.setEnabled(true);
        submit.setOnClickListener(this);
        favouriteLicenseImageLayout.setOnClickListener(this);
        tv_apply_change.setOnClickListener(this);
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
            case R.id.apply_for_favourite_license_image_layout:
                CommonUtil.choicePicture(ApplyForFavouriteActivity.this, SelectPicActivity.class, 2);
                break;
            case R.id.apply_for_favourite_Submit:
                if (!(favouriteName.getText().length() > 0 && favouriteIdCard.getText().toString().length() > 0 && favouriteMobile.getText().toString().length() > 0 && favouriteAddress.getText().length() > 0)) {
                    showToast("请填写完整信息");
                    return;
                }
                if (TextUtils.isEmpty(licenseImageUrl)) {
                    showToast("您还没上传证件号");
                    return;
                }
                // TODO: 16/7/27 校验
                ApplyFavourite();
                break;
            case R.id.tv_apply_change: {
                pageNo++;
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
                        String imagePath = CommonUtil.getRealPathFromUri(ApplyForFavouriteActivity.this, uri);
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
                    //显示头像
                    ImageLoaderKit.getInstance().displayImage(licenseImageUrl, favouriteLicenseImage);
                }
            }
        }
    }

    /**
     * 上传头像
     */
    public void ossAuth(final String filePath) {
        userModel.ossAuth(MConstants.UPLOAD_TYPE_AVATAR, new Callback<OssAuth>() {
            @Override
            public void onSuccess(OssAuth ossAuth) {
                //调用上传
                Intent intent = new Intent(ApplyForFavouriteActivity.this, UpLoadFileActivity.class);
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
     * 申请成为星尚
     */
    private void ApplyFavourite() {
        FavouriteApplyBody favouriteApplyBody = new FavouriteApplyBody();
        favouriteApplyBody.userId = CacheCenter.getInstance().getTokenUserId();
        favouriteApplyBody.userName = favouriteName.getText().toString();
        favouriteApplyBody.idCard = favouriteIdCard.getText().toString();
        favouriteApplyBody.userMobile = favouriteMobile.getText().toString();
        favouriteApplyBody.addres = favouriteAddress.getText().toString();
        favouriteApplyBody.idCardUserImg = licenseImageUrl;
        favouriteApplyBody.idCardBackImg = licenseImageUrl;
        favouriteApplyBody.idCardFrontImg = licenseImageUrl;
        favouriteApplyBody.labelCode = labelInfoList.get(labelListAdapter.checkPosition).getLabelName();
        userModel.favouriteApply(favouriteApplyBody, new Callback<FavouriteApplyBody>() {
            @Override
            public void onSuccess(FavouriteApplyBody favouriteApplyBody) {
                CustomDialogHelper.OneButtonDialog(ApplyForFavouriteActivity.this, getString(R.string.prompt), getString(R.string.apply_for_authentication_success), getString(R.string.close), new View.OnClickListener() {
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

    private void getHotLabelList() {
        BasePageBody pageBody = new BasePageBody();
        pageBody.pageNum = pageNo;
        pageBody.pageSize = MConstants.PAGE_SIZE;
        userModel.getHotLabelList(pageBody, new Callback<Pager<LabelInfo>>() {
            @Override
            public void onSuccess(Pager<LabelInfo> labelInfoPager) {
                if(labelInfoPager.list.size() > 0) {
                    labelInfoList.addAll(labelInfoPager.list);
                    labelListAdapter.replaceAll(labelInfoList);
                }else{
                    showToast("没有更多标签");
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }
}
