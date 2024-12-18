// _document is only rendered on the server side and not on the client side
// Event handlers like onClick can"t be added to this file

// ./pages/_document.js
import Document, {Head, Html, Main, NextScript} from "next/document";
import React from "react";

class MyDocument extends Document {

    static async getInitialProps(ctx) {
        console.log("[LIFECYCLE](Document) getInitialProps");
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
                    <link rel="icon" href={"/static/favicon/favicon-1-blue.png"} type="image/x-icon"/>
                    <link rel="shortcut icon" href={"/static/favicon/favicon-1-blue.png"} type="image/x-icon"/>

                    <link rel="stylesheet" href={"/static/plugins/hybrid-console/hybird-console.css"}/>
                </Head>
                <body>
                <Main/>
                <NextScript nonce={""} crossOrigin={""}/>
                </body>
                <script type="text/javascript" src={"/static/plugins/hybrid-console/hybird-console.js"}/>
            </Html>
        );
    }
}

export default MyDocument;
