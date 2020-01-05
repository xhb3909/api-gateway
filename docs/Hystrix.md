`hystrix`
========

```

    @HystrixCommand(fallbackMethod = "getDynamicPhoneFallback",
            ignoreExceptions = {BusinessException.class},
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "5000"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "0")
            })

```

注解实现请求缓存

@CacheResult 标记请求命令返回的结果应该被缓存，必须与@HystrixCommand结合使用
@CacheRemove 让请求命令的缓存失效，失效的缓存根据定义的key决定
@CacheKey 

circuitBreaker断路器

Command属性

* execution配置
```
execution.isolation.strategy: 该属性用来设置HystrixCommand.run() 执行的隔离策略， 它有如下两个选项。
THREAD: 通过线程池隔离的策略。 它在独立的线程上执行， 并且它的并发限制
受线程池中线程数量的限制。
SEMAPHORE: 通过信号量隔离的策略。 它在调用线程上执行， 并且它的并发限
制受信号量计数的限制。
execution.isolation.thread.timeoutinMilliseconds: 该属性用来配
                                                  置HystrixCommand执行的超时时间， 单位为毫秒。 当HystrixCommand执行
                                                  时间超过该配置值之后， Hystrix会将该执行命令标记为TIMEOUT并进入服务降级
                                                  处理逻辑。
execution.timeout.enabled   该属性用来配置Hys七豆xCommand.run()的
                            执 行是否启用超时时间。 默认为true, 如果设置为false, 那么 execu巨on.
                            isola巨on.七hread.timeoutinM口liseconds属性的配置将不再起作用。
                            
execution.isolation.thread.interruptOnTimeout: 该属性用来配置当
                                                 HystrixCommand.run()执行超时的时候是否要将它中断。
execution.isolation.thread.interruptOnCancel: 执行被取消的时候是否要将它中断。

execution.isolation.semaphore.maxConcurrentRequests: : 当HystrixCommand
                                                     的隔离策略使用信号量的时候， 该属性用来配置信号量的大小（并发请求数）。 当最
                                                     大并发请求数达到该设置值时， 后续的请求将会被拒绝。 

```

* fallback 配置

```
fallback.isolation.semaphore. maxConcurrentRequests  该属性用来
                                                    设置从调用线程中允许HystrixComrnand.get Fallback()方法执行的最大并发
                                                    请求数。 当达到最大并发请求数时， 后续的请求将会被拒绝并抛出异常（因为它已
                                                    经没有后续的fallback可以被调用了）。
                                                    
fallback.enabled 该属性用来设置服务降级策略是否启用， 如果设置为false,
                 那么当请求失败或者拒绝发生时， 将不会调用Hys红骂Comrnand.ge七Fallback ()
                 来执行服务降级逻辑。

```

* circuitBreaker 断路器配置

```

circuitBreaker.enabled 该属性用来确定当服务请求命令失败时， 是否使用
                       断路器来跟踪其健康指标和熔断请求
                       
circuitBreaker.requestVolumeThreshold : 该属性用来设置在滚动时间窗
                                      中，断路器熔断的最小请求数。 例如，默认该值为 20 的时候，如果滚动时间窗（默
                                      认10秒）内仅收到了19个请求， 即使这19个请求都失败了，断路器也不会打开。

circuitBreaker.sleepWindowinMilliseconds 该属性用来设置当断路器
                                         打开之后的休眠时间窗。 休眠时间窗结束之后，会将断路器置为 “ 半开” 状态， 尝
                                         试熔断的请求命令，如果依然失败就将断路器继续设置为 “打开” 状态，如果成功
                                         就设置为 “ 关闭 ” 状态。
circuitBreaker.errorThresholdPercentage   该属性用来设置断路器打开
                                          的错误百分比条件。 例如，默认值为 5000 的情况下，表示在滚动时间窗中，在请求
                                          数量超过circuitBreaker.requestVolumeThreshold阅值的前提下，如果
                                          错误请求数的百分比超过50, 就把断路器设置为 “ 打开” 状态， 否则就设置为 “ 关
                                          闭
                                          ” 状态。      
circuitBreaker.forceOpen    : 如果将该属性设置为 true, 断路器将强制进入“ 打
                            开
                            ” 状态， 它会拒绝所有请求。 该属性优先于 circui七Breaker.forceClosed
                            属性。    
circui七Breaker.forceClosed 如果将该属性设置为 true, 断路器将强制进入
                           “ 关闭 ” 状态， 它会接收所有请求。 如果 c江cuitBreaker.forceOpen 属性为
                           true, 该属性不会生效。
```

* metrics 配置

```
• metrics.rollingStats.timeinMilliseconds 该属性用来设置滚动时间窗
                                        的长度， 单位为毫秒。 该时间用于断路器判断健康度时需要收集信息的持续时间。
                                        断路器在收集指标信息的时候会根据设置的时间窗长度拆分成多个“ 桶
                                        ” 来累计各
                                        度量值， 每个“ 桶
                                        ” 记录了 一段时间内的采集指标。 例如， 当采用默认值10000毫
                                        秒时， 断路器默认将其拆分成10个桶（桶的数量也可通过metrics.rollingStats.numBuckets参数设置）， 每个桶记录1000毫秒内的指标信息。
metrics.rollingstats.numBuckets 该属性用来设置滚动时间窗统计指标
                               信息时划分 “ 桶
                               ” 的数量。
metrics.rollingPercentile.timeinMilliseconds 该属性用来设置百分
                                             位统计的滚动窗口的持续时间， 单位为毫秒。
```

* requestContext 配置

```
requestCache.enabled 此属性用来配置是否开启请求缓存。

requestLog.enabled 该属性用来设置Hys立ixCommand的执行和事件是否
                   打印日志到HystrixRequestLog中。

```

dashboard 配置

http://hystrix-app:port/hystrix.stream
