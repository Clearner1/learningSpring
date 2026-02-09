package com.yzr.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 去哪一个包下面找Component
@Retention(RetentionPolicy.RUNTIME) // 在运行时，JVM会将这个注解进行加载
@Target(ElementType.TYPE) // 标注注解必须在类上
public @interface ComponentScan {
    // default "" 不需要强制传参，不传参时是 ""
    String value() default "";
}
