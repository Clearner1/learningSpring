package com.yzr.service;

import com.yzr.spring.Autowired;
import com.yzr.spring.BeanNameAware;
import com.yzr.spring.Component;
import com.yzr.spring.InitializingBean;
import com.yzr.spring.Scope;

@Component("userService")
@Scope("singleton")
public class UserService implements BeanNameAware, InitializingBean, UserInterface {

    public String beanName;
    // @Autowired
    // private OrderService orderService;

    public void test() {
        System.out.println("userService");
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Initializing");
    }

}
