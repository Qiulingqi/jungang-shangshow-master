package com.shangshow.showlive.controller.hotvideoexcel;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.controller.adapter.HotmannlerAdapter;
import com.shangshow.showlive.controller.member.BunchActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.body.HotMoreListBody;
import com.shangshow.showlive.network.service.models.body.HotMoreListContontBody;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shaojun.widget.superAdapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HotMannler extends BaseActivity {


    private RecyclerView hot_recyclerview;
    private List<HotMoreListContontBody> data = new ArrayList<>();
    private HotmannlerAdapter hotmannlerAdapter;
    private UserModel userModel;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_hot_mannler;
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        userModel = new UserModel(this);
        titleBarView.initCenterTitle("订阅频道");
        //设置字体颜色
        titleBarView.getCenterTitle().setTextColor(getResources().getColor(R.color.a1_color));
        initData();
        initView();
    }

    private void initData() {

        PageBody pageBody = new PageBody();
        pageBody.pageNum = 1;
        pageBody.pageSize = 10;
        userModel.getHotMoreList(pageBody, new Callback<HotMoreListBody>() {
            @Override
            public void onSuccess(HotMoreListBody hotMoreListBody) {
                data.addAll(hotMoreListBody.list);
                hotmannlerAdapter.addAll(hotMoreListBody.list);
                hotmannlerAdapter.notifyDataSetHasChanged();
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        });
    }

    private void initView() {
        hot_recyclerview = (RecyclerView) findViewById(R.id.hot_recyclerview);
        hot_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        hotmannlerAdapter = new HotmannlerAdapter(getBaseContext(), data, R.layout.image_info_layout);
        hot_recyclerview.setAdapter(hotmannlerAdapter);
        hotmannlerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                Intent intent = new Intent(getBaseContext(), BunchActivity.class);
                intent.putExtra("currTitle", data.get(position).pgcVideoTypesId);
                startActivity(intent);
            }
        });
    }
}
