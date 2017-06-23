package com.jackiez.zgithub.test;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.jackiez.base.assist.rx.ApiException;
import com.jackiez.base.common.ErrStatus;
import com.jackiez.base.rxbus2.RxBus;
import com.jackiez.base.util.AppDebugLog;
import com.jackiez.base.util.NetworkUtil;
import com.jackiez.zgithub.test.data.Error;
import com.jackiez.zgithub.test.data.SuperData;
import com.jackiez.zgithub.test.data.User;
import com.jackiez.zgithub.view.dialog.DialogHelper;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zsigui on 17-3-23.
 */

public class DataProvider {

    // Rx结论：
    // doOnSubscribe 是从下往上执行，即是越后调用越先执行，执行方式如 { this.doOnSubscribe(); -> parent.doOnSubscribe();}
    // doOnTerminate 则是从上往下执行，即是越先调用越先执行
    // 线程的作用顺序调用所在线程，再由下往上根据设置，subscribeOn 针对 Observable，observeOn 针对 Subscriber/Observer

    // 此写法下 retry 重复次数完成之后不会进入 error 而是直接执行 complete，需要修改
    // 修改方式，重试次数+1，并判断最后一次时再次抛出异常，跳出重试循环，并执行 doOnTerminate() -> observer.onError()

    // 另一问题，当抛出异常为运行时异常时(如 NullPointerException 等)，会继续执行后续的 doOnTerminate()，而不会执行 observer.onError()，
    // 所以对于运行时异常，最后再包裹一层 ApiException()，防止跳过后续执行

    // 通过 Disposable.dispose() 取消任务不再经过后续的 doOnTerminate()/onNext()/onError()/onComplete()

    // 无出错重试时执行顺序 doOnSubscribe -> onSubscribe -> retryWhen -> defer -> flatMap -> observer.onNext -> doOnTerminate
    // -> observer.onComplete
    // 有出错重试时执行顺序 doOnSubscribe -> onSubscribe -> retryWhen [-> defer -> zipWith -> flatMap](循环) -> observer.onNext
    // -> doOnTerminate -> observer.onComplete
    public static void testProvider(final Context context, final String name, final String pwd) {
        Observable.defer(new Callable<ObservableSource<User>>() {
            @Override
            public ObservableSource<User> call() throws Exception {
                // 模拟异步获取
                AppDebugLog.d(AppDebugLog.TAG_UTIL, "defer当前所处线程: " + Thread.currentThread().getName());
                Thread.sleep((int) (Math.random() * 5) * 1000);
                int i = (int) (Math.random() * 4);
                if (i == 0) {
                    // 模拟抛出异常
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "defer抛出模拟错误");
//                    return Observable.error(new ApiException(1, "模拟错误"));
                    throw new ApiException(1, "模拟错误");
                } else if (i == 1) {
                    // 模拟抛出未知异常，对于运行时异常，最后再包裹一层 ApiException()，防止跳过后续执行
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "defer抛出空指针错误");
//                    return Observable.error(new NullPointerException("中途空指针错误"));
                    throw new NullPointerException("中途空指针错误");
                }
                User u = new User();
                u.setName(name);
                u.setPassword(pwd);
                return Observable.just(u);
            }
        })
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Observable<Throwable> errors) throws Exception {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "retryWhen当前所处线程: " + Thread.currentThread().getName());
                        return errors.zipWith(Observable.range(1, 3), new BiFunction<Throwable, Integer, Integer>() {
                            @Override
                            public Integer apply(Throwable throwable, Integer integer) throws Exception {
                                AppDebugLog.d(AppDebugLog.TAG_UTIL, "zipWith当前所处线程: "
                                        + Thread.currentThread().getName() + ", i = " + integer);

                                return integer;
                            }
                        }).flatMap(new Function<Integer, ObservableSource<Long>>() {
                            @Override
                            public ObservableSource<Long> apply(Integer retryCount) throws Exception {
                                AppDebugLog.d(AppDebugLog.TAG_UTIL, "flatMap当前所处线程: "
                                        + Thread.currentThread().getName() + ", i = " + retryCount);
                                return Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "doOnSubscribe当前所处线程: " + Thread.currentThread().getName());
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "doOnTerminate当前所处线程: " + Thread.currentThread().getName());
                        DialogHelper.hideProgressDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {

                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onSubscribe当前所处线程: " + Thread.currentThread().getName());
                        DialogHelper.showProgressDialog(context);
                        DialogHelper.setCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                if (mDisposable != null && !mDisposable.isDisposed()) {
                                    mDisposable.dispose();
                                    mDisposable = null;
                                }
                            }
                        });
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(User value) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onNext当前所处线程: " + Thread.currentThread().getName());
                        // 实际这里可以通过BUS直接发送订阅事件，Test处订阅事件
                        BusSingleton.get().post(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onError当前所处线程: " + Thread.currentThread().getName());
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "接收到的error: " + e);
                    }

                    @Override
                    public void onComplete() {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onComplete当前所处线程: " + Thread.currentThread().getName());
                    }
                });
    }

    public static void testRetryProvider(final Context context, final String name, final String pwd) {
        Observable.defer(new Callable<ObservableSource<User>>() {
            @Override
            public ObservableSource<User> call() throws Exception {
                // 模拟异步获取
                AppDebugLog.d(AppDebugLog.TAG_UTIL, "defer当前所处线程: " + Thread.currentThread().getName());
                Thread.sleep((int) (Math.random() * 5) * 1000);
                int i = (int) (Math.random() * 4);
                if (i == 0) {
                    // 模拟抛出异常
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "defer抛出模拟错误");
//                    return Observable.error(new ApiException(1, "模拟错误"));
                    throw new ApiException(1, "模拟错误");
                } else if (i == 1) {
                    // 模拟抛出未知异常，对于运行时异常，最后再包裹一层 ApiException()，防止跳过后续执行
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "defer抛出空指针错误");
//                    return Observable.error(new NullPointerException("中途空指针错误"));
                    throw new NullPointerException("中途空指针错误");
                }
                User u = new User();
                u.setName(name);
                u.setPassword(pwd);
                return Observable.just(u);
            }
        })
                .retryWhen(new RetryWhenFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "doOnSubscribe当前所处线程: " + Thread.currentThread().getName());

                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "doOnTerminate当前所处线程: " + Thread.currentThread().getName());
                        DialogHelper.hideProgressDialog();

                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "doOnDispose当前所处线程: " + Thread.currentThread().getName());
                    }
                })
                .subscribe(new Observer<User>() {

                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onSubscribe当前所处线程: " + Thread.currentThread().getName());

                        if (!NetworkUtil.isConnected(context)) {
                            // 此处采用 throw 抛出异常的话不会被捕捉而是直接崩溃，故直接dispose。
                            // 不过有另一个问题是这样只走 doOnDispose 而不进行后续操作，另外如果在 doOnSubscribe 调用 dispose 则连 doOnDispose 都不执行
                            AppDebugLog.d(AppDebugLog.TAG_UTIL, "doOnSubscribe抛出404网络异常");
                            if (!d.isDisposed())
                                d.dispose();
                            // 由于后续事件不会继续执行，需要手动调起通知网络失败
                            onError(new ApiException(ErrStatus.ERR_C_NET_FAIL_CONN, ErrStatus.STR_ERR_C_NET_FAIL_CONN));
                            return;
                        }
                        DialogHelper.showProgressDialog(context);
                        DialogHelper.setCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                AppDebugLog.d(AppDebugLog.TAG_UTIL, "DialogHelper.onCancel ");
                                if (mDisposable != null && !mDisposable.isDisposed()) {
                                    mDisposable.dispose();
                                    mDisposable = null;
                                }
                            }
                        });
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(User value) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onNext当前所处线程: " + Thread.currentThread().getName());
                        // 实际这里可以通过BUS直接发送订阅事件，Test处订阅事件
                        BusSingleton.get().post(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onError当前所处线程: " + Thread.currentThread().getName());
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "接收到的error: " + e);
                    }

                    @Override
                    public void onComplete() {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onComplete当前所处线程: " + Thread.currentThread().getName());
                    }
                });
    }

    public static void testDiffBackProvider(final Context context, final String name, final String pwd) {
        Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                String s = null;
                int random = (int) (Math.random() * 3);
                if (random == 0) {
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "设置User 结果");
                    User user = new User();
                    user.setName(name);
                    user.setPassword(pwd);
                    s = LoganSquare.serialize(user);
                } else if (random == 1) {
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "设置Error 错误");
                    Error e = new Error();
                    e.code = ErrStatus.ERR_S_PARAM_ILLEGAL;
                    e.msg = ErrStatus.STR_ERR_S_PARAM_ILLEGAL;
                    s = LoganSquare.serialize(e);
                } else {
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "抛出网络异常");
                    throw new IOException("网络读取错误!");
                }
                AppDebugLog.d(AppDebugLog.TAG_UTIL, "正常执行到此");
                return s == null ? Observable.<String>empty() : Observable.just(s);
            }
        })
                .retryWhen(new RetryWhenFunction())
                .map(new MapperUniverseFunction<>(User.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "doOnTerminate当前所处线程: " + Thread.currentThread().getName());
                        DialogHelper.hideProgressDialog();

                    }
                })
                .subscribe(new Observer<SuperData<User>>() {

                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onSubscribe当前所处线程: " + Thread.currentThread().getName());

                        if (!NetworkUtil.isConnected(context)) {
                            // 此处采用 throw 抛出异常的话不会被捕捉而是直接崩溃，故直接dispose。
                            // 不过有另一个问题是这样只走 doOnDispose 而不进行后续操作，另外如果在 doOnSubscribe 调用 dispose 则连 doOnDispose 都不执行
                            AppDebugLog.d(AppDebugLog.TAG_UTIL, "doOnSubscribe抛出404网络异常");
                            if (!d.isDisposed())
                                d.dispose();
                            // 由于后续事件不会继续执行，需要手动调起通知网络失败
                            onError(new ApiException(ErrStatus.ERR_C_NET_FAIL_CONN, ErrStatus.STR_ERR_C_NET_FAIL_CONN));
                            return;
                        }
                        DialogHelper.showProgressDialog(context);
                        DialogHelper.setCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                AppDebugLog.d(AppDebugLog.TAG_UTIL, "DialogHelper.onCancel ");
                                if (mDisposable != null && !mDisposable.isDisposed()) {
                                    mDisposable.dispose();
                                    mDisposable = null;
                                }
                            }
                        });
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(SuperData<User> value) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onNext当前所处线程: " + Thread.currentThread().getName());
                        if (value == null) {
                            // value为Null，也是返回数据出错导致
                            AppDebugLog.d(AppDebugLog.TAG_UTIL, "value == null");
                            Toast.makeText(context.getApplicationContext(), ErrStatus.STR_ERR_C_BAD_CALLBACK_DATA, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value.isSuccess()) {
                            AppDebugLog.d(AppDebugLog.TAG_UTIL, "value.isSuccess");
                            // 实际这里可以通过BUS直接发送订阅事件，Test处订阅事件
                            RxBus.get().post(value.data);
                        } else {
                            // 这里是处理服务端返回的错误信息
                            if (value.e == null) {
                            AppDebugLog.d(AppDebugLog.TAG_UTIL, "value.e == null");
                                // 按理此处为空应当在格式化时抛出异常被 onError 处理
                                Toast.makeText(context.getApplicationContext(), ErrStatus.STR_ERR_C_BAD_CALLBACK_DATA, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            AppDebugLog.d(AppDebugLog.TAG_UTIL, "value.e");
                            Toast.makeText(context.getApplicationContext(), value.e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onError当前所处线程: " + Thread.currentThread().getName());
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "接收到的error: " + e);
                    }

                    @Override
                    public void onComplete() {
                        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onComplete当前所处线程: " + Thread.currentThread().getName());
                    }
                });
    }
}
