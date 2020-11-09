## [install docker for mac](https://docs.docker.com/docker-for-mac/install/)

### [download docker desktop for mac](https://hub.docker.com/editions/community/docker-ce-desktop-mac/)

### run
* 在应用里找到 docker 并打开, 状态栏出现 docker 图标, 接下来就可以进行各种操作了

### 运行案例

* 下载
```shell script
docker run --name repo alpine/git clone https://github.com/docker/getting-started.
git
```

* 编译
```
cd getting-started 
docker build -t docker101tutorial . 
```

* error An unexpected error occurred: "https://registry.yarnpkg.com/body-parser/-/body-parser-1.19.0.tgz: getaddrinfo EAI_AGAIN registry.yarnpkg.com".
> https://blog.csdn.net/parsebobo/article/details/90670886
```shell script
npm install -g npm
docker build -t docker101tutorial . 
```

* 运行
```shell script
docker run -d -p 80:80 --name docker-tutorial docker101tutorial
```

* 保存以及分享
> https://hub.docker.com/repository/docker/krmao/docker101tutorial
```shell script
docker tag docker101tutorial krmao/docker101tutorial 
LPT45573:getting-started krmao$ docker push krmao/docker101tutorial
```

### 文档
* [本地文档](http://localhost/tutorial/)
* [中文文档](http://www.dockerinfo.net/image%e9%95%9c%e5%83%8f)
* [中文文档](https://www.widuu.com/docker/installation/mac.html)