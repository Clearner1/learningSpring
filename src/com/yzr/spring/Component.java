package com.yzr.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 找到需要注入容器的Bean
@Retention(RetentionPolicy.RUNTIME) // 在运行时，JVM会将这个注解进行加载
@Target(ElementType.TYPE) // 标注注解必须在类上
public @interface Component {
    String value() default "";
}
