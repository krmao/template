import React from "react";

import css from "./navigationLeft.scss";

export default class extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
    }

    render() {
        return (
            <div className={css.root}>
                <div className={css.typeNavigation}>
                    <h4>
                        TITLE-0
                    </h4>
                    <ul>
                        <li>
                            xxxxx0
                        </li>
                        <li>
                            xxxxx0
                        </li>
                        <li>
                            xxxxx0
                        </li>
                        <li>
                            xxxxx0
                        </li>
                        <li>
                            xxxxx0
                        </li>
                        <li>
                            xxxxx0
                        </li>
                        <li>
                            xxxxx0
                        </li>
                        <li>
                            xxxxx0
                        </li>
                        <li>
                            xxxxx0
                        </li>

                    </ul>
                </div>
                <div className={css.typeContact}>
                    <h4>
                        TITLE-1
                    </h4>
                    <p>
                        DESC-1
                    </p>
                </div>
            </div>
        );
    }
}
