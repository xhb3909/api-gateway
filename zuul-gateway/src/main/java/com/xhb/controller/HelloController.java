package com.xhb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haobo
 * @Date: 2020-01-05 15:24
 */
@RestController
public class HelloController {

    @RequestMapping("/local/hello")
    public String hello() {
        return "Hello World Local";
    }
}
