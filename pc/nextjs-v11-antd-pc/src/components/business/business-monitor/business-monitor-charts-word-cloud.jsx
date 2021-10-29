/**
 * https://charts.ant.design/
 */
import React, {useState, useEffect} from "react";
import {WordCloud} from "@ant-design/charts";

const BusinessMonitorChartsWordCloud = (props) => {
    const [data, setData] = useState([]);
    useEffect(() => {
        asyncFetch();
    }, []);
    const asyncFetch = () => {
        fetch("https://gw.alipayobjects.com/os/antfincdn/jPKbal7r9r/mock.json")
            .then((response) => response.json())
            .then((json) => setData(json))
            .catch((error) => {
                console.log("fetch data failed", error);
            });
    };
    let config = {
        data: data,
        wordField: "x",
        weightField: "value",
        color: "#1658dc",
        wordStyle: {
            fontFamily: "Verdana",
            fontSize: [24, 80]
        },
        interactions: [{type: "element-active"}],
        state: {active: {style: {lineWidth: 3}}}
    };
    return <WordCloud className={props.className} {...config} />;
};

export default BusinessMonitorChartsWordCloud;
