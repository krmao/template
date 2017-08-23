# fruit-shop-html

> A Vue.js project

## Build Setup

``` bash
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



##目录结构说明

|     目录/文件	            |           说明                       |
| --------                |           :------                    |
|build                    |	最终发布的代码存放位置。                |
|config	                  | 配置目录，包括端口号等。我们初学可以使用默认的。|
|node_modules            	| npm 加载的项目依赖模块|
|src	                    | 这里是我们要开发的目录，基本上要做的事情都在这个目录里。里面包含了几个目录及文件：|
|   ----src/assets                   | 放置一些图片，如logo等。|
|   ----src/components               | 目录里面放了一个组件文件，可以不用。|
|   ----src/App.vue                  | 项目入口文件，我们也可以直接将组件写这里，而不使用 components 目录。|
|   ----src/main.js                  | 项目的核心文件。|
|static	                  | 静态资源目录，如图片、字体等。|
|test	                    | 初始测试目录，可删除|
|.xxxx                    |  这些是一些配置文件，包括语法配置，git配置等。|
|index.html               |	首页入口文件，你可以添加一些 meta 信息或同统计代码啥的。|
|package.json             |	项目配置文件。|
|README.md                |	项目的说明文档，markdown 格式|



#备注

* WEBPACK <img src="http://www.runoob.com/wp-content/uploads/2017/01/what-is-webpack.png"/>
  http://www.runoob.com/w3cnote/webpack-tutorial.html
  ```
  Webpack 是一个前端资源加载/打包工具。它将根据模块的依赖关系进行静态分析，然后将这些模块按照指定的规则生成对应的静态资源。
  从图中我们可以看出，Webpack 可以将多种静态资源 js、css、less 转换成一个静态文件，减少了页面的请求
  
  WebPack可以看做是模块打包机：它做的事情是，分析你的项目结构，找到JavaScript模块以及其它的一些浏览器不能直接运行的拓展语言（Scss，TypeScript等），并将其打包为合适的格式以供浏览器使用。
  今的很多网页其实可以看做是功能丰富的应用，它们拥有着复杂的JavaScript代码和一大堆依赖包。为了简化开发的复杂度，前端社区涌现出了很多好的实践方法
  a:模块化，让我们可以把复杂的程序细化为小的文件;
  b:类似于TypeScript这种在JavaScript基础上拓展的开发语言：使我们能够实现目前版本的JavaScript不能直接使用的特性，并且之后还能能装换为JavaScript文件使浏览器可以识别；
  c:scss，less等CSS预处理器
  .........
  这些改进确实大大的提高了我们的开发效率，但是利用它们开发的文件往往需要进行额外的处理才能让浏览器识别,而手动处理又是非常反锁的，这就为WebPack类的工具的出现提供了需求。
  ```

* VUE_ROUTER
  路由映射

* BABEL
  一个广泛使用的转码器,可以将ES6代码转为ES5代码,从而在现有环境执行

* POST_CSS
  PostCSS的目标是通过自定义插件和工具这样的生态系统来改造CSS,PostCSS把扩展的语法和特性转换成现代的浏览器友好的CSS
  http://www.cnblogs.com/terrylin/p/5229169.html
  
* EDITOR_CONFIG
  使得所有的IDE编辑器按照统一的编码风格

