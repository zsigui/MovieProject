package com.jackiez.zgithub.model.net.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by zsigui on 17-3-22.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RespTreeItem {

    public String path;

    public String mode;

    public String type;

    public String sha;

    public long size;

    public String url;

}
