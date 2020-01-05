package com.xhb.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.xhb.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @Author: haobo
 * @Date: 2020-01-04 22:13
 */
@Service
public class HelloService {

    @Autowired
    private RestTemplate restTemplate;

    // Observable<String> ho = new UserCommand(restTemplate, 1L).observe();
    // 返回Hot Observable，调用observe()时会立即执行，当Observable每次被订阅的时候会重放它的行为
    // Observable<String> co = new UserCommand(restTemplate, 1L).toObservable();
    // 后者返回一个Cold Observable 调用toObservable()执行之后，命令不会被立即执行，只有当所有订阅者都订阅它之后才会执行.


    /**
     * 同步执行
     * @return
     */
    @HystrixCommand(fallbackMethod = "helloFallback", commandKey = "helloKey")
    public String helloService() {
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
    }

    /**
     * 异步执行
     * @param id
     * @return
     */
    @HystrixCommand
    public Future<User> getUserByIdAsync(final String id) {
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
            }
        };
    }

    /**
     * 用 @HystrixCommand 注解实现响应式命令时，
     * 可以通过 observableExecutionMode 参数来控制是使用 observe()
     * 还是toObservable()的执行方式
     *
     *  @HystrixCommand(observableExecutionMode = ObservableExecutionMode. EAGER) :
     *  EAGER是该参数的模式值， 表示使用 observe ()执行方式。
     *
     *  @HystrixCommand(observableExecutionMode = ObservableExecutionMode. LAZY):
     *  表示使用 toObservable()执行方式。
     * 参数有下面两种设置方式
     * @param id
     * @return
     */
    @HystrixCommand
    public Observable<User> getUserById(final String id) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        User user = restTemplate.getForObject("http://HELLO-SERVICE/users/{1}", User.class, id);
                        observer.onNext(user);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }

    /**
     * 通过ignoreExceptions忽略某个异常
     * @param id
     * @return
     */
    @HystrixCommand(ignoreExceptions = {HystrixBadRequestException.class})
    public User getUserById(Long id) {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
    }

    public String helloFallback() {
        return "error";
    }

    /**
     * 降级方法接收throwable对象
     * @param id
     * @return
     */
    @CacheResult
    @HystrixCommand(fallbackMethod = "fallback1", commandKey = "getUserById", groupKey = "UserGroup", threadPoolKey = "getUserByIdThread")
    public User getUserByIdError(String id) {
        throw new RuntimeException("getUserById command failed");
    }


    @CacheResult(cacheKeyMethod = "getUserByIdCacheKey")
    @HystrixCommand
    public User getUserByIdCache(Long id) {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
    }

    /**
     * 对象的id属性作为缓存key
     * @param user
     * @return
     */
    @CacheResult
    @HystrixCommand
    public User getUseById(@CacheKey("id") User user) {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, user.getId());
    }

    /**
     * remove掉缓存
     */
    @CacheRemove(commandKey = "getUserById")
    @HystrixCommand
    public void update(@CacheKey("id") User user) {
        restTemplate.postForObject("http://USER-SERVICE/users", user, User.class);
    }

    private Long getUserByIdCacheKey(Long id) {
        return id;
    }

    /**
     * 请求合并器
     * @param id
     * @return
     */
    @HystrixCollapser(batchMethod = "findAll", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
    })
    public User find(Long id) {
        return null;
    }

    public List<User> findAll(List<Long> ids) {
        return restTemplate.getForObject("http://USER-SERVICE/users?ids={1}",
                List.class, StringUtils.join(ids, ","));
    }

    public User fallback1(String id, Throwable e) {
        assert "getUserById commandd failed".equals(e.getMessage());
        return new User();
    }
}
