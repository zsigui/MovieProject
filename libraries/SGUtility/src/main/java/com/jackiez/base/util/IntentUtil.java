package com.jackiez.base.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by zsigui on 17-3-7.
 */

public class IntentUtil {

    public static void dealIntent(Context context, Intent intent, boolean newTask) {
        if (intent == null || context == null)
            return;
        if (intent.resolveActivity(context.getPackageManager()) == null)
            return;
        if (newTask)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
