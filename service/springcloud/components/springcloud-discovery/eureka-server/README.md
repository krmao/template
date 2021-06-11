### bootRun
```shell script
./gradlew :eureka-server:clean :eureka-server:bootRun --info --stacktrace
```

### kill port
```shell script
lsof -i :5388
kill -9 pid
```
