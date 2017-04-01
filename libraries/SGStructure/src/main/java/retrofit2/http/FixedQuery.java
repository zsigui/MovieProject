package retrofit2.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义添加固定参数
 *
 * Created by zsigui on 17-3-22.
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedQuery {

    /**
     * 固定参数的键名
     */
    String key();

    /**
     * 固定参数的值
     */
    String value();

    /**
     * Specifies whether the parameter {@linkplain #value() name} and value are already URL encoded.
     */
    boolean encoded() default false;
}
