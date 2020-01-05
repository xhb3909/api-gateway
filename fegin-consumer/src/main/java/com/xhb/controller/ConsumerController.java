package com.xhb.controller;

import com.xhb.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haobo
 * @Date: 2020-01-05 12:01
 */
@RestController
public class ConsumerController {

    @Autowired
    HelloService helloService;

    @RequestMapping(value = "/fegin-consumer", method = RequestMethod.GET)
    public String helloConsumer() {
        return helloService.hello();
    }
}
