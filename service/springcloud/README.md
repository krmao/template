# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/gradle-plugin/reference/html/#build-image)

### Guides
The following guides illustrate how to use some features concretely:

* [Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Mine
* https://blog.eiyouhe.com/articles/2020/01/06/1578318104295.html
* https://www.cnblogs.com/huoli/p/11864959.html
* https://www.springcloud.cc/
* https://www.cnblogs.com/zhainan-blog/p/11634621.html

### [IDEA Tools Services Windows](https://www.cnblogs.com/javalbb/p/12922238.html)
> View->Tool Windows->Services

### 压测工具 [JMETER](https://jmeter.apache.org/download_jmeter.cgi)
* https://jmeter.apache.org/download_jmeter.cgi

### 端口被占用
>  org.springframework.boot.web.server.PortInUseException: Port 5388 is already in use

```shell script
lsof -i tcp:5388
kill -9 PID
```

### MAC 修改 mysql 端口
> 系统偏好设置->MYSQL->Configuration->Configuration File->vi ~/.mysql.conf->Apply->重启电脑

```.mysql.conf
cat ~/.mysql.conf
[mysqld]
port=5378
```

```shell script
# 查询端口号命令：
# % mysql -uroot -p
# Enter password:
# Welcome to the MySQL monitor.  Commands end with ; or \g.
# Your MySQL connection id is 8
# Server version: 8.0.21 MySQL Community Server - GPL
#
# Copyright (c) 2000, 2020, Oracle and/or its affiliates. All rights reserved.

# Oracle is a registered trademark of Oracle Corporation and/or its
# affiliates. Other names may be trademarks of their respective
# owners.
#
# Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
#
# mysql> show global variables like 'port';
# +---------------+-------+
# | Variable_name | Value |
# +---------------+-------+
# | port          | 5378  |
# +---------------+-------+
# 1 row in set (0.01 sec)
#
# mysql> exit;
# Bye
```

# 启动顺序
1. 先开启 springcloud-discovery, 否则 Cannot execute request on any known server
2. 再开启 app
3. 最后开启 springcloud-routing
