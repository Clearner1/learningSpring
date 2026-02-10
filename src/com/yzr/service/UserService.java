package com.yzr.service;

import com.yzr.spring.Autowired;
import com.yzr.spring.BeanNameAware;
import com.yzr.spring.Component;
import com.yzr.spring.Scope;

@Component("userService")
@Scope("singleton")
public class UserService implements BeanNameAware {

    public String beanName;
    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
