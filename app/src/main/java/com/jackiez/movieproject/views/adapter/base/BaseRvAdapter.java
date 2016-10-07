package com.jackiez.movieproject.views.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jackiez.movieproject.views.listener.OnItemClickListener;

import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/6
 */

public abstract class BaseRVAdapter<Data> extends RecyclerView.Adapter<BaseHolder> implements IBaseAdapter<Data>,
        View.OnClickListener {

    protected List<Data> mData;
    protected Context mContext;
    protected OnItemClickListener mItemClickListener;

    public BaseRVAdapter(Context context) {
        this(context, null);
    }

    public BaseRVAdapter(Context context, List<Data> data) {
        mData = data;
        mContext = context;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public Data getItem(int position) {
        return getItemCount() <= position ? null : mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(List<Data> data) {
        mData = data;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag(TAG_POSITION) == null) {
            return;
        }
        int pos = (int) v.getTag(TAG_POSITION);
        if (mItemClickListener != null) {
            mItemClickListener.itemClick(v, pos, getItem(pos));
        }
    }

    /**
     * 更新数据并通知界面刷新，需要传入数据非null和非空
     */
    @Override
    public void updateData(List<Data> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * 通知更新更多数据，出发{}
     */
    @Override
    public void addMoreData(List<Data> moreData) {
        if (moreData == null || moreData.isEmpty()) {
            return;
        }
        int start = 0;
        if (mData != null) {
            start = mData.size();
            mData.addAll(moreData);
        } else {
            mData = moreData;
        }
        notifyItemRangeInserted(start, moreData.size());
    }

    @Override
    public void onDestroy() {
        if (mData != null) {
            mData.clear();
        }
        mData = null;
        mContext = null;
    }
}
