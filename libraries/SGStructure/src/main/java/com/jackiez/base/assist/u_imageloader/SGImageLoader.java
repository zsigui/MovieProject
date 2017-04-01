package com.jackiez.base.assist.u_imageloader;

import android.widget.ImageView;

/**
 * 封装的图片加载类，以减少图片请求库跟逻辑代码的耦合，易于变动
 *
 * Created by zsigui on 17-3-14.
 */
public class SGImageLoader {

    private static SGImageLoader sInstance;

    public static SGImageLoader get() {
        if (sInstance == null) {
            synchronized (SGImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new SGImageLoader();
                }
            }
        }
        return sInstance;
    }

//    private IImageLoaderStrategy mStrategy = new PicassoImageLoaderStrategy();
//    private IImageLoaderStrategy mStrategy = new GlideImageLoaderStrategy();
//    private IImageLoaderStrategy mStrategy = new UILImageLoaderStrategy();
//    private IImageLoaderStrategy mStrategy = new FrescoImageLoaderStrategy();
    /**
     * 负责实际执行图片加载请求的策略类，根据需求选择对应的图片请求库
     */
    private IImageLoaderStrategy mStrategy;

    public void init() {

    }

    public void onTrimMemory(int level) {

    }

    public void onLowMemory() {

    }

    public void loadImage(String url, int placeholder, ImageView iv) {
        iv.setImageResource(placeholder);
    }

}
