
### react native for ios pod repo 远程私有仓库
#### 1. 新建一个 [空的 github 仓库](https://github.com/krmao/libsforios) 作为 pod repo 远程私有仓库
#### 2. 新建一个本地 pod repo 仓库并指向第一步创建的远端 github 仓库
> 作用是同步本地仓库到远程, 这样其他人也可以使用了, 没有这一步只能在本机使用
```shell script
# 增加 pod repo 仓库
pod repo add libsforios https://github.com/krmao/libsforios.git
# 删除 pod repo 仓库
# pod repo remove repo_name
# 查看 pod repo 仓库
pod repo
# - Type: git (main)
# - URL:  https://github.com/krmao/libsforios.git
# - Path: /Users/krmao/.cocoapods/repos/libsforios
```
#### 3. 下载 react-native 源码, 并修改脚本
> 默认的 ./scripts/process-podspecs.sh 脚本只能生成少量的不完善的依赖库的配置, 所以自己修改并增加递归查找配置的功能
```shell script
git clone https://github.com/krmao/react-native.git
cd react-native
git remote add upstream git://github.com/facebook/react-native.git
git checkout master
git pull
git pull --tags
git fetch upstream
git fetch upstream --tags

# 合并两个版本的代码
git merge upstream/master
git push origin master

# remote local tag
# git tag -d v0.62.2
# remove remote tag
# git push origin :v0.62.2

# 仅仅推送 tag v0.62.2 到 origin 仓库
git fetch upstream v0.62.2
git status
git push origin v0.62.2

# 推送所有 tags 到 origin 仓库
git fetch upstream --tags
git fetch upstream -t
git status
git push origin --tags

# 切换到 tag v0.62.2
git checkout v0.62.2 # 第一遍报错? 再执行第二遍试试
git checkout v0.62.2
git branch local/v0.62.2 # 在 tag v0.62.2 的基础上新建本地分支
git checkout local/v0.62.2
git status
On branch local/v0.62.2
nothing to commit, working tree clean

# 将本文件同目录的 process-podspecs-0.62.2.sh 文件 拷贝到 react-native/scripts/ 目录
# 保存到自定义的 tag
git tag -m "scripts/process-podspecs-0.62.2.sh for v0.62.2" v0.62.2.1
git push origin v0.62.2.1
```

##### 3.1. 脚本以及源码的 git patch for react-native v0.62.2 版本
* git patch https://github.com/krmao/react-native/commit/3bdb0afc7c31f07a18787e98ebd0447d436cd1ca
* pull request https://github.com/facebook/react-native/pull/30305

#### 4. 执行 ./scripts/process-podspecs-0.62.2.sh 脚本生成所有ios依赖RN的所有配置项并自动上传到第二步的本地仓库(上传后会自动同步到第一步的 github 仓库, 未同步则进入目录 /Users/krmao/.cocoapods/repos/libsforios 手动 git commit/push 提交)
```shell script
# 更新 pod
# pod repo remove master
# pod setup
# 执行命令生成配置项
# Specify `SPEC_REPO` as an env variable if you want to push to a specific spec repo.
# Defaults to `react-test`, which is meant to be a dummy repo used to test that the specs fully lint.
# : ${SPEC_REPO:="react-test"}
# SPEC_REPO_DIR="$HOME/.cocoapods/repos/$SPEC_REPO"
# 指示该脚本上传到哪一个本地 pod repo 仓库
export SPEC_REPO=repo && export RN_NODE_MODULES_PATH="~/workspace/template/mobile/react_native/node_modules" && ./scripts/process-podspecs.sh
cd /Users/krmao/.cocoapods/repos/libsforios
git add .
git commit -m "rn v0.62.2 libsforios"
git push
# 可以打开 https://github.com/krmao/libsforios 网址查看下提交记录
```
#### 5. 备忘:手动转换某一个 podspec to json 并保存到 pod repo
```shell script
pod ipc spec React-Core.podspec >> React-Core.podspec.json
pod spec lint React-Core.podspec.json --no-clean --verbose --allow-warnings  --sources=https://github.com/krmao/libsforios.git,https://github.com/CocoaPods/Specs.git
pod repo push libsforios React-Core.podspec.json --allow-warnings
```
### 6. 将前几个步骤生成的 rn 远程依赖集成到现有 ios 项目
```ruby
# 在自己的待集成 rn 的 ios 工程中 Podfile 文件首行添加
source 'https://github.com/krmao/libsforios.git'

def rnpodremote
  pod 'FBLazyVector', '0.62.2'
  pod 'FBReactNativeSpec', '0.62.2'
  pod 'RCTRequired', '0.62.2'
  pod 'RCTTypeSafety', '0.62.2'
  pod 'React', '0.62.2'
  pod 'React-Core', '0.62.2'
  pod 'React-CoreModules', '0.62.2'
  pod 'React-Core/DevSupport', '0.62.2'
  pod 'React-RCTActionSheet', '0.62.2'
  pod 'React-RCTAnimation', '0.62.2'
  pod 'React-RCTBlob', '0.62.2'
  pod 'React-RCTImage', '0.62.2'
  pod 'React-RCTLinking', '0.62.2'
  pod 'React-RCTNetwork', '0.62.2'
  pod 'React-RCTSettings', '0.62.2'
  pod 'React-RCTText', '0.62.2'
  pod 'React-RCTVibration', '0.62.2'
  pod 'React-Core/RCTWebSocket', '0.62.2'
  pod 'React-cxxreact',  '0.62.2'
  pod 'React-jsi',  '0.62.2'
  pod 'React-jsiexecutor',  '0.62.2'
  pod 'React-jsinspector',  '0.62.2'
  pod 'ReactCommon/callinvoker', '0.62.2'
  pod 'ReactCommon/turbomodule/core', '0.62.2'

  pod 'Yoga', '1.14.0'
  pod 'DoubleConversion', '1.1.6'
  pod 'glog', '0.3.5'
  pod 'Folly', '2018.10.22.00'

  # https://github.com/software-mansion/react-native-gesture-handler/pull/366
  # https://github.com/software-mansion/react-native-gesture-handler/issues/205
  pod 'RNGestureHandler', '1.5.3'
  pod 'RNReanimated', '1.13.1'
  pod 'react-native-safe-area-context', '0.6.4'
  pod 'RNVectorIcons', '6.7.0'
end

target 'Template' do
  inherit! :search_paths
  rnpodremote
end
```
```shell script
pod install
```

### 参考文档
* https://imfong.com/post/Private-Pods-Add-react-native
* https://blog.csdn.net/zenghao0708/article/details/83245218
* https://github.com/SquarePants1991/RNTopics/wiki/Topics
* https://www.folio3.com/developing-and-distributing-private-libraries-with-cocoapods
* https://blog.csdn.net/hx_lei/article/details/53673437
* https://www.jianshu.com/p/9d7ac27e2219
* https://blog.csdn.net/zenghao0708/article/details/83245218
* http://blog.wtlucky.com/blog/2015/02/26/create-private-podspec/
* https://imfong.com/post/Private-Pods-Add-react-native#yoga
