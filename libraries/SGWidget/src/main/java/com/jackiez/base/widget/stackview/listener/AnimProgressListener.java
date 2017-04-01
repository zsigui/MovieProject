package com.jackiez.base.widget.stackview.listener;

import android.view.View;

/**
 * Created by zsigui on 17-4-1.
 */

public interface AnimProgressListener {

    /**
     * 当前顶端视图位置
     *
     * @param v 执行进度变化的视图
     * @param p 当前的位置，0.0f~1.0f，0表示原位置，1表示完成
     *
     * @return true 表示已经设置了该视图当前位置的动画变化， false 表示不处理
     */
    boolean onProgressUpdate(View v, float p);
}
