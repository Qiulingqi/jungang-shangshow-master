package com.shangshow.showlive.controller.liveshow.live;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.netease.LSMediaCapture.lsMediaCapture;
import com.netease.LSMediaCapture.lsMessageHandler;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.shangshow.showlive.controller.liveshow.widget.MixAudioDialog;
import com.shangshow.showlive.controller.liveshow.widget.NetWorkInfoDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImageFaceFilter;

public class LivePlayer implements lsMessageHandler {

    public final static String STREAM_START = "MSG_START_LIVESTREAMING_FINISHED";
    public final static String STREAM_FINISH = "MSG_STOP_LIVESTREAMING_FINISHED";
    public static final int LS_LOG_QUIET = 0x00;            //!< log输出模式：不输出
    public static final int LS_LOG_ERROR = 1 << 0;          //!< log输出模式：输出错误
    public static final int LS_LOG_WARNING = 1 << 1;          //!< log输出模式：输出警告
    public static final int LS_LOG_INFO = 1 << 2;          //!< log输出模式：输出信息
    public static final int LS_LOG_DEBUG = 1 << 3;          //!< log输出模式：输出调试信息
    public static final int LS_LOG_DETAIL = 1 << 4;          //!< log输出模式：输出详细
    public static final int LS_LOG_RESV = 1 << 5;          //!< log输出模式：保留
    public static final int LS_LOG_LEVEL_COUNT = 6;
    public static final int LS_LOG_DEFAULT = LS_LOG_WARNING;    //!< log输出模式：默认输出警告
    // 音视频
    public static final int HAVE_AUDIO = 0;
    public static final int HAVE_VIDEO = 1;
    public static final int HAVE_AV = 2;
    // 协议
    public static final int FLV = 0;
    public static final int RTMP = 1;
    // 前后置摄像头
    public static final int CAMERA_POSITION_BACK = 0;
    public static final int CAMERA_POSITION_FRONT = 1;
    // 横竖屏
    public static final int CAMERA_ORIENTATION_PORTRAIT = 0;
    public static final int CAMERA_ORIENTATION_LANDSCAPE = 1;
    // 语音编码
    public static final int LS_AUDIO_CODEC_AAC = 0;
    public static final int LS_AUDIO_CODEC_SPEEX = 1;
    public static final int LS_AUDIO_CODEC_MP3 = 2;
    public static final int LS_AUDIO_CODEC_G711A = 3;
    public static final int LS_AUDIO_CODEC_G711U = 4;
    // 视频编码
    public static final int LS_VIDEO_CODEC_AVC = 0;
    public static final int LS_VIDEO_CODEC_VP9 = 1;
    public static final int LS_VIDEO_CODEC_H265 = 2;
    //网络QoS开关
    public static final int OpenQoS = 0;
    public static final int CloseQoS = 1;
    /*************************
     * 伴音监听广播
     *********************/
    //伴音相关宏定义：
    //AUDIO_ROUTE_EARPIECE：有线耳机模式
    //AUDIO_ROUTE_LOUDERSPEAKER：外放模式
    //AUDIO_ROUTE_BLUETOOTH：蓝牙耳机模式
    public static final int AUDIO_ROUTE_EARPIECE = 0;
    public static final int AUDIO_ROUTE_LOUDERSPEAKER = 1;
    public static final int AUDIO_ROUTE_BLUETOOTH = 2;
    private final String TAG = "LivePlayer_TAG";
    /**
     * ******************************** Activity 生命周期直播状态控制 ********************************
     */
    boolean isActivityPause = false;
    private ActivityProxy activityProxy;
    private SurfaceView liveView;
    private Intent mAlertServiceIntent;
    private boolean live = false; // 是否已经开始推流（断网重连用），推流没有暂停
    private boolean isManualPause = false; // 是否手动暂停
    private boolean isNeedStartAV = true; // 第一次开始播放，或者手动暂停之后重新播放
    // 视频采集器
    private lsMediaCapture mLSMediaCapture; // 直播实例
    private lsMediaCapture.LSLiveStreamingParaCtx mLSLiveStreamingParaCtx;
    // 基本配置
    private String mliveStreamingURL; // 推流地址
    private int mVideoEncodeWidth, mVideoEncodeHeight; // 推流分辨率
    private String mLogPath = null; //直播日志路径
    private int mLogLevel = LS_LOG_ERROR;
    // 状态控制
    private boolean m_liveStreamingInitFinished = false;
    private boolean m_liveStreamingOn = false;
    private boolean m_liveStreamingPause = false;
    private boolean m_tryToStopLiveStreaming = false;
    private boolean m_liveStreamingPreviewOn = false;
    private boolean mIsFilterMode = false;
    //视频水印相关变量
    private Bitmap mBitmap;
    private boolean mWaterMarkOn = true;//视频水印开关，默认关闭，需要视频水印的用户可以开启此开关
    private String mWaterMarkFilePath;//视频水印文件路径
    private String mWaterMarkFileName = "ic_water_mark.png";//视频水印文件名
    private File mWaterMarkAppFileDirectory = null;
    private int mWaterMarkPosX = 0;//视频水印坐标(X)
    private int mWaterMarkPosY = 30;//视频水印坐标(Y)
    //伴音相关
    private String mMixAudioFilePath;
    private int mRouteMode = AUDIO_ROUTE_LOUDERSPEAKER;
    private int mixAudioVolume = 10; // 伴音强度范围【1-10】
    private AudioManager mAudioManager;
    private AudioMixVolumeMsgReceiver mAudioReceiver;
    /**
     * ******************************** lsMessageHandler ********************************
     */

    private boolean m_liveStreamingInit = false;
    private boolean m_tryToStopLivestreaming = false;
    private boolean mAlertServiceOn = false;
    private long mLastVideoProcessErrorAlertTime = 0;
    private long mLastAudioProcessErrorAlertTime = 0;


    public LivePlayer(SurfaceView liveView, String url, ActivityProxy proxy, boolean isFilterMode) {
        this.liveView = liveView;
        this.mliveStreamingURL = url;
        this.activityProxy = proxy;
        this.mIsFilterMode = isFilterMode;
    }


    public LivePlayer(String url, ActivityProxy proxy) {
        this.mliveStreamingURL = url;
        this.activityProxy = proxy;
    }

    public void onActivityResume() {
        isActivityPause = false;
        if (mLSMediaCapture != null) {
            //关闭推流固定图像
            if (!mIsFilterMode) {
                mLSMediaCapture.resumeVideoEncode();
            }

            //关闭推流静音帧
            //mLSMediaCapture.stopAudioEncode();
        }
    }

    public void onActivityPause(boolean tryToStopLiveStreaming) {
        isActivityPause = true;
        m_tryToStopLiveStreaming = tryToStopLiveStreaming;
        if (mLSMediaCapture != null) {
            //关闭视频Preview
//            mLSMediaCapture.stopVideoPreview();

            if (m_tryToStopLiveStreaming) {
                //m_liveStreamingOn = false;
            } else if (!mIsFilterMode) {
                //继续视频推流，推固定图像
                mLSMediaCapture.backgroundVideoEncode();

                //释放音频采集资源
                //mLSMediaCapture.stopAudioRecord();
            }
        }
    }

    /**
     * ******************************** 初始化 ********************************
     */

    // 设置推流参数
    private void initLiveParam() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏

        m_liveStreamingOn = false;
        m_liveStreamingPause = false;
        m_tryToStopLivestreaming = false;

        // 推流URL和分辨率信息
        mVideoEncodeWidth = 320;
        //   mVideoEncodeHeight = 480;
        mVideoEncodeHeight = 240;

        //创建直播实例
        mLSMediaCapture = new lsMediaCapture(this, getActivity(), mVideoEncodeWidth, mVideoEncodeHeight, mIsFilterMode);

        //2、配置视频预览参数
        if (mIsFilterMode) {
            ((OpenGLSurfaceView) liveView   ).setPreviewSize(mVideoEncodeWidth, mVideoEncodeHeight);
        } else {
            ((LiveSurfaceView) liveView).setPreviewSize(mVideoEncodeWidth, mVideoEncodeHeight);
        }


        //创建参数实例
        mLSLiveStreamingParaCtx = mLSMediaCapture.new LSLiveStreamingParaCtx();
        mLSLiveStreamingParaCtx.eHaraWareEncType = mLSLiveStreamingParaCtx.new HardWareEncEnable();
        mLSLiveStreamingParaCtx.eOutFormatType = mLSLiveStreamingParaCtx.new OutputFormatType();
        mLSLiveStreamingParaCtx.eOutStreamType = mLSLiveStreamingParaCtx.new OutputStreamType();
        mLSLiveStreamingParaCtx.sLSAudioParaCtx = mLSLiveStreamingParaCtx.new LSAudioParaCtx();
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.codec = mLSLiveStreamingParaCtx.sLSAudioParaCtx.new LSAudioCodecType();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx = mLSLiveStreamingParaCtx.new LSVideoParaCtx();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new LSVideoCodecType();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new CameraPosition();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.interfaceOrientation = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new CameraOrientation();
        mLSLiveStreamingParaCtx.sLSQoSParaCtx = mLSLiveStreamingParaCtx.new LSQoSParaCtx();

        //配置音视频和camera参数
        configLiveStream();
    }

    private void configLiveStream() {
        //输出格式：视频、音频和音视频
        mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType = HAVE_AV;

        //输出封装格式
        mLSLiveStreamingParaCtx.eOutFormatType.outputFormatType = RTMP;

        //摄像头参数配置
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition = CAMERA_POSITION_FRONT;//后置摄像头
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.interfaceOrientation.interfaceOrientation = CAMERA_ORIENTATION_PORTRAIT;//竖屏

        //音频编码参数配置
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.samplerate = 44100;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.bitrate = 64000;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.frameSize = 2048;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.channelConfig = AudioFormat.CHANNEL_IN_MONO;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.codec.audioCODECType = LS_AUDIO_CODEC_AAC;

        //网络QoS开关
        mLSLiveStreamingParaCtx.sLSQoSParaCtx.qosType = OpenQoS;//默认打开QoS

        //硬件编码参数设置
        mLSLiveStreamingParaCtx.eHaraWareEncType.hardWareEncEnable = false;


        //滤镜模式下不开视频水印
        if (mWaterMarkOn) {
            waterMark();
        }

        //视频编码参数配置
//        if(mVideoResolution.equals("HD")) {
//            mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 20;
//            mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 800000;
//            mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
//            mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 800;
//            mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 600;
//        }
//        else if(mVideoResolution.equals("SD")) {
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 20;
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 600000;
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = mVideoEncodeWidth;
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = mVideoEncodeHeight;
//        }
//        else {
//        mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 15;
//        mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 250000;
//        mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
//        mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 320;
//        mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 240;
//        }

        //设置日志级别和日志文件路径
        getLogPath();

        if (mLSMediaCapture != null) {
            if (mIsFilterMode) {
                mLSMediaCapture.startVideoPreviewOpenGL((OpenGLSurfaceView) liveView, mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition);
                mLSMediaCapture.setFilterType(new GPUImageFaceFilter());
            } else {
                mLSMediaCapture.startVideoPreview((LiveSurfaceView) liveView, mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition);
            }

            m_liveStreamingPreviewOn = true;

            //初始化直播推流
            m_liveStreamingInitFinished = mLSMediaCapture.initLiveStream(mliveStreamingURL, mLSLiveStreamingParaCtx);
            m_liveStreamingInit = true;
        }
    }

    //获取日志文件路径
    public void getLogPath() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mLogPath = getActivity().getApplicationContext().getExternalFilesDir(null) + "/log/";
            }
            if (mLSMediaCapture != null) {
                mLSMediaCapture.setTraceLevel(mLogLevel, mLogPath);
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
    }

    //视频水印相关方法(1)
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    //视频水印相关方法(2)
    public void waterMark() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mWaterMarkAppFileDirectory = getActivity().getExternalFilesDir(null);
        } else {
            mWaterMarkAppFileDirectory = getActivity().getFilesDir();
        }

        AssetManager assetManager = getActivity().getAssets();

        //拷贝水印文件到APP目录
        String[] files = null;
        File fileDirectory;

        try {
            files = assetManager.list("waterMark");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        if (mWaterMarkAppFileDirectory != null) {
            fileDirectory = mWaterMarkAppFileDirectory;
        } else {
            fileDirectory = Environment.getExternalStorageDirectory();
            mWaterMarkFilePath = fileDirectory + "/" + mWaterMarkFileName;
        }

        for (String filename : files) {
            try {
                InputStream in = assetManager.open("waterMark/" + filename);
                File outFile = new File(fileDirectory, filename);
                FileOutputStream out = new FileOutputStream(outFile);
                copyFile(in, out);
                mWaterMarkFilePath = outFile.toString();
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file", e);
            }
        }
    }

    /**
     * ******************************** 直播控制 ********************************
     */

    public void initMixAudio(String path) {
        mMixAudioFilePath = path;
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MixAudioDialog.AUDIO_MIX_ACTION);
        filter.addAction(MixAudioDialog.AUDIO_MIX_VOLUME_ACTION);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        mAudioReceiver = new AudioMixVolumeMsgReceiver();
        getActivity().registerReceiver(mAudioReceiver, filter);
    }

    public void unInitMixAudio() {
        if (mAudioReceiver != null) {
            getActivity().unregisterReceiver(mAudioReceiver);
            mAudioReceiver = null;
        }
    }

    /**
     * 返回是否成功开启
     *
     * @return
     */
    public boolean startStopLive() {
        if (!m_liveStreamingOn) {
            if (!m_liveStreamingPause) {
                if (mliveStreamingURL.isEmpty()) {
                    return false;
                }

                initLiveParam();
                // 如果音视频直播或者视频直播，需要等待preview finish之后才能开始直播；如果音频直播，则无需等待preview finish，可以立即开始直播
                if (mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType == HAVE_AUDIO) {
                    startAV();
                }
                live = true;
                return true;
            }
        }

        return true;
    }

    //开始直播
    private void startAV() {
        if (mLSMediaCapture != null) {
            // 水印
            if (mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType == HAVE_AV || mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType == HAVE_VIDEO) {
                mWaterMarkPosX = mVideoEncodeHeight - 130;
                mLSMediaCapture.setWaterMarkPara(mWaterMarkOn, mWaterMarkFilePath, mWaterMarkPosX, mWaterMarkPosY);
            }
            mLSMediaCapture.startLiveStreaming();
            m_liveStreamingOn = true;
            m_liveStreamingPause = false;
        }
    }

    //切换前后摄像头
    public void switchCamera() {
        if (mLSMediaCapture != null) {
            mLSMediaCapture.switchCamera();
        }
    }

    public void setFilterStrength(int progress) {
        if (mLSMediaCapture != null) {
            mLSMediaCapture.setFilterStrength(progress);
        }
    }

    /**
     * 重启直播（例如：断网重连）
     *
     * @return 是否开始重启
     */
    public boolean restartLive() {
        if (live) {
            // 必须是已经开始推流 才需要处理断网重新开始直播
            LogUtil.i(TAG, "restart live on connected");
            if (mLSMediaCapture != null) {
                mLSMediaCapture.initLiveStream(mliveStreamingURL, mLSLiveStreamingParaCtx);
                mLSMediaCapture.startLiveStreaming();
                m_tryToStopLivestreaming = false;
                m_liveStreamingOn = true;
                return true;
            }
        }

        return false;
    }

    /**
     * 停止直播（例如：断网了）
     */
    public void stopLive() {
        m_tryToStopLivestreaming = true;
        if (mLSMediaCapture != null) {
            mLSMediaCapture.stopLiveStreaming();
            m_liveStreamingOn = false;
        }
    }

    public void tryStop() {
        m_tryToStopLiveStreaming = true;
    }

    public void resetLive() {
        if (mLSMediaCapture != null && m_liveStreamingInitFinished && m_liveStreamingPreviewOn && m_liveStreamingOn) {
            Log.i(TAG, "test: in resetLive, 1");
            mLSMediaCapture.stopLiveStreaming();
            mLSMediaCapture.stopVideoPreview();
            mLSMediaCapture.destroyVideoPreview();
            //反初始化推流实例
            mLSMediaCapture.uninitLsMediaCapture(false);
            mLSMediaCapture = null;

            m_liveStreamingInitFinished = false;
            m_liveStreamingOn = false;
            m_liveStreamingPause = false;
            m_tryToStopLiveStreaming = false;
            m_liveStreamingPreviewOn = false;
            Log.i(TAG, "test: in resetLive, 2");
        } else if (mLSMediaCapture != null && m_liveStreamingInitFinished && m_liveStreamingPreviewOn) {

            Log.i(TAG, "test: in resetLive, 3");
            mLSMediaCapture.stopVideoPreview();
            mLSMediaCapture.destroyVideoPreview();
            //反初始化推流实例
            mLSMediaCapture.uninitLsMediaCapture(true);
            mLSMediaCapture = null;

            m_liveStreamingInitFinished = false;
            m_liveStreamingOn = false;
            m_liveStreamingPause = false;
            m_tryToStopLiveStreaming = false;
            Log.i(TAG, "test: in resetLive, 4");
        } else if (mLSMediaCapture != null && !m_liveStreamingInitFinished) {
            if (m_liveStreamingPreviewOn) {
                mLSMediaCapture.stopVideoPreview();
                mLSMediaCapture.destroyVideoPreview();
                m_liveStreamingPreviewOn = false;
            }
            //反初始化推流实例
            mLSMediaCapture.uninitLsMediaCapture(true);
        }

        if (m_liveStreamingInit) {
            m_liveStreamingInit = false;
        }

        if (mAlertServiceOn) {
            mAlertServiceIntent = new Intent(getActivity(), AlertService.class);
            getActivity().stopService(mAlertServiceIntent);
            mAlertServiceOn = false;
        }

        if (isManualPause) {
            activityProxy.onFinished();
        }
    }

    public boolean isManualPause() {
        return isManualPause;
    }

    public void setManualPause(boolean manualPause) {
        isManualPause = manualPause;
        isNeedStartAV = true;
    }

    //处理SDK抛上来的异常和事件
    @Override
    public void handleMessage(int msg, Object object) {
        final Context context = getActivity();
        switch (msg) {
            case MSG_INIT_LIVESTREAMING_OUTFILE_ERROR:
            case MSG_INIT_LIVESTREAMING_VIDEO_ERROR:
            case MSG_INIT_LIVESTREAMING_AUDIO_ERROR: {
                Log.i(TAG, "test: in handleMessage, MSG_INIT_LIVESTREAMING_AUDIO_ERROR");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingInit) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_INIT_LIVESTREAMING_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                }
                Toast.makeText(context, "初始化推流失败", Toast.LENGTH_SHORT).show();
                activityProxy.onInitFailed();
            }
            case MSG_START_PREVIEW_ERROR://视频预览出错，可能是获取不到camera的使用权限
            {
                Log.i(TAG, "test: in handleMessage, MSG_START_PREVIEW_ERROR");
                Toast.makeText(context, "打开摄像头失败", Toast.LENGTH_SHORT).show();
                activityProxy.onInitFailed();
                break;
            }
            case MSG_AUDIO_RECORD_ERROR://音频采集出错，获取不到麦克风的使用权限
            {
                Log.i(TAG, "test: in handleMessage, MSG_AUDIO_RECORD_ERROR");
                Toast.makeText(context, "打开录音失败", Toast.LENGTH_SHORT).show();
                activityProxy.onInitFailed();
                break;
            }
            case MSG_START_LIVESTREAMING_ERROR:
                Log.i(TAG, "test: in handleMessage, MSG_START_LIVESTREAMING_ERROR");
            case MSG_AUDIO_PROCESS_ERROR: {
                Log.i(TAG, "test: in handleMessage, MSG_AUDIO_PROCESS_ERROR");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingOn && System.currentTimeMillis() - mLastAudioProcessErrorAlertTime >= 10000) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_AUDIO_PROCESS_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                    mLastAudioProcessErrorAlertTime = System.currentTimeMillis();
                }

            }
            case MSG_VIDEO_PROCESS_ERROR: {
                Log.i(TAG, "test: in handleMessage, MSG_VIDEO_PROCESS_ERROR");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingOn && System.currentTimeMillis() - mLastVideoProcessErrorAlertTime >= 10000) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_VIDEO_PROCESS_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                    mLastVideoProcessErrorAlertTime = System.currentTimeMillis();
                }
            }
            case MSG_SEND_STATICS_LOG_ERROR: {
                Log.i(TAG, "test: in handleMessage, MSG_SEND_STATICS_LOG_ERROR");
            }
            case MSG_GET_STATICS_INFO://获取统计信息的反馈消息
            {
                //Log.i(TAG, "test: in handleMessage, MSG_GET_STATICS_INFO");
                if (!(object instanceof lsMediaCapture.Statistics)) {
                    return;
                }

                lsMediaCapture.Statistics statistics = (lsMediaCapture.Statistics) object;
                if (statistics == null) {
                    return;
                }
                Intent intent = new Intent(NetWorkInfoDialog.NETINFO_ACTION);
                intent.putExtra("videoFrameRate", statistics.videoEncodeFrameRate);
                intent.putExtra("videoBitRate", statistics.videoEncodeBitRate);
                intent.putExtra("audioBitRate", statistics.audioEncodeBitRate);
                intent.putExtra("totalRealBitrate", statistics.totalRealSendBitRate);

                if (mLSLiveStreamingParaCtx.sLSVideoParaCtx.width == 1280 && mLSLiveStreamingParaCtx.sLSVideoParaCtx.height == 720) {
                    intent.putExtra("resolution", 1);
                } else if (mLSLiveStreamingParaCtx.sLSVideoParaCtx.width == 640 && mLSLiveStreamingParaCtx.sLSVideoParaCtx.height == 480) {
                    intent.putExtra("resolution", 2);
                } else if (mLSLiveStreamingParaCtx.sLSVideoParaCtx.width == 320 && mLSLiveStreamingParaCtx.sLSVideoParaCtx.height == 240) {
                    intent.putExtra("resolution", 3);
                }
                getActivity().sendBroadcast(intent);

                break;
            }
            case MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR: {
                Log.i(TAG, "test: in handleMessage, MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR");
            }
            case MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR: {
                Log.i(TAG, "test: in handleMessage, MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR");
            }
            case MSG_NEW_AUDIORECORD_INSTANCE_ERROR: {
                Log.i(TAG, "test: in handleMessage, MSG_NEW_AUDIORECORD_INSTANCE_ERROR");
            }
            case MSG_AUDIO_START_RECORDING_ERROR: {
                Log.i(TAG, "test: in handleMessage, MSG_AUDIO_START_RECORDING_ERROR");
            }
            case MSG_STOP_LIVESTREAMING_ERROR: {
                if (context == null) {
                    return;
                }
                if (m_liveStreamingOn) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_STOP_LIVESTREAMING_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                }
                break;
            }
            case MSG_RTMP_URL_ERROR: {
                  /*
                  if(m_liveStreamingOn && System.currentTimeMillis() - mLastRtmpUrlErrorAlertTime >= 10000)
		    	  {
	      		      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_RTMP_URL_ERROR");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
	      		      mLastRtmpUrlErrorAlertTime = System.currentTimeMillis();
		    	  }
		    	  */
                //Log.i(TAG, "test: in handleMessage, MSG_RTMP_URL_ERROR");
                activityProxy.onNetWorkBroken();
                break;
            }
            case MSG_URL_NOT_AUTH: {
                if (context == null) {
                    return;
                }
                if (m_liveStreamingInit) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_URL_NOT_AUTH");
                    Intent intent = new Intent(getActivity(), AlertService.class);
                    intent.putExtras(bundle);
                    getActivity().startService(intent);
                    mAlertServiceOn = true;
                }
                Toast.makeText(context, "地址不合法", Toast.LENGTH_SHORT).show();
                activityProxy.onInitFailed();
                break;
            }
            case MSG_URL_FORMAT_NOT_RIGHT:
                Toast.makeText(context, "地址不合法", Toast.LENGTH_SHORT).show();
                activityProxy.onInitFailed();
                break;
            case MSG_QOS_TO_STOP_LIVESTREAMING: {
                  /*
                  if(m_liveStreamingOn && System.currentTimeMillis() - mLastQosToStopLivestreamingAlertTime >= 10000)
		    	  {
		    	      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_QOS_TO_STOP_LIVESTREAMING");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
	      		      mLastQosToStopLivestreamingAlertTime = System.currentTimeMillis();
		    	  }
		    	  */
                break;
            }
            case MSG_HW_VIDEO_PACKET_ERROR: {
                if (context == null) {
                    return;
                }
                if (m_liveStreamingOn) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_HW_VIDEO_PACKET_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                }
                break;
            }
            case MSG_START_PREVIEW_FINISHED: {
                //如果是音视频直播或者视频直播，视频preview之后才允许开始直播
                Log.d(TAG, "preview finished");
                if (isNeedStartAV && (mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType == HAVE_AV || mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType == HAVE_VIDEO)) {
                    startAV();
                    isNeedStartAV = false;
                }
                break;
            }
            case MSG_START_LIVESTREAMING_FINISHED: {
                Log.d(TAG, "start live stream finished");
                activityProxy.onLiveStart();
                LocalBroadcastManager.getInstance(activityProxy.getActivity()).sendBroadcast(new Intent(STREAM_START));
                break;
            }
            case MSG_STOP_LIVESTREAMING_FINISHED: {
                Log.d(TAG, "stop live stream finished");
                if (!isManualPause()) {
                    activityProxy.onFinished();
                    LocalBroadcastManager.getInstance(activityProxy.getActivity()).sendBroadcast(new Intent(STREAM_FINISH));
                }
                break;
            }
            case MSG_STOP_VIDEO_CAPTURE_FINISHED: {
                Log.i(TAG, "test: in handleMessage: MSG_STOP_VIDEO_CAPTURE_FINISHED, m_tryToStopLivestreaming is " + m_tryToStopLivestreaming);
                if (!m_tryToStopLivestreaming && mLSMediaCapture != null && !mIsFilterMode) {
                    //继续视频推流，推最后一帧图像
                    mLSMediaCapture.backgroundVideoEncode();
                }
                break;
            }
            case MSG_STOP_RESUME_VIDEO_CAPTURE_FINISHED: {
                Log.i(TAG, "test: in handleMessage: MSG_STOP_RESUME_VIDEO_CAPTURE_FINISHED");
                if (mLSMediaCapture != null) {
                    mLSMediaCapture.resumeVideoPreview();
                    m_liveStreamingOn = true;
                    //开启视频推流，推正常帧
                    mLSMediaCapture.startVideoLiveStream();
                }
                break;
            }
            case MSG_STOP_AUDIO_CAPTURE_FINISHED: {
                //Log.i(TAG, "test: in handleMessage: MSG_STOP_AUDIO_CAPTURE_FINISHED");
                if (!m_tryToStopLivestreaming && mLSMediaCapture != null) {
                    //继续音频推流，推静音帧
                    mLSMediaCapture.resumeAudioEncode();
                }
                break;
            }
            case MSG_STOP_RESUME_AUDIO_CAPTURE_FINISHED: {
                //Log.i(TAG, "test: in handleMessage: MSG_STOP_RESUME_AUDIO_CAPTURE_FINISHED");
                //开启音频推流，推正常帧
                if (mLSMediaCapture != null) {
                    mLSMediaCapture.startAudioLiveStream();
                }
                break;
            }
            case MSG_SWITCH_CAMERA_FINISHED: {
                int cameraId = (Integer) object;//切换之后的camera id
                break;
            }
            case MSG_SEND_STATICS_LOG_FINISHED: {
                //Log.i(TAG, "test: in handleMessage, MSG_SEND_STATICS_LOG_FINISHED");
                break;
            }
            case MSG_BAD_NETWORK_DETECT:
                if (!isActivityPause) {
                    activityProxy.onNetWorkBroken();
                }
                break;
            case MSG_MIX_AUDIO_FINISHED: {
                //伴音一首MP3歌曲结束后的反馈
                //Log.i(TAG, "test: in handleMessage, MSG_MIX_AUDIO_FINISHED");
                break;
            }
        }
    }

    private Activity getActivity() {
        return activityProxy.getActivity();
    }

    public interface ActivityProxy {
        Activity getActivity();

        void onLiveStart();

        void onInitFailed();

        void onNetWorkBroken();

        void onFinished();
    }

    //用于接收Service发送的伴音消息
    public class AudioMixVolumeMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();
            //监听设备耳机插拔的广播消息，支持有线耳机和外放模式
            if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
                if (intent.getIntExtra("state", 0) == 0) {
                    mRouteMode = AUDIO_ROUTE_LOUDERSPEAKER;//外放
                } else if (intent.getIntExtra("state", 0) == 1) {
                    mRouteMode = AUDIO_ROUTE_EARPIECE;//耳机
                }

                if (mLSMediaCapture != null) {
                    mLSMediaCapture.setAudioRouteMode(mRouteMode);
                }
            } else if (action.equals(MixAudioDialog.AUDIO_MIX_ACTION)) { //伴音开关
                int audioMixMsg = intent.getIntExtra("AudioMixMSG", 0);
                String mixAudioPath = mMixAudioFilePath + "/" + intent.getStringExtra("AudioMixFilePathMSG");

                switch (audioMixMsg) {
                    case 1:
                        if (mLSMediaCapture != null) {
                            mLSMediaCapture.setMixIntensity(mixAudioVolume);
                            mLSMediaCapture.setAudioRouteMode(mRouteMode);
                            mLSMediaCapture.startPlayMusic(mixAudioPath);
                        }
                        break;
                    case 2:
                        if (mLSMediaCapture != null) {
                            mLSMediaCapture.resumePlayMusic();
                        }
                        break;
                    case 3:
                        if (mLSMediaCapture != null) {
                            mLSMediaCapture.pausePlayMusic();
                        }
                        break;
                    case 4:
                        if (mLSMediaCapture != null) {
                            mLSMediaCapture.stopPlayMusic();
                        }
                        break;
                    default:
                        break;
                }
            } else if (action.equals(MixAudioDialog.AUDIO_MIX_VOLUME_ACTION)) { //伴音音量的控制
                int audioMixVolumeMsg = intent.getIntExtra("AudioMixVolumeMSG", 0);
                //外放模式
                if (mRouteMode == AUDIO_ROUTE_LOUDERSPEAKER) {
                    int maxStreamVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int streamVolume = audioMixVolumeMsg * maxStreamVolume / 10;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamVolume, 1);
                } else if (mRouteMode == AUDIO_ROUTE_EARPIECE) {//耳机模式
                    mLSMediaCapture.setMixIntensity(audioMixVolumeMsg);
                }
            }

        }
    }
}
