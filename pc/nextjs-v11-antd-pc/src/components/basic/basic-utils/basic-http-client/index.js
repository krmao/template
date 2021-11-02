require("es6-promise").polyfill(); // 安卓4.3以下不兼容 promise, 无法使用 axios, 该库可以解决这个问题, 必须在使用 axios 之前执行

import fetch from "isomorphic-unfetch";

class BasicHttpClient {
    static axiosClient() {
        if (!!BasicHttpClient.__axiosClient) {
            return BasicHttpClient.__axiosClient;
        }
        // noinspection JSUnresolvedFunction
        // baseURL 将自动加在 url 前面(get/post 里面的 url 填写 ip:port 后面的 path), 除非 url 是一个绝对 URL
        // 不设 baseUrl, 则 package.json 里面的 proxy 才会生效
        const innerAxios = require("axios").create({timeout: BasicHttpClient.timeout}); // `timeout` 指定请求超时的毫秒数(0 表示无超时时间)

        // 添加请求拦截器
        innerAxios.interceptors.request.use(
            function (config) {
                // 在发送请求之前做些什么
                console.log(config);
                return config;
            },
            function (error) {
                // 对响应错误做点什么
                if (error.response) {
                    // 请求已发出，但服务器响应的状态码不在 2xx 范围内
                    console.log(error.response.data);
                    console.log(error.response.status);
                    console.log(error.response.headers);
                } else {
                    // Something happened in setting up the request that triggered an Error
                    console.log("Error", error.message);
                }
                console.log(error.config);
                return Promise.reject(error);
            }
        );

        // 添加响应拦截器
        innerAxios.interceptors.response.use(
            function (response) {
                // 对响应数据做点什么
                console.log(response.data);
                console.log(response.status);
                console.log(response.statusText);
                console.log(response.headers);
                console.log(response.config);
                return response;
            },
            function (error) {
                // 对响应错误做点什么
                if (error.response) {
                    // 请求已发出，但服务器响应的状态码不在 2xx 范围内
                    console.log(error.response.data);
                    console.log(error.response.status);
                    console.log(error.response.headers);
                } else {
                    // Something happened in setting up the request that triggered an Error
                    console.log("Error", error.message);
                }
                console.log(error.config);
                return Promise.reject(error);
            }
        );

        BasicHttpClient.__axiosClient = innerAxios;
    }

    static get(url, data) {
        return this.request(url, data, {method: "get"});
    }

    static post(url, data) {
        return this.request(url, data, {method: "post"});
    }

    static request(url, data, {method}) {
        console.log("--[request](start)", url, data);
        return new Promise((resolve, reject) => {
            BasicHttpClient.axiosClient()
                .request({
                    url: url,
                    method: method,
                    data: data,
                    validateStatus: function (status) {
                        return status >= 200 && status < 300; // `validateStatus` 定义对于给定的HTTP 响应状态码是 resolve 或 reject  promise 。如果 `validateStatus` 返回 `true` (或者设置为 `null` 或 `undefined`)，promise 将被 resolve; 否则，promise 将被 reject
                    },
                    transformRequest: [
                        function (data) {
                            return data;
                        }
                    ],
                    transformResponse: [
                        function (data) {
                            return data;
                        }
                    ]
                })
                .then(function (response) {
                    if (response?.code === BasicHttpClient.code) {
                        console.log("--[response](success)", url, response);
                        resolve(response.data);
                    } else {
                        reject("response code:" + response.code + " invalid");
                    }
                })
                .catch(function (error) {
                    console.log("--[response](failure)", url, error);
                    reject(error ? error.message : "request failure");
                });
        });
    }

    static fetch = (url, data, {method}) => {
        console.log("--[request](start)", url, data);
        return new Promise((resolve, reject) => {
            // noinspection JSCheckFunctionSignatures
            fetch(
                url,
                {
                    method: method,
                    body: JSON.stringify(data),
                    headers: {"Content-Type": "application/json;charset=UTF-8"}
                },
                {credentials: "include"}
            )
                .then((response) => {
                    let result = response.json();
                    console.log("--[response](success)", url, result);
                    if (response?.status === BasicHttpClient.code) {
                        resolve(result);
                    } else {
                        reject("response status:" + response.status + " invalid");
                    }
                })
                .catch((error) => {
                    console.log("--[response](failure)", url, error);
                    reject(error ? error.message : "request failure");
                });
        });
    };
}

BasicHttpClient.timeout = 15 * 1000;
BasicHttpClient.code = 200;
// 外部不可设置/调用
BasicHttpClient.__axiosClient = null;

export default BasicHttpClient;
