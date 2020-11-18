### CENTOS 7 WINDOWS10 虚拟机集群
|主机名|root密码|
|--|--|
|k8s-master-1 |88888888|
|k8s-node-1   |88888888|
|k8s-node-2   |88888888|
|k8s-node-3   |88888888|

---

### 创建步骤
1. 下载 [CentOS-7-x86_64-Minimal-2009 iso 镜像](https://mirrors.aliyun.com/centos/7.9.2009/isos/x86_64/CentOS-7-x86_64-Minimal-2009.torrent)
2. 在 windows10中开启 Hyper-V, 然后新建 CENTOS7 虚拟机时选择默认的内部网络交换机(如果新建一个外部网络交换机, 会发现只有第一台虚拟机才能自动获取网络, 其它虚拟机皆不能上网)
3. 安装虚拟机时设置网络类型为 ip4 dchp address 固定ip 192.168.220.100, 连接网络成功后再安装, 安装成功后 ping www.baidu.con 确认可以上网, 然后 yum upgrade 升级更新
4. 复制虚拟机, copy 以上新生成的并且更新后的虚拟机副本到新的文件夹, 在 Hyper-V 中导入虚拟机指向副本文件夹->复制虚拟机->将虚拟机存储在其它位置
5. 虚拟机配置文件夹->D:\Hyper-V\
6. 检查点存储文件夹->D:\Hyper-V\k8s-node-2\ (填写想要创建的虚拟机名称)
7. 智能分页文件夹  ->D:\Hyper-V\k8s-node-2\ (填写想要创建的虚拟机名称)
8. 虚拟硬盘存储目录->D:\Hyper-V\k8s-node-2\Virtual Hard Disks\ (填写想要创建的虚拟机名称) ->完成
9. Hyper-V 中修改虚拟机名称为 k8s-node-2, 启动并连接虚拟机
10. 查询主机名称 hostname && hostnamectl && hostnamectl status
11. 设置主机名称 hostnamectl set-hostname k8s-node-2
12. 修改固定ip vi /etc/sysconfig/network-scripts/ifcfg-eth0
    ```
    IPADDR="192.168.200.102"
    ```
13. reboot
14. ping www.baidu.com && ip a s && yum upgrade
15. hyper-v 管理器中保存虚拟机并创建检查点方便还原

---

### 宿主机与虚拟机互相 ping 通 亲测失败！
1. 经过以上步骤(使用网络地址转换NAT+端口转发的方式), 虚拟机可以直接ping通宿主机, 但是宿主机无法ping通虚拟机
2. win10桌面右击此电脑->属性->远程设置->允许远程连接
3. 关闭 win10 防火墙
4. 关闭 centos7 防火墙
    > systemctl stop firewalld.service && firewall-cmd --state
5. 宿主机控制面板->网络和 Internet -> 网络连接 -> 选中本地连接 + ctrl + 同时选中默认的 hyper-v 虚拟交换机 + 右击桥接
### 参考
* https://blog.csdn.net/psimper/article/details/107016713
* https://blog.csdn.net/weixin_36914964/article/details/104261171