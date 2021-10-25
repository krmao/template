import React from "react";
import {Popover} from "antd";
import Image from "next/image";
import logoImage from "@public/logo.png";

const PopoverContent = (content) => {
    return (
        <pre>
            补图覆盖新增详情：
            <br />
            用户补图：122 <br />
            员工补图：32 <br />
            供应商覆盖：214
        </pre>
    );
};

const BasicMonitorData = (props) => {
    const commonStyle = {display: "flex", flexDirection: "row", alignItems: "center", justifyContent: "center"};
    return (
        <div style={{...commonStyle}}>
            <Image width={60} height={60} alt="" src={logoImage} />
            <div style={{...commonStyle, flexDirection: "column", marginLeft: 10}}>
                <div>{props?.label ?? "数据新增"}</div>
                <div style={{...commonStyle, marginTop: 15}}>
                    +{props?.count ?? 245}
                    <Popover placement="right" title={null} content={PopoverContent(props?.content)} trigger="hover">
                        <Image width={10} height={10} alt="" src={logoImage} />
                    </Popover>
                </div>
            </div>
        </div>
    );
};

export default BasicMonitorData;
