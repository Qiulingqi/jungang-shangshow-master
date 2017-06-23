package com.shangshow.showlive.common.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;


public class CustomDialogHelper {

    public interface OnDialogActionListener {
        void doCancelAction();

        void doPositiveAction();
    }

    /**
     * 自定义填充View的dialog
     *
     * @param mContext
     * @param positiveBtnString
     * @param positiveListener
     */
    public static void CustomViewDialog(Context mContext, View customView, CharSequence positiveBtnString, final View.OnClickListener positiveListener) {
        customMyDialogOptions(mContext, null, null, customView, positiveBtnString, positiveListener, null, null);
    }

    /**
     * 一个btn的dialog
     *
     * @param mContext
     * @param titleString
     * @param msgString
     * @param positiveBtnString
     * @param positiveListener
     */
    public static void OneButtonDialog(Context mContext, CharSequence titleString, CharSequence msgString, CharSequence positiveBtnString, final View.OnClickListener positiveListener) {
        customMyDialogOptions(mContext, titleString, msgString, null, positiveBtnString, positiveListener, null, null);
    }

    /**
     * 二个btn的dialog
     *
     * @param mContext
     * @param titleString
     * @param msgString
     * @param positiveBtnString
     * @param positiveListener
     * @param cancelBtnString
     * @param cancelListener
     */
    public static void TwoButtonDialog(Context mContext, CharSequence titleString, CharSequence msgString, CharSequence positiveBtnString, final View.OnClickListener positiveListener, CharSequence cancelBtnString, final View.OnClickListener cancelListener) {
        customMyDialogOptions(mContext, titleString, msgString, null, positiveBtnString, positiveListener, cancelBtnString, cancelListener);
    }


    /**
     * 二个btn的dialog
     *
     * @param mContext
     * @param titleString
     * @param msgString
     * @param positiveBtnString
     * @param cancelBtnString
     * @param listener
     */
    public static void TwoButtonDialog(Context mContext, CharSequence titleString, CharSequence msgString,
                                       CharSequence positiveBtnString, CharSequence cancelBtnString, final OnDialogActionListener listener) {
        final CustomDialog dialog = new CustomDialog(mContext);
        View.OnClickListener positiveListener = new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                listener.doPositiveAction();
            }
        };
        View.OnClickListener cancelListener = new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                listener.doCancelAction();
            }
        };
        customMyDialogOptions(mContext, titleString, msgString, null, positiveBtnString, positiveListener, cancelBtnString, cancelListener);
    }


    /**
     * 全属性方法
     *
     * @param mContext
     * @param titleString
     * @param msgString
     * @param positiveBtnString
     * @param positiveListener
     * @param cancelBtnString
     * @param cancelListener
     */
    public static void customMyDialogOptions(Context mContext, CharSequence titleString, CharSequence msgString, View customView,
                                             CharSequence positiveBtnString, final View.OnClickListener positiveListener,
                                             CharSequence cancelBtnString, final View.OnClickListener cancelListener) {
        final CustomDialog dialog = new CustomDialog(mContext);
        if (TextUtils.isEmpty(titleString)) {
            dialog.setTitleVisible(false);
        } else {
            dialog.setTitle(titleString);
        }
        if (TextUtils.isEmpty(msgString)) {
            dialog.setMessageVisible(false);
        } else {
            dialog.setMessage(msgString);
        }
        if (customView == null) {
            dialog.setCustomViewVisible(false);
        } else {
            dialog.setCustomView(customView);
        }
        if (!TextUtils.isEmpty(positiveBtnString) && positiveListener != null) {
            dialog.addPositiveButton(positiveBtnString, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (positiveListener != null) {
                        positiveListener.onClick(v);
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(cancelBtnString) && cancelListener != null) {
            dialog.setCancelable(true);
            dialog.addNegativeButton(cancelBtnString, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (cancelListener != null) {
                        cancelListener.onClick(v);
                    }
                }
            });
        }
        // 点击周围是否可以取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}