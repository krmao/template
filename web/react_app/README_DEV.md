### 1. 工程介绍

> 本项目采用 **react** 技术栈, 通过 _rem_ 进行页面适配*(阿里巴巴高清页面适配方案)*, 使用 _eslint/prettier_ 优化代码

### 2. 目录介绍

```
.
└── src
    ├── index.css
    ├── index.js
    ├── library             # react 基础库(任意工程可用公共库)
    ├── library_business    # react 基础库(与本工程业务相关的公共库)
    ├── logo.svg
    ├── modules             # 所有的模块(页面)
    ├── repository          # 所有的网络请求
    └── serviceWorker.js
```

### 3. 代码开发规范

1. 强制使用 双引号
2. 强制使用 行尾分号
3. 强制使用 驼峰命名法
4. 强制使用 _let_/_const_, 禁用 _var_
5. 提交代码前 强制执行 _npm run eslint_ 检查错误
6. 提交代码前 强制执行 _npm run format_ 格式化代码

### 4. 代码提交规范

1. 编写代码后准备提交
2. npm run eslint # 检查代码错误
3. npm run format # 格式化代码
4. git commit -m "" && git pull --rebase && git push

### 5. 开发实时预览

```bash
npm run start
```

### 6. 打包发布

```bash
npm run build
```

-   对即将部署的 build 文件夹可以进行静态资源访问测试

```bash
npm install -g serve
serve -s build -p 5389
```

### 7. 部署到 nginx/tomcat

### 8. 参考

-   prettier [配置](https://prettier.io/docs/en/options.html)
-   eslint [配置](https://cloud.tencent.com/developer/chapter/12618)
