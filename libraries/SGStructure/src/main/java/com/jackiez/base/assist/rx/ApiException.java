package com.jackiez.base.assist.rx;

import java.util.Locale;

/**
 * Created by zsigui on 16-11-24.
 */

public class ApiException extends Exception {

    public int code;
    public String msg;

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiException(Throwable cause, int code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public String errMsg() {
        return String.format(Locale.CHINA, "c : %d, m : %s", code, msg);
    }
}
