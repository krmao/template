import React from "react";

import css from "./footer.scss";
import ComponentMenu from "./menu";

export default class extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
    }

    render() {
        return (
            <div className={css.root}>
                <ComponentMenu/>
                <div className={css.desc}>
                    TT 婚纱摄影 热线电话: 18217758888
                </div>
            </div>
        );
    }
}
