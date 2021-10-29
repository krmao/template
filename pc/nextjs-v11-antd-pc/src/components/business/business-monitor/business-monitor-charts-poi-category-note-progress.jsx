// noinspection NpmUsedModulesInstalled

import React, {useEffect, useState} from "react";
import {Progress} from "antd";
import "./business-monitor-charts-poi-category-note-progress.module.scss";
import ScrollContainerUtil from "@basic-utils/basic-scroll-container-util";
import {UpOutlined} from "@ant-design/icons";

const MonitorCategory = (props) => {
    const commonStyle = {display: "flex", flexDirection: "row", alignItems: "center", justifyContent: "center"};
    return (
        <div key={props?.index} style={{...commonStyle, marginTop: props?.index ?? 1 > 1 ? 10 : 0}}>
            <div
                style={{
                    ...commonStyle,
                    color: "white",
                    backgroundColor: "#1658dc",
                    width: 20,
                    height: 20,
                    borderRadius: 10,
                    marginRight: 20
                }}>
                {props?.index ?? 1}
            </div>
            <div>{props?.label ?? "主题乐园"}</div>
            <Progress
                style={{flex: 1, marginRight: 20, marginLeft: 20}}
                percent={props?.percent ?? 50}
                trailColor={"#00000000"}
                showInfo={false}
                status={"normal"}
            />
            <div>{props?.count ?? 12374}</div>
        </div>
    );
};

const BusinessMonitorChartsPoiCategoryNoteProgress = (props) => {
    const [arrowUp, setArrowUp] = useState(null);

    function updateCurrentArrowStatus(event) {
        let isOnBottom = event
            ? ScrollContainerUtil.isOnBottomByScrollEvent(event)
            : ScrollContainerUtil.isOnBottom("list");
        let isOnTop = event ? ScrollContainerUtil.isOnTopByScrollEvent(event) : ScrollContainerUtil.isOnTop("list");
        if (isOnTop && isOnBottom) {
            // 内容不满一页
            setArrowUp(null);
        } else if (isOnTop) {
            setArrowUp(false);
        } else if (isOnBottom) {
            setArrowUp(true);
        }
    }

    useEffect(() => {
        updateCurrentArrowStatus();
    }, [props.data]);

    return (
        <div className={`category-poi-container${props.className ? " " + props.className : ""}`}>
            <div
                id={"list"}
                className={"category-poi"}
                onScroll={(event) => {
                    updateCurrentArrowStatus(event);
                }}>
                {props.data?.map((item, index) => {
                    return MonitorCategory({
                        index: index + 1,
                        label: "主题乐园:" + index,
                        percent: index > 9 ? 100 - (index - 10) * 10 : 10 + index * 10,
                        count: 1213 + index * 10
                    });
                })}
            </div>

            <UpOutlined
                style={{
                    display: arrowUp === null ? "none" : "inline-block",
                    color: "#1658dc",
                    fontSize: 20,
                    marginTop: 20,
                    userSelect: "none"
                }}
                onClick={() => {
                    if (arrowUp !== null) {
                        if (arrowUp) {
                            setArrowUp(false);
                            ScrollContainerUtil.scrollToTop("list");
                        } else {
                            setArrowUp(true);
                            ScrollContainerUtil.scrollToBottom("list");
                        }
                    }
                }}
                rotate={arrowUp ? 0 : 180}
            />
        </div>
    );
};

export default BusinessMonitorChartsPoiCategoryNoteProgress;
