package com.jackiez.movieproject.views.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/6
 */

public abstract class BaseLVAdapter<Data, VH extends BaseHolder> extends BaseAdapter implements IBaseAdapter<Data> {
    private static final int TAG_HOLDER = 0xF123FBCE;

    protected List<Data> mData;
    protected Context mContext;

    public BaseLVAdapter(Context context) {
        this(context, null);
    }

    public BaseLVAdapter(Context context, List<Data> data) {
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onDestroy() {
        if (mData != null) {
            mData.clear();
        }
        mData = null;
        mContext = null;
    }

    public List<Data> getData() {
        return mData;
    }

    public void setData(List<Data> data) {
        mData = data;
    }

    @Override
    public Data getItem(int position) {
        return getCount() == 0 ? null : mData.get(position);
    }

    @Override
    public void updateData(List<Data> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public void addMoreData(List<Data> moreData) {
        if (moreData == null || moreData.isEmpty()) {
            return;
        }
        if (mData != null) {
            mData.addAll(moreData);
        } else {
            mData = moreData;
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            holder = onCreateViewHolder(parent, getItemViewType(position));
            if (holder == null) return null;
            convertView = holder.itemView;
            convertView.setTag(TAG_HOLDER, holder);
        } else {
            holder = (VH) convertView.getTag(TAG_HOLDER);
        }
        onBindViewHolder(holder, position);
        return convertView;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(VH holder, int position);
}
