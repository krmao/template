/**
 * https://charts.ant.design/
 */
import React, {useState, useEffect} from "react";
import {Area} from "@ant-design/charts";

const BusinessMonitorChartsPoiCount = (props) => {
    const [data, setData] = useState([]);

    useEffect(() => {
        asyncFetch();
    }, []);

    const asyncFetch = () => {
        fetch("https://gw.alipayobjects.com/os/bmw-prod/b21e7336-0b3e-486c-9070-612ede49284e.json")
            .then((response) => response.json())
            .then((json) => setData(json))
            .catch((error) => {
                console.log("fetch data failed", error);
            });
    };
    // noinspection NonAsciiCharacters
    let config = {
        data: data,
        xField: "date",
        yField: "value",
        seriesField: "country",
        legend: {
            selected: {
                北美: props.poiCountType === "month",
                中南美: props.poiCountType === "halfYear",
                欧洲: props.poiCountType === "year",
                中东: props.poiCountType === "range"
            }
        }
    };
    return <Area className={props.className} {...config} />;
};

export default BusinessMonitorChartsPoiCount;
