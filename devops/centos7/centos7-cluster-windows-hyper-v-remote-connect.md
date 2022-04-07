### Hyper-V端口映射
> 有时候我们为了让局域网或外网用户直接远程访问或访问虚拟机里的服务，可以将实机端口直接映射到Hyper-V的虚拟机里面，省去很多麻烦。

### 下面是hyper-v共享IP端口映射一些常用命令
1. 查询端口映射情况
    > netsh interface portproxy show v4tov4
2. 增加一个端口映射
    > netsh interface portproxy add v4tov4 listenport=5389 listenaddress=10.32.33.157 connectaddress=192.168.200.10 connectport=22
3. 删除一个端口映射
    > netsh interface portproxy delete v4tov4 listenaddress=10.32.33.157 listenport=5389