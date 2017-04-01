package com.jackiez.zgithub.test.data;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by zsigui on 17-3-28.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Contributor {

    public String name;

    public String avatar;
}
