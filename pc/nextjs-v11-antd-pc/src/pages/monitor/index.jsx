// noinspection ES6CheckImport

import React from "react";
import {DatePicker, Divider, Popover, Tabs} from "antd";
import AsyncComponent from "@components/basic-async-component";
import BasicMonitorData from "@components/basic-monitor/basic-monitor-data";
import moment from "moment";
import textUtil from "@utils/basic-text-util";
import Image from "next/image";
import logoImage from "@public/logo.png";
import style from "./index.module.scss";

const {TabPane} = Tabs;
const ChartsPoiCount = AsyncComponent(() => import("@components/basic-monitor/basic-monitor-charts-poi-count"));
const ChartsWordCloud = AsyncComponent(() => import("@components/basic-monitor/basic-monitor-charts-word-cloud"));
const ChartsPoiLanguage = AsyncComponent(() => import("@components/basic-monitor/basic-monitor-charts-poi-language"));
const ChartsPoiCategoryNoteProgress = AsyncComponent(() =>
    import("@components/basic-monitor/basic-monitor-charts-poi-category-note-progress")
);

export default class MonitorPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            breadcrumbData: {data: []},
            poiCountType: "month",
            poiCountTypeDateRangeString: null,
            data: {}
        };
    }

    componentDidMount() {
        this.props.handleBreadcrumbData(this.state.breadcrumbData);
    }

    render() {
        const that = this;
        return (
            <React.Fragment>
                <div className={style["container"]}>
                    <div
                        className={style["container-data"]}
                        style={{display: that.state.data == null ? "none" : "flex"}}>
                        <div className={style["poi-data-dynamic-container"]}>
                            <div className={style["title"]}>
                                每日数据动态
                                <Popover
                                    placement="right"
                                    title={null}
                                    content={"统计前一天景点、玩乐、餐厅、购物类型数据变更信息"}
                                    trigger="hover">
                                    <Image width={10} height={10} alt={""} src={logoImage} />
                                </Popover>
                            </div>
                            <div className={style["poi-data-dynamic"]}>
                                <BasicMonitorData />
                                <Divider type="vertical" style={{height: 52, width: 1}} />
                                <BasicMonitorData />
                                <Divider type="vertical" style={{height: 52, width: 1}} />
                                <BasicMonitorData />
                                <Divider type="vertical" style={{height: 52, width: 1}} />
                                <BasicMonitorData />
                            </div>
                        </div>
                        <div className={style["poi-count-container"]}>
                            <div className={style["poi-count-title"]}>
                                <div className={style["title"]}>
                                    数量趋势
                                    <Popover
                                        placement="right"
                                        title={null}
                                        content={"统计前一天景点、玩乐、餐厅、购物类型数据变更信息"}
                                        trigger="hover">
                                        <Image width={10} height={10} alt={""} src={logoImage} />
                                    </Popover>
                                </div>
                                <div className={style["poi-count-title-menu"]}>
                                    <span
                                        className={style["poi-count-title-menu-item"]}
                                        style={{color: this.state.poiCountType === "month" ? "#1658dc" : "#333333"}}
                                        onClick={() =>
                                            this.setState({poiCountType: "month", poiCountTypeDateRangeString: null})
                                        }>
                                        本月
                                    </span>
                                    <span
                                        className={style["poi-count-title-menu-item"]}
                                        style={{color: this.state.poiCountType === "halfYear" ? "#1658dc" : "#333333"}}
                                        onClick={() =>
                                            this.setState({poiCountType: "halfYear", poiCountTypeDateRangeString: null})
                                        }>
                                        半年
                                    </span>
                                    <span
                                        className={style["poi-count-title-menu-item"]}
                                        style={{color: this.state.poiCountType === "year" ? "#1658dc" : "#333333"}}
                                        onClick={() =>
                                            this.setState({poiCountType: "year", poiCountTypeDateRangeString: null})
                                        }>
                                        全年
                                    </span>
                                    <DatePicker.RangePicker
                                        className={style["poi-count-title-menu-item-range"]}
                                        getPopupContainer={(trigger) => trigger.parentElement}
                                        style={{width: 300, height: 30}}
                                        showTime={false}
                                        value={
                                            this.state.poiCountType === "range" &&
                                            textUtil.isNotBlank(this.state.poiCountTypeDateRangeString)
                                                ? [
                                                      moment(this.state.poiCountTypeDateRangeString?.[0], "YYYY-MM-DD"),
                                                      moment(this.state.poiCountTypeDateRangeString?.[1], "YYYY-MM-DD")
                                                  ]
                                                : ""
                                        }
                                        format="YYYY-MM-DD"
                                        allowClear={false}
                                        suffixIcon={""}
                                        onChange={(value, dateString) => {
                                            console.log("数量趋势时间段 onChange", value, dateString);
                                            this.setState({
                                                poiCountType: "range",
                                                poiCountTypeDateRangeString: dateString
                                            });
                                        }}
                                    />
                                </div>
                            </div>
                            <ChartsPoiCount
                                className={style["poi-count"]}
                                poiCountType={this.state.poiCountType}
                                poiCountTypeDateRangeString={this.state.poiCountTypeDateRangeString}
                            />
                        </div>

                        <div className={style["poi-word-cloud-category"]}>
                            <div className={style["poi-word-cloud-container"]}>
                                <div className={style["title"]}>类目热力图</div>
                                <ChartsWordCloud className={style["poi-word-cloud"]} />
                            </div>
                            <div className={style["poi-category-container"]}>
                                <div className={style["title"]}>笔记挂靠类目排行</div>
                                <ChartsPoiCategoryNoteProgress
                                    className={style["poi-category"]}
                                    data={[1, 2, 3, 4, 5, 6, 7, 5, 4, 4, 1, 21, 2, 3, 4, 5, 6, 7, 5, 4, 4, 1, 2]}
                                />
                            </div>
                        </div>
                        <div className={style["poi-language-container"]}>
                            <div className={style["title"]}>
                                多语言覆盖
                                <Popover
                                    placement="right"
                                    title={null}
                                    content={"统计前一天景点、玩乐、餐厅、购物类型数据变更信息"}
                                    trigger="hover">
                                    <Image alt={""} width={10} height={10} src={logoImage} />
                                </Popover>
                            </div>
                            <Tabs
                                className={style["poi-language-tabs"]}
                                defaultActiveKey="1"
                                onChange={(key) => {
                                    console.log(key);
                                }}>
                                <TabPane tab="英语覆盖" key="1">
                                    <ChartsPoiLanguage className={style["poi-language"]} />
                                </TabPane>
                                <TabPane tab="本国语言覆盖" key="2">
                                    <ChartsPoiLanguage className={style["poi-language"]} />
                                </TabPane>
                            </Tabs>
                        </div>
                    </div>
                    <div
                        className={style["container-empty"]}
                        style={{display: that.state.data == null ? "flex" : "none"}}>
                        <Image width={280} height={280} alt={""} src={logoImage} />
                        <div className={style["empty-text"]}>功能上线中，敬请期待~</div>
                    </div>
                </div>
            </React.Fragment>
        );
    }
}
