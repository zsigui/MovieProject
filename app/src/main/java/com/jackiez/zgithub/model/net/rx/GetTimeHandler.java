package com.jackiez.zgithub.model.net.rx;

/**
 * 一个计算第n次执行操作所需间隔的处理器接口
 *
 * Created by zsigui on 17-3-24.
 */
public interface GetTimeHandler {

    /**
     * 计算并返回第N次操作时间间隔
     */
    int calTime(int n);
}
