# server-house-keeper

####1 过滤器 Filter(ResourceUrlEncodingFilter)
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