// noinspection SpellCheckingInspection,HttpUrlsUsage

import BasicHttpClient from "./basic-utils/basic-http-client";
import BasicEnvUtil from "@basic/basic-env";
import {APP_DOMAIN_PROD, APP_DOMAIN_UAT, APP_REQUEST_PATH_PREFIX} from "@basic/basic-constants";

BasicHttpClient.code = 200;
BasicHttpClient.timeout = 15 * 1000;

/**
 * 所有网络请求统一放在该类管理
 */
export default class BasicApi {
    static baseUrl = (serverContext) => {
                  return `${
            BasicEnvUtil.getApiEnv(serverContext) !== BasicEnvUtil.PROD ? APP_DOMAIN_UAT : APP_DOMAIN_PROD
        }${APP_REQUEST_PATH_PREFIX}`;
    };

    /**
     * 服务端渲染时调用, 才需要传递 serverContext, 为了区分当前请求环境
     * 客户端渲染不用
     * @param data
     * @param serverContext
     * @return {Promise<unknown>}
     */
    static requestByFetch = (data, {serverContext}) => {
        return BasicHttpClient.fetch(`${BasicApi.baseUrl(serverContext)}/getPoiInfo`, data);
    };
    static requestByAxios = (data, {serverContext}) => {
        return BasicHttpClient.post(`${BasicApi.baseUrl(serverContext)}/getPoiInfo`, data);
    };
}
