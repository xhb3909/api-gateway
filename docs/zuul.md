`zuul`
======

特点:

* 它作为系统的统一入口， 屏蔽了系统内部各个微服务的细节。
* 它可以与服务治理框架结合，实现自动化的服务实例维护以及负载均衡的路由转发。
* 它可以实现接口权限校验与微服务业务逻辑的解耦。
* 通过服务网关中的过炖器， 在各生命周期中去校验请求的内容， 将原本在对外服务
层做的校验前移， 保证了微服务的无状态性， 同时降低了微服务的测试难度， 让服
务本身更集中关注业务逻辑的处理。

配置参数

hystrix.command.default.execution.isolation.thread.timeoutinMillseconds

该 参 数可以用来设置API 网 关中路由转发请求的
HystrixCommand执行超时时间， 单位为毫秒。 当路由转发请求的命令执行时间
超过该配置值之后，Hystrix会将该执行命令标记为TIMEOUT并抛出异常，Zuul会
对该异常进行处理并返回如下JSON信息给外部调用方。

ribbon.ConnectTimeout

该参数用来设置路由转发请求的时候， 创建请求连
接的超时时间。当ribbon.ConnectTimeout的配置值小于hystrix.command.
default.execution.isolation.thread.timeoutlnMilliseconds 配置
值的时候， 若出现路由请求出现连接超时， 会自动进行重试路由请求

ribbon.ReadTimeout

该参数用来设置路由转发请求的超时时间。 它的处理与
ribbon.ConnectTimeout类似， 只是它的超时是对请求连接建立之后的处理时
间。

过滤器

filterType: 该函数需要返回字符串来代表过滤器的类型
* pre: 可以在请求被路由之前调用。
* routing: 在路由请求时被调用。
* post: 在 routing 和 error 过滤器之后被调用。
* error: 处理请求时发生错误时被调用。

filterOrder: 通过 int 值来定义过滤器的执行顺序， 数值越小优先级越高。

shouldFilter: 返回一个 boolean 值来判断该过滤器是否要执行。 我们可以通
过此方法来指定过滤器的有效范围。
run: 过滤器的具体逻辑。 在该函数中， 我们可以实现自定义的过滤逻辑， 来确定
是否要拦截当前的请求， 不对其进行后续的路由， 或是在请求路由返回结果之后，
对处理结果做一些加工等。


