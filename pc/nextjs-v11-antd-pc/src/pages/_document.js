// _document is only rendered on the server side and not on the client side
// Event handlers like onClick can"t be added to this file
// noinspection JSUnusedGlobalSymbols,HtmlRequiredTitleElement

// ./pages/_document.js
import Document, {Head, Html, Main, NextScript} from "next/document";
import React from "react";

// 处理控制台警告 Warning: useLayoutEffect does nothing on the server, because its effect cannot be encoded into the server renderer's output format.
// https://github.com/ant-design/ant-design/issues/30396
// https://github.com/ant-design/ant-design/issues/30396#issuecomment-927299855
React.useLayoutEffect = React.useEffect;

class MyDocument extends Document {
    static async getInitialProps(ctx) {
        const initialProps = await Document.getInitialProps(ctx);
        return {...initialProps};
    }

    constructor() {
        super();
        console.log("[LIFECYCLE](Document) constructor");
    }

    render() {
        console.log("[LIFECYCLE](Document) render");
        return (
            <Html>
                <Head nonce={""} crossOrigin={""}>
                    <link rel="icon" href={"/static/favicon/favicon-1-blue.png"} type="image/x-icon" />
                    <link rel="shortcut icon" href={"/static/favicon/favicon-1-blue.png"} type="image/x-icon" />
                </Head>
                <body>
                    <Main />
                    <NextScript nonce={""} crossOrigin={""} />
                </body>
            </Html>
        );
    }
}

export default MyDocument;
