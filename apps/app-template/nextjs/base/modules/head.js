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
                LOGO & TITLE MESSAGE
            </div>
        );
    }
}
