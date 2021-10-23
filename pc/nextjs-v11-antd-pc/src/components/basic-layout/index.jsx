// noinspection JSUnusedLocalSymbols,JSUnresolvedFunction

import React from "react";
import {Layout, Menu} from "antd";
import styles from "./index.module.scss";
import {Icon} from "@ant-design/compatible";
import BasicHeaderBar from "@components/basic-headerbar";
import BasicBreadcrumb from "@components/basic-breadcrumb";
import BasicImageSvgs from "@utils/basic-image-svgs";
import MapSvg from "@public/map.svg";

import stylesLess from "./index.module.less";

const menuData = [
    {
        title: "数据监控",
        icon: BasicImageSvgs.dataStatistic,
        key: "/monitor",
        titleAlias: "数据监控"
    },
    {
        title: "数据监控",
        icon: MapSvg,
        key: "/monitor",
        titleAlias: "数据监控"
    }
];

export default class BasicLayout extends React.Component {
    static _DEBUG = false;
    static _ENABLE_SUBMENU = false;

    constructor(props) {
        super(props);
        let {paths} = props;

        let defaultOpenKeysForOneLevel = [paths && paths.length > 0 ? "/" + paths[0] : "/home"];
        let defaultSelectedKeysForFinalLevel = defaultOpenKeysForOneLevel;

        if (BasicLayout._DEBUG) {
            console.log(
                "paths",
                paths,
                "defaultOpenKeysForOneLevel",
                defaultOpenKeysForOneLevel,
                "defaultSelectedKeysForFinalLevel",
                defaultSelectedKeysForFinalLevel
            );
        }

        this.state = {
            headerHeight: "64px",
            paths: paths,
            defaultOpenKeysForOneLevel: defaultOpenKeysForOneLevel, // default 只有初次生效, 所以需要定义 current
            currentOpenKeysForOneLevel: defaultOpenKeysForOneLevel, // default 只有初次生效, 所以需要定义 current, 后续改变则会刷新 menu
            defaultSelectedKeysForFinalLevel: defaultSelectedKeysForFinalLevel,
            currentSelectedKeysForFinalLevel: defaultSelectedKeysForFinalLevel,
            breadcrumbData: {data: []}
        };
    }

    componentDidMount() {
        if (BasicLayout._DEBUG) console.log("BasicLayout componentDidMount");
    }

    componentWillUnmount() {
        if (BasicLayout._DEBUG) console.log("BasicLayout componentWillUnmount");
    }

    renderMenu(data) {
        let that = this;
        return data.map((item, index) => {
            if (BasicLayout._ENABLE_SUBMENU && item.children) {
                return (
                    <Menu.SubMenu
                        icon={item.icon ? <Icon component={item.icon} /> : ""}
                        title={item.titleAlias || item.title}
                        key={item.key}>
                        {this.renderMenu(item.children)}
                    </Menu.SubMenu>
                );
            }
            return (
                <Menu.Item
                    className={index === 0 ? "menu-item-first" : "menu-item"}
                    icon={item.icon ? <Icon component={item.icon} /> : ""}
                    title={item.titleAlias || item.title}
                    onClick={() => {
                        that.goto(item.key);
                    }}
                    key={item.key}>
                    {item.title}
                </Menu.Item>
            );
        });
    }

    render() {
        let that = this;
        // noinspection JSCheckFunctionSignatures
        return (
            <React.Fragment>
                <div className={styles["layout-root"]}>
                    <div className={styles["layout-header"]} style={{padding: 0, backgroundColor: "white"}}>
                        <div className={styles.logo} style={{height: that.state.headerHeight}}>
                            <div className={styles.logoImgDiv} />
                            <span>TEST</span>
                            <span>
                                {" "}
                                <MapSvg />
                            </span>
                        </div>
                        <BasicHeaderBar userName={that.props.userName} user={that.props.user} />
                    </div>

                    <Layout className={styles["layout-content-sider"]} hasSider style={{minHeight: "100vh"}}>
                        <Layout.Sider className={styles.sidebar} theme="light">
                            <Menu
                                defaultSelectedKeys={that.state.defaultSelectedKeysForFinalLevel} // 默认打开的最终页面对应的菜单, 只有初次有效
                                openKeys={that.state.currentSelectedKeysForFinalLevel} // 当前打开的最终页面对应的菜单, 修改有效
                                defaultOpenKeys={that.state.defaultOpenKeysForOneLevel} // 默认打开的一级菜单, 只有初次有效
                                selectedKeys={that.state.currentOpenKeysForOneLevel} // 当前打开的一级菜单, 修改有效
                                className="menu"
                                theme="light"
                                mode="inline">
                                {that.renderMenu(menuData)}
                            </Menu>
                            <a className={styles.feedback} target="_blank" rel="noreferrer" href={""}>
                                意见反馈
                            </a>
                            <a className={stylesLess.layoutRootLess} target="_blank" rel="noreferrer" href={""}>
                                意见反馈2
                            </a>
                        </Layout.Sider>
                        <Layout.Content className={styles["layout-content-breadcrumb"]}>
                            <div
                                className={styles.breadcrumb}
                                style={{
                                    paddingTop:
                                        this.state.breadcrumbData &&
                                        this.state.breadcrumbData.data &&
                                        this.state.breadcrumbData.data.length > 0
                                            ? 24
                                            : 0
                                }}>
                                <BasicBreadcrumb
                                    data={this.state.breadcrumbData}
                                    breadcrumbClick={(url) => {
                                        that.goto(url);
                                    }}
                                    style={{
                                        display:
                                            this.state.breadcrumbData &&
                                            this.state.breadcrumbData.data &&
                                            this.state.breadcrumbData.data.length > 0
                                                ? "block"
                                                : "none"
                                    }}
                                />
                            </div>

                            <div className={styles["layout-content"]}>
                                {React.cloneElement(this.props.children, {
                                    userName: that.props.userName,
                                    user: that.props.user,
                                    handleBreadcrumbData: (value) => {
                                        that.setState({breadcrumbData: value});
                                    }
                                })}
                            </div>
                        </Layout.Content>
                    </Layout>
                </div>
            </React.Fragment>
        );
    }
}
