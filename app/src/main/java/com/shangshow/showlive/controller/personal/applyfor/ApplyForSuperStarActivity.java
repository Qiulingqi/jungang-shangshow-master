package com.shangshow.showlive.controller.personal.applyfor;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.LabelInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.BasePageBody;
import com.shangshow.showlive.network.service.models.body.StarApplyBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请认证星咖
 */
public class ApplyForSuperStarActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private ClearableEditTextWithIcon ApplyStarName;//星咖姓名
    private ClearableEditTextWithIcon ApplyStarAgent;//经纪人
    private ClearableEditTextWithIcon ApplyStarMobile;//手机号
    private ClearableEditTextWithIcon ApplyStarRemark;//备注
    private TextView tv_apply_change;
    private BaseButton ApplyStarSubmitBtn;//提交资料

    private GridView recyclerView;
    private LabelListAdapter labelListAdapter;
    private List<LabelInfo> labelInfoList = new ArrayList<LabelInfo>();
//    private int pageNo = 1;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_apply_for_superstar;
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
        titleBarView.initCenterTitle(R.string.title_apply_for_superstar);
        ApplyStarName = (ClearableEditTextWithIcon) findViewById(R.id.apply_superstar_Name);
        ApplyStarAgent = (ClearableEditTextWithIcon) findViewById(R.id.apply_superstar_Agent);
        ApplyStarMobile = (ClearableEditTextWithIcon) findViewById(R.id.apply_superstar_Mobile);
        ApplyStarRemark = (ClearableEditTextWithIcon) findViewById(R.id.apply_superstar_Remark);
        ApplyStarSubmitBtn = (BaseButton) findViewById(R.id.apply_superstar_Submit);
        tv_apply_change = (TextView) findViewById(R.id.tv_apply_change);

        recyclerView = (GridView) findViewById(R.id.mark_description_recycler_view);
        recyclerView.setNumColumns(5);
        labelListAdapter = new LabelListAdapter(ApplyForSuperStarActivity.this, R.layout.item_gridview_label, labelInfoList);
        recyclerView.setAdapter(labelListAdapter);
        getHotLabelList();
    }

    @Override
    protected void bindEven() {
        ApplyStarSubmitBtn.setEnabled(true);
        ApplyStarSubmitBtn.setOnClickListener(this);
        tv_apply_change.setOnClickListener(this);
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_superstar_Submit:
                if (!(ApplyStarName.getText().length() > 0 && ApplyStarAgent.getText().length() > 0 && ApplyStarMobile.getText().toString().length() > 0 && ApplyStarRemark.getText().length() > 0)) {
                    showToast("请填写完整信息");
                    return;
                }
                // TODO: 16/7/27 校验
                ApplyStar();
                break;
            case R.id.tv_apply_change: {
//                pageNo++;
                getHotLabelList();
            }
            break;
        }
    }

    /**
     * 申请星咔
     */
    private void ApplyStar() {
        StarApplyBody starApplyBody = new StarApplyBody();
        starApplyBody.userId = CacheCenter.getInstance().getTokenUserId();
        starApplyBody.userName = ApplyStarName.getText().toString();
        starApplyBody.startAgent = ApplyStarAgent.getText().toString();
        starApplyBody.userMobile = ApplyStarMobile.getText().toString();
//        starApplyBody.labelCode = labelInfoList.get(labelListAdapter.checkPosition).getLabelName();
        starApplyBody.labelCode = "";
                starApplyBody.remark = ApplyStarRemark.getText().toString();
        userModel.superstarApply(starApplyBody, new Callback<StarApplyBody>() {
            @Override
            public void onSuccess(StarApplyBody starApplyBody) {
                CustomDialogHelper.OneButtonDialog(ApplyForSuperStarActivity.this, getString(R.string.prompt), getString(R.string.apply_for_authentication_success), getString(R.string.close), new View.OnClickListener() {
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
}
