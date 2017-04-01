package com.jackiez.zgithub.test.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jackiez.base.assist.u_imageloader.SGImageLoader;
import com.jackiez.base.for_adapter.binding.BaseBindingHolder;
import com.jackiez.base.for_adapter.binding.BaseBindingProvider;
import com.jackiez.base.util.AppDebugLog;
import com.jackiez.zgithub.R;
import com.jackiez.zgithub.databinding.TestItemRepoBinding;
import com.jackiez.zgithub.test.data.Contributor;
import com.jackiez.zgithub.test.vm.TestItemRepoVM;

import java.util.ArrayList;

/**
 * Created by zsigui on 17-3-28.
 */

public class TestRepoProvider extends BaseBindingProvider<TestItemRepoVM, TestItemRepoBinding, TestRepoProvider.TestRepoVH>{

    @Override
    protected int getLayoutId() {
        return com.jackiez.zgithub.R.layout.test_item_repo;
    }

    @Override
    protected TestRepoVH createHolder(View itemView, TestItemRepoBinding binding) {
        return new TestRepoVH(itemView, binding);
    }

    public static class TestRepoVH extends BaseBindingHolder<TestItemRepoVM, TestItemRepoBinding> implements View
            .OnClickListener {

//        private MultiTypeAdapter mAdapter;
        private ArrayList<ImageView> ivs;

        public TestRepoVH(@NonNull View itemView, @NonNull TestItemRepoBinding binding) {
            super(itemView, binding);
            ivs = new ArrayList<>(5);
            ivs.add(binding.iv1);
            ivs.add(binding.iv2);
            ivs.add(binding.iv3);
            ivs.add(binding.iv4);
            ivs.add(binding.iv5);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onViewRecycled() {
            super.onViewRecycled();
            // setRepo(null) 会移除对于该 ViewModel 的监听
            getBinding().setRepo(null);
            getBinding().executePendingBindings();
            ImageView v;
            for (int i = 0; i < ivs.size(); i++) {
                v = ivs.get(i);
                if (v != null) {
                    v.setImageDrawable(null);
                }
            }
        }

        @Override
        public void bindData(@NonNull TestItemRepoVM testItemRepoVM, int position) {
            // 如果数据相同，不进行更新
            AppDebugLog.d(AppDebugLog.TAG_UTIL, "now is to Bind data : " + position + ", " + testItemRepoVM);
            if (getBinding().getRepo() != null
                    && testItemRepoVM.equals(getBinding().getRepo()))
                return;
            AppDebugLog.d(AppDebugLog.TAG_UTIL, "real bind data: " + testItemRepoVM);
            getBinding().setRepo(testItemRepoVM);
            getBinding().executePendingBindings();
//            if (mAdapter == null) {
//                mAdapter = new MultiTypeAdapter(testItemRepoVM.getContributors());
//                mAdapter.register(Contributor.class, new TestItemPicProvider());
//                LinearLayoutManager llm = new LinearLayoutManager(itemView.getContext(),
//                        LinearLayoutManager.HORIZONTAL, false);
//                getBinding().rvBuiltBy.setLayoutManager(llm);
//                getBinding().rvBuiltBy.setAdapter(mAdapter);
//            } else {
//                mAdapter.setItems(testItemRepoVM.getContributors());
//                mAdapter.notifyDataSetChanged();
//            }
            Contributor c;
            int count = testItemRepoVM.getContributors().size();
            ImageView v;
            for (int i = 0; i < ivs.size(); i++) {
                v = ivs.get(i);
                if (i < count) {
                    c = testItemRepoVM.getContributors().get(i);
                    SGImageLoader.get().loadImage(c.avatar, R.drawable.ic_about, v);
                    if (v.getVisibility() != View.VISIBLE)
                        v.setVisibility(View.VISIBLE);
                } else {
                    if (v.getVisibility() != View.GONE)
                        v.setVisibility(View.GONE);
                }

            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "执行了点击操作！", Toast.LENGTH_LONG).show();
        }
    }
}
