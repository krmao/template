### spring security filter 执行顺序
    这些都是内置的Filter，请求会从上往下依次过滤。这里由于我们主要关心用户名和密码的验证，
    所以就要从UsernamePasswordAuthenticationFilter 下手了。（UsernamePasswordAuthenticationFilter需要AuthenticationManager去进行验证。）


### [Security filter chain](http://blog.csdn.net/zshake/article/details/25233093)
    ConcurrentSessionFilter
    SecurityContextPersistenceFilter
    LogoutFilter
    UsernamePasswordAuthenticationFilter
    RequestCacheAwareFilter
    SecurityContextHolderAwareRequestFilter
    RememberMeAuthenticationFilter
    AnonymousAuthenticationFilter
    SessionManagementFilter
    ExceptionTranslationFilter
    FilterSecurityInterceptor
  
### 相关文档
```
[http://www.jianshu.com/p/ec9b7bc47de9](http://www.jianshu.com/p/ec9b7bc47de9)
[http://www.jianshu.com/p/6307c89fe3fa](http://www.jianshu.com/p/6307c89fe3fa)
[http://www.jianshu.com/p/4ef9881090ec](http://www.jianshu.com/p/4ef9881090ec)
```
