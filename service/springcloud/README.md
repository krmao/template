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
