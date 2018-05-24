### 0: 安装工具
```
npm install -g react-native-cli
npm install -g install-local
```

### 1: 在线调试
* 在纯的 rn 项目开启服务 **npm run start** 查看本地 IP 为 *10.10.10.111*
* 设置 **IP:PORT** *10.10.10.111:8081* (Dev Settings -> Debug server host & port for device)



### 2: 发布离线包
```
// bundle
react-native bundle  --platform android  --dev true  --entry-file ./index.js  --bundle-output ./build/bundle/assets/index.android.bundle  --assets-dest ./build/bundle/res/

// unbundle
react-native unbundle  --platform android  --dev true  --entry-file ./index.js  --bundle-output ./build/bundle/assets/index.android.bundle  --assets-dest ./build/bundle/res/
```

* base
```
react-native bundle  --platform android  --dev true  --entry-file ./base.js  --bundle-output ./build/bundle/base/base.android.bundle
```

* business
```
react-native bundle  --platform android  --dev true  --entry-file ./index.js  --bundle-output ./build/bundle/business/business.android.bundle  --exclude  ./build/bundle/base/base.android.bundle.json
```

# Custom Metro To Split Modules For React Native

🚇 Reference Metro From Native Path
- **🚅 build es6 to es5**: 
```
cd ~/workspace/smart-metro
npm run build-clean && npm run build
```
- **🚅 publish local release**: 
```
cd packages/metro
npm run package-release

```
- **🚅 install local**: 
```
npm install -g install-local

cd ~/workspace/ReactNativeProject
rm -rf node_modules/metro
install-local --save ~/workspace/smart-metro/packages-release/metro
```
- **🚅 install and update local with one key**:

```
cd ~/workspace/smart-metro/ && npm run build-clean && npm run build && cd packages/metro && npm run package-release && cd ~/workspace/template/apps/app-template/react_native && install-local --save ~/workspace/smart-metro/packages-release/metro
```