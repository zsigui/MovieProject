package com.jackiez.zgithub.test.data;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by zsigui on 17-3-28.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Repo {

    public String name;

    public String language;

    public String meta;

    public String des;

    public String href;

    public String link;

    public List<Contributor> contributors;

    public String owner;

    public boolean isStart;

}
