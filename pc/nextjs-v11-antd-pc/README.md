# [nextjs](http://nextjs.frontendx.cn/docs)

-   [英文官方文档](https://nextjs.org/docs)

## npm install 使用临时源

```shell
npm install @ant-design/compatible --registry https://registry.npm.taobao.org
```

## [响应式与自适应的区别](https://www.cnblogs.com/qianduanchenbao/p/10198834.html)

## next-with-less 不要设置 plugins/babel-plugin-import, next-plugin-antd-less 则需要设置, next-plugin-antd-less 会覆盖 withPlugins 的 nextConfig 配置, 以及使得 npm run analyze 失效(https://github.com/SolidZORO/next-plugin-antd-less/issues/84)

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

## scss 类名规定, IDEA 提示 bug

-   如果 scss 中类名带中横线, 则 js/jsx 中引用时不可以使用小驼峰命名法引用，必须使用字符串引用，否则样式不生效
-   scss 中类名使用小驼峰命名不带中横线，则 js/jsx 中可以直径变量引用

## husky not work

```shell
npm run install:taobao
rm -rf .git/hooks
npm i -D husky --registry https://registry.npm.taobao.org
```
