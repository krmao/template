import React from "react";

import css from "./footer.scss";

export default class extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
    }

    render() {
        return (
            <div className={css.root}>
                TT 婚纱摄影 热线电话: 18217758888
            </div>
        );
    }
}
