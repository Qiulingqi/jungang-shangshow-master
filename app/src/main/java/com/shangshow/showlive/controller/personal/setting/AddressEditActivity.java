package com.shangshow.showlive.controller.personal.setting;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.VerifyUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.UserAddress;
import com.shangshow.showlive.network.service.models.body.AddressBody;


/**
 * 添加收货地址
 *
 * @author
 */
public class AddressEditActivity extends BaseActivity implements View.OnClickListener {
    public static String key_ADDRESS;
    private UserModel userModel;
    private UserAddress userAddress;
    private ClearableEditTextWithIcon addressUserName;
    private ClearableEditTextWithIcon addressMobile;
    private EditText addressDetail;
    private CheckBox addressIsDefault;
    private BaseButton editCompleteBtn;

    private int EDIT_TYPE_ADD = 0;//新增
    private int EDIT_TYPE_EDIT = 1;//编辑
    private int currType = -1;
    private boolean isOrder;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_address_edit;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        if (getIntent().hasExtra(key_ADDRESS)) {
            userAddress = (UserAddress) getIntent().getExtras().getSerializable(key_ADDRESS);
        }
        isOrder = getIntent().getBooleanExtra("isOrder", false);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(getString(R.string.title_address_detail));
        addressUserName = (ClearableEditTextWithIcon) findViewById(R.id.address_edit_username);
        addressMobile = (ClearableEditTextWithIcon) findViewById(R.id.address_edit_mobile);
        addressDetail = (EditText) findViewById(R.id.address_edit_detail);
        addressIsDefault = (CheckBox) findViewById(R.id.address_edit_isDefault);
        editCompleteBtn = (BaseButton) findViewById(R.id.address_edit_complete_button);
    }

    @Override
    protected void bindEven() {
        editCompleteBtn.setEnabled(false);
        editCompleteBtn.setOnClickListener(this);
        addressUserName.addTextChangedListener(watcher);
        addressMobile.addTextChangedListener(watcher);
        addressDetail.addTextChangedListener(watcher);
    }

    @Override
    protected void setView() {
        if (userAddress != null) {
            currType = EDIT_TYPE_EDIT;
            editCompleteBtn.setText("更新");
            addressUserName.setText(userAddress.userName);
            addressMobile.setText(userAddress.userMobile);
            addressDetail.setText(userAddress.address);
            if (userAddress.isDefault.equals(MConstants.ISDEFAULT_ADDRESS_YES)) {
                addressIsDefault.setChecked(true);
            } else if (userAddress.isDefault.equals(MConstants.ISDEFAULT_ADDRESS_NO)) {
                addressIsDefault.setChecked(false);
            }
        } else {
            currType = EDIT_TYPE_ADD;
            editCompleteBtn.setText("添加");

        }

    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (addressUserName.getText().length() > 0 && addressMobile.getText().length() > 0 && addressDetail.getText().toString().length() > 0) {
                editCompleteBtn.setEnabled(true);
            } else {
                editCompleteBtn.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_edit_complete_button:
                if (!VerifyUtils.checkMobileNO(this, addressMobile.getText().toString()) || !VerifyUtils.checkNickName(this, addressUserName.getText().toString())) {
                    return;
                }
                if (currType == EDIT_TYPE_ADD) {
                    addAddress();
                } else if (currType == EDIT_TYPE_EDIT) {
                    eidtAddress();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 添加收货地址
     */
    private void addAddress() {
        AddressBody addressBody = new AddressBody();
        addressBody.userName = addressUserName.getText().toString();
        addressBody.userMobile = addressMobile.getText().toString();
        addressBody.address = addressDetail.getText().toString();
        addressBody.isDefault = addressIsDefault.isChecked() ? MConstants.ISDEFAULT_ADDRESS_YES : MConstants.ISDEFAULT_ADDRESS_NO;
        userModel.addressAdd(addressBody, new Callback<UserAddress>() {
            @Override
            public void onSuccess(UserAddress userAddress) {
                if(isOrder) {
                    Intent intent = new Intent();
                    intent.putExtra(AddressListActivity.ADDRESS_KEY, userAddress);
                    setResult(RESULT_OK);
                }else{
                    setResult(RESULT_OK);
                }
                finish();
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 编辑收货地址
     */
    private void eidtAddress() {
        AddressBody addressBody = new AddressBody();
        addressBody.userAddressId = userAddress.userAddressId;
        addressBody.userName = addressUserName.getText().toString();
        addressBody.userMobile = addressMobile.getText().toString();
        addressBody.address = addressDetail.getText().toString();
        addressBody.isDefault = addressIsDefault.isChecked() ? MConstants.ISDEFAULT_ADDRESS_YES : MConstants.ISDEFAULT_ADDRESS_NO;
        userModel.addressUpdate(addressBody, new Callback<UserAddress>() {
            @Override
            public void onSuccess(UserAddress userAddress) {
                setResult(RESULT_OK);
                finish();
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
