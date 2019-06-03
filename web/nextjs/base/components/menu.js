import React from "react";
import Link from "next/link";
import {withRouter} from "next/router";
import css from "./menu.scss";

export default withRouter(class extends React.Component {
    constructor(props) {
        super(props);
        this.path = this.props.router.asPath;
        this.currentPath = this.path.replace(new RegExp("/", "g"), "");
    }

    render() {
        return (
            <div className={css.root}>
                <div className={css.menu}>
                    <table className={css.menuTable} width="100%" border="0" align="center" cellPadding={0} cellSpacing={0}>
                        <tbody>
                        <tr>
                            <td className={(this.currentPath === "index" || this.currentPath === "") ? css.menuSelected : css.menuNormal} align="center" valign="middle">
                                <Link href={{pathname: "/index"}} passHref>
                                    <a>网站首页</a>
                                </Link>
                            </td>
                            <td className={this.currentPath === "about" ? css.menuSelected : css.menuNormal} align="center" valign="middle">
                                <Link href={{pathname: "/about"}} passHref>
                                    <a>关于我们</a>
                                </Link>
                            </td>
                            <td className={this.currentPath === "example" ? css.menuSelected : css.menuNormal} align="center" valign="middle">
                                <Link href={{pathname: "/example"}} passHref>
                                    <a>成品案例</a>
                                </Link>
                            </td>
                            <td className={this.currentPath === "trend" ? css.menuSelected : css.menuNormal} align="center" valign="middle">
                                <Link href={{pathname: "/trend"}} passHref>
                                    <a>行业动态</a>
                                </Link>
                            </td>
                            <td className={this.currentPath === "contact" ? css.menuSelected : css.menuNormal} align="center" valign="middle">
                                <Link href={{pathname: "/contact"}} passHref>
                                    <a>联系我们</a>
                                </Link>
                            </td>
                        </tr>
                        </tbody>

                    </table>
                </div>
            </div>
        );
    }
});
