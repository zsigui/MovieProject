package com.jackiez.zgithub.util;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by zsigui on 17-5-9.
 */

public class ViewUtil {

    @SuppressWarnings("unchecked")
    public static  <T extends View> T findViewById(View v, @IdRes int id) {
        return (T) v.findViewById(id);
    }
}
