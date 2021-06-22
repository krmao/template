### 文档
* [教程](https://www.baeldung.com/spring-security-oauth-auth-server)
* https://docs.spring.io/spring-security/site/docs/current/reference/html5/#introduction
* [迁移指南](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide)
### bootRun
```shell script
./gradlew :auth-server:clean :auth-server:bootRun --info --stacktrace
```


### kill port

```shell script
lsof -i :9000
kill -9 pid
kill -9 $(lsof -nP -iTCP:9000 |grep LISTEN|awk '{print $2;}')
```

1. 查看指定端口占用情况

```
sudo lsof -nP -iTCP:9000 -sTCP:LISTEN
```

2. 查看所有进程监听的端口

```
sudo lsof -nP -iTCP -sTCP:LISTEN
```

3. 查看所有java进程监听的端口

```
sudo lsof -nP -iTCP -sTCP:LISTEN | grep java
```

4. 输出占用该端口的 PID

```
lsof -nP -iTCP:9000 |grep LISTEN|awk '{print $2;}'
```

5. 杀掉指定进程/端口

```
kill -9 PID
kill -9 $(lsof -nP -iTCP:9000 |grep LISTEN|awk '{print $2;}')
```

6. 杀掉所有java进程

```
kill -9 $(sudo lsof -nP -iTCP -sTCP:LISTEN | grep java | awk '{print $2}')
```

### 参考

* https://www.jianshu.com/p/9734ba34ec4b
