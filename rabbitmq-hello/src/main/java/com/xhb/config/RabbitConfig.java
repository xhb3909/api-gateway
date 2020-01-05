package com.xhb.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: haobo
 * @Date: 2020-01-05 17:45
 */
@Configuration
public class RabbitConfig {

    public Queue helloQueue() {
        return new Queue("hello");
    }


}
