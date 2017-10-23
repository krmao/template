# house-keeper

# IOS / Android 区别

* IOS    /WKWebView 对SPA框架(VUE) 内部路由跳转/返回 可以拦截到 Url, onPageFinish 拦截不到
* Android/WebView   对SPA框架(VUE) 内部路由跳转/返回 不能拦截到 Url, onPageFinish 可以拦截

# BUGS

1) Uncaught SyntaxError: Unexpected strict mode reserved word
    
    在 android sdk <=15 (4.0.3/4.0.4)  上有此问题, 使用[replace-bundle-webpack-plugin](https://github.com/kimhou/replace-bundle-webpack-plugin)解决了此问题
    
    ```
    
    var ReplaceBundleStringPlugin = require('replace-bundle-webpack-plugin')
    new ReplaceBundleStringPlugin([{
        partten: /"use strict";/g,
        replacement: function () {
            return '';
        }
    }])
    ```
    
    注: 在 android sdk >= 16 此问题不再出现

2) [Uncaught ReferenceError: Promise is not defined](https://github.com/axios/axios/issues/188)
    
    [promise 各系统版本的支持](http://caniuse.com/#search=promise)
    
    此问题在 android sdk 4.4.2 出现
    
    解决方案:
    ```
    import 'es6-promise/auto'
    ```
    
    注: 在 android sdk >= 21 ( >= 5.0 ) 此问题不再出现
    