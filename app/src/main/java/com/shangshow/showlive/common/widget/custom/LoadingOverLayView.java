package com.shangshow.showlive.common.widget.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.common.utils.ScreenUtil;


public class LoadingOverLayView extends FrameLayout {

    public enum LoadingState {
        loadingStateDefault, loadingStateGone, loadingStateLoading, loadingStateFailing, loadingStateEmpty, loadingStateComplete
    }

    private RelativeLayout loadingLayout;
    private ImageView loadingProgressBg;
    private ImageView loadingProgressImg;
    private LinearLayout failLayout;
    private Button failRefreshBtn;
    private LinearLayout noDataLayout;
    private ImageView noDataImg;
    private TextView noDataText;
    private Button noDataBtn;


    private int noDataImgResId;
    private String noDataTextStr;
    private String noDataBtnStr;
    private float noDataLayoutMarginTop;
    private float loadingLayoutMarginTop;


    private OnRefreshClickListener onRefreshClickListener;

    public interface OnRefreshClickListener {
        void onRefreshBtn();
    }

    public void setOnRefreshClickListener(OnRefreshClickListener listener) {
        this.onRefreshClickListener = listener;
    }

    private OnNoDataClickListener onNoDataClickListener;

    public interface OnNoDataClickListener {
        void onNoDataBtn();
    }

    public void setOnNoDataBtnClickListener(OnNoDataClickListener listener) {
        this.onNoDataClickListener = listener;
    }

    public LoadingOverLayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context, attrs);
    }

    public LoadingOverLayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context, attrs);
    }

    public LoadingOverLayView(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context, AttributeSet attrs) {
        initializeView(context);

        if (attrs == null) {
            setDefaultValue();
            return;
        }
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingOverLayView, 0, 0);
        try {
            noDataTextStr = array.getString(R.styleable.LoadingOverLayView_noDataText);
            if (!TextUtils.isEmpty(noDataTextStr)) {
                noDataText.setText(noDataTextStr);
            }

            //传了btn显示
            noDataBtnStr = array.getString(R.styleable.LoadingOverLayView_noDataBtn);
            if (!TextUtils.isEmpty(noDataBtnStr)) {
                noDataBtn.setVisibility(View.VISIBLE);
                noDataBtn.setText(noDataBtnStr);
            } else {
                noDataBtn.setVisibility(View.GONE);
            }

            noDataImgResId = array.getResourceId(R.styleable.LoadingOverLayView_noDataImg, 0);
            if (noDataImgResId > 0) {
                noDataImg.setImageResource(noDataImgResId);
            }

            noDataLayoutMarginTop = array.getDimension(R.styleable.LoadingOverLayView_loadingLayoutMarginTop, 0);
            if (noDataLayoutMarginTop > 0) {
                LayoutParams layoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                layoutParam.setMargins(0, (int) noDataLayoutMarginTop, 0, 0);
                noDataLayout.setLayoutParams(layoutParam);
            }

            //加载overlay可设定距离顶部MarginTop【满足特定需求使用】
            loadingLayoutMarginTop = array.getDimension(R.styleable.LoadingOverLayView_loadingLayoutMarginTop, 0);
            if (loadingLayoutMarginTop > 0) {
                LayoutParams layoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                layoutParam.setMargins(0, (int) loadingLayoutMarginTop, 0, 0);
                loadingLayout.setLayoutParams(layoutParam);
            }


        } finally {
            array.recycle();
        }

    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_loading_overlay, this);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading_overlay_loading_layout);
        loadingProgressImg = (ImageView) findViewById(R.id.loading_layout_progress_icon);
        loadingProgressBg = (ImageView) findViewById(R.id.loading_layout_progress_bg);

        failLayout = (LinearLayout) findViewById(R.id.loading_overlay_fail_layout);
        failRefreshBtn = (Button) findViewById(R.id.loading_overlay_fail_refresh_btn);
        failRefreshBtn.setOnClickListener(new refreshBtnClick());

        noDataLayout = (LinearLayout) findViewById(R.id.no_data_layout);
        noDataImg = (ImageView) findViewById(R.id.no_data_icon);
        noDataText = (TextView) findViewById(R.id.no_data_text);
        noDataBtn = (Button) findViewById(R.id.no_data_btn);
        noDataBtn.setOnClickListener(new noDataBtnClick());

        changeLoadingState(LoadingState.loadingStateDefault);

        setVisibility(View.GONE);

    }

    private void setDefaultValue() {

    }


    public void setCustomEmptyView(View view) {
        if (view == null || noDataLayout == null) {
            return;
        }
        noDataLayout.removeAllViews();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        noDataLayout.addView(view);
    }


    public void changeLoadingState(LoadingState state) {
        switch (state) {
            case loadingStateDefault:
                setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                failLayout.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.VISIBLE);
                ScreenUtil.startRotateAnimation(loadingProgressBg);
                break;
            case loadingStateGone:
                setVisibility(View.GONE);
                loadingLayout.setVisibility(View.VISIBLE);
                failLayout.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.VISIBLE);
                ScreenUtil.startRotateAnimation(loadingProgressBg);

                break;
            case loadingStateLoading:
                setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                failLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);
                ScreenUtil.startRotateAnimation(loadingProgressBg);
                break;
            case loadingStateFailing:
                setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
                failLayout.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.GONE);

                break;
            case loadingStateEmpty:
                setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
                failLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
                break;
            case loadingStateComplete:
                setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                failLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public class refreshBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (onRefreshClickListener != null) {
                onRefreshClickListener.onRefreshBtn();
            }
        }
    }

    public class noDataBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (onNoDataClickListener != null) {
                onNoDataClickListener.onNoDataBtn();
            }
        }
    }
}
