// noinspection SpellCheckingInspection,HttpUrlsUsage

import BasicHttpClient from "./basic-utils/basic-http-client";

/**
 * 所有网络请求统一放在该类管理
 */
export default class BasicApi {
    static requestInfo = (param) => {
        return BasicHttpClient.fetch("getPoiInfo", param);
    };
}
