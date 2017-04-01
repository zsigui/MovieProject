package com.jackiez.zgithub.model.net.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jackiez.zgithub.test.data.User;

/**
 * Created by zsigui on 17-3-21.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RespRepository {

    public long id;

    public String name;

    public String full_name;

    public User owner;

    public String html_url;

    public String description;

    public boolean fork;

    public String url;

    public String forks_url;

    public String collaborators_url;

    public String events_url;

    public String branches_url;

    public String tags_url;

    public String stargazers_url;

    public String languages_url;

    public String commits_url;

    public String contents_url;

    public String created_at;

    public String updated_at;

    public int stargazers_count;

    public int forks_count;

    public String language;

    public int size;

    public String homepage;

}
