# spring cloud

* 分布式/版本化配置
* 服务注册和发现
* 路由
* service - to - service 调用
* 负载均衡
* 断路器
* 分布式消息传递

eureka: 服务治理组件，包括服务注册中心、服务注册与发现机制的实现。
`Hystrix`: 容错管理组件，实现断路器模式
Ribbon: 客户端负载均衡的服务调用组件
Feign: 基于Ribbon和`Hystrix`的声明式服务调用组件
`Zuul`: 网关组件，提供智能路由、访问过滤等功能。 
`Archaius`: 外部化配置组件
Spring Cloud Bus: 事件、消息总线，用于传播集群中的状态变化或事件，以触发后续的处理
Spring Cloud Cluster: 针对 Zookeeper、Redis、Hazelcast、Consul的选举算法和通用状态模式的实现。

Ribbon 是一个基于HTTP和TCP的客户端负载均衡器

一个微服务应用只可以属于一个Region，不特别配置，默认为default

Ribbon实现服务调用时，对于Zone的设置可以在负载均衡时实现区域亲和特性: Ribbon的默认策略会优先访问同客户端处于一个Zone
中的服务端实例，只有当同一个Zone中没有可用服务端实例的时候才会访问其他Zone中的实例

注册中心存储了两层Map结构，第一层的key存储服务名: InstanceInfo中的appName属性，第二层的key存储实例名: InstanceInfo
中的instanceId属性。

