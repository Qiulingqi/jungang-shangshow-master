package com.shangshow.showlive.controller.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;


/**
 * 联系我们
 */
public class ContactUsActivity extends BaseActivity implements View.OnClickListener {
    private BaseButton btnContactUs;//拨打电话
    private Context mContext;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_contact_us;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        mContext = this;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.contact_us);
        btnContactUs = (BaseButton) findViewById(R.id.contact_us_btn);
    }


    @Override
    protected void bindEven() {
        btnContactUs.setOnClickListener(this);
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_us_btn:
             /*   Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:021222222"));
                startActivity(intent);*/
                CustomDialogHelper.TwoButtonDialog(this, "", getString(R.string.call_tel_phone), getString(R.string.confirm), getString(R.string.cancel), new CustomDialogHelper.OnDialogActionListener() {
                    @Override
                    public void doCancelAction() {
                    }
                    @Override
                    public void doPositiveAction() {
                        Uri uri = Uri.parse("tel:021222222");
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
    }
}
