import React from "react";
import Head from "next/head";
import Link from "next/link";

import css from "./index.scss";
import ComponentHead from "../base/modules/head";
import ComponentFooter from "../base/modules/footer";
import ComponentNavigationLeft from "../base/modules/navigationLeft";

export default class extends React.Component {

    render() {
        return (
            <div className={css.root}>
                <Head>
                    <title>大神灭霸</title>
                    <meta name="viewport" content="initial-scale=1.0, width=device-width" key="viewport"/>
                </Head>

                <ComponentHead/>

                <div className={css.content}>

                    <div className={css.contentLeft}>
                        <ComponentNavigationLeft/>
                    </div>

                    <div className={css.contentRight}>
                        Click{" "}
                        <Link href={{pathname: "/about", query: {name: "krmao"}}} passHref>
                            <a>
                                here
                            </a>
                        </Link>{" "}
                        to read more
                    </div>
                </div>

                <ComponentFooter/>
            </div>
        );
    }
}
