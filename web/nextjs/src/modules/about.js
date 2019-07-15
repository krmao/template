import React from "react";
import Head from "next/head";
import "isomorphic-unfetch";

import css from "./about.scss";
import ComponentHead from "../../src/library_business/components/head";
import ComponentFooter from "../../src/library_business/components/footer";
import ComponentNavigationLeft from "../../src/library_business/components/navigationLeft";

class About extends React.Component {
    static async getInitialProps() {
        // eslint-disable-next-line no-undef
        const res = await fetch("https://api.github.com/repos/krmao/template");
        const json = await res.json();
        return {stars: json.stargazers_count};
    }

    constructor(props) {
        super(props);
    }

    render() {
        console.log("router->", this.props.router);
        return (
            <div className={css.root}>
                <Head>
                    <title>TT婚纱摄影</title>
                    <meta name="viewport" content="width=device-width" />
                </Head>

                <ComponentHead />

                <div className={css.content}>
                    <div className={css.contentLeft}>
                        <ComponentNavigationLeft />
                    </div>

                    <div className={css.contentRight}>
                        <p>template has {this.props.stars} ⭐️</p>

                        <div className={css.imageList}>
                            <img src="/static/logo.png" alt="logo" />
                            <img src="/static/logo.png" alt="logo" />
                            <img src="/static/logo.png" alt="logo" />
                        </div>
                        <div className={css.imageList}>
                            <img src="/static/logo.png" alt="logo" />
                            <img src="/static/logo.png" alt="logo" />
                            <img src="/static/logo.png" alt="logo" />
                        </div>
                        <div className={css.imageList}>
                            <img src="/static/logo.png" alt="logo" />
                            <img src="/static/logo.png" alt="logo" />
                            <img src="/static/logo.png" alt="logo" />
                        </div>
                        <div className={css.imageList}>
                            <img src="/static/logo.png" alt="logo" />
                            <img src="/static/logo.png" alt="logo" />
                            <img src="/static/logo.png" alt="logo" />
                        </div>
                    </div>
                </div>

                <ComponentFooter />
            </div>
        );
    }
}

export default About;
