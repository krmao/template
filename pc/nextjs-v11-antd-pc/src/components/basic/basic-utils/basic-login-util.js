// noinspection JSUnusedGlobalSymbols,JSUnusedLocalSymbols,JSUnresolvedVariable

import BasicConstants from "../basic-constants";
import BasicEnvUtil from "../basic-env";
import {v4 as uuidv4} from "uuid";

import {getCookies, setCookies, removeCookies} from "cookies-next";

export default class BasicLoginUtil {
    static _debug = false;

    static _domain = BasicConstants.DOMAIN;

    /**
     * 退出登录并清除 cookie
     */
    static loginOut = () => {
        let logoutUrl = "xxx";
        const exp = new Date();
        exp.setTime(exp.getTime() - 1);
        const options = {timeout: exp, domain: BasicLoginUtil._domain};
        removeCookies(BasicConstants.LOGIN_COOKIE_PRINCIPAL, options);
        removeCookies(BasicConstants.LOGIN_COOKIE_USERINFO, options);
        window.location.href = logoutUrl + encodeURIComponent("" + window.location.origin + window.location.pathname);
    };

    /**
     * 跳转到登录页
     * sso统一登录界面跳转逻辑
     * 第一步, 请求sso登录接口并会返回一个ticket，sso会302到你们服务的地址
     */
    static goToLogin = () => {
        if (BasicLoginUtil._debug) {
            console.log("goToLogin");
        }
        const timeout = new Date();
        timeout.setTime(timeout.getTime() - 1);
        const options = {timeout: timeout, domain: BasicLoginUtil._domain};
        removeCookies(BasicConstants.LOGIN_COOKIE_PRINCIPAL, options);
        removeCookies(BasicConstants.LOGIN_COOKIE_USERINFO, options);
        const loginUrl = BasicEnvUtil.getApiEnv() === BasicEnvUtil.PROD ? "xxx" : "xxx";
        window.location.href = loginUrl + window.location.origin + window.location.pathname;
    };
    static validateLogin = (ticket) => {
        let validateUrl = BasicEnvUtil.getApiEnv() === BasicEnvUtil.PROD ? "xxx" : "hxxx";
        validateUrl =
            validateUrl +
            "?ticket=" +
            ticket +
            "&response_type=JSON&service=" +
            window.location.origin +
            window.location.pathname;

        return new Promise((resolve, reject) => {
            fetch(validateUrl, {credentials: "include", method: "GET"})
                .then((res) => res.json())
                .then((res) => {
                    if (res && res.result) {
                        return resolve(res.result);
                    } else {
                        reject(res);
                    }
                })
                .catch((e) => {
                    reject(e);
                });
        });
    };

    /**
     * 判断用户是否登录,
     * 未登录会直接跳转登录
     */
    static isLogin = () => {
        const principal = getCookies(BasicConstants.LOGIN_COOKIE_PRINCIPAL);
        if (BasicLoginUtil._debug) {
            console.log("check login start principal", principal);
        }
        let checkLoginUrl = BasicEnvUtil.getApiEnv() === BasicEnvUtil.PROD ? "xxx" : "xxx";
        checkLoginUrl =
            checkLoginUrl + "?principalId=" + principal + "&callback=" + encodeURIComponent(window.location);
        fetch(checkLoginUrl, {credentials: "include", method: "GET"})
            .then((response) => {
                response.json().then((res) => {
                    if (!res || res.code !== 0 || !res.result) {
                        if (BasicLoginUtil._debug) {
                            console.log("check login failure, go to login");
                        }
                        BasicLoginUtil.goToLogin();
                    } else {
                        if (BasicLoginUtil._debug) {
                            console.log("check login success");
                        }
                    }
                });
            })
            .catch((error) => {
                if (BasicLoginUtil._debug) {
                    console.log("check login error, go to login", error);
                }
                BasicLoginUtil.goToLogin();
            });
    };

    /**
     * ticket获取的用户信息保存到sso服务器上
     * @return {principal,userName,user}
     */
    static pushLoginVoucher = (appId, path, principal) => {
        let pushUrl = BasicEnvUtil.getApiEnv() === BasicEnvUtil.PROD ? "xxx" : "xxx";
        const uuid = uuidv4();
        const body = {
            id: appId + "_" + uuid,
            principal: JSON.stringify(principal),
            expire: 2592000
        };

        let requestInstance = new Request(pushUrl, {
            method: "post",
            headers: {"Content-Type": "application/json;charset=utf-8"},
            body: JSON.stringify(body)
        });
        return new Promise((resolve, reject) => {
            fetch(requestInstance)
                .then((res) => res.json())
                .then((res) => {
                    if (res && res.code === 0) {
                        const user = JSON.stringify(principal, [
                            "city",
                            "company",
                            "department",
                            "displayName",
                            "employee",
                            "mail",
                            "name",
                            "sn"
                        ]);

                        //region save info to cookie
                        const timeout = new Date(new Date().getTime() + 30 * 24 * 60 * 60 * 1000);
                        setCookies(
                            BasicConstants.LOGIN_COOKIE_PRINCIPAL,
                            appId + "_" + uuid,
                            timeout,
                            path,
                            BasicLoginUtil._domain
                        );
                        setCookies(BasicConstants.LOGIN_COOKIE_USERINFO, user, timeout, path, BasicLoginUtil._domain);
                        //endregion

                        resolve({
                            principal: appId + "_" + uuid,
                            userName: principal.displayName,
                            user: JSON.parse(user)
                        });
                    } else {
                        reject();
                    }
                })
                .catch((e) => {
                    reject(e);
                });
        });
    };

    /**
     * @param appId
     * @param ticket
     * @param path
     * @param callback({principal, userName, user})
     */
    static handleLogin(appId, ticket, path, callback) {
        const principal = getCookies(BasicConstants.LOGIN_COOKIE_PRINCIPAL);
        const userInfo = decodeURIComponent(getCookies(BasicConstants.LOGIN_COOKIE_USERINFO));
        if (BasicLoginUtil._debug) {
            console.log("handleLogin start principal=", principal, ", userInfo=", userInfo);
        }

        if (userInfo) {
            let user;
            try {
                user = JSON.parse(userInfo);
                if (BasicLoginUtil._debug) {
                    console.log("handleLogin user valid user=", user);
                }
                if (!user.employee) return BasicLoginUtil.goToLogin();
            } catch (e) {
                if (BasicLoginUtil._debug) {
                    console.log("handleLogin user not valid, go to login user=", user);
                }
                return BasicLoginUtil.goToLogin();
            }
            callback({
                principal: principal,
                userName: user.displayName,
                user: user
            });
        } else {
            if (BasicLoginUtil._debug) {
                console.log("handleLogin user not valid userInfo=", userInfo);
            }
            callback({
                principal: principal,
                userName: "",
                user: {}
            });
        }

        if (principal) {
            if (BasicLoginUtil._debug) {
                console.log("handleLogin principal valid principal=", principal);
            }
            BasicLoginUtil.isLogin();
        } else {
            if (!ticket) {
                if (BasicLoginUtil._debug) {
                    console.log(
                        "handleLogin principal not valid and ticket not valid, go to login principal=",
                        principal,
                        ", ticket=",
                        ticket
                    );
                }
                BasicLoginUtil.goToLogin();
            } else {
                if (BasicLoginUtil._debug) {
                    console.log("handleLogin validateLogin ticket=", ticket);
                }
                BasicLoginUtil.validateLogin(ticket)
                    .then((principalData) => {
                        if (BasicLoginUtil._debug) {
                            console.log(
                                "handleLogin pushLoginVoucher appId=",
                                appId,
                                ", principalData=",
                                principalData
                            );
                        }
                        BasicLoginUtil.pushLoginVoucher(appId, path, principalData).then(function (data) {
                            if (BasicLoginUtil._debug) {
                                console.log("handleLogin pushLoginVoucher end data=", data);
                            }
                            callback({
                                principal: data.principal,
                                userName: data.userName,
                                user: data.user
                            });
                        });
                    })
                    .catch((e) => {
                        if (BasicLoginUtil._debug) {
                            console.log("handleLogin validateLogin error e=", e);
                        }
                        BasicLoginUtil.goToLogin();
                    });
            }
        }
    }

    static getUserInfo() {
        let userInfoString = decodeURIComponent(getCookies(BasicConstants.LOGIN_COOKIE_USERINFO));
        let userInfo = JSON.parse(userInfoString);
        console.log("getUserInfo", userInfo);
        return userInfo;
    }
}
