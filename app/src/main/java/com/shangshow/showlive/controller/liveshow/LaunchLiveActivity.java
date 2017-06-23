package com.shangshow.showlive.controller.liveshow;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.aliyun.Oss;
import com.shangshow.showlive.aliyun.OssUploadService;
import com.shangshow.showlive.aliyun.UIDisplayer;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.permission.MPermission;
import com.shangshow.showlive.common.permission.MPermissionUtil;
import com.shangshow.showlive.common.permission.annotation.OnMPermissionDenied;
import com.shangshow.showlive.common.permission.annotation.OnMPermissionGranted;
import com.shangshow.showlive.common.permission.annotation.OnMPermissionNeverAskAgain;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.XmlDB;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.controller.common.UpLoadFileActivity;
import com.shangshow.showlive.controller.common.picture.SelectPicActivity;
import com.shangshow.showlive.model.LiveModel;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.http.Response;
import com.shangshow.showlive.network.service.models.LiveInfo;
import com.shangshow.showlive.network.service.models.OssAuth;
import com.shangshow.showlive.network.service.models.body.StartLiveBody;
import com.shangshow.showlive.network.service.models.requestJson.ApplyVideo;
import com.shaojun.utils.log.Logger;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.shangshow.showlive.common.utils.XmlDB.getObject4XmlDB;


// 开始直播

public class LaunchLiveActivity extends BaseActivity implements View.OnClickListener {

    /***********************
     * 录音摄像头权限申请
     *******************************/

    // 权限控制
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE};

    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    private LiveModel liveModel;
    private UserModel userModel;
    private RelativeLayout liveLogoLayout;
    private ImageView liveLogo;
    //    private VideoView videoView;
    private TextView liveLogoUploadTypeText;
    private ClearableEditTextWithIcon liveTitle;
    private BaseButton launchLiveBtn;//发起直播
    private BaseButton launchWelfareLiveBtn;//发起公益直播
    private ProgressBar progressBar;
    //上传头像返回
    private String logoUrl;
    private int uploadType;
    private String uploadPath;
    private File file = null;
    private String videoPath;
    private View ll_all_async;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_live_launch;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
        liveModel = new LiveModel(this);
        userModel = new UserModel(this);

    }
    @Override
    protected void initWidget() {
        super.initWidget();
        uploadType = getIntent().getIntExtra("upload_type", -1);
        uploadPath = getIntent().getStringExtra("uploadPath");
        if (uploadType == 0) {
            titleBarView.initCenterTitle(R.string.launch_live);
        } else if (uploadType == 1) {
            titleBarView.initCenterTitle(R.string.upload_video);
        }
        liveLogoLayout = (RelativeLayout) findViewById(R.id.launch_live_logo_layout);
        liveLogoUploadTypeText = (TextView) findViewById(R.id.launch_live_upload_type);
        liveLogo = (ImageView) findViewById(R.id.launch_live_logo);
        //用picasso加载用户头像作为开播图片
        UserInfo object4XmlDB = getObject4XmlDB(MConstants.KEY_USER, UserInfo.class);
        Picasso.with(context).load(object4XmlDB.avatarUrl).into(liveLogo);
//      videoView = (VideoView) findViewById(R.id.launch_live_video);
        liveTitle = (ClearableEditTextWithIcon) findViewById(R.id.launch_live_title);
        launchLiveBtn = (BaseButton) findViewById(R.id.launch_live_btn);
        launchWelfareLiveBtn = (BaseButton) findViewById(R.id.launch_welfare_live_btn);
        progressBar = (ProgressBar) findViewById(R.id.upload_progressbar);
        ll_all_async = findViewById(R.id.ll_all_async);
        if (uploadType == 0) {
            liveLogoUploadTypeText.setText(R.string.change_header_image);
            launchLiveBtn.setText(R.string.launch_live);
            liveLogo.setVisibility(View.VISIBLE);
            ll_all_async.setVisibility(View.GONE);
//            videoView.setVisibility(View.GONE);
        } else if (uploadType == 1) {
            liveLogoUploadTypeText.setText(R.string.upload_image);
            launchLiveBtn.setText(R.string.submitted_for_review);
            liveLogo.setVisibility(View.GONE);
//            videoView.setVisibility(View.VISIBLE);
             /* 获取MediaController对象，控制媒体播放 */
            MediaController mc = new MediaController(this);
            mc.hide();
//            videoView.setMediaController(mc);
        }

    }

//    private void play() {
//        Uri uri = Uri.parse(uploadPath);
//        if (uri != null) {
//            videoView.setVideoURI(Uri.parse(uploadPath));
//            videoView.start();/*  请求获取焦点 */
////                            videoView.pause();
//            videoView.requestFocus();
//        }
//    }

    @Override
    protected void bindEven() {
        launchLiveBtn.setOnClickListener(this);
        launchWelfareLiveBtn.setOnClickListener(this);
        liveLogoLayout.setOnClickListener(this);
    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.launch_live_logo_layout:
                if (uploadType == 0) {
//                    CommonUtil.choiceFileByType(LaunchLiveActivity.this, SelectPicActivity.class, 0,2);
                    CommonUtil.choicePicture(LaunchLiveActivity.this, SelectPicActivity.class);
                } else if (uploadType == 1) {
//                    CommonUtil.choiceFileByType(LaunchLiveActivity.this, SelectPicActivity.class, 1,2);
                    CommonUtil.choicePicture(LaunchLiveActivity.this, SelectPicActivity.class);
                }

                break;
            case R.id.launch_live_btn:
                //判断当前用户类型普通用户不让直播
                if (uploadType == 0) {
                    startLive(0);
                } else if (uploadType == 1) {
                    String title = liveTitle.getText().toString();
                    if (TextUtils.isEmpty(title)) {
                        showToast("请输入视频的标题");
                        return;
                    }
                    if (TextUtils.isEmpty(logoUrl)) {
                        showToast("请上传视频封面");
                        return;
                    }
                    File file = new File(uploadPath);
                    if (!file.exists()) {
                        showToast("您上传的视频找不到路径");
                        return;
                    }
                    ossAuth(2, MConstants.UPLOAD_TYPE_VIDEO, file.getPath(), false);
                }
                break;
            case R.id.launch_welfare_live_btn:
                // 判断当前用户类型普通用户不让直播
                startLive(1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MConstants.PHOTO_REQUEST_SELECT || requestCode == MConstants.FILE_REQUEST_SELECT) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    if (uploadType == 0) {
                        if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_CONTENT)) {
                            String imagePath = CommonUtil.getRealPathFromUri(LaunchLiveActivity.this, uri);
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
                            ossAuth(1, MConstants.UPLOAD_TYPE_AVATAR, file.getPath(), false);
                        }
                    } else if (uploadType == 1) {
                        try {
                            if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_CONTENT)) {
                                String imagePath = CommonUtil.getRealPathFromUri(LaunchLiveActivity.this, uri);
                                file = new File(imagePath);
                            } else if (uri.toString().contains(MConstants.MEDIAFROM_TYPE_FILE)) {
                                try {
                                    file = new File(new URI(uri.toString()));
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (file != null) {
                                ossAuth(1, MConstants.UPLOAD_TYPE_COVER, file.getPath(), false);
                            }
//                            liveLogo.setImageBitmap(ImageUtil.getInstance().autoResizeFromLocalFile(file.getAbsolutePath(), liveLogo));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                } else {
                    Logger.e("拍照或选择的文件生成为空，可能出现异常");
                }

            } else if (requestCode == MConstants.REQUESTCODE_UPLOAD) {
                if (data.hasExtra(UpLoadFileActivity.KEY_RESULT_URL)) {
                    logoUrl = data.getStringExtra(UpLoadFileActivity.KEY_RESULT_URL);
                    ImageLoaderKit.getInstance().displayImage(logoUrl, liveLogo);
                    liveLogo.setVisibility(View.VISIBLE);
//                    Logger.i("上传成功！图片地址：" + logoUrl);
                    //上传成功更新用户信息
                }
            }
        }
    }

    /**
     * 开启直播
     */
    private void startLive(int isCommonweal) {
        UserInfo user=XmlDB.getObject4XmlDB(MConstants.KEY_USER, UserInfo.class);
        StartLiveBody startLiveBody = new StartLiveBody();
        startLiveBody.name = liveTitle.getText().toString();
        startLiveBody.logoUrl = TextUtils.isEmpty(logoUrl) ? user.avatarUrl : logoUrl;
        startLiveBody.isCommonweal = isCommonweal;
        startLiveBody.videoType = "LIVE";
        if (TextUtils.isEmpty(startLiveBody.name)) {
            showToast("请输入标题");
            return;
        }
        liveModel.startLive(startLiveBody, new Callback<LiveInfo>() {
            @Override
            public void onSuccess(LiveInfo liveInfo) {
                LiveAnchorActivity.start(LaunchLiveActivity.this, liveInfo);
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 上传头像-上传视频
     */
    public void ossAuth(final int type, String mediaType, final String filePath, final boolean isNeedProgress) {
        userModel.ossAuth(mediaType, new Callback<OssAuth>() {
            @Override
            public void onSuccess(final OssAuth ossAuth) {
                if (type == 1) {
                    //调用上传
                    Intent intent = new Intent(LaunchLiveActivity.this, UpLoadFileActivity.class);
                    intent.putExtra(UpLoadFileActivity.KEY_OSSAUTH, ossAuth);
                    intent.putExtra(UpLoadFileActivity.KEY_FILEPATH, filePath);
                    intent.putExtra(UpLoadFileActivity.KEY_SHOW_UPLOAD_PROGRESS, isNeedProgress);
                    startActivityForResult(intent, MConstants.REQUESTCODE_UPLOAD);
                } else if (type == 2) {
                    OssUploadService ossService = Oss.initOSS(LaunchLiveActivity.this, ossAuth, new UIDisplayer(null, progressBar, new TextView(getApplicationContext()), LaunchLiveActivity.this));
                    final String objectKey = ossAuth.filePath + "/" + CommonUtil.getUUIDMediaName(MConstants.UPLOAD_FILE_TYPE_VIDEO);
                    ossService.setUploadListener(new OssUploadService.UploadListener() {
                        @Override
                        public void onUploadSuccess(PutObjectRequest request, PutObjectResult result) {
                            videoPath = ossAuth.domainUrl + "/" + objectKey;
                            ApplyVideo applyVideo = new ApplyVideo();
                            applyVideo.name = "" + liveTitle.getText().toString();
                            applyVideo.logoUrl = logoUrl;
                            applyVideo.videoUrl = videoPath;
                            userModel.applyVideo(applyVideo, new Callback<Response<Object>>() {
                                @Override
                                public void onSuccess(Response<Object> objectResponse) {
                                    CustomDialogHelper.OneButtonDialog(LaunchLiveActivity.this, "提示", getString(R.string.dialog_upload_complete_context), "关闭", new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(int resultCode, String message) {
                                    showToast("" + message);
                                }
                            });
                        }

                        @Override
                        public void onUploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {

                        }

                        @Override
                        public void onUploadProgress(PutObjectRequest request, long currentSize, long totalSize) {
                            int progress = 100 - (int) ((1 - (float) ((float) currentSize / (float) totalSize)) * 100);
                            progressBar.setProgress(progress);
                        }
                    });
                    final OSSAsyncTask task = ossService.asyncPutFile(objectKey, filePath);
                    closeListener = new CloseListener() {
                        @Override
                        public void close() {
                            task.cancel();
                        }
                    };
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        videoView.pause();
//        videoView.clearFocus();
//        videoView.stopPlayback();
        if (closeListener != null) {
            closeListener.close();
        }
        liveModel.unSubscribe();
    }

    private void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(LIVE_PERMISSION_REQUEST_CODE)
                .permissions(LIVE_PERMISSIONS)
                .request();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        play();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {

    }

    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
//        Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, LIVE_PERMISSIONS);
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

//        Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
    }

    private CloseListener closeListener;

    public interface CloseListener {
        void close();
    }


}
