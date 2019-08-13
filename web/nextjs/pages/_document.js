// _document is only rendered on the server side and not on the client side
// Event handlers like onClick can"t be added to this file

// ./pages/_document.js
import Document, {Html, Head, Main, NextScript} from "next/document";
import React from "react";

class MyDocument extends Document {
    static async getInitialProps(ctx) {
        const initialProps = await Document.getInitialProps(ctx);
        return {...initialProps};
    }

    render() {
        return (
            <Html>
                <Head>
                    <link rel="icon" href="/static/favicon.ico" type="image/x-icon" />
                    <link rel="shortcut icon" href="/static/favicon.ico" type="image/x-icon" />

                    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no,minimal-ui,viewport-fit=cover" />

                </Head>
                <body>
                    <Main />
                    <NextScript />
                </body>
               <script type="text/javascript" src="/static/plugins/hybrid-console/hybird-console.js" />
               <link rel="stylesheet" href="/static/plugins/hybrid-console/hybird-console.css" />
            </Html>
        );
    }
}

export default MyDocument;
