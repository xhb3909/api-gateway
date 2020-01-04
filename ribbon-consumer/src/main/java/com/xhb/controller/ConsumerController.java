package com.xhb.controller;

import com.xhb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: haobo
 * @Date: 2020-01-04 13:06
 */
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/ribbon-consumer", method = RequestMethod.GET)
    public String helloConsumer() {

        // 第一种方式
        // ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://USER-SERVICE/user?name={1}", String.class, "didi");

        // 第二种方式
//        UriComponents uriComponents = UriComponentsBuilder
//                .fromUriString("http://USER-SERVICE/user?name={name}")
//                .build()
//                .expand("dodo")
//                .encode();
//        URI uri = uriComponents.toUri();
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
    }

    @RequestMapping(value = "/ribbon-consumer", method = RequestMethod.POST)
    public String helloConsumerPost() {

        // postForObject
//        User user = new User("didi", 20);
//        String postResult = restTemplate.postForObject("ht七p: //USER-SERVICE/user", user,
//                String.class);

         // URI
//        User user = new User("didi", 40);
//        URI responseURI = restTemplate.postForLocation("http:/ /USER-SERVICE/user", user);

        User user = new User("didi", 30);
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("ht七p://USER-SERVICE/user", user, String.class);
        return responseEntity.getBody();

    }

    @RequestMapping(value = "ribbon-consumer", method = RequestMethod.PUT)
    public void helloConsumerPut(@RequestParam Long id) {
        User user = new User("didi", 40);
        restTemplate.put("http://USER-SERVICE/user/{l}", user, id);
    }

    @RequestMapping(value = "ribbon-consumer", method = RequestMethod.DELETE)
    public void helloConsumerDelete(@RequestParam Long id) {
        restTemplate.delete("http://USER-SERVICE/user/{1)", id);
    }


}
