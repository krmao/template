# 工程说明
## 编译环境
* 必须使用 JDK1.8
* 必须使用 MYSQL 8
    * runtime('mysql:mysql-connector-java:8.0.21')
    * driver-class-name: com.mysql.cj.jdbc.Driver
    * 安装 MYSQL 8
        * https://dev.mysql.com/downloads/
        * 初始密码: 安装时记住随机生成的初始登录密码, 后面重置密码时会用到 例如 9j;#(3dMwj6g
        * 开启服务: 安装好后，进入系统偏好设置 -> mysql -> start mysql server (auto start)
        * 环境变量:
            ```
                vi ~/.baserc
                export PATH=$PATH:/usr/local/mysql/bin
                source ~/.baserc
            ```
        * 设置密码
            ```
            $ mysql -uroot -p
            Enter password:9j;#(3dMwj6g

            mysql> SET PASSWORD FOR 'root'@'localhost' = PASSWORD('777');
            mysql> exit
            ```
* 必须使用 LOG4J2
    * druid.filters: stat,wall,log4j2
* 数据库名称中不能包含中横线(MYSQL 8中执行 'schema-mysql.sql' 出现了 SQL 语句解析错误)
* 数据库连接中必须包含 &allowPublicKeyRetrieval=true
* 运行 gradle -> bootRun
* 访问静态网页路径 http://127.0.0.1:5388/template/index.html
* 一次成功的 Post 请求访问案例
    * 请求类型 POST
    * 请求路径 http://127.0.0.1:5388/template/test/message
    * 请求实体数据类型 JSON(application/json)
    * 请求实体数据 {}
    * 发送请求
    * 返回数据
        ```json
        {
            "code": 200,
            "message": "响应成功",
            "data": "hello hybird"
        }
        ```
* 运行 [druid](https://github.com/alibaba/druid)
    * ♨ 为监控而生的数据库连接池！阿里云DRDS(https://www.aliyun.com/product/drds )、阿里巴巴TDDL 连接池powered by Druid https://github.com/alibaba/druid/wiki
    * 运行后访问 [http://localhost:7776/druid/index.html](http://localhost:7776/druid/index.html)
