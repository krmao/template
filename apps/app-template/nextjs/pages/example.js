import React from "react";
import Head from "next/head";
// import Link from "next/link";
import {withRouter} from "next/router";

import css from "./example.scss";
import ComponentHead from "../base/components/head";
import ComponentFooter from "../base/components/footer";
import ComponentNavigationLeft from "../base/components/navigationLeft";

export default withRouter(class extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        console.log("router->", this.props.router);
        return (
            <div className={css.root}>
                <Head>
                    <title>TT婚纱摄影</title>
                    <meta name="viewport" content="width=device-width"/>
                </Head>

                <ComponentHead/>

                <div className={css.content}>

                    <div className={css.contentLeft}>
                        <ComponentNavigationLeft/>
                    </div>

                    <div className={css.contentRight}>
                        <div className={css.imageList}>
                            <img src="../static/logo.png" alt="logo"/>
                            <img src="../static/logo.png" alt="logo"/>
                            <img src="../static/logo.png" alt="logo"/>
                        </div>
                        <div className={css.imageList}>
                            <img src="../static/logo.png" alt="logo"/>
                            <img src="../static/logo.png" alt="logo"/>
                            <img src="../static/logo.png" alt="logo"/>
                        </div>
                        <div className={css.imageList}>
                            <img src="../static/logo.png" alt="logo"/>
                            <img src="../static/logo.png" alt="logo"/>
                            <img src="../static/logo.png" alt="logo"/>
                        </div>
                        <div className={css.imageList}>
                            <img src="../static/logo.png" alt="logo"/>
                            <img src="../static/logo.png" alt="logo"/>
                            <img src="../static/logo.png" alt="logo"/>
                        </div>
                    </div>
                </div>

                <ComponentFooter/>
            </div>
        );
    }
});
