package com.jackiez.movieproject.model.entities;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/29
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class SpokenLanguage implements Serializable {

    public String iso_639_1;
    public String name;
}
