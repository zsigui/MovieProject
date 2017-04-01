package com.jackiez.zgithub.test;

import android.view.View;

import com.jackiez.base.for_adapter.BaseHolder;
import com.jackiez.base.for_adapter.BaseProvider;
import com.jackiez.base.listener.ItemClickListener;
import com.jackiez.base.view.R;
import com.jackiez.zgithub.test.data.User;

/**
 * Created by zsigui on 17-3-16.
 */

public class UserProvider extends BaseProvider<User, UserProvider.UserVH> {

    private ItemClickListener<User> mListener;

    public UserProvider(ItemClickListener<User> listener) {
        mListener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.abc_action_bar_view_list_nav_layout;
    }

    @Override
    protected UserVH createHolder(View itemView) {
        return new UserVH(itemView, mListener);
    }

    public static class UserVH extends BaseHolder<User> implements View.OnClickListener {

        private ItemClickListener<User> mListener;

        public UserVH(View itemView, ItemClickListener<User> listener) {
            super(itemView);
            mListener = listener;
        }

        @Override
        public void bindData(User data, int position) {
            itemView.setContentDescription(data.getName());
            itemView.setOnClickListener(this);
            itemView.setTag(TAG_POS, position);
            itemView.setTag(TAG_ITEM, data);
        }

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag(TAG_POS);
            Object data = v.getTag(TAG_ITEM);
            if (mListener != null) {
                mListener.onItemClick(v, pos, (User) data);
            }
        }
    }
}
