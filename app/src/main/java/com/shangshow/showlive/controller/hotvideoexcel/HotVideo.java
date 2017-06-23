package com.shangshow.showlive.controller.hotvideoexcel;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.controller.adapter.HotVideoAdapter;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.body.YoutubeListBody;
import com.shangshow.showlive.network.service.models.body.YoutubeListContontBody;
import com.shaojun.widget.superAdapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HotVideo extends BaseActivity {


    private UserModel userModel;
    private List<YoutubeListContontBody> data = new ArrayList<>();
    private String type;
    private HotVideoAdapter hotVideoAdapter;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_hot_video;
    }

    @Override
    protected void bindEven() {

    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initWidget() {
        userModel = new UserModel(this);
        super.initWidget();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        titleBarView.initCenterTitle("视频列表");
        titleBarView.getCenterTitle().setTextColor(getResources().getColor(R.color.a1_color));
        initData();
        initView();
    }

    private void initView() {

        RecyclerView hot_list_recyclerview = (RecyclerView) findViewById(R.id.hot_list_recyclerview);
        hot_list_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        hotVideoAdapter = new HotVideoAdapter(getBaseContext(), data, R.layout.hot_video_contont_layout);
        hot_list_recyclerview.setAdapter(hotVideoAdapter);
        hotVideoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {


            }
        });

    }

    private void initData() {

        userModel.getYoutubeList(type, new Callback<YoutubeListBody>() {
            @Override
            public void onSuccess(YoutubeListBody youtubeListBody) {
                data.addAll(youtubeListBody.list);
                hotVideoAdapter.addAll(youtubeListBody.list);
                hotVideoAdapter.notifyDataSetHasChanged();
            }
            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }
}
