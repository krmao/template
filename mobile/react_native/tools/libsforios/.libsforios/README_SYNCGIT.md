### sync fork repo
```shell script
git clone https://github.com/krmao/react-native.git
cd react-native
git remote add upstream git://github.com/facebook/react-native.git

git checkout master
git pull
git pull -t
git pull --tags
git pull origin master
git pull origin master -t
git pull origin master --tags

# You asked to pull from the remote 'upstream', but did not specify
# a branch. Because this is not the default configured remote
# for your current branch, you must specify a branch on the command line.
# git pull upstream
git fetch upstream
git fetch upstream -t
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
```

### 参考文档
* https://github.com/krmao/react-native
* https://github.com/facebook/react-native
* https://www.cnblogs.com/houpeiyong/p/5888381.html
* https://blog.csdn.net/jdsjlzx/article/details/98654951
* https://blog.csdn.net/wrfff/article/details/79256187
