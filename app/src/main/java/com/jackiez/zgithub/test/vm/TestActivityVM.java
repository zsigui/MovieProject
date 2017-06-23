package com.jackiez.zgithub.test.vm;

import android.databinding.Bindable;
import android.view.View;

import com.jackiez.base.viewmodel.BaseActivityVM;
import com.jackiez.zgithub.BR;
import com.jackiez.zgithub.databinding.TestActivityBinding;
import com.jackiez.zgithub.test.BusSingleton;
import com.jackiez.zgithub.test.Mapper;
import com.jackiez.zgithub.test.data.User;
import com.jackiez.zgithub.test.view.TestActivity;

/**
 * Created by zsigui on 17-3-23.
 */

public class TestActivityVM extends BaseActivityVM<TestActivity> {

    private String name;
    private String password;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public TestActivityVM(TestActivity activity) {
        super(activity);
    }

    @Override
    public void registerListener() {
        BusSingleton.get().register(this);
    }

    @Override
    public void unregisterListener() {
        BusSingleton.get().unregister(this);
    }

    public void onClick(View v) {
        TestActivityBinding binding = getActivity().getBinding();
        if (binding == null)
            return;
//        DataProvider.testDiffBackProvider(getActivity(), binding.etName.getText().toString(), binding.etPwd.getText().toString());
    }

    public void handleGetUserFrom(User user) {
        Mapper.mapperUserToTestVM(user, this);
    }
}
