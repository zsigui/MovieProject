package com.jackiez.movieproject.model.entities;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class PageData<Item> implements Serializable {

    public int id;
    public int page;
    public List<Item> results;
    public int total_pages;
    public int total_results;

    @Override
    public String toString() {
        return "id = " + id + ", page = " + page + ", total_page = " + total_pages + ", total_result = " + total_results
                + ", results = " + results;
    }
}
