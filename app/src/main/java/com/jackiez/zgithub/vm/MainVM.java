package com.jackiez.zgithub.vm;

import com.google.common.eventbus.EventBus;
import com.jackiez.base.viewmodel.BaseActivityVM;
import com.jackiez.zgithub.view.activity.MainActivity;

/**
 * Created by zsigui on 17-3-20.
 */

public class MainVM extends BaseActivityVM<MainActivity> {

    public MainVM(MainActivity activity) {
        super(activity);
    }

    @Override
    public void registerListener() {
        new EventBus().register(this);
    }

    @Override
    public void unregisterListener() {
        new EventBus().unregister(this);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }

}
