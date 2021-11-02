// noinspection SpellCheckingInspection,HttpUrlsUsage

import BasicHttpClient from "./basic-utils/basic-http-client";
import {APP_REQUEST_BASE_URL} from "./basic-config";

BasicHttpClient.code = 200;
BasicHttpClient.timeout = 15 * 1000;

/**
 * 所有网络请求统一放在该类管理
 */
export default class BasicApi {
    /**
     * 服务端渲染时调用, 才需要传递 serverContext, 为了区分当前请求环境
     * 客户端渲染不用
     * @param data
     * @param serverContext
     * @return {Promise<unknown>}
     */
    static requestByFetch = (data, serverContext) => {
        return BasicHttpClient.fetch(`${APP_REQUEST_BASE_URL(serverContext)}/getPoiInfo`, data);
    };
    static requestByAxios = (data, serverContext) => {
        return BasicHttpClient.post(`${APP_REQUEST_BASE_URL(serverContext)}/getPoiInfo`, data);
    };
}
