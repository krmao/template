# server-house-keeper

####1 过滤器 [Servlet Filter](http://www.runoob.com/servlet/servlet-writing-filters.html)(ResourceUrlEncodingFilter)
   * 使用范围: filter 是 servlet 规范规定的，只能用于 web 程序中
   * 规范   : filter 是在 servlet 规范中定义的，是 servlet 容器支持的
   * 深度   : filter 只能在 servlet 前后起到作用
   
   * web.xml 中注册
   * 可过滤所有请求(包括静态资源的访问)
   * 只依赖 servlet 容器,与 spring mvc 框架没有任何关系
   * 多个过滤器的执行顺序是:
     ```
         a.doFilterBefore()
         b.doFilterBefore()
         c.doFilterBefore()
         ...
         c.doFilterAfter()
         b.doFilterAfter()
         a.doFilterAfter()
     ```
   
####2 拦截器 HandlerInterceptor 
   * 使用范围: 拦截器既可以用于 web 程序，也可以用于 application/swing 程序中
   * 规范   : 拦截器是在 spring 容器内的，是spring 框架支持的
   * 使用资源: 拦截器是一个 spring 的组件，归spring 管理，配置在 spring 文件中，因此能使用spring 里的任何资源/对象
              例如 service 对象/数据源/事务管理等，通过IoC注入到拦截器即可，而Filter则不能
   * 深度   : 拦截器能够深入到方法前后/异常抛出前后灯，因此拦截器具的使用具有更大的弹性，所以在spring架构的程序中，优先使用拦截器           
   
   * 在 spring mvc 的配置文件中注册, 先配置先执行
   * 有三个方法:
     ```
         preHandle:         在DispatcherServlet之前执行
         postHandle         在controller执行之后的DispatcherServlet之后执行 
         afterCompletion    在页面渲染完成返回给客户端之前执行

     ```
   * 执行顺序(与Filter)  
     ```
         a.doFilterBefore()
         b.doFilterBefore()
         c.doFilterBefore()
         ======================================
         
         xInterceptor.preHandle()
         yInterceptor.preHandle()
         zInterceptor.preHandle()
         
         **** controller executer ****
         
         xInterceptor.postHandle()
         yInterceptor.postHandle()
         zInterceptor.postHandle()
         
         **** jsp loading now ****
         
         xInterceptor.afterCompletion()
         yInterceptor.afterCompletion()
         zInterceptor.afterCompletion()
         
         ======================================
         c.doFilterAfter()
         b.doFilterAfter()
         a.doFilterAfter()
     ```
   * 只能拦截controller 里面的请求
   
####3 监听器   


#### 总结

```
个人认为过滤是一个横向的过程，首先把客户端提交的内容进行过滤(例如未登录用户不能访问内部页面的处理)；过滤通过后，
拦截器将检查用户提交数据的验证，做一些前期的数据处理，接着把处理后的数据发给对应的Action；Action处理完成返回后，
拦截器还可以做其他过程，再向上返回到过滤器的后续操作。
过滤器（Filter）：当你有一堆东西的时候，你只希望选择符合你要求的某一些东西。定义这些要求的工具，就是过滤器。
拦截器（Interceptor）：在一个流程正在进行的时候，你希望干预它的进展，甚至终止它进行，这是拦截器做的事情。
监听器（Listener）：当一个事件发生的时候，你希望获得这个事件发生的详细信息，而并不想干预这个事件本身的进程，这就要用到监听器。
```

#### [spring 是如何封装 servlet](http://blog.csdn.net/initphp/article/details/38171219)

#### web.xml配置各节点说明
* <filter>指定一个过滤器。
* <filter-name>用于为过滤器指定一个名字，该元素的内容不能为空。
* <filter-class>元素用于指定过滤器的完整的限定类名。
* <init-param>元素用于为过滤器指定初始化参数，它的子元素<param-name>指定参数的名字，<param-value>指定参数的值。
* 在过滤器中，可以使用FilterConfig接口对象来访问初始化参数。
* <filter-mapping>元素用于设置一个 Filter 所负责拦截的资源。一个Filter拦截的资源可通过两种方式来指定：Servlet 名称和资源访问的请求路径
* <filter-name>子元素用于设置filter的注册名称。该值必须是在<filter>元素中声明过的过滤器的名字
* <url-pattern>设置 filter 所拦截的请求路径(过滤器关联的URL样式)
* <servlet-name>指定过滤器所拦截的Servlet名称。
* <dispatcher>指定过滤器所拦截的资源被 Servlet 容器调用的方式，可以是REQUEST,INCLUDE,FORWARD和ERROR之一，默认REQUEST。用户可以设置多个<dispatcher>子元素用来指定 Filter 对资源的多种调用方式进行拦截。
* <dispatcher>子元素可以设置的值及其意义
* REQUEST：当用户直接访问页面时，Web容器将会调用过滤器。如果目标资源是通过RequestDispatcher的include()或forward()方法访问时，那么该过滤器就不会被调用。
* INCLUDE：如果目标资源是通过RequestDispatcher的include()方法访问时，那么该过滤器将被调用。除此之外，该过滤器不会被调用。
* FORWARD：如果目标资源是通过RequestDispatcher的forward()方法访问时，那么该过滤器将被调用，除此之外，该过滤器不会被调用。
* ERROR：如果目标资源是通过声明式异常处理机制调用时，那么该过滤器将被调用。除此之外，过滤器不会被调用。