// noinspection JSUnusedGlobalSymbols,JSUnusedLocalSymbols,JSUnresolvedVariable

const BasicConstants = require("./basic-constants");
const BasicValueUtil = require("./basic-utils/basic-value-util");
const UAT = "UAT";
const PROD = "PROD";

let _apiEnv = "";
let _didLog = false;

/**
 * https://github.com/vercel/next.js/issues/1553#issuecomment-290062887
 * @param currentHostname
 * @return {string}
 */
const getApiEnvByURL = (currentHostname = window?.location?.hostname ?? "") => {
    const prodHostName = new URL(BasicConstants.APP_DOMAIN_PROD).hostname;
    const uatHostName = new URL(BasicConstants.APP_DOMAIN_UAT).hostname;

    let currentApiEnv;
    if (currentHostname === prodHostName) {
        currentApiEnv = PROD;
    } else if (currentHostname === uatHostName) {
        currentApiEnv = UAT;
    } else {
        currentApiEnv = UAT; // 服务端渲染时调用, 务必传入正确的 currentHostname, 不能使用 window 变量
    }
    if (!_didLog) {
        console.log("env currentHostname =", currentHostname);
        console.log("env prodHostName =", prodHostName);
        console.log("env uatHostName =", uatHostName);
        console.log("env", currentApiEnv);
        _didLog = true;
    }
    return currentApiEnv;
};

const getApiEnv = (serverContext) => {
    if (_apiEnv === "") {
        if (typeof window !== "undefined") {
            console.log("> call on client side");
            const currentHostName = window?.location?.hostname ?? "";
            _apiEnv = getApiEnvByURL(currentHostName);
        } else {
            let currentHost = serverContext?.req?.headers?.host ?? "";
            currentHost = BasicValueUtil.stringStartsWith(currentHost, "http") ? "" : "https://" + currentHost;
            console.log("> call on server side", new URL(currentHost));
            const currentHostName = BasicValueUtil.isStringNotBlank(currentHost) ? new URL(currentHost).hostname : "";
            _apiEnv = getApiEnvByURL(currentHostName);
        }
    }
    return _apiEnv;
};

module.exports = {
    getApiEnv: getApiEnv, // 网络请求环境
    getDeploymentEnv: () => process.env.APP_ENV, // 部署环境
    UAT: UAT,
    PROD: PROD
};
