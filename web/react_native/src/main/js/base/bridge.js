import {NativeModules} from "react-native";

/**
 * import NB from './assets/common/script/nb'
 *
 * 1: bindToWindow      ```
 *                          NB(window)
 *                      ```
 * 2: bindToObject      ```
 *                          let object = NB()
 *                          //or
 *                          let object = {}
 *                          NB(object)
 *                      ```
 *
 * @param bindObj
 * @returns {{}}
 */
window.bridge = {};
module.export = (function(bindObj = null) {
    let _bind = bindObj && bindObj instanceof Object ? bindObj : {};

    //======================================================================

    _bind.open = function(url, callback) {
        let params = JSON.stringify({url: url});
        _bind.invoke("open", params, callback);
    };
    _bind.close = function(result, callback) {
        let params = JSON.stringify({result: result});
        _bind.invoke("close", params, callback);
    };
    _bind.showToast = function(message, callback) {
        let params = JSON.stringify({message: message});
        _bind.invoke("showToast", params, callback);
    };
    _bind.put = function(key, value, callback) {
        let params = JSON.stringify({key: key, value: !value ? "" : value});
        _bind.invoke("put", params, callback);
    };
    _bind.get = function(key, callback) {
        let params = JSON.stringify({key: key});
        _bind.invoke("get", params, callback);
    };
    _bind.getUserInfo = function(callback) {
        let params = JSON.stringify({});
        _bind.invoke("getUserInfo", params, callback);
    };
    _bind.getLocationInfo = function(callback) {
        let params = JSON.stringify({});
        _bind.invoke("getLocationInfo", params, callback);
    };
    _bind.getDeviceInfo = function(callback) {
        let params = JSON.stringify({});
        _bind.invoke("getDeviceInfo", params, callback);
    };
    //======================================================================
    _bind.invoke = function(functionName, params, callback) {
        NativeModules.NativeManager.callNative(functionName, params).then(
            (successResult) => {
                console.log(`successResult=${successResult}`);
                if (callback) {
                    callback(JSON.parse(successResult));
                }
            },
            (errorCode, errorMsg, error) => {
                console.log(`errorCode=${errorCode}, errorMsg=${errorMsg}, error=${error}`);
                if (callback) {
                    callback(undefined);
                }
            }
        );
    };
    return _bind;
})(window.bridge);
