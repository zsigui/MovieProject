package com.jackiez.zgithub.common;

import com.github.moduth.blockcanary.BlockCanaryContext;

/**
 * Created by zsigui on 17-3-29.
 */

public class AppCanaryContext extends BlockCanaryContext {


    @Override
    public boolean displayNotification() {
        return true;
    }
}
