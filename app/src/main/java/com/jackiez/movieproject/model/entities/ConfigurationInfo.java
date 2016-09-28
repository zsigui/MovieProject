package com.jackiez.movieproject.model.entities;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ConfigurationInfo implements Serializable {
    public ConfigurationImage images;
    public String [] change_keys;
}
