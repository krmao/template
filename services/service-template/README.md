### mysql
* [下载地址](https://cdn.mysql.com//Downloads/MySQL-5.7/mysql-5.7.20-macos10.12-x86_64.dmg)
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

### [druid](https://github.com/alibaba/druid)
    ♨ 为监控而生的数据库连接池！阿里云DRDS(https://www.aliyun.com/product/drds )、阿里巴巴TDDL 连接池powered by Druid https://github.com/alibaba/druid/wiki
    
    运行后访问 [http://localhost:7776/druid/index.html](http://localhost:7776/druid/index.html)