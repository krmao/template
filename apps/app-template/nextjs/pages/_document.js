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
                <link rel="icon" href="../static/favicon.ico" type="image/x-icon"/>
                <link rel="shortcut icon" href="../static/favicon.ico" type="image/x-icon"/>

                // <meta name="viewport" content="width=device-width"/> // bug 在每个页面的 Head 里面加这一行，否则系统会默认加这一行
            </Head>
            <body>
            <Main/>
            <NextScript/>
            </body>
            </Html>
        );
    }
}

export default MyDocument;
