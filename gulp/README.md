#框架说明

###配置项目[gulpfile.js]
```
    //config
    let modules = [new ModuleModel("20170920hd", 7778, "maintain"), new ModuleModel("20170920hd_sh", 7779)];

```
#运行项目
```
    >gulp
```
#打包项目
```
    >gulp build
```
## npm 安装插件
```
    npm install gulp -D //安装并保存到 package.json
```
## Unresolved function or method
```
    //Issues when coding node.js using IntelliJ IDEA , please check all
    IntelliJ IDEA -> Preferences -> JavaScript -> Libraries 
        -> [Ensure 'Node.js Globals' is checked]
```
## gulp 升级到 4.0

```
    # Uninstall previous Gulp installation and related packages, if any
    $ npm rm gulp -g
    $ npm rm gulp-cli -g
    $ cd [your-project-dir/]
    $ npm rm gulp --save-dev
    $ npm rm gulp --save
    $ npm rm gulp --save-optional
    $ npm cache clean
    
    # Install the latest Gulp CLI tools globally
    $ npm install gulpjs/gulp-cli -g
    
    # Install Gulp 4 into your project from 4.0 GitHub branch as dev dependency
    $ npm install gulpjs/gulp#4.0 --save-dev
    
    # Check the versions installed. Make sure your versions are not lower than shown.
    $ gulp -v
    ---
    [10:48:35] CLI version 1.2.2
    [10:48:35] Local version 4.0.0-alpha.2
    
    作者：记忆是条狗
    链接：http://www.jianshu.com/p/b20b6f17cc66
    來源：简书
    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```
