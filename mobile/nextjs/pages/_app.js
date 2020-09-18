import React from "react";
import App from "next/app";
import Head from "next/head";
import "antd-mobile/dist/antd-mobile.css";

class MyApp extends App {
    static async getInitialProps({Component, ctx}) {
        console.log("[LIFECYCLE](App) getInitialProps");
        let pageProps = {};

        if (Component.getInitialProps) {
            pageProps = await Component.getInitialProps(ctx);
        }

        return {pageProps};
    }

    constructor() {
        super();
        console.log("[LIFECYCLE](App) constructor 只会执行一次 process.browser=", process.browser);
    }

    render() {
        /**
         * 兼容性
         * https://caniuse.com/?search=devicePixelRatio
         * https://www.zhangxinxu.com/wordpress/2012/08/window-devicepixelratio/
         *
         * PC 上按照物理像素设置 px
         * 手机上按照设备独立像素设置 px, 即无需额外的设置以达到适配不同手机屏幕的目的, 在手机设备上, 相当于 android 的 dp 单位
         */
        if (typeof window !== "undefined" && typeof document !== "undefined") {
            let devicePixelRatio = window.devicePixelRatio;
            let screenWidth = window.screen.width;
            let clientWidth = document.body.clientWidth;
            let offsetWidth = document.body.offsetWidth;
            console.log("[LIFECYCLE](App) render devicePixelRatio=" + devicePixelRatio + ", screenWidth=" + screenWidth + ", clientWidth=" + clientWidth);
        } else {
            console.log("[LIFECYCLE](App) render");
        }

        const {Component, pageProps} = this.props;
        return (
            <>
                <Head>
                    <meta id="viewport" name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui,viewport-fit=cover"/>
                </Head>
                <Component {...pageProps} />
            </>
        );
    }
}

export default MyApp;
