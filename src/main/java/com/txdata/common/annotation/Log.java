package com.txdata.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * 写上这个注解的方法将被LogAspect切面拦截来记录日志
 * @author huangmk
 * @since 2018-11-21
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
	String value() default "";
}
