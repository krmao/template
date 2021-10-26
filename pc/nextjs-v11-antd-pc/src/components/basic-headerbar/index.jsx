import React, {Component} from "react";

import styles from "./index.module.scss";
import {Select, Space, Avatar, Spin, notification} from "antd";
import BasicLoginUtil from "@utils/basic-login-util";
import Image from "next/image";
import stylesLess from "./index.module.less";
import logoImage from "@public/logo.png";

export default class BasicHeaderBar extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className={styles["header-content"]}>
                <ul>
                    <li
                        style={{marginLeft: "49px", cursor: "pointer"}}
                        onClick={() => {
                            // noinspection JSCheckFunctionSignatures
                            notification.open({title: "提示", description: "功能暂未上线", duration: 1});
                        }}>
                        <div style={{display: "flex", flexDirection: "row", alignItems: "center"}}>
                            <div className={stylesLess.layoutRootLess}>
                                <Image width={20} height={20} alt={""} src={logoImage} />
                            </div>
                            <span
                                style={{
                                    marginLeft: "7px",
                                    whiteSpace: "nowrap",
                                    userSelect: "none",
                                    fontFamily: "PingFangSC-Medium, Microsoft YaHei, serif",
                                    fontWeight: 600
                                }}>
                                消息通知
                            </span>
                        </div>
                    </li>
                    <li style={{marginLeft: "68px"}}>
                        <Avatar
                            size={32}
                            style={{
                                borderWidth: 1,
                                backgroundColor: "#E0F1FF",
                                borderStyle: "solid",
                                borderColor: "#E7E8EC"
                            }}
                            src={"/default_avatar.png"}
                        />
                        {this.props.userName ? (
                            <Select
                                onChange={() => BasicLoginUtil.loginOut()}
                                value={this.props.userName}
                                style={{
                                    fontFamily: "PingFangSC-Medium, Microsoft YaHei, serif",
                                    fontWeight: 600,
                                    fontSize: 16,
                                    color: "#102247"
                                }}
                                bordered={false}>
                                <Select.Option
                                    style={{
                                        textAlign: "center",
                                        fontFamily: "PingFangSC-Medium, Microsoft YaHei, serif",
                                        fontWeight: 600
                                    }}
                                    value="">
                                    退出
                                </Select.Option>
                            </Select>
                        ) : (
                            <Space style={{marginLeft: "28px"}} size="middle">
                                <Spin size="small" />
                            </Space>
                        )}
                    </li>
                </ul>
            </div>
        );
    }
}
