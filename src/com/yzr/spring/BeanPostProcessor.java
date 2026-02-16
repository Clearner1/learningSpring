package com.yzr.spring;

public interface BeanPostProcessor {
    // 前置处理是为了检查 Bean 的环境，然后给 Bean 准备环境和属性
    public Object postProcessBeforeInitialization(String name, Object bean);

    // 后置处理是进行AOP
    public Object postProcessAfterInitialization(String name, Object bean);
}
