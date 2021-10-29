// noinspection JSUnusedGlobalSymbols,JSUnusedLocalSymbols,JSUnresolvedVariable

const FAT = "FAT";
const UAT = "UAT";
const PROD = "PROD";

let _apiEnv = "";
let _didLog = false;

const getApiEnvByURL = () => {
    let origin = window.location.origin;
    let env;
    if (origin.match(/([\s\S]*)localhost|([\s\S]*)127\.0/i) || origin.match(/a.uat.qa.nt.xxx.com/)) {
        env = UAT;
    } else {
        if (origin.match(/([\s\S]*)\.fat\d*\.qa\.nt\.xxx\.com/i)) {
            env = FAT;
        } else if (origin.match(/([\s\S]*)\.uat\d*\.qa\.nt\.xxx\.com/i)) {
            env = UAT;
        } else {
            env = PROD;
        }
    }
    if (!_didLog) {
        console.log("ENV:", env);
        _didLog = true;
    }
    return env;
};

const getApiEnv = () => {
    if (_apiEnv === "") {
        if (typeof window !== "undefined") {
            console.log("window.ENV=", window.ENV);
            _apiEnv = window.ENV === undefined ? getApiEnvByURL() : window.ENV;
        } else {
            _apiEnv = PROD;
        }
    }
    return _apiEnv;
};

module.exports = {
    getApiEnv: getApiEnv, // 网络请求环境
    getDeploymentEnv: () => process.env.APP_ENV, // 部署环境
    FAT: FAT,
    UAT: UAT,
    PROD: PROD
};
