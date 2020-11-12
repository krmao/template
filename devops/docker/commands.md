## DOCKER COMMANDS
### 镜像相关指令
* 查看镜像列表
    ```shell script
    docker images     # 等同于 docker image ls
    docker images -q  # 查看所有镜像的 IMAGE ID
    ```
* 查看某个镜像信息
    ```shell script
    docker inspect krmao/spingcloud-discovery-eureka-server
    docker inspect krmao/spingcloud-discovery-eureka-server | grep Entrypoint   # 查看入口信息
    docker inspect java:8 | grep Entrypoint
    ```
* 删除所有的镜像
    ```shell script
    docker rmi $(docker images -q)
    ```
* 删除某个镜像
    ```shell script
    docker rmi krmao/spingcloud-discovery-eureka-server:0.0.1
    docker rmi krmao/spingcloud-discovery-eureka-server:0.0.1 --force
    ```
* 运行镜像
    ```shell script
    # -d 后台运行
    docker run -d -p 5388:5388 --name spingcloud-discovery-eureka-server krmao/spingcloud-discovery-eureka-server
    ```
  
---
     
### 容器相关指令   
* 查看正在运行的容器
    ```shell script
    docker ps
    docker ps -a
    docker ps -aq
    docker ps --filter status=running   # running/exited/paused/dead
    ```
* 停止所有容器
    ```shell script
    docker stop $(docker ps -aq)
    ```
* 停止单个容器
    ```shell script
    docker stop krmao/spingcloud-discovery-eureka-server
    ```
* 删除所有容器
    ```shell script
    docker rm $(docker ps -aq)
    ```
* 删除单个容器
    ```shell script
    docker rm krmao/spingcloud-discovery-eureka-server
    ```
* 查看单个容器日志
    ```shell script
    docker logs spingcloud-routing-zuul
    docker logs spingcloud-routing-zuul -f # Follow log output
    ```
* 进入单个容器命令行
    ```shell script
    docker exec -it spingcloud-mysql bash
    mysql -uroot -p
    exit
    exit
    ```
* 查看网络环境状态
    ```shell script
    docker network ls
    ```
``
--- 容器编排
* 运行容器栈
    ```shell script
    docker-compose -f docker-compose.yml up -d
    ```
* 运行单个容器栈
    ```shell script
    docker-compose -f docker-compose.yml up -d spingcloud-mysql
  
    # 查看日志
    docker-compose logs spingcloud-mysql
    # 进入控制台
    docker exec -it spingcloud-mysql bash
    # 登录 mysql
    mysql -uroot -p
    # 退出 mysql
    exit
    # 退出控制台
    exit
    ```
* 停止单个容器栈
    ```shell script
    docker-compose -f docker-compose.yml stop spingcloud-mysql
    ```
* 重新运行单个容器栈
    ```shell script
    docker-compose -f docker-compose.yml start spingcloud-mysql
    docker-compose -f docker-compose.yml restart spingcloud-mysql
    ```
* 查看容器栈运行状态
    ```shell script
    docker-compose ps
    ```
* 停止并删除容器栈
    ```shell script
    docker-compose -f docker-compose.yml down
    ```
* 查看日志
    ```shell script
    docker-compose logs
    docker-compose logs spingcloud-app-b
    ```

---

### 常见问题
* docker load' command failed with error: mkdir /var/lib/docker/tmp/docker-import-042855953: read-only file system
    1. https://stackoverflow.com/a/52872667/4348530
