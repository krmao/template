// noinspection JSUnusedGlobalSymbols,JSUnusedLocalSymbols,JSUnresolvedVariable

export default class BasicEnvUtil {
    static FAT = "fat";
    static UAT = "uat";
    static PROD = "prod";
    static LOCAL = "local";

    static _didLog = false;

    static getEnvByURL = () => {
        let origin = window.location.origin;
        let env;
        if (origin.match(/([\s\S]*)localhost|([\s\S]*)127\.0/i) || origin.match(/a.uat.qa.nt.xxx.com/)) {
            env = BasicEnvUtil.LOCAL;
        } else {
            if (origin.match(/([\s\S]*)\.fat\d*\.qa\.nt\.xxx\.com/i)) {
                env = BasicEnvUtil.FAT;
            } else if (origin.match(/([\s\S]*)\.uat\d*\.qa\.nt\.xxx\.com/i)) {
                env = BasicEnvUtil.UAT;
            } else {
                env = BasicEnvUtil.PROD;
            }
        }
        if (!BasicEnvUtil._didLog) {
            console.log("ENV:", env);
            BasicEnvUtil._didLog = true;
        }
        return env;
    };

    static _env = "";
    static getEnv = () => {
        if (BasicEnvUtil._env === "") {
            if (typeof window !== "undefined") {
                BasicEnvUtil._env = window.ENV === undefined ? BasicEnvUtil.getEnvByURL() : window.ENV;
            } else {
                BasicEnvUtil._env = BasicEnvUtil.LOCAL;
            }
        }
        return BasicEnvUtil._env;
    };
}
