import React from "react";
import Head from "next/head";
// import Link from "next/link";
import {withRouter} from "next/router";

import css from "./index.scss";
import ComponentHead from "../../components/library_business/common/head";
import ComponentFooter from "../../components/library_business/common/footer";
import ComponentNavigationLeft from "../../components/library_business/common/navigationLeft";

export default withRouter(
    class extends React.Component {
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
                            {/*Click{" "}
                        <Link href={{pathname: "/about", query: {name: "krmao"}}} passHref>
                            <a>
                                here
                            </a>
                        </Link>{" "}
                        to read more*/}

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
);
