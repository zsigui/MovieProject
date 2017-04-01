package com.jackiez.zgithub.test.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.jackiez.base.assist.u_imageloader.SGImageLoader;
import com.jackiez.base.for_adapter.BaseHolder;
import com.jackiez.base.for_adapter.BaseProvider;
import com.jackiez.zgithub.R;
import com.jackiez.zgithub.test.data.Contributor;

/**
 * Created by zsigui on 17-3-29.
 */

public class TestItemPicProvider extends BaseProvider<Contributor, TestItemPicProvider.TestItemPicVH> {

    @Override
    protected int getLayoutId() {
        return R.layout.test_item_img;
    }

    @Override
    protected TestItemPicVH createHolder(View itemView) {
        return new TestItemPicVH(itemView);
    }

    static class TestItemPicVH extends BaseHolder<Contributor> {

        public TestItemPicVH(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(@NonNull Contributor data, int position) {
            SGImageLoader.get().loadImage(data.avatar, R.drawable.ic_about, (ImageView) itemView);
        }
    }
}
