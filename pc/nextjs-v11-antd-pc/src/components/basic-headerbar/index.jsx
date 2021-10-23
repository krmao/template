import React, {Component} from "react";

import styles from "./index.module.scss";
import {Select, Space, Avatar, Spin, notification} from "antd";
import logoImage from "@public/logo.png";
import Image from "next/image";
import BasicLoginUtil from "@utils/basic-login-util";

export default class BasicHeaderBar extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className={styles["header-content"]}>
                <ul>
                    <li style={{marginLeft: "49px", cursor: "pointer"}}>
                        <Image width={60} height={60} alt={""} src={logoImage} />
                    </li>
                    <li
                        style={{marginLeft: "49px", cursor: "pointer"}}
                        onClick={() => {
                            notification.open({title: "提示", description: "功能暂未上线", duration: 1});
                        }}>
                        <div style={{display: "flex", flexDirection: "row", alignItems: "center"}}>
                            {/*<Icon component={UpOutlined}/>*/}
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
