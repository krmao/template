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
                FOOTER & FOOTER MESSAGE
            </div>
        );
    }
}
