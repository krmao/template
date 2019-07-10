### 1. 代码风格检查

-   prettier [配置](https://prettier.io/docs/en/options.html)
-   eslint [配置](https://cloud.tencent.com/developer/chapter/12618)

### 2. 开发步骤

1. 编写代码后准备提交
2. npm run eslint # 检查代码错误
3. npm run format # 格式化代码
4. git commit -m "" && git pull --rebase && git push

### 3. 目录介绍

```
.
└── src
    ├── App.css
    ├── App.js
    ├── App.test.js
    ├── index.css
    ├── index.js
    ├── library             # react 基础库(任意工程可用公共库)
    ├── library_business    # react 基础库(与本工程业务相关的公共库)
    ├── logo.svg
    ├── modules             # 所有的模块(页面)
    ├── repository          # 所有的网络请求
    └── serviceWorker.js
```

### 4. 开发规范

1. 强制使用 双引号
2. 强制使用 行尾分号
3. 强制使用 驼峰命名法
4. 强制使用 let/const, 禁用 var
5. 提交代码前 强制执行 **npm run eslint** 检查错误
6. 提交代码前 强制执行 **npm run format** 格式化代码
