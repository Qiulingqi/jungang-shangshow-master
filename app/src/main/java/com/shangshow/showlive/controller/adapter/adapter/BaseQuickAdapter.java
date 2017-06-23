package com.shangshow.showlive.controller.adapter.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseQuickAdapter<T, H extends BaseAdapterHelper> extends
        BaseAdapter {

    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();

    protected final Context context;

    protected int layoutResId;

    protected final List<T> data;

    protected boolean displayIndeterminateProgress = false;

    /**
     * Create a QuickAdapter.
     *
     * @param context
     *            The context.
     * @param layoutResId
     *            The layout resource id of each item.
     */
    public BaseQuickAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
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
    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    protected MultiItemTypeSupport<T> mMultiItemSupport;

    public BaseQuickAdapter(Context context, List<T> data,
                            MultiItemTypeSupport<T> multiItemSupport) {
        this.mMultiItemSupport = multiItemSupport;
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
    }

    @Override
    public int getCount() {
        int extra = displayIndeterminateProgress ? 1 : 0;
        return data.size() + extra;
    }

    @Override
    public T getItem(int position) {
        if (position >= data.size())
            return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (mMultiItemSupport != null)
            return mMultiItemSupport.getViewTypeCount() + 1;
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (displayIndeterminateProgress) {
            if (mMultiItemSupport != null)
                return position >= data.size() ? 0 : mMultiItemSupport
                        .getItemViewType(position, data.get(position));
        } else {
            if (mMultiItemSupport != null)
                return mMultiItemSupport.getItemViewType(position,
                        data.get(position));
        }

        return position >= data.size() ? 0 : 1;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            return createIndeterminateProgressView(convertView, parent);
        }
        final H helper = getAdapterHelper(position, convertView, parent);
        T item = getItem(position);
        helper.setAssociatedObject(item);
        convert(helper, item, position);
        return helper.getView();

    }

    private View createIndeterminateProgressView(View convertView,
                                                 ViewGroup parent) {
        if (convertView == null) {
            FrameLayout container = new FrameLayout(context);
            container.setForegroundGravity(Gravity.CENTER);
            ProgressBar progress = new ProgressBar(context);
            container.addView(progress);
            convertView = container;
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return position < data.size();
    }

    public void add(T elem) {
        data.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    /** Clear data list */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void showIndeterminateProgress(boolean display) {
        if (display == displayIndeterminateProgress)
            return;
        displayIndeterminateProgress = display;
        notifyDataSetChanged();
    }

    protected abstract void convert(H helper, T item, int position);

    protected abstract H getAdapterHelper(int position, View convertView,
                                          ViewGroup parent);

}
