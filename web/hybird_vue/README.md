# html

> A Vue.js project

## 开发步骤
1. 在 hybird_vue 项目中开发, 使用 npm run dev 进行在线调试
2. 执行 npm run build 自动打包并自动 copy 到 template/hybird/assets/hybird 目录 (template.json/template.zip)
3. 将 template.json 的内容 复制并替换 all.json 里面对应的模块
4. 运行 template app 在 hybird tab 页面 点击 'HYBIRD PAGE' 预览效果

## Build Setup

```bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```

For detailed explanation on how things work, checkout the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).

# IOS / Android 区别

-   IOS /WKWebView 对 SPA 框架(VUE) 内部路由跳转/返回 可以拦截到 Url, onPageFinish 拦截不到
-   Android/WebView 对 SPA 框架(VUE) 内部路由跳转/返回 不能拦截到 Url, onPageFinish 可以拦截

# BUGS

1. Uncaught SyntaxError: Unexpected strict mode reserved word

    在 android sdk <=15 (4.0.3/4.0.4) 上有此问题, 使用[replace-bundle-webpack-plugin](https://github.com/kimhou/replace-bundle-webpack-plugin)解决了此问题

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

2. [Uncaught ReferenceError: Promise is not defined](https://github.com/axios/axios/issues/188)
   [promise 各系统版本的支持](http://caniuse.com/#search=promise)
   此问题在 android sdk 4.4.2 出现
   解决方案:
    ```
    import 'es6-promise/auto'
    ```
    注: 在 android sdk >= 21 ( >= 5.0 ) 此问题不再出现

### [前端布局方式](https://blog.csdn.net/baiccnymyh/article/details/77986049?utm_source=blogxgwz3)

```
<!--
标签：
块属性标签：div(常用来布局划分区域）p（段落） h1~h6（标题）
特点：支持宽高 独占一行 支持所有css样式
行属性标签： span（文字） a（超链接）
特点：不支持宽高 排成一排 不支持上下的margin和padding

行块属性标签： img（图片）
特点：既支持宽高 又排成一行
//a标签取出默认样式
a{
text-decoration：none；
}
//列表标签去除默认样式
ul{
    list-style：none；
}

*********************************************************************************
CSS的三种引入方式：
行间样式：在标签中写入style属性
样式写在style中（最不常用，优先级最高）

内部引入方式：在head中写入style标签 样式写在style标签中
外部引入方式：在head中 使用link标签引入外部css文件（开发中使用最多）
注意： 外部和内部引入方式优先级相同，文档后面的会覆盖前面的。
********************************************************************************
选择器：
标签选择器：语法——> 标签名{样式}
class选择器：语法：“.”+class名{样式}
特点：1. 优先级比标签选择器高，2.一个标签可以有多个class名
3.不同标签可以使用相同的class名。
后代选择器：
语法：父选择器 空格 子选择器{样式}
优先级为 选择器权重相加（class权重为10 标签选择器权重为1）
//id选择器：
#box{

}
//组合选择器：
div,p{

}
**************************************************************************

盒模型：
margin（外边距）
用来调整元素位置，相对于父元素的边缘或相邻元素
注意： 元素的第一个子元素的margin-top会传递给父元素，解决方案是：给父元素设置
overflow：hidden；
border（边框）
一般设置三个参数 分别为 边框颜色 边框宽度 边框样式（常用实线 solid）
三个参数没有顺序
例如： border：1px solid black；
padding（内边距）
内容区域距离元素边缘的距离
1.内容不会占用padding的位置  2.padding的颜色和背景颜色相同
注意：border和padding都会让元素变大
补充内容： border-radius（圆角度）
圆角度的值为 圆角的半径
*********************************************************************************
float（浮动）：
浮动包括left和right（左、右浮动）
设置浮动是为了让块属性元素可以排成一排
1.浮动可以使用行属性元素支持宽高
2.浮动具有流式布局效果和文字环绕效果
3.浮动会有脱离文档流效果
浮动的影响：
如果父元素没有高度，靠子元素撑开。当子元素浮动，脱离文档流，父元素则不会被撑开
进而失去高度，这样会影响布局
解决方案：给父元素设置overflow：hidden；
*********************************************************************************

position（定位）：
fixed（固定定位）：
相对于浏览器可视窗口，一般用于悬浮窗，小广告，返回顶部等按钮功能；
relative（相对定位）
定位相对于它原来的位置，一般配合子元素的 绝对位置（absolute）使用。
absolute（绝对定位）：
定位现对于拥有定位的父级元素，如果父元素没有定位，则一直向上查找有定位的祖先元素
直到body为止。
注意：定位只适用于，某一个模块中的摸个小部件（小模块），不可以使用于大方围布局。
绝对定位大多数情况配合父元素的相对定位使用，可以搭配记忆；
********************************************************************************
居中问题：
水平居中：
块属性元素水平居中：给需居中的元素设置 margin：0 auto；
行属性/行块属性/文本元素水平居中：给父元素设置 text-align：center；
垂直居中：
大部分情况，没有捷径，使用margin或者定位调整。
单行文本/行属性标签 垂直居中 设置 line-height：父元素高度；
******************************************************************************
display：
display：block；把元素转化为块属性；
display：inline；把元素转化为行属性
display：inline-block；把元素转化为行块属性（最常用，让行块属性元素支持宽高）
display：none；隐藏 （常用来制作动态效果）
*******************************************************************************
hover：
hover规定了 鼠标移入元素后展示的效果，hover只能控制它本身或者子元素
例如：
控制本身
选择器：hover{鼠标移入的效果}
控制子元素
父选择器：hover 空格 子选择器{鼠标移入的效果}

 -->
```
