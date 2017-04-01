package com.jackiez.base.for_adapter;

import android.view.View;

import com.jackiez.base.listener.ItemClickListener;

/**
 * 该方法主要提供设置OnClick的基础配置
 *
 * Created by zsigui on 17-3-17.
 */
public abstract class BaseClickHolder<Data> extends BaseHolder<Data> implements View.OnClickListener {

    private ItemClickListener<Data> mItemClickListener;

    public BaseClickHolder(View itemView, ItemClickListener<Data> listener) {
        super(itemView);
        mItemClickListener = listener;
    }

    /**
     * 通过该方法为视图绑定本地的点击事件，并传递相关参数
     */
    public void bindClick(View v, Data data, int position) {
        v.setOnClickListener(this);
        v.setTag(TAG_POS, position);
        v.setTag(TAG_ITEM, data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onClick(View v) {
        int pos = (int) v.getTag(TAG_POS);
        Data data = (Data) v.getTag(TAG_ITEM);
        if (onItemClick(v, pos, data))
            return;
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, pos, data);
        }
    }

    /**
     * 重写该方法由本地处理点击事件，优先于设置的 {@link ItemClickListener<Data>} 事件
     * @return true 代表该事件已经由本地处理，不再继续传递。 false 则表示继续处理
     */
    public boolean onItemClick(View v, int pos, Data data) {
        return false;
    }
}
