/**
 * https://charts.ant.design/
 */
import React, {useState, useEffect} from "react";
import {Column} from "@ant-design/charts";

const BasicMonitorChartsPoiLanguage = (props) => {
    const [data, setData] = useState([]);
    useEffect(() => {
        asyncFetch();
    }, []);
    const asyncFetch = () => {
        fetch("https://gw.alipayobjects.com/os/antfincdn/iPY8JFnxdb/dodge-padding.json")
            .then((response) => response.json())
            .then((json) => setData(json))
            .catch((error) => {
                console.log("fetch data failed", error);
            });
    };
    let config = {
        data: data,
        isGroup: true,
        xField: "月份",
        yField: "月均降雨量",
        seriesField: "name",
        dodgePadding: 2,
        intervalPadding: 20,
        label: {
            position: "middle",
            layout: [{type: "interval-adjust-position"}, {type: "interval-hide-overlap"}, {type: "adjust-color"}]
        }
    };
    return <Column {...config} className={props.className} />;
};

export default BasicMonitorChartsPoiLanguage;
