package com.yzr.service;

import com.yzr.spring.YzrApplicationContext;

public class Test {
    public static void main(String[] args) {
        // 传递配置类进入容器
        // 通过配置类注册为bean
        YzrApplicationContext applicationContext = new YzrApplicationContext(AppConfig.class);
        UserService bean = (UserService) applicationContext.getBean("userService");
        bean.test();
        String beanName = bean.beanName;
        System.out.println(beanName);

    }
}
