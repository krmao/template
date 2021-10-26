# NextJs 项目学习与使用

-   [英文官方文档](https://nextjs.org/docs)
-   [中文文档](http://nextjs.frontendx.cn/docs)

#### 配置 eslint

> http://eslint.cn/docs/user-guide/getting-started#configuration

```bash
    npm install --save-dev eslint
```

#### 导出静态页

```bash
    next build
    next export
```

#### css 横向布局 左侧固定，右侧自适应

> 重点 1 左侧 float: left
> 重点 2 右侧 margin-left >= 左侧 width
> 重点 3 overflow: hidden 超出边界不显示

```scss
.content {
    width: 100%;
    overflow: hidden;
    height: 100%;
    background: white;

    .contentLeft {
        float: left;
        width: 200px;
        height: 400px;
    }

    .contentRight {
        margin-left: 200px;
        background: blueviolet;
        height: 100%;
    }
}
```

#### css 水平居中

-   使用 inline-block 和 text-align 实现

    ```scss
    .parent {
        text-align: center;
    }
    .child {
        display: inline-block;
    }
    ```

-   使用 margin:0 auto 来实现
    ```scss
    .child {
        width: 200px;
        margin: 0 auto;
    } // 要指定宽度
    ```

#### css 垂直居中

```scss
.parent {
    display: inline-block;
    vertical-align: middle;
    line-height: 20px;
}
```

#### npm install 使用临时源

```shell
npm install @ant-design/compatible --registry https://registry.npm.taobao.org
```

#### can not import Icon from "@ant-design/icons" while use babel-plugin-import

> import Icon from "@ant-design/icons";
>
> to
>
> import {Icon} from '@ant-design/compatible';

```
npm install @ant-design/compatible --registry https://registry.npm.taobao.org

// https://github.com/ant-design/babel-plugin-import/issues/465
// https://stackoverflow.com/a/61023819/4348530
// https://ant.design/docs/react/migration-v4#Icon-upgrade
import {Icon} from '@ant-design/compatible';
```

#### [响应式与自适应的区别](https://www.cnblogs.com/qianduanchenbao/p/10198834.html)

#### next-with-less 不要设置 plugins/babel-plugin-import, next-plugin-antd-less 则需要设置, next-plugin-antd-less 会覆盖 withPlugins 的 nextConfig 配置, 以及使得 npm run analyze 失效(https://github.com/SolidZORO/next-plugin-antd-less/issues/84)

```javascript
// next-with-less
// npm install next-with-less less less-loader && npm uninstall babel-plugin-import next-plugin-antd-less && remove plugins in babel.config.js
// https://github.com/elado/next-with-less/issues/6#issuecomment-831676037
// import "antd/dist/antd.less"; in _app.js
// module.exports = withPlugins(
//     [
//         [withBundleAnalyzer, {}],
//         [
//             withLess,
//             {
//                 lessLoaderOptions: {
//                     lessOptions: {
//                         modifyVars: {
//                             "primary-color": "#ff0000",
//                             "border-radius-base": "2px"
//                         }
//                     }
//                 }
//             }
//         ]
//     ],
//     nextConfig
// );
//
// next-plugin-antd-less
// npm install next-plugin-antd-less babel-plugin-import && npm uninstall next-with-less less less-loader && add plugins(the follow code) in babel.config.js
// // import "antd/dist/antd.css"; in _app.js
/*const plugins = [
    ["import", {libraryName: "antd", libraryDirectory: "lib", style: true}, "antd"],
    [
        "import",
        {
            libraryName: "@ant-design/icons",
            libraryDirectory: "lib/icons",
            camel2DashComponentName: false,
            style: false
        },
        "@ant-design/icons"
    ]
];*/
```

#### scss 类名规定, IDEA 提示 bug

-   如果 scss 中类名带中横线, 则 js/jsx 中引用时不可以使用小驼峰命名法引用，必须使用字符串引用，否则样式不生效
-   scss 中类名使用小驼峰命名不带中横线，则 js/jsx 中可以直径变量引用
