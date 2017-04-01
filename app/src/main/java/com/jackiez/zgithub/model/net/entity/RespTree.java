package com.jackiez.zgithub.model.net.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by zsigui on 17-3-22.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RespTree {

    public List<RespTree> tree;

    public String sha;

    public String url;
}
