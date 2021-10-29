// noinspection JSUnusedGlobalSymbols,JSUnusedLocalSymbols,JSUnresolvedVariable

const BasicConstants = require("./basic-constants");
const UAT = "UAT";
const PROD = "PROD";

let _apiEnv = "";
let _didLog = false;

const getApiEnvByURL = () => {
    const originUrl = new URL(window.location.origin);
    const prodUrl = new URL(BasicConstants.APP_DOMAIN_PROD);
    const uatUrl = new URL(BasicConstants.APP_DOMAIN_UAT);

    let env;
    if (originUrl.hostname === prodUrl.hostname) {
        env = PROD;
    } else if (originUrl.hostname === uatUrl.hostname) {
        env = UAT;
    } else {
        env = UAT;
    }
    if (!_didLog) {
        console.log("env originUrl =", originUrl);
        console.log("env prodUrl =", prodUrl);
        console.log("env uatUrl =", uatUrl);
        console.log("env window.ENV=", window.ENV);
        console.log("env", env);
        _didLog = true;
    }
    return env;
};

const getApiEnv = () => {
    if (_apiEnv === "") {
        if (typeof window !== "undefined") {
            _apiEnv = getApiEnvByURL();
        } else {
            _apiEnv = PROD;
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
