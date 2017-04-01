package com.jackiez.zgithub.test.data;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by zsigui on 17-3-16.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.ANNOTATIONS_ONLY)
public class User {
//public class User extends Error{

    @JsonField(name = "name")
    private String mName;

    @JsonField(name = "passowrd")
    private String mPassword;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

//    @Override
//    public String toString() {
//        return "code = " + code  + ", msg = " + msg + ", name = " + mName + ", password = " + mPassword;
//    }
}
