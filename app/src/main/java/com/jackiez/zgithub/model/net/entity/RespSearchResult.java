package com.jackiez.zgithub.model.net.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by zsigui on 17-3-23.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RespSearchResult<T> {

    public int total_count;

    public boolean incomplete_results;

    public List<T> items;
}
