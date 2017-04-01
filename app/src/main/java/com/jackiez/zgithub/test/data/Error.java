package com.jackiez.zgithub.test.data;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Locale;

/**
 * Created by zsigui on 17-3-28.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Error {

    public int code;

    public String msg;

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "code: %d, msg: %s", code, msg);
    }

    public boolean isFailed() {
        return code == 0;
    }
}
