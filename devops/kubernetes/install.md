### docker desktop for mac install kubernetes

### install
> docker desktop -> settings -> kubernetes -> enable kubernetes / deploy / show system containers -> apply

### 打开控制台
```shell script
git clone https://github.com/AliyunContainerService/k8s-for-docker-desktop.git
cd k8s-for-docker-desktop
./load_images.sh

# 切换Kubernetes运行上下文至 docker-desktop (之前版本的 context 为 docker-for-desktop)
kubectl config use-context docker-desktop

# 验证 Kubernetes 集群状态
kubectl cluster-info
kubectl get nodes

# 部署 Kubernetes dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.4/aio/deploy/recommended.yaml
# 或
kubectl create -f kubernetes-dashboard.yaml

# 检查 kubernetes-dashboard 应用状态
kubectl get pod -n kubernetes-dashboard

# 开启 API Server 访问代理
kubectl proxy

# 通过如下 URL 访问 Kubernetes dashboard
# http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

# 配置控制台访问令牌
# 对于Mac环境
TOKEN=$(kubectl -n kube-system describe secret default| awk '$1=="token:"{print $2}')
kubectl config set-credentials docker-for-desktop --token="${TOKEN}"
echo $TOKEN

# 对于Windows环境
$TOKEN=((kubectl -n kube-system describe secret default | Select-String "token:") -split " +")[1]
kubectl config set-credentials docker-for-desktop --token="${TOKEN}"
echo $TOKEN
```

### 安装  ingress
```shell script
# 若安装脚本无法安装，[可以跳转到该地址查看最新操作](https://github.com/kubernetes/ingress-nginx/blob/master/docs/deploy/index.md)
# 安装
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-0.32.0/deploy/static/provider/cloud/deploy.yaml

# 验证
kubectl get pods -n ingress-nginx -l app.kubernetes.io/name=ingress-nginx # --watch

# 测试示例应用
kubectl create -f sample/apple.yaml
kubectl create -f sample/banana.yaml
kubectl create -f sample/ingress.yaml

# 测试示例应用
curl -kL http://localhost/apple
apple
curl -kL http://localhost/banana
banana

# 删除示例应用
kubectl delete -f sample/apple.yaml
kubectl delete -f sample/banana.yaml
kubectl delete -f sample/ingress.yaml

# 删除 Ingress
kubectl delete -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-0.32.0/deploy/static/provider/cloud/deploy.yaml
```

### 常用指令合集
```shell script
# 1. 创建资源
# 一般创建资源会有两种方式：通过文件或者命令创建。

# 通过文件创建一个Deployment
kubectl create -f /path/to/deployment.yaml
cat /path/to/deployment.yaml | kubectl create -f -
# 不过一般可能更常用下面的命令来创建资源
kubectl apply -f /path/to/deployment.yaml

# 通过kubectl命令直接创建
kubectl run nginx_app --image=nginx:1.9.1 --replicas=3 
# kubectl还提供了一些更新资源的命令，比如kubectl edit、kubectl patch和kubectl replace等。

# kubectl edit：相当于先用get去获取资源，然后进行更新，最后对更新后的资源进行apply
kubectl edit deployment/nginx_app

# kubectl patch：使用补丁修改、更新某个资源的字段，比如更新某个node
kubectl patch node/node-0 -p '{"spec":{"unschedulable":true}}'
kubectl patch -f node-0.json -p '{"spec": {"unschedulable": "true"}}'

# kubectl replace：使用配置文件来替换资源
kubectl replace -f /path/to/new_nginx_app.yaml
# 2. 查看资源
# 获取不同种类资源的信息。

# 一般命令的格式会如下：
kubectl get <resource_type>
# 比如获取K8s集群下pod的信息
kubectl get pod
# 更加详细的信息
kubectl get pod -o wide
# 指定资源的信息，格式：kubectl get <resource_type>/<resource_name>，比如获取deployment nginx_app的信息
kubectl get deployment/nginx_app -o wide
# 也可以对指定的资源进行格式化输出，比如输出格式为json、yaml等
kubectl get deployment/nginx_app -o json
# 还可以对输出结果进行自定义，比如对pod只输出容器名称和镜像名称
kubectl get pod httpd-app-5bc589d9f7-rnhj7 -o custom-columns=CONTAINER:.spec.containers[0].name,IMAGE:.spec.containers[0].image
# 获取某个特定key的值还可以输入如下命令得到，此目录参照go template的用法，且命令结尾'\n'是为了输出结果换行
kubectl get pod httpd-app-5bc589d9f7-rnhj7 -o template --template='{{(index spec.containers 0).name}}{{"\n"}}'
# 还有一些可选项可以对结果进行过滤，这儿就不一一列举了，如有兴趣，可参照kubectl get --help说明
# 3. 部署命令集
# 部署命令包括资源的运行管理命令、扩容和缩容命令和自动扩缩容命令。

# 3.1 rollout命令
# 管理资源的运行，比如eployment、Daemonet、StatefulSet等资源。

# 查看部署状态：比如更新deployment/nginx_app中容器的镜像后查看其更新的状态。
kubectl set image deployment/nginx_app nginx=nginx:1.9.1
kubectl rollout status deployment/nginx_app
# 资源的暂停及恢复：发出一次或多次更新前暂停一个 Deployment，然后再恢复它，这样就能在Deployment暂停期间进行多次修复工作，而不会发出不必要的 rollout。
# 暂停
kubectl rollout pause deployment/nginx_app
# 完成所有的更新操作命令后进行恢复
kubectl rollout resume deployment/nginx_app
# 回滚：如上对一个Deployment的image做了更新，但是如果遇到更新失败或误更新等情况时可以对其进行回滚。
# 回滚之前先查看历史版本信息
kubectl rollout history deployment/nginx_app
# 回滚
kubectl rollout undo deployment/nginx_app
# 当然也可以指定版本号回滚至指定版本
kubectl rollout undo deployment/nginx_app --to-revision=<version_index>
# 3.2 scale命令
# 对一个Deployment、RS、StatefulSet进行扩/缩容。

# 扩容
kubectl scale deployment/nginx_app --replicas=5
# 如果是缩容，把对应的副本数设置的比当前的副本数小即可
# 另外，还可以针对当前的副本数目做条件限制，比如当前副本数是5则进行缩容至副本数目为3
kubectl scale --current-replicas=5 --replicas=3 deployment/nginx_app
# 3.3 autoscale命令
# 通过创建一个autoscaler，可以自动选择和设置在K8s集群中Pod的数量。

# 基于CPU的使用率创建3-10个pod
kubectl autoscale deployment/nginx_app --min=3 --max=10 --cpu_percent=80
# 4. 集群管理命令
# 4.1 cordon & uncordon命令
# 设置是否能够将pod调度到该节点上。

# 不可调度
kubectl cordon node-0

# 当某个节点需要维护时，可以驱逐该节点上的所有pods(会删除节点上的pod，并且自动通过上面命令设置
# 该节点不可调度，然后在其他可用节点重新启动pods)
kubectl drain node-0
# 待其维护完成后，可再设置该节点为可调度
kubectl uncordon node-0
# 4.2 taint命令
# 目前仅能作用于节点资源，一般这个命令通常会结合pod的tolerations字段结合使用，对于没有设置对应toleration的pod是不会调度到有该taint的节点上的，这样就可以避免pod被调度到不合适的节点上。一个节点的taint一般会包括key、value和effect(effect只能在NoSchedule, PreferNoSchedule, NoExecute中取值)。

# 设置taint
kubecl taint nodes node-0 key1=value1:NoSchedule
# 移除taint
kubecl taint nodes node-0 key1:NoSchedule-
# 如果pod想要被调度到上述设置了taint的节点node-0上，则需要在该pod的spec的tolerations字段设置：

tolerations:
- key: "key1"
  operator: "Equal"
  value: "value1"
  effect: "NoSchedule"

# 或者
tolerations:
- key: "key1"
  operator: "Exists"
  effect: "NoSchedule"
# 5. 其它
# 映射端口允许外部访问
kubectl expose deployment/nginx_app --type='NodePort' --port=80
# 然后通过kubectl get services -o wide来查看被随机映射的端口
# 如此就可以通过node的外部IP和端口来访问nginx服务了

# 转发本地端口访问Pod的应用服务程序
kubectl port-forward nginx_app_pod_0 8090:80
# 如此，本地可以访问：curl -i localhost:8090

# 在创建或启动某些资源的时候没有达到预期结果，可以使用如下命令先简单进行故障定位
kubectl describe deployment/nginx_app
kubectl logs nginx_pods
kubectl exec nginx_pod -c nginx-app <command>

# 集群内部调用接口(比如用curl命令)，可以采用代理的方式，根据返回的ip及端口作为baseurl
kubectl proxy &

# 查看K8s支持的完整资源列表
kubectl api-resources

# 查看K8s支持的api版本
kubectl api-versions
```

### 参考
* https://www.cnblogs.com/darope/p/12624678.html
s* https://github.com/AliyunContainerService/k8s-for-docker-desktop
* https://github.com/kubernetes/ingress-nginx/blob/master/docs/deploy/index.md
* https://zhuanlan.zhihu.com/p/88994751