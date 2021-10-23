import {Button} from "antd";
import Router from "next/router";
import React from "react";
import Image from "next/image";
import emptyImage from "@public/empty.png";

export default function Custom500() {
    return (
        <div className="container">
            <style jsx>{`
                .container {
                    position: absolute;
                    top: 0;
                    display: flex;
                    width: 100%;
                    height: 100%;
                    flex-direction: column;
                    align-items: center;
                    justify-content: center;
                }
                .error-image {
                    width: 200px;
                    height: 200px;
                    margin: 10px 0;
                }
            `}</style>
            <Image className={"error-image"} width={60} height={60} alt={""} src={emptyImage} />
            <h3>The page is error | 500～</h3>
            <Button onClick={() => Router.push("/")} type="primary" ghost>
                Back Home
            </Button>
        </div>
    );
}
