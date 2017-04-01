package com.jackiez.zgithub.test.data;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.lang.*;

/**
 * Created by zsigui on 17-3-28.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class SuperData<T> {

    public T data;

    public Error e;

    public boolean isSuccess() {
        return data != null;
    }

    @Override
    public String toString() {
        return "data = " + data + ", e = " + e;
    }
}
