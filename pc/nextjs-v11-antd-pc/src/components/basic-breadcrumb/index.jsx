import React from "react";
import {Breadcrumb} from "antd";

/**
 * onItemClick 参数有效时，需在componentDidMount设置setState breadcrumbData
 * constructor会存在失效情况
 */
export default class BasicBreadcrumb extends React.Component {
    constructor(props) {
        super(props);
    }

    _handleGoto(url) {
        this.props.breadcrumbClick(url);
    }

    render() {
        const data = this.props.data && this.props.data.data;
        return (
            <div>
                <Breadcrumb style={{userSelect: "none"}} separator=">">
                    {(data || []).map((item, index) => {
                        return (
                            <Breadcrumb.Item
                                className={item.path ? "breadcrumb-item-link" : "breadcrumb-item-text"}
                                style={
                                    index === data.length - 1
                                        ? {
                                              color: "#4c5a75",
                                              fontSize: "14px",
                                              fontFamily: "PingFangSC-Regular Microsoft YaHei, serif",
                                              letterSpacing: "0"
                                          }
                                        : {
                                              cursor: "pointer",
                                              color: "#8790a3",
                                              fontSize: "14px",
                                              fontFamily: "PingFangSC-Regular Microsoft YaHei, serif",
                                              letterSpacing: "0"
                                          }
                                }
                                key={index}
                                onClick={
                                    item.path
                                        ? () => {
                                              this._handleGoto(item.path);
                                          }
                                        : () => {
                                              item.onItemClick && item.onItemClick();
                                          }
                                }>
                                {item.name}
                            </Breadcrumb.Item>
                        );
                    })}
                </Breadcrumb>
            </div>
        );
    }
}
