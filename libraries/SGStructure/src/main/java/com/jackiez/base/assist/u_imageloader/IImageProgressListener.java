package com.jackiez.base.assist.u_imageloader;

/**
 * 加载图片的进度回调接口
 *
 * Created by zsigui on 17-3-14.
 */
public interface IImageProgressListener {

    /**
     * 通知图片加载的更新进度
     *
     * @param read 已经读取的图片大小 (byte)
     * @param total 总的图片大小 (byte)
     */
    void onUpdate(int read, int total);
}
