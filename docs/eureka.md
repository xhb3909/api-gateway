eureka
======

eureka客户端配置主要分为以下两方面:
* 服务注册相关的配置信息，包括服务注册中心的地址、服务获取的间隔时间、可用区域等。
* 服务实例相关的配置信息，包括服务实例的名称、IP地址、端口号、健康检查路径等

EurekaClientConfigBean 类:
```sbtshell
参数名                             说明                  默认值

enabled                         启用eureka客户端         true
registryFetchIntervalSeconds    从eureka服务端获取注册信息的时间间隔，单位为秒  30
instanceInfoReplicationIntervalSeconds  更新实例信息的变化到eureka服务端的时间间隔    30
initialInstanceReplicationIntervalSeconds   初始化实例信息到eureka服务端的间隔时间，单位为秒 40
eurekaServiceUrlPollIntervalSeconds 轮询eureka服务端地址更改的间隔时间    300
eurekaServerReadTimeoutSeconds  读取eurekaServer信息的超时时间   8

```

设置eureka client的实例id
eureka.instance.instanceId=${spring.application.name}:${random.int}
应用名称加随机数区分不同实例

把eureka客户端的健康检查交给spring-boot-actuator模块的/health端点。
* pom.xml中引入spring-boot-starter-actuator模块的依赖
* 在application.properties中增加参数配置`eureka.client.healthcheck.enable`=true
