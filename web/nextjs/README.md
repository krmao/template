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

#### 自适应

```

    /**
        only(限定某种设备)
        screen 是媒体类型里的一种
        and 被称为关键字，其他关键字还包括 not(排除某种设备)

        <link rel="stylesheet" type="text/css" href="styleB.css"  media="screen and (min-width: 600px) and (max-width: 800px)">
     */
    /*大于1200px屏幕尺寸应用以下样式*/
    @media screen and (min-width: 1200px) {
        $pageWidth: 1200px;
        $font-size: 20px;
    }

    /*大于960px小于1199px屏幕尺寸应用以下样式*/
    @media screen and (min-width: 960px) and (max-width: 1199px) {
        $pageWidth: 960px;
        $font-size: 20px;
    }

    /*大于768px小于959px屏幕尺寸应用以下样式*/
    @media screen and (min-width: 768px) and (max-width: 959px) {
        $pageWidth: 768px;
        $font-size: 20px;
    }

    /*大于480px小于767px屏幕尺寸应用以下样式*/
    @media only screen and (min-width: 480px) and (max-width: 767px) {
        $pageWidth: 480px;
        $font-size: 20px;
    }

    /*小于479px屏幕尺寸应用以下样式*/
    @media only screen and (max-width: 479px) {
        $pageWidth: 300px;
        $font-size: 20px;
    }

```
