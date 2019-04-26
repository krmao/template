import React from "react";
import Head from "next/head";
// import Link from "next/link";
import {withRouter} from "next/router";

import css from "./about.scss";
import ComponentHead from "../base/components/head";
import ComponentFooter from "../base/components/footer";
import ComponentNavigationLeft from "../base/components/navigationLeft";

export default withRouter(class extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        console.log(this.props.router.asPath);
        return (
            <div className={css.root}>
                <Head>
                    <title>TT婚纱摄影</title>

                    <link rel="icon" href="../static/favicon.ico" type="image/x-icon"/>
                    <link rel="shortcut icon" href="../static/favicon.ico" type="image/x-icon"/>

                    <meta name="viewport" content="initial-scale=1.0, width=device-width" key="viewport"/>
                </Head>

                <ComponentHead/>

                <div className={css.content}>

                    <div className={css.contentLeft}>
                        <ComponentNavigationLeft/>
                    </div>

                    <div className={css.contentRight}>
                        {/*Click{" "}
                        <Link href={{pathname: "/about", query: {name: "krmao"}}} passHref>
                            <a>
                                here
                            </a>
                        </Link>{" "}
                        to read more*/}
                    </div>
                </div>

                <ComponentFooter/>
            </div>
        );
    }
});
