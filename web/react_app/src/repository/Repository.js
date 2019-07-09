require("es6-promise").polyfill(); // 安卓4.3以下不兼容 promise, 无法使用 axios, 该库可以解决这个问题, 必须在使用 axios 之前执行
const axios = require("axios");

/**
 * 所有的网络请求
 * 所有的本地存储
 */
class Repository {
    /**
     * 登录
     * @param requestData
     * @param onSuccess
     * @param onFailure
     */
    static requestLogin(requestData, onSuccess, onFailure) {
        this.request("/login", requestData, onSuccess, onFailure);
    }

    static request(url, requestData, onSuccess, onFailure) {
        console.warn("request data:" + requestData);
        axios
            .post(url, requestData)
            .then(function(response) {
                console.warn("response data:" + response);
                if (response && response.code === 200) {
                    onSuccess(response.data);
                } else {
                    throw Error("response code invalid");
                }
            })
            .catch(function(error) {
                console.warn("response error:" + error);
                onFailure(error);
            });
    }
}

export default Repository;
