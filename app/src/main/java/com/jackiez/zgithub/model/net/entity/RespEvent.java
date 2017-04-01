package com.jackiez.zgithub.model.net.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jackiez.zgithub.test.data.User;

/**
 * Created by zsigui on 17-3-21.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RespEvent {

    public String id;

    public String type;

    public User actor;

    public RespRepository repo;

    public String created_at;

}
