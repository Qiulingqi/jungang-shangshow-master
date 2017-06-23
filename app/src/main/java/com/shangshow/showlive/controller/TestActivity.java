package com.shangshow.showlive.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.common.widget.ultra.loadmore.LRecyclerView;
import com.shangshow.showlive.common.widget.ultra.loadmore.LoadMoreFooterView;
import com.shangshow.showlive.controller.common.UpLoadFileActivity;
import com.shangshow.showlive.controller.common.picture.SelectPicActivity;
import com.shangshow.showlive.controller.liveshow.LiveAnchorActivity;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.LiveInfo;
import com.shangshow.showlive.network.service.models.OssAuth;
import com.shangshow.showlive.network.service.models.body.StartLiveBody;
import com.shangshow.showlive.network.test.MeiZhi;
import com.shangshow.showlive.network.test.TestApiWrapper;
import com.shaojun.utils.log.Logger;
import com.shaojun.widget.ActionSheet;
import com.shaojun.widget.superAdapter.SuperAdapter;
import com.shaojun.widget.superAdapter.internal.SuperViewHolder;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import rx.Subscription;


public class TestActivity extends BaseActivity implements View.OnClickListener, LRecyclerView.LoadModeDataListener {
    private LRecyclerView lRecyclerView;
    private MySuperAdapter mySuperAdapter;
    private long currPage = 1;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(false);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle("测试");
        titleBarView.initLeft("接口", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionSheet();
            }
        });
        lRecyclerView = (LRecyclerView) findViewById(R.id.test_RecyclerView);


    }


    @Override
    protected void bindEven() {
    }

    @Override
    protected void setView() {
        loadData(MConstants.DATA_4_REFRESH);
    }

    public void setMeiZhiView(int loadType, List<MeiZhi> meiZhis) {
        if (mySuperAdapter == null) {
            lRecyclerView.setLayoutManager(new LinearLayoutManager(TestActivity.this, OrientationHelper.VERTICAL, false));
            lRecyclerView.setLoadModeDataListener(new LRecyclerView.LoadModeDataListener() {
                @Override
                public void onLoadMore() {
                    loadData(MConstants.DATA_4_LOADMORE);
                }

                @Override
                public void onRetry() {
                    loadData(MConstants.DATA_4_LOADMORE);
                }
            });
            mySuperAdapter = new MySuperAdapter(TestActivity.this, meiZhis, R.layout.item_recycler_meizhi);
            lRecyclerView.setAdapter(mySuperAdapter, true);
        } else {
            if (loadType == MConstants.DATA_4_REFRESH) {
                mySuperAdapter.replaceAll(meiZhis);
            } else {
                mySuperAdapter.addAll(meiZhis);
            }
        }

    }

    private void loadData(final int loadType) {
        TestApiWrapper testApiWrapper = new TestApiWrapper();
        Subscription subscription = testApiWrapper.getMeiZhiList(currPage).subscribe(new NewSubscriber<List<MeiZhi>>(TestActivity.this, false) {
            @Override
            public void onNext(List<MeiZhi> meiZhiList) {
                currPage++;
                if (currPage == 6) {
                    lRecyclerView.changeLoadMoreState(LoadMoreFooterView.LOAD_MORE_STATE_NODATA);
                } else {
                    setMeiZhiView(loadType, meiZhiList);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lRecyclerView.changeLoadMoreState(LoadMoreFooterView.LOAD_MORE_STATE_COMPLETE);
                        }
                    }, 500);
                }

            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                if (loadType == MConstants.DATA_4_LOADMORE) {
                    lRecyclerView.changeLoadMoreState(LoadMoreFooterView.LOAD_MORE_STATE_ERROR);
                }
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    public void showActionSheet() {
        setTheme(R.style.ActionSheetStyleiOS7);
        ActionSheet.createBuilder(TestActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消(Cancel)")
                .setOtherButtonTitles("新的测试", "切换显示样式", "弹出框测试", "直播", "", "选择图片上传")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                startActivity(new Intent(TestActivity.this, Test1Activity.class));
                                break;
                            case 1:
                                CustomDialogHelper.TwoButtonDialog(TestActivity.this, "提示", "选择列表的显示方式", "LinearLayout", "Grid", new CustomDialogHelper.OnDialogActionListener() {
                                    @Override
                                    public void doCancelAction() {
                                        lRecyclerView.switchType(MConstants.RECYCLER_GRID);
                                    }

                                    @Override
                                    public void doPositiveAction() {
                                        lRecyclerView.switchType(MConstants.RECYCLER_LINEAR);
                                    }
                                });
                                break;
                            case 2:
                                CustomDialogHelper.OneButtonDialog(TestActivity.this, "提示", "一个button的dialog\\点击打开2个button的dialog", "打开", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CustomDialogHelper.TwoButtonDialog(TestActivity.this, "提示", "选择打开", "再打开一个2个button的Dialog", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CustomDialogHelper.TwoButtonDialog(TestActivity.this, "提示", "这次选择打开", "customView", "OneButtonDialog", new CustomDialogHelper.OnDialogActionListener() {
                                                    @Override
                                                    public void doCancelAction() {
                                                        CustomDialogHelper.OneButtonDialog(TestActivity.this, "提示", "还是一个button的dialog", "关闭", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                showToast("关闭");
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void doPositiveAction() {
                                                        View view = LayoutInflater.from(TestActivity.this).inflate(R.layout.layout_logic_user_info, null);
                                                        CustomDialogHelper.CustomViewDialog(TestActivity.this, view, "确定", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                showToast("关闭");
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }, "取消", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showToast("取消");

                                            }
                                        });
                                    }
                                });

                                break;

                            case 3:
                                startLive(0);
                                break;
                            case 4:
//                                ossAuth();
                                break;
                            case 5:
                                choiceImage();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 开启直播
     */
    private void startLive(int isCommonweal) {
        ApiWrapper apiWrapper = new ApiWrapper();
        StartLiveBody startLiveBody = new StartLiveBody();
        startLiveBody.name = "我是新建直播";
        startLiveBody.logoUrl = "http://c.hiphotos.baidu.com/image/pic/item/3ac79f3df8dcd10078ef3228708b4710b9122f4c.jpg";
        startLiveBody.isCommonweal = isCommonweal;
        Subscription subscription = apiWrapper.startLive(startLiveBody).subscribe(new NewSubscriber<LiveInfo>(TestActivity.this, true) {
            @Override
            public void onNext(LiveInfo liveInfo) {
                LiveAnchorActivity.start(TestActivity.this, liveInfo);
            }
        });
        addSubscrebe(subscription);
    }

    /**
     * 上传文件认证
     */
    private void ossAuth(final String filePath) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.ossAuth(MConstants.UPLOAD_TYPE_AVATAR).subscribe(new NewSubscriber<OssAuth>(TestActivity.this, true) {
            @Override
            public void onNext(OssAuth ossAuth) {
                Intent intent = new Intent(TestActivity.this, UpLoadFileActivity.class);
                intent.putExtra(UpLoadFileActivity.KEY_OSSAUTH, ossAuth);
                intent.putExtra(UpLoadFileActivity.KEY_FILEPATH, filePath);
                intent.putExtra(UpLoadFileActivity.KEY_SHOW_UPLOAD_PROGRESS, false);
                startActivityForResult(intent, MConstants.REQUESTCODE_UPLOAD);
            }
        });
        addSubscrebe(subscription);
    }

    private void choiceImage() {
        CommonUtil.choicePicture(TestActivity.this, SelectPicActivity.class, 1);
    }

    @Override
    public void onLoadMore() {
        ToastUtils.show("onLoadMore");
        loadData(MConstants.DATA_4_LOADMORE);
    }

    @Override
    public void onRetry() {

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
                        String imagePath = CommonUtil.getRealPathFromUri(TestActivity.this, uri);
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
                    String url = data.getStringExtra(UpLoadFileActivity.KEY_RESULT_URL);
                    Logger.i("上传成功！图片地址：" + url);
                    showToast(url);
                }
            }
        }
    }

    private class MySuperAdapter extends SuperAdapter<MeiZhi> {
        public MySuperAdapter(Context context, List<MeiZhi> datas, int layoutResId) {
            super(context, datas, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, MeiZhi item) {
            ImageView imageView = holder.findViewById(R.id.iv_meizhi);
            TextView title = holder.findViewById(R.id.tv_title);
            ImageLoaderKit.getInstance().displayImage(item.url, imageView);
            title.setText(item.desc);
        }

        @Override
        public void noHolder(View convertView, int layoutPosition, MeiZhi item) {

        }
    }
}
