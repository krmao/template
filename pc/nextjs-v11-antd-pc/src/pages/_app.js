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

// noinspection JSUnusedGlobalSymbols
export default function MyApp({Component, pageProps}) {
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
