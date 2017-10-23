# house-keeper

# IOS / Android 区别

* IOS    /WKWebView 对SPA框架(VUE) 内部路由跳转/返回 可以拦截到 Url, onPageFinish 拦截不到
* Android/WebView   对SPA框架(VUE) 内部路由跳转/返回 不能拦截到 Url, onPageFinish 可以拦截

# BUGS

1. Uncaught SyntaxError: Unexpected strict mode reserved word
    
    在 android sdk 15(4.0.3/4.0.4)  上有此问题
    在 android sdk 16 此问题不再出现
