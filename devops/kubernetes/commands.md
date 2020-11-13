## KUBERNETES COMMANDS
### 相关指令
* 切换上下文
    ```shell script
    kubectl config use-context docker-desktop
    ```
* 查看集群状态
    ```shell script
    kubectl cluster-info
    kubectl get nodes
    ```
* 应用/部署 dashboard
    ```shell script
    kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.4/aio/deploy/recommended.yaml
    # 或 kubectl create -f kubernetes-dashboard.yaml
    ```
* 查看应用状态
    ```shell script
    kubectl get pod -n kubernetes-dashboard
    ```
* 开启访问代理
    ```shell script
    kubectl proxy
    ```
* 查看 pod 信息
    ```shell script
    kubectl get pod
    kubectl get pod -o wide
    ```
---

### 常见问题
* docker load' command failed with error: mkdir /var/lib/docker/tmp/docker-import-042855953: read-only file system
    1. https://stackoverflow.com/a/52872667/4348530
