package com.shangshow.showlive.controller.personal.setting;

import android.content.Intent;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.controller.personal.adapter.AddressAdapter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.UserAddress;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.widget.SwipeListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 收货地址列表
 *
 * @author
 */
public class AddressListActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private PtrHTFrameLayout addressPtrFrameLayout;
    private SwipeListView addressRecyclerView;
    private AddressAdapter addressAdapter;
    private boolean checkAddress = false;
    public static final String ADDRESS_KEY = "address_key";

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_personal_address;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        userModel = new UserModel(this);
        checkAddress = getIntent().getBooleanExtra("checkAddress", false);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(getString(R.string.title_address));
        titleBarView.initRight(getString(R.string.add), 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增收货地址
                Intent intent = new Intent(AddressListActivity.this, AddressEditActivity.class);
                startActivityForResult(intent, MConstants.REQUESTCODE_UPDATE_ADDRESS_ADD);

            }
        });

        addressPtrFrameLayout = (PtrHTFrameLayout) findViewById(R.id.address_ptr_framelayout);
        CommonUtil.SetPtrRefreshConfig(this, addressPtrFrameLayout, MConstants.REFRESH_HEADER_WHITE);
        addressPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getAddressList();
            }
        });
        addressRecyclerView = (SwipeListView) findViewById(R.id.address_recyclerView);
        addressRecyclerView.setRightViewWidth(80);
        //分割线
        addressAdapter = new AddressAdapter(addressRecyclerView.getRightViewWidth(), this, R.layout.item_recycler_address, new ArrayList<UserAddress>());
        addressRecyclerView.setAdapter(addressAdapter);
        addressAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void delete(final int position) {
                userModel.addressDelete(addressAdapter.getItem(position).userAddressId + "", new Callback<Object>() {
                    @Override
                    public void onSuccess(Object object) {
                        addressPtrFrameLayout.autoRefresh();
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });
            }

            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                UserAddress userAddress = addressAdapter.getItem(position);
                if(!checkAddress) {
                    //更新收货地址
                    Intent intent = new Intent(AddressListActivity.this, AddressEditActivity.class);
                    intent.putExtra(AddressEditActivity.key_ADDRESS, userAddress);
                    startActivityForResult(intent, MConstants.REQUESTCODE_UPDATE_ADDRESS_UPDATE);
                }else{
                    Intent intent = new Intent();
                    intent.putExtra(ADDRESS_KEY, userAddress);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {
        getAddressList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //新增修改成功后，重新获取收货地址列表
            if (requestCode == MConstants.REQUESTCODE_UPDATE_ADDRESS_ADD) {
                getAddressList();
            } else if (requestCode == MConstants.REQUESTCODE_UPDATE_ADDRESS_UPDATE) {
                getAddressList();
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
                addressAdapter.replaceAll(addresses);
                addressPtrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addressPtrFrameLayout.refreshComplete();
                    }
                }, 300);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                addressPtrFrameLayout.refreshComplete();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }
}
