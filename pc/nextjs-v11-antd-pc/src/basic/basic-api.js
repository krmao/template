// noinspection SpellCheckingInspection,HttpUrlsUsage

import BasicFetchUtil from "./utils/basic-fetch-util";

/**
 * 所有网络请求统一放在该类管理
 */
export default class BasicApi {
    static requestInfo = (param) => {
        return BasicFetchUtil.fetchByRelativeUrl("getPoiInfo", param);
    };
}
