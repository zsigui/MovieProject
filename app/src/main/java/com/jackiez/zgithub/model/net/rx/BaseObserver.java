package com.jackiez.zgithub.model.net.rx;

import com.jackiez.base.util.AppDebugLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 封装的RX观察方法，避免部分请求重复的初始化操作
 *
 * Created by zsigui on 17-3-22.
 */
public abstract class BaseObserver<T> implements Observer<T> {

    // 将弹窗的开关交由 RxDefaultTransformer 统一管理
//    private WeakReference<Disposable> mDisposableRef;

//    public BaseObserver() {
//        DialogHelper.setCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (mDisposableRef != null) {
//                    Disposable d = mDisposableRef.get();
//                    if (d != null && !d.isDisposed())
//                        d.dispose();
//                }
//            }
//        });
//    }

    @Override
    public void onSubscribe(Disposable d) {
        // 从源码 ObservableSubscribeOn.java 看出 onSubscribe 是在 Observable 调用的场景下调用，则主线程调用 Observable，该处也处于主线程下
//        mDisposableRef = new WeakReference<>(d);
    }

    @Override
    public void onError(Throwable e) {
        AppDebugLog.d(AppDebugLog.TAG_NET, e);
    }

    @Override
    public void onComplete() {
//        DialogHelper.hideProgressDialog();
    }
}
