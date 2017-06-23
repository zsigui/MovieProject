package com.jackiez.zgithub.model.local;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.jackiez.zgithub.model.net.entity.RespEvent;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by zsigui on 17-5-19.
 */

public class DBLoader extends AsyncTaskLoader<RespEvent> {

    public DBLoader(Context context) {
        super(context);
    }

    @Override
    public RespEvent loadInBackground() {
        // dosth background work here
        for (int i = 0 ; i < 15; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        RespEvent event = new RespEvent();
        Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                return Observable.just("1");
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {

                    }
                });

        return event;
    }



}
