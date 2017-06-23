package com.shangshow.showlive.common.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class AndroidBug5497Workaround {

	static int statusBarHeight = 0;
	public static void assistActivity(Activity activity) {
		new AndroidBug5497Workaround(activity);
		//获取status_bar_height资源的ID
		int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
		}
	}

	public static void assistActivity(Activity activity, OnCallBack onCallBack) {
		new AndroidBug5497Workaround(activity, onCallBack);
		//获取status_bar_height资源的ID
		int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
		}
	}

	private View mChildOfContent;
	private int usableHeightPrevious;
	private FrameLayout.LayoutParams frameLayoutParams;

	private AndroidBug5497Workaround(Activity activity) {
		FrameLayout content = (FrameLayout) activity
				.findViewById(android.R.id.content);
		mChildOfContent = content.getChildAt(0);
		mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						possiblyResizeChildOfContent();
					}
				});
		frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent
				.getLayoutParams();
	}

	private AndroidBug5497Workaround(Activity activity, OnCallBack onCallBack) {
		FrameLayout content = (FrameLayout) activity
				.findViewById(android.R.id.content);
		mChildOfContent = content.getChildAt(0);
		mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						possiblyResizeChildOfContent();
					}
				});
		frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent
				.getLayoutParams();
		this.onCallBack = onCallBack;
	}

	private void possiblyResizeChildOfContent() {
		int usableHeightNow = computeUsableHeight();
		if (usableHeightNow != usableHeightPrevious) {
			int usableHeightSansKeyboard = mChildOfContent.getRootView()
					.getHeight();
			int heightDifference = usableHeightSansKeyboard - usableHeightNow;
			if (heightDifference > (usableHeightSansKeyboard / 4)) {
				frameLayoutParams.height = usableHeightSansKeyboard
						- heightDifference + statusBarHeight;
			} else {
				frameLayoutParams.height = usableHeightSansKeyboard;
			}
			mChildOfContent.requestLayout();
			usableHeightPrevious = usableHeightNow;
			if(onCallBack != null){
				onCallBack.callBack();
			}
		}
	}

	private int computeUsableHeight() {
		Rect r = new Rect();
		mChildOfContent.getWindowVisibleDisplayFrame(r);
		return (r.bottom - r.top);
	}

	//  干嘛用？？

	private OnCallBack onCallBack;

	public void setOnCallBack(OnCallBack onCallBack) {
		this.onCallBack = onCallBack;
	}

	public interface OnCallBack{
		void callBack();
	}

}
