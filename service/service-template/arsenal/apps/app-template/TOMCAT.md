# 新建 tomcat 文件
cp /usr/software/tomcat/apache-tomcat-8.5.24/bin/catalina.sh /etc/init.d/tomcat
vi /etc/init.d/tomcat

# 在最前面增加如下代码
```

#!/bin/sh

#chkconfig:2345 10 90
#description:Tomcat service
export JAVA_HOME=/usr/software/java/jdk1.8.0_151/
export CATALINA_HOME=/usr/software/tomcat/apache-tomcat-8.5.24
export PATH=$PATH:$JAVA_HOME:$CATALINA_HOME


```

# 增加到系统服务列表
chmod 755 /etc/init.d/tomcat
chkconfig --add tomcat
chkconfig --list

# 测试
service tomcat start
service tomcat stop
service tomcat restart

# 设置为开机自动启动
# /etc/init.d目录下为系统服务脚本
# /etc/rc.local里面的内容为开机执行的脚本
