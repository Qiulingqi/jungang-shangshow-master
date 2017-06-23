package com.shangshow.showlive.controller.adapter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.shangshow.showlive.controller.adapter.adapter.BaseAdapterHelper.get;

public abstract class QuickAdapter<T> extends
        BaseQuickAdapter<T, BaseAdapterHelper> {

    /**
     * Create a QuickAdapter.
     *
     * @param context
     *            The context.
     * @param layoutResId
     *            The layout resource id of each item.
     */
    public QuickAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with some
     * initialization data.
     *
     * @param context
     *            The context.
     * @param layoutResId
     *            The layout resource id of each item.
     * @param data
     *            A new list is created out of this one to avoid mutable list
     */
    public QuickAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    public QuickAdapter(Context context, List<T> data,
                        MultiItemTypeSupport<T> multiItemSupport) {
        super(context, data, multiItemSupport);
    }

    protected BaseAdapterHelper getAdapterHelper(int position,
                                                 View convertView, ViewGroup parent) {

        if (mMultiItemSupport != null) {
            return get(
                    context,
                    convertView,
                    parent,
                    mMultiItemSupport.getLayoutId(position, data.get(position)),
                    position);
        } else {
            return get(context, convertView, parent, layoutResId, position);
        }
    }

}
