### bootRun

```shell script
./gradlew :springboot-admin:clean :springboot-admin:bootRun --info --stacktrace
```

### kill port

```shell script
lsof -i :5385
kill -9 pid
kill -9 $(lsof -nP -iTCP:5385 |grep LISTEN|awk '{print $2;}')
```

1. 查看指定端口占用情况

```
sudo lsof -nP -iTCP:5385 -sTCP:LISTEN
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
lsof -nP -iTCP:5385 |grep LISTEN|awk '{print $2;}'
```

5. 杀掉指定进程/端口

```
kill -9 PID
kill -9 $(lsof -nP -iTCP:5385 |grep LISTEN|awk '{print $2;}')
```

6. 杀掉所有java进程

```
kill -9 $(sudo lsof -nP -iTCP -sTCP:LISTEN | grep java | awk '{print $2}')
```

### 参考

* https://www.jianshu.com/p/9734ba34ec4b
