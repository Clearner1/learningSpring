package com.yzr.service;

import com.yzr.spring.Autowired;
import com.yzr.spring.BeanPostProcessor;
import com.yzr.spring.Component;

@Component
public class OrderService implements BeanPostProcessor {
    @Autowired
    private UserInterface userService;

    @Override
    public Object postProcessBeforeInitialization(String name, Object bean) {
        System.out.println("11111");

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String name, Object bean) {
        System.out.println("22222");

        return bean;
    }
}
