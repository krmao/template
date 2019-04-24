import React from "react";

import css from "./head.scss";

export default class extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
    }

    render() {
        return (
            <div className={css.root}>
                <div className={css.header}>
                    <img className={css.logo} src="../../static/logo.jpg" width={"80px"} height={"80px"} alt="logo"/>
                    <div className={css.title}>
                        XXX-TITLE
                    </div>
                    <div className={css.tip}>
                        *****************<br/>
                        ==========
                    </div>

                    <div className={css.tel}>
                        XXXXXXXXXX
                    </div>
                </div>
                <div className={css.menu}>
                    <table className={css.menuTable} width="100%" border="0" align="center" cellPadding={0} cellSpacing={0}>
                        <tbody>
                        <tr>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                            <td align="center" valign="middle">
                                HOME
                            </td>
                        </tr>
                        </tbody>

                    </table>
                </div>
            </div>
        );
    }
}
