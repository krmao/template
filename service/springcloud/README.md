# Spring-Cloud Template Project

### 1. 微服务运行顺序
1. springcloud-discovery/eureka-server (or Cannot execute request on any known server)
2. app-a
3. app-b
4. springcloud-routing/zuul

### 2. 微服务编排生成 DOCKER 镜像并运行
```shell script
docker images
docker ps -a

docker-compose ps
docker-compose -f docker-compose.yml down
docker-compose ps

docker rm spingcloud-discovery-eureka-server --force
docker rmi krmao/spingcloud-discovery-eureka-server --force
docker rm spingcloud-routing-zuul --force
docker rmi krmao/spingcloud-routing-zuul --force
docker rm spingcloud-app-a --force
docker rmi krmao/spingcloud-app-a --force
docker rm spingcloud-app-b --force
docker rmi krmao/spingcloud-app-b --force

./gradlew eureka-server:clean eureka-server:jibDockerBuild --info --stacktrace
./gradlew zuul:clean zuul:jibDockerBuild --info --stacktrace
./gradlew app-a:clean app-a:jibDockerBuild --info --stacktrace
./gradlew app-b:clean app-b:jibDockerBuild --info --stacktrace

docker-compose -f docker-compose.yml up -d
docker-compose ps
```

### 3. 微服务生成单个 DOCKER 镜像并运行
```shell script
/**
 * create docker image by jib
 *
 * > ./gradlew eureka-server:clean eureka-server:jibDockerBuild --info --stacktrace
 * > docker run --rm -p5388:5388 -d --name spingcloud-discovery-eureka-server krmao/spingcloud-discovery-eureka-server
 * > curl localhost:5388
 * > docker rm spingcloud-discovery-eureka-server --force
 * > docker rmi krmao/spingcloud-discovery-eureka-server --force
 *
 * > ./gradlew eureka-server:clean eureka-server:jib --info --stacktrace
 * > docker pull krmao/spingcloud-discovery-eureka-server
 * > docker run --rm -p5388:5388 -d --name spingcloud-discovery-eureka-server krmao/spingcloud-discovery-eureka-server
 * > curl localhost:5388
 *
 * @see "https://github.com/GoogleContainerTools/jib/issues/2891#issuecomment-725708828"
 */
```

### 4. 其他
1. [IDEA Tools Services Windows](https://www.cnblogs.com/javalbb/p/12922238.html)
    > View->Tool Windows->Services
2. 压测工具 [JMeter](https://jmeter.apache.org/download_jmeter.cgi)
    * https://jmeter.apache.org/download_jmeter.cgi
3. 解决端口被占用问题
    >  org.springframework.boot.web.server.PortInUseException: Port 5388 is already in use
    ```shell script
    lsof -i tcp:5388
    kill -9 PID
    ```
4. MAC 修改 mysql 端口
    > 系统偏好设置->MYSQL->Configuration->Configuration File->vi ~/.mysql.conf->Apply->重启电脑
    ```.mysql.conf
    cat ~/.mysql.conf
    [mysqld]
    port=5382
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
    # | port          | 5382  |
    # +---------------+-------+
    # 1 row in set (0.01 sec)
    #
    # mysql> exit;
    # Bye
    ```
5. mysql 允许其它主机以 root 用户登录
    ```shell script
    # mysql -uroot -p
    # Enter password:
    # Welcome to the MySQL monitor.  Commands end with ; or \g.
    # Your MySQL connection id is 75
    # Server version: 8.0.21 MySQL Community Server - GPL
    #
    # Copyright (c) 2000, 2020, Oracle and/or its affiliates. All rights reserved.
    #
    # Oracle is a registered trademark of Oracle Corporation and/or its
    # affiliates. Other names may be trademarks of their respective
    # owners.
    #
    # Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
    #
    # mysql> use mysql;
    # Reading table information for completion of table and column names
    # You can turn off this feature to get a quicker startup with -A
    #
    # Database changed
    # mysql> update user set host = '%' where user = 'root';
    # Query OK, 1 row affected (0.01 sec)
    # Rows matched: 1  Changed: 1  Warnings: 0
    #
    # mysql> select host, user from user;
    # +-----------+------------------+
    # | host      | user             |
    # +-----------+------------------+
    # | %         | root             |
    # | localhost | mysql.infoschema |
    # | localhost | mysql.session    |
    # | localhost | mysql.sys        |
    # +-----------+------------------+
    # 4 rows in set (0.00 sec)
    #
    # mysql> flush privileges;
    # Query OK, 0 rows affected (0.01 sec)
    ```
6. yml 配置文件不能设置 中文注释, 在 windows 操作系统解析报错
7. 关于负载均衡
    1. 物理负载均衡 nginx
        > 当浏览器向后台发出请求的时候，会首先向反向代理服务器发送请求，反向代理服务器会根据客户端部署的ip：port映射表以及负载均衡策略，来决定向哪台服务器发送请求，一般会使用到nginx反向代理技术。
    2. 网关负载均衡 外部访问内部 zuul
    3. 内部服务之间互相调用的负载均衡 ribbon + eureka
    5. 参考
        a. https://www.cnblogs.com/smiledada/p/10607923.html
        b. https://blog.csdn.net/qq_38386438/article/details/107353718

### 5. TODO LIST
> [高可用](https://zhuanlan.zhihu.com/p/43723276)（High Availability）是分布式系统架构设计中必须考虑的因素之一，它通常是指，通过设计减少系统不能提供服务的时间。
* 高可用 eureka-client 集群, zuul 已经实现了
* 高可用 eureka-server 集群(eureka-server 本身是可能宕机的)
* 高可用 zuul 集群(zuul 本身是可能宕机的)
* nginx 是如何负载均衡的

### 6. 参考文档
* https://blog.eiyouhe.com/articles/2020/01/06/1578318104295.html
* https://www.cnblogs.com/huoli/p/11864959.html
* https://www.springcloud.cc/
* https://www.cnblogs.com/zhainan-blog/p/11634621.html
* [Kubernetes 部署 SpringCloud 网关 Zuul 1.x + Eureka 动态路由](http://www.mydlq.club/article/44/)

### tips
* IDEA -> View -> Tools Windows -> Services
