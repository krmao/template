import React from "react";

import css from "./navigationLeft.scss";

export default class extends React.Component {
    constructor(props) {
        super(props);

        this.data = [
            {
                title: "婚礼花篮",
                item: [
                    {
                        title: "玫瑰"
                    }
                ]
            }
        ];
    }

    render() {
        return (
            <div className={css.root}>
                <div className={css.typeNavigation}>
                    <h4>
                        关于我们
                    </h4>
                    <ul>
                        <li>
                            关于我们
                        </li>
                        <li>
                            联系我们
                        </li>
                    </ul>
                </div>
                <div className={css.typeContact}>
                    <h4>
                        联系方式
                    </h4>
                    <p>
                        沭阳 TT 婚纱摄影<br/>
                        联系人: 张经理
                    </p>
                </div>
            </div>
        );
    }
}
