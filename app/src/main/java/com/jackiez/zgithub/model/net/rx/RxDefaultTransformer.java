package com.jackiez.zgithub.model.net.rx;

import android.content.Context;

import com.jackiez.base.assist.rx.ApiException;
import com.jackiez.base.common.ErrStatus;
import com.jackiez.base.util.NetworkUtil;
import com.jackiez.zgithub.view.dialog.DialogHelper;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zsigui on 17-3-24.
 */

public class RxDefaultTransformer<T> implements ObservableTransformer<T, T> {

    private WeakReference<Context> mContextRef;
    private boolean mIsShowDialog;
    private WeakReference<Observer<T>> mObserverRef;

    public RxDefaultTransformer(Context context, boolean isShowDialog, Observer<T> observer) {
        mContextRef = new WeakReference<>(context);
        mIsShowDialog = isShowDialog;
        mObserverRef = new WeakReference<>(observer);
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                // 进行重试条件设定
                .retryWhen(new RxRetryWhenFunc())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Context c;
                        if (mContextRef == null) {
                            return;
                        }
                        c = mContextRef.get();
                        if (c == null)
                            return;
                        if (NetworkUtil.isConnected(c)) {
                            if (mIsShowDialog) {
                                DialogHelper.showProgressDialog(c);
                            }
                        } else {
                            // 网络连接问题是否有必要立即中断请求？如果无，此处observer即毫无必要了
                            if (!disposable.isDisposed()) {
                                disposable.dispose();
                            }
                            if (mObserverRef == null)
                                return;
                            Observer<T> observer = mObserverRef.get();
                            if (observer != null) {
                                // 当前无可用网络，不发起连接
                                observer.onError(new ApiException(ErrStatus.ERR_C_NET_FAIL_CONN,
                                        ErrStatus.STR_ERR_C_NET_FAIL_CONN));
                            }
                        }
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (mIsShowDialog) {
                            DialogHelper.hideProgressDialog();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        clearState();
                    }
                });
    }

    private void clearState() {
        if (mContextRef != null) {
            mContextRef.clear();
            mContextRef = null;
        }
        if (mObserverRef != null) {
            mObserverRef.clear();
            mObserverRef = null;
        }
    }
}
