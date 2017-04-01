package com.jackiez.base.entity.json;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jackiez.base.common.ErrStatus;

import java.util.Locale;

/**
 * 进行网络请求的后返回的基本JSON结构对象
 *
 * Created by zsigui on 16-11-23.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class JsonResponse<T> {

    public int code;

    public String msg;

    public T data;

    public boolean isValid() {
        return code == ErrStatus.SUCCESS;
    }

    public String errMsg() {
        return String.format(Locale.CHINA, "请求失败! code: %d, msg: %s", code, msg);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("code=").append(code).append("&");
        builder.append("msg=").append(msg).append("&");
        builder.append("data=").append(data.toString());
        return builder.toString();
    }
}
