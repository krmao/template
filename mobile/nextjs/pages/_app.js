import React from "react";
import App, {Container} from "next/app";

class MyApp extends App {
    static async getInitialProps({Component, ctx}) {
        console.log("[LIFECYCLE](App) getInitialProps");
        let pageProps = {};

        if (Component.getInitialProps) {
            pageProps = await Component.getInitialProps(ctx);
        }

        return {pageProps};
    }

    constructor(props) {
        super(props);
        console.log("[LIFECYCLE](App) constructor"); // props=", props, "state=", this.state);
    }

    render() {
        console.log("[LIFECYCLE](App) render");
        const {Component, pageProps} = this.props;

        return (
            <Container>
                <Component {...pageProps} />
            </Container>
        );
    }
}

export default MyApp;
