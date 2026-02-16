package com.yzr.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.yzr.spring.BeanPostProcessor;
import com.yzr.spring.Component;

@Component
public class YZRBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(String name, Object bean) {
        if (name.equals("userService")) {
            System.out.println("YZRBeanPostProcessor Aware userService");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String name, Object bean) {
        if (name.equals("userService")) {
            // AOP 核心：返回代理对象而不是原始对象
            return Proxy.newProxyInstance(YZRBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("AOP 代理前置逻辑: " + method.getName());
                            // 执行原始对象的方法
                            Object result = method.invoke(bean, args);
                            System.out.println("AOP 代理后置逻辑");
                            return result;
                        }
                    });
        }
        return bean;
    }

}
