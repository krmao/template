import HttpClient from "./remote/HttpClient";

/**
 * 所有的网络请求
 * 所有的本地存储
 */
class Repository {
    /**
     * 登录
     */
    static getDirList(onSuccess, onFailure) {
        HttpClient.get("/others", onSuccess, onFailure);
    }
}

export default Repository;
