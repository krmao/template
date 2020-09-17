import React from "react";
import App from "next/app";
import Head from "next/head";

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
        console.log("[LIFECYCLE](App) constructor");
    }

    render() {
        console.log("[LIFECYCLE](App) render");
        const {Component, pageProps} = this.props;

        return (
            <>
                <Head>
                    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no,minimal-ui,viewport-fit=cover" />
                </Head>
                <Component {...pageProps} />
            </>
        );
    }
}

export default MyApp;
