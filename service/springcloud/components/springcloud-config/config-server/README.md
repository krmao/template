### bootRun
```shell script
./gradlew :config-server:clean :config-server:bootRun --info --stacktrace
```

### kill port
```shell script
lsof -i :8888
kill -9 pid
```
