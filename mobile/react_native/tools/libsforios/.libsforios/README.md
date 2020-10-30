### 制作步骤
1. 新建一个 [空的 github 仓库](https://github.com/krmao/libsforios-rn)
2. 新建一个本地仓库并指向第一步创建的远端仓库
```shell script
% pod repo add repo https://github.com/krmao/libsforios-rn.git
Cloning spec repo `repo` from `https://github.com/krmao/libsforios-rn.git`

% cat ~/.cocoapods/repos/repo/README.md
# libsforios-

% pod repo

master
- Type: git (master)
- URL:  https://github.com/CocoaPods/Specs.git
- Path: /Users/krmao/.cocoapods/repos/master

repo
- Type: git (main)
- URL:  https://github.com/krmao/libsforios-rn.git
- Path: /Users/krmao/.cocoapods/repos/repo

trunk
- Type: CDN
- URL:  https://cdn.cocoapods.org/
- Path: /Users/krmao/.cocoapods/repos/trunk

3 repos

# 删除 repo
# pod repo remove repo_name
# pod repo

```
3. 使用RN源码下./scripts/process-podspecs.sh 生成 podspecs
```shell script
pod repo remove master
pod setup

# Specify `SPEC_REPO` as an env variable if you want to push to a specific spec repo.
# Defaults to `react-test`, which is meant to be a dummy repo used to test that the specs fully lint.
# : ${SPEC_REPO:="react-test"}
# SPEC_REPO_DIR="$HOME/.cocoapods/repos/$SPEC_REPO"
export SPEC_REPO=repo
./scripts/process-podspecs.sh
```

4. convert .podspec to .podspec.json
```shell script
pod ipc spec React-Core.podspec >> React-Core.podspec.json
pod spec lint React-Core.podspec.json --no-clean --verbose --allow-warnings  --sources=https://github.com/krmao/libsforios-rn.git,https://github.com/CocoaPods/Specs.git
pod repo push libsforios React-Core.podspec.json --allow-warnings
```

### 参考文档
* https://imfong.com/post/Private-Pods-Add-react-native
* https://blog.csdn.net/zenghao0708/article/details/83245218
```

### 参考文档
* https://github.com/SquarePants1991/RNTopics/wiki/Topics
* https://www.folio3.com/developing-and-distributing-private-libraries-with-cocoapods
* https://blog.csdn.net/hx_lei/article/details/53673437
* https://www.jianshu.com/p/9d7ac27e2219
* https://blog.csdn.net/zenghao0708/article/details/83245218
* http://blog.wtlucky.com/blog/2015/02/26/create-private-podspec/
* https://imfong.com/post/Private-Pods-Add-react-native#yoga
