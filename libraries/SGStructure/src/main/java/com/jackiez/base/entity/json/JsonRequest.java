package com.jackiez.base.entity.json;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * 进行网络请求的基本参数格式的JSON架构
 *
 * Created by zsigui on 17-3-16.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class JsonRequest<T> {

    public String imei;

    public String imsi;

    public String cid;

    public String mac;

    public String apn;

    public String cn;

    public String dd;

    public String dv;

    public String os;

    public int chn;

    public int version;

    public T data;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("imei=").append(imei).append("&");
        builder.append("imsi=").append(imsi).append("&");
        builder.append("cid=").append(cid).append("&");
        builder.append("mac=").append(mac).append("&");
        builder.append("apn=").append(apn).append("&");
        builder.append("cn=").append(cn).append("&");
        builder.append("dd=").append(dd).append("&");
        builder.append("dv=").append(dv).append("&");
        builder.append("os=").append(os).append("&");
        builder.append("chn=").append(chn).append("&");
        builder.append("version=").append(version).append("&");
        builder.append("data=").append(data.toString());
        return builder.toString();
    }
}
