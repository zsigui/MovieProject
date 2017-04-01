package com.jackiez.zgithub.model.net.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by zsigui on 17-3-22.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RespToken {

    public int id;

    public String url;

    public List<String> scopes;

    public String token;

    public String hashed_token;

    public String token_last_eight;

    public String note;

    public String note_url;

    public String created_at;

    public String updated_at;

    public String fingerprint;

}
