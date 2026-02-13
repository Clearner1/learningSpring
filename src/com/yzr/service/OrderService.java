package com.yzr.service;

import com.yzr.spring.Autowired;
import com.yzr.spring.Component;

@Component
public class OrderService {
    @Autowired
    private UserService userService;
}
