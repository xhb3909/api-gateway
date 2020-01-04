ribbon
======

spring cloud ribbon封装
* 服务提供者需要启动多个服务实例注册到一个注册中心或是多个相关联的服务注册中心。
* 服务消费者直接通过调用被@LoadBalanced注解修饰过的RestTemplate来实现面向服务的接口调用.

自动化配置类中，做三件事
* 创建了一个LoadBalanceInterceptor的Bean,用千实现对客户端发起请求时进行拦截，以实现客户端负载均衡。
* 创建了一个RestTemplateCustomizer的Bean, 用于给RestTemplate增加LoadBalanceInterceptor拦截器。
* 维护了一个被@LoadBalanced 注解修饰的RestTemplate对象列表，并在这里进行初始化，
通过调用RestTemplateCustomizer的实例来给需要客户端负载均衡的RestTemplate增加LoadBalanceInterceptor拦截器。


Spring Cloud Ribbon 自动化构建接口:
* IClientConfig: Ribbon的客户端配置, 默认采用 com.netflix.client.config.DefaultClientConfigImpl实现.
* IRule: Ribbon的负载均衡策略, 默认采用 com.netflix.loadBalance.ZoneAvoidanceRule实现，该策略能够在多区域环境下选出
最佳区域的实例进行访问
* IPing: Ribbon的实例检查策略，默认采用 com.netflix.loadBalance.NoOpPing实现，该检查策略是一个特殊的实现，实际上并不会检查
实例是否可用，而是始终返回true，默认认为服务实例都是可用的。
* ServerList<Server>: 服务实例清单的维护机制，默认采用 com.netflix.loadBalance.ConfigurationBasedServerList实现。
* ServerListFilter<Server>: 服务实例清单过滤机制，默认采用`org.springframework.clud.netflix.ribbon.ZonePreferenceServerListFilter`
实现，该策略能够优先过滤出于请求调用方出于同区域的服务实例。
* `ILoadBalancer`: 负载均衡器，默认采用 `com.netflix.loadbalancer.ZoneAwareLoadBalancer`实现，具备区域感知能力。

把客户端的IPing接口实现替换为PingUrl，只需要在application.properties配置中增加下面的内容:
`hello-service.ribbon.NFLoadBalancerPingClassName=com.netflix.loadbalancer.PingUrl`

参数配置:
* ribbon.ConnectTimeout=250 ribbon创建连接的超时时间
* ribbon.eureka.enable=false 禁用eureka对ribbon服务实例的维护
