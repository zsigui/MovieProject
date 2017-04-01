package retrofit2.converter.logansquare;

import com.bluelinelabs.logansquare.LoganSquare;
import com.jackiez.base.util.AppDebugLog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import static com.bluelinelabs.logansquare.ConverterUtils.parameterizedTypeOf;

/**
 * Created by zsigui on 16-11-23.
 */

final class CustomLoganSquareResponseBodyConverter implements Converter<ResponseBody, Object> {

    private final Type type;

    CustomLoganSquareResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public Object convert(ResponseBody value) {
        try {
//            InputStream is = value.byteStream();
            String is = value.string();
            AppDebugLog.d(AppDebugLog.TAG_UTIL, "response : " + is);
            if (type instanceof Class) {
                // Plain object conversion
                return LoganSquare.parse(is, (Class<?>) type);

            } else if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                Type firstType = typeArguments[0];

                Type rawType = parameterizedType.getRawType();
                if (rawType == Map.class) {
                    return LoganSquare.parseMap(is, (Class<?>) typeArguments[1]);

                } else if (rawType == List.class) {
                    return LoganSquare.parseList(is, (Class<?>) firstType);

                } else {
                    // Generics
                    return LoganSquare.parse(is, parameterizedTypeOf(type));
                }
            }


        } catch (Throwable e) {
            AppDebugLog.d(AppDebugLog.TAG_NET, e);
        }
        return null;
    }
}