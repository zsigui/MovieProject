package com.jackiez.zgithub.test.data;

/**
 * Created by zsigui on 17-3-29.
 */

public class ErrorOp {

    public Throwable t;
    public int cmd;

    public ErrorOp(int cmd, Throwable t) {
        this.t = t;
        this.cmd = cmd;
    }
}
