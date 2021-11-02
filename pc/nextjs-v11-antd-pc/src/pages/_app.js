//region 全局样式一次引入
import "antd/lib/style/themes/default.less";
import "antd/dist/antd.less";
import "@basic-styles/basic-global-vars.css";
import "@basic-styles/basic-global.css";
//endregion

//region 全局一次配置
import "moment/locale/zh-cn"; // DatePicker 月份显示中文
//endregion

import Head from "next/head";
import React from "react";
import {ConfigProvider} from "antd";
import zhCN from "antd/lib/locale/zh_CN";
import BasicLayout from "@basic/basic-layout";
// import App from "next/app";

// noinspection JSUnusedGlobalSymbols
function MyApp({Component, pageProps}) {
    return (
        <ConfigProvider locale={zhCN}>
            <div>
                <Head>
                    <title>TEST</title>
                </Head>
                <BasicLayout>
                    <Component {...pageProps} />
                </BasicLayout>
            </div>
        </ConfigProvider>
    );
}

// Only uncomment this method if you have blocking data requirements for
// every single page in your application. This disables the ability to
// perform automatic static optimization, causing every page in your app to
// be server-side rendered.
//
// MyApp.getInitialProps = async (appContext) => {
//     // calls page's `getInitialProps` and fills `appProps.pageProps`
//     const appProps = await App.getInitialProps(appContext);
//
//     return {...appProps};
// };

export default MyApp;
