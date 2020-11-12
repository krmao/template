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
  
### 常见问题
* docker load' command failed with error: mkdir /var/lib/docker/tmp/docker-import-042855953: read-only file system
    1. https://stackoverflow.com/a/52872667/4348530
