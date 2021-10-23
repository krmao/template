//region 全局样式一次引入
import "antd/dist/antd.css";
import "@styles/basic-global-vars.css";
import "@styles/basic-global.css";
//endregion

//region 全局一次配置
import "moment/locale/zh-cn"; // DatePicker 月份显示中文
//endregion

import Head from "next/head";
import React from "react";
import {ConfigProvider} from "antd";
import zhCN from "antd/lib/locale/zh_CN";
import BasicLayout from "@components/basic-layout";

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
