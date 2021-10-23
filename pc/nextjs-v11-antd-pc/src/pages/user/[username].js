import {useRouter} from "next/router";
import React from "react";

const UserDetail = () => {
    const router = useRouter();
    console.log(router);
    return <h1>用户信息：{router.query.username}</h1>;
};

export default UserDetail;
