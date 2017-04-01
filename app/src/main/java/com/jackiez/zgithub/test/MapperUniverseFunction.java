package com.jackiez.zgithub.test;

import com.bluelinelabs.logansquare.LoganSquare;
import com.jackiez.base.assist.rx.ApiException;
import com.jackiez.base.common.ErrStatus;
import com.jackiez.base.util.AppDebugLog;
import com.jackiez.zgithub.test.data.Error;
import com.jackiez.zgithub.test.data.SuperData;

import io.reactivex.functions.Function;
/**
 * 转换数据
 *
 * Created by zsigui on 17-3-28.
 */

//public class MapperUniverseFunction<T extends Error> implements Function<String, SuperData<T>> {
public class MapperUniverseFunction<T> implements Function<String, SuperData<T>> {

    private Class<T> mClass;

    MapperUniverseFunction(Class<T> c) {
        mClass = c;
    }

    @Override
    public SuperData<T> apply(String s) throws Exception {
        SuperData<T> result = null;
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "MapperUniverseFunction 开始处理: " + s);
//        if (LoganSquare.supports(mClass)
//                && LoganSquare.supports(SuperData.class)) {
            AppDebugLog.d(AppDebugLog.TAG_UTIL, "开始执行数据转换: " + s);
            result = new SuperData<>();
            try {
                /**
                 * 说明：如果 json 结构正常，如 {"code":30001,"msg":"参数不合法"}，则调用 LoganSquare.parse()一定会解析成功，
                 * 区别只是属性是否为空，此处由于 error_code 不为0，所以先解析为 ERROR 进行判断，如果没异常且 Error 实例内容为空，
                 * 则尝试解析对应数据类型T。
                 *
                 * 另外的处理方式，可以是 T extends Error，也就是让实际的数据继承于 ERROR
                 */
                result.e = LoganSquare.parse(s, Error.class);
                if (result.e == null || result.e.isFailed()) {
                    result.data = LoganSquare.parse(s, mClass);
                }
//                result.data = LoganSquare.parse(s, mClass);
//                AppDebugLog.d(AppDebugLog.TAG_UTIL, "result.data = " + result.data.toString());
//                if (!result.data.isFailed()) {
//                    Error e = new Error();
//                    e.code = result.data.code;
//                    e.msg = result.data.msg;
//                    result.e = e;
//                    result.data = null;
//                }
            } catch (Throwable e1) {
                AppDebugLog.d(AppDebugLog.TAG_UTIL, "不能处理的错误内容1: " + e1);
                throw new ApiException(e1, ErrStatus.ERR_C_BAD_CALLBACK_DATA, ErrStatus.STR_ERR_C_BAD_CALLBACK_DATA);
            }
//        }
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "执行结果：" + result);
        return result;
    }
}
