package com.shangshow.showlive.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangshow.showlive.R;
import com.shangshow.showlive.common.utils.ScreenUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 自定义dialog
 */
public class CustomDialog extends Dialog {

    private Context context;

    public static final int NO_TEXT_COLOR = -99999999;

    public static final int NO_TEXT_SIZE = -99999999;

    private View titleView;

    private ImageView titleImage;

    private TextView titleTV;

    private TextView messageTV;

    private LinearLayout customViewLayout;
    private View customView;

    private Button positiveBtn, negativeBtn;

    private View btnDivideView;

    private CharSequence title = "", message = "", positiveBtnTitle = "", negativeBtnTitle = "";

    private int titleTextColor = NO_TEXT_COLOR, msgTextColor = NO_TEXT_COLOR, positiveBtnTitleTextColor = NO_TEXT_COLOR, negativeBtnTitleTextColor = NO_TEXT_COLOR;

    private float titleTextSize = NO_TEXT_SIZE, msgTextSize = NO_TEXT_SIZE, positiveBtnTitleTextSize = NO_TEXT_SIZE, negativeBtnTitleTextSize = NO_TEXT_SIZE;

    private int resourceId;

    private boolean isPositiveBtnVisible = true, isNegativeBtnVisible = false;

    private boolean isTitleVisible = false, isMessageVisble = true, isCustomViewVisble = false, isTitleBtnVisible = false;

    private View.OnClickListener positiveBtnListener, negativeBtnListener;

    private HashMap<Integer, View.OnClickListener> mViewListener = new HashMap<Integer, View.OnClickListener>();

    public CustomDialog(Context context, int resourceId, int style) {
        super(context, style);
        this.context = context;
        if (-1 != resourceId) {
            setContentView(resourceId);
            this.resourceId = resourceId;
        }

        WindowManager.LayoutParams Params = getWindow().getAttributes();
        Params.width = WindowManager.LayoutParams.MATCH_PARENT;
        Params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((WindowManager.LayoutParams) Params);
    }

    public CustomDialog(Context context, int style) {
        this(context, -1, style);
        resourceId = R.layout.layout_mydialog_content;
    }

    public CustomDialog(Context context) {
        this(context, R.style.Dialog);
        resourceId = R.layout.layout_mydialog_content;
    }

    public void setTitle(CharSequence title) {
        isTitleVisible = TextUtils.isEmpty(title) ? false : true;
        setTitleVisible(isTitleVisible);
        if (null != title) {
            this.title = title;
            if (null != titleTV)
                titleTV.setText(title);
        }
    }

    public void setTitleVisible(boolean visible) {
        isTitleVisible = visible;
        if (titleView != null) {
            titleView.setVisibility(isTitleVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setTitleImageVisible(boolean visible) {
        isTitleBtnVisible = visible;
        if (titleImage != null) {
            titleImage.setVisibility(isTitleBtnVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setTitleTextColor(int color) {
        titleTextColor = color;
        if (null != titleTV && NO_TEXT_COLOR != color)
            titleTV.setTextColor(color);
    }

    public void setMessageTextColor(int color) {
        msgTextColor = color;
        if (null != messageTV && NO_TEXT_COLOR != color)
            messageTV.setTextColor(color);

    }

    public void setMessageTextSize(float size) {
        msgTextSize = size;
        if (null != messageTV && NO_TEXT_SIZE != size)
            messageTV.setTextSize(size);
    }

    public void setTitleTextSize(float size) {
        titleTextSize = size;
        if (null != titleTV && NO_TEXT_SIZE != size)
            titleTV.setTextSize(size);
    }

    public void setMessageVisible(boolean visible) {
        isMessageVisble = visible;
        if (messageTV != null) {
            messageTV.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void setMessage(CharSequence message) {
        if (null != message) {
            this.message = message;
            if (null != messageTV)
                messageTV.setText(message);
        }
    }


    public void setCustomView(View view) {
        isCustomViewVisble = null == view ? false : true;
        setCustomViewVisible(isCustomViewVisble);
        if (null != view) {
            this.customView = view;
        }
    }


    public void setCustomViewVisible(boolean visible) {
        this.isCustomViewVisble = visible;
        if (null != customView) {
            customView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void addPositiveButton(CharSequence title, int color, float size,
                                  View.OnClickListener positiveBtnListener) {
        isPositiveBtnVisible = true;
        positiveBtnTitle = TextUtils.isEmpty(title) ? context
                .getString(R.string.ok) : title;
        positiveBtnTitleTextColor = color;
        positiveBtnTitleTextSize = size;
        this.positiveBtnListener = positiveBtnListener;

        if (positiveBtn != null) {
            positiveBtn.setText(positiveBtnTitle);
            positiveBtn.setTextColor(positiveBtnTitleTextColor);
            positiveBtn.setTextSize(positiveBtnTitleTextSize);
            positiveBtn.setOnClickListener(positiveBtnListener);
        }
    }

    public void addNegativeButton(CharSequence title, int color, float size,
                                  View.OnClickListener negativeBtnListener) {
        isNegativeBtnVisible = true;
        negativeBtnTitle = TextUtils.isEmpty(title) ? context
                .getString(R.string.cancel) : title;
        negativeBtnTitleTextColor = color;
        negativeBtnTitleTextSize = size;
        this.negativeBtnListener = negativeBtnListener;

        if (negativeBtn != null) {
            negativeBtn.setText(negativeBtnTitle);
            negativeBtn.setTextColor(negativeBtnTitleTextColor);
            negativeBtn.setTextSize(negativeBtnTitleTextSize);
            negativeBtn.setOnClickListener(negativeBtnListener);
        }
    }

    public void addPositiveButton(CharSequence title,
                                  View.OnClickListener positiveBtnListener) {
        addPositiveButton(title, NO_TEXT_COLOR, NO_TEXT_SIZE,
                positiveBtnListener);
    }

    public void addNegativeButton(CharSequence title,
                                  View.OnClickListener negativeBtnListener) {
        addNegativeButton(title, NO_TEXT_COLOR, NO_TEXT_SIZE,
                negativeBtnListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resourceId);
        try {
            ViewGroup root = (ViewGroup) findViewById(R.id.dialog_rootView);
            if (root != null) {
                ViewGroup.LayoutParams params = root.getLayoutParams();
                params.width = (int) ScreenUtil.getDialogWidth(getContext());
                root.setLayoutParams(params);
            }

            titleView = findViewById(R.id.dialog_title_layout);
            if (titleView != null) {
                setTitleVisible(isTitleVisible);
            }
            titleImage = (ImageView) findViewById(R.id.dialog_title_imageview);
            if (titleImage != null) {
                setTitleImageVisible(isTitleBtnVisible);
            }
            titleTV = (TextView) findViewById(R.id.dialog_title);
            if (titleTV != null) {
                titleTV.setText(title);
                if (NO_TEXT_COLOR != titleTextColor)
                    titleTV.setTextColor(titleTextColor);
                if (NO_TEXT_SIZE != titleTextSize)
                    titleTV.setTextSize(titleTextSize);
            }


            messageTV = (TextView) findViewById(R.id.dialog_message);
            if (messageTV != null) {
                messageTV.setText(message);
                setMessageVisible(isMessageVisble);
                if (NO_TEXT_COLOR != msgTextColor)
                    messageTV.setTextColor(msgTextColor);
                if (NO_TEXT_SIZE != msgTextSize)
                    messageTV.setTextSize(msgTextSize);
            }

            customViewLayout = (LinearLayout) findViewById(R.id.dialog_custom_layout);

            if (customViewLayout != null && customView != null && isCustomViewVisble) {
                customViewLayout.setVisibility(View.VISIBLE);
                customViewLayout.removeAllViews();
                customViewLayout.addView(customView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                customViewLayout.setVisibility(View.GONE);
            }

            positiveBtn = (Button) findViewById(R.id.dialog_btn_positiveButton);
            if (isPositiveBtnVisible && positiveBtn != null) {
                positiveBtn.setVisibility(View.VISIBLE);
                if (NO_TEXT_COLOR != positiveBtnTitleTextColor) {
                    positiveBtn.setTextColor(positiveBtnTitleTextColor);
                }
                if (NO_TEXT_SIZE != positiveBtnTitleTextSize) {
                    positiveBtn.setTextSize(positiveBtnTitleTextSize);
                }
                positiveBtn.setText(positiveBtnTitle);
                positiveBtn.setOnClickListener(positiveBtnListener);
            }

            negativeBtn = (Button) findViewById(R.id.dialog_btn_negativeButton);
            btnDivideView = findViewById(R.id.dialog_btn_divide_view);
            if (isNegativeBtnVisible) {
                negativeBtn.setVisibility(View.VISIBLE);
                btnDivideView.setVisibility(View.VISIBLE);
                if (NO_TEXT_COLOR != this.negativeBtnTitleTextColor) {
                    negativeBtn.setTextColor(negativeBtnTitleTextColor);
                }
                if (NO_TEXT_SIZE != this.negativeBtnTitleTextSize) {
                    negativeBtn.setTextSize(negativeBtnTitleTextSize);
                }
                negativeBtn.setText(negativeBtnTitle);
                negativeBtn.setOnClickListener(negativeBtnListener);
            } else {
                negativeBtn.setVisibility(View.GONE);
                btnDivideView.setVisibility(View.GONE);
            }

            if (mViewListener != null && mViewListener.size() != 0) {
                Iterator iter = mViewListener.entrySet().iterator();
                View view = null;
                while (iter.hasNext()) {
                    Map.Entry<Integer, View.OnClickListener> entry = (Map.Entry) iter.next();
                    view = findViewById(entry.getKey());
                    if (view != null && entry.getValue() != null) {
                        view.setOnClickListener(entry.getValue());
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Button getPositiveBtn() {
        return positiveBtn;
    }

    public Button getNegativeBtn() {
        return negativeBtn;
    }

    public void setViewListener(int viewId, View.OnClickListener listener) {
        mViewListener.put(viewId, listener);
    }

}