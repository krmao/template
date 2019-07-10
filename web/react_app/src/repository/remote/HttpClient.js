require("es6-promise").polyfill(); // 安卓4.3以下不兼容 promise, 无法使用 axios, 该库可以解决这个问题, 必须在使用 axios 之前执行

let baseUrl = "";
if (process.env.NODE_ENV === "production") {
    baseUrl = "http://localhost:8000";
} else if (process.env.NODE_ENV === "development") {
    baseUrl = ""; // 不设 baseUrl, 则 package.json 里面的 proxy 才会生效, get/post 里面的 url 填写 ip:port 后面的 path
}

console.log("environment:" + process.env.NODE_ENV + ", baseUrl:" + baseUrl);

// noinspection JSUnresolvedFunction
const axios = require("axios").create({
    baseURL: baseUrl, // `baseURL` 将自动加在 `url` 前面，除非 `url` 是一个绝对 URL
    timeout: 15 * 1000 // `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
});

// 添加请求拦截器
axios.interceptors.request.use(
    function(config) {
        // 在发送请求之前做些什么
        console.log(config);
        return config;
    },
    function(error) {
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
axios.interceptors.response.use(
    function(response) {
        // 对响应数据做点什么
        console.log(response.data);
        console.log(response.status);
        console.log(response.statusText);
        console.log(response.headers);
        console.log(response.config);
        return response;
    },
    function(error) {
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

class HttpClient {
    static get(url, onSuccess, onFailure) {
        return this.request(url, "get", null, onSuccess, onFailure);
    }

    static post(url, requestData, onSuccess, onFailure) {
        return this.request(url, "post", requestData, onSuccess, onFailure);
    }

    static request(url, method, requestData, onSuccess, onFailure) {
        return this.requestByConfig(
            {
                url: url,
                method: method,
                data: requestData,
                validateStatus: function(status) {
                    console.error("status:" + status);
                    return status >= 200 && status < 300; // `validateStatus` 定义对于给定的HTTP 响应状态码是 resolve 或 reject  promise 。如果 `validateStatus` 返回 `true` (或者设置为 `null` 或 `undefined`)，promise 将被 resolve; 否则，promise 将被 rejecte
                },
                transformRequest: [
                    function(data) {
                        return data;
                    }
                ],
                transformResponse: [
                    function(data) {
                        return data;
                    }
                ]
            },
            onSuccess,
            onFailure
        );
    }

    static requestByConfig(config, onSuccess, onFailure) {
        return axios
            .request(config)
            .then(function(response) {
                if (response && response.code === 200) {
                    onSuccess(response.data);
                } else {
                    throw Error("response code:" + response.code + " invalid");
                }
            })
            .catch(function(error) {
                onFailure(error ? error.message : "request failure");
            });
    }
}

export default HttpClient;
