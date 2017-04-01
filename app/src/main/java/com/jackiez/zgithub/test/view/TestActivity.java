package com.jackiez.zgithub.test.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jackiez.base.view.bingding.BaseBindingActivity;
import com.jackiez.zgithub.R;
import com.jackiez.zgithub.databinding.TestActivityBinding;
import com.jackiez.zgithub.test.vm.TestActivityVM;


/**
 * Created by zsigui on 17-3-16.
 */

public class TestActivity extends BaseBindingActivity<TestActivityBinding, TestActivityVM> {


    TestFragment mFragment;

    @Override
    protected void afterSetContentView(@Nullable Bundle saveInstanceState) {
        super.afterSetContentView(saveInstanceState);
        if (saveInstanceState == null) {
            mFragment = TestFragment.newInstance();
            loadRootFragment(R.id.fl_list, mFragment);
        } else {
            mFragment = findFragment(TestFragment.class);
            showHideFragment(mFragment);
        }
    }

    @Override
    protected TestActivityVM createVM() {
        TestActivityVM vm = new TestActivityVM(this);
        getBinding().setObj(vm);
        return vm;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.test_activity;
    }

}
