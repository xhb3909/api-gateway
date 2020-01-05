package com.xhb.command;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.xhb.entity.User;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: haobo
 * @Date: 2020-01-04 22:35
 */
public class UserCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;
    private Long id;

    public UserCommand(Setter setter, RestTemplate restTemplate, Long id) {
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }

    public UserCommand() {
        // 设置命令组，hystrix会根据组来组织和统计命令的警告
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GroupName"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("commandName"))
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ThreadPoolKey")));
    }

    @Override
    protected User run() throws Exception {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
    }

    @Override
    protected User getFallback() {
        return new User();
    }

    /**
     * 开启请求缓存
     * 好处:
     * 减少重复的请求数， 降低依赖服务的并发度。
     * 在同一用户请求的上下文中， 相同依赖服务的返回数据始终保持一致。
     * 请求缓存在 run() 和 construct ()执行之前生效， 所以可以有效减少不必要的线程开销。
     * @return
     */
    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    public static void flushCache(Long id) {
        // 刷新缓存，根据id进行清理
        HystrixRequestCache.getInstance(HystrixCommandKey.Factory.asKey("CommandKey"),
                HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }

    // 同步执行 User u = new UserCommand(restTemplate, 1L).execute()
    // 异步执行: Future<User> futureUser = new UserCommand(restTemplate, 1L).queue();
}
