1: debug
* 在纯的 rn 项目开启服务 **npm run start** 查看本地 IP 为 *10.10.10.111*
* 设置 **IP:PORT** *10.10.10.111:8081* (Dev Settings -> Debug server host & port for device)
* 注释 **//.setJSBundleFile("assets://index.android.js")**

2: release 
* 将 纯的 rn 项目打包到 assets 目录下
* 取消注释 **//.setJSBundleFile("assets://index.android.js")**
