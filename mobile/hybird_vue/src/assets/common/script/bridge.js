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
module.export = (function (bindObj = null) {
    let _bind = bindObj && bindObj instanceof Object ? bindObj : {};

    _bind.callbackMap = new Map();

    //======================================================================

    _bind.open = function (url, callback) {
        let params = JSON.stringify({url: url});
        _bind.invoke(callback, "open", params);
    };
    _bind.close = function (result, callback) {
        let params = JSON.stringify({result: result});
        _bind.invoke(callback, "close", params);
    };
    _bind.showToast = function (message, callback) {
        let params = JSON.stringify({message: message});
        _bind.invoke(callback, "showToast", params);
    };
    _bind.put = function (key, value, callback) {
        let params = JSON.stringify({key: key, value: !value ? "" : value});
        _bind.invoke(callback, "put", params);
    };
    _bind.get = function (key, callback) {
        let params = JSON.stringify({key: key});
        _bind.invoke(callback, "get", params);
    };
    _bind.getUserInfo = function (callback) {
        let params = JSON.stringify({});
        _bind.invoke(callback, "getUserInfo", params);
    };
    _bind.getLocationInfo = function (callback) {
        let params = JSON.stringify({});
        _bind.invoke(callback, "getLocationInfo", params);
    };
    _bind.getDeviceInfo = function (callback) {
        let params = JSON.stringify({});
        _bind.invoke(callback, "getDeviceInfo", params);
    };
    //======================================================================
    _bind.invoke = function (callback, ...args) {
        let methodName = args[0];
        let paramsString = "";

        args.slice(1, args.length).forEach(function (value, index, array) {
            paramsString += encodeURIComponent(array[index]) + (index < array.length - 1 ? "," : "");
        });

        let callbackId = "";
        if (callback) {
            callbackId = new Date().getTime();
            _bind.callbackMap.set(callbackId, callback);
        }
        _bind.goTo("smart://hybird/bridge/" + methodName + "?params=" + paramsString + "&callbackId=" + callbackId);
    };
    //======================================================================

    _bind.onResume = function () {
        console.log("[html] onResume()");
    };

    _bind.onPause = function () {
        console.log("[html] onPause()");
    };

    _bind.onNetworkStateChanged = function (available) {
        console.log("[html] onNetworkStateChanged():available=" + available);
    };

    /**
     * @param args args[0]==hashcode 后面的为其它不定长参数
     */
    _bind.onCallback = function (...args) {
        console.log("[html]", "onCallback:start:args=", args, " , callbackMap.size==" + _bind.callbackMap.size, _bind.callbackMap);
        let result = null;
        try {
            let callbackId = args[0];
            // let result = args.slice(1, args.length);
            let result = args.length > 1 ? JSON.parse(args[1]) : {};
            console.log("[html] onCallback:invoke before: callbackId=" + callbackId + " , result=" + args[1]);
            if (callbackId) {
                console.log("[html] onCallback:hashcode=" + callbackId);
                _bind.callbackMap.forEach(function (value, key, mapObj) {
                    console.log("map:value:" + value + ", key:" + key + ", key==hashcode?" + (key === callbackId));
                });
                let method = _bind.callbackMap.get(callbackId);
                console.log("[html] onCallback:method=" + method);
                if (method) {
                    method(result);
                    _bind.callbackMap.delete(callbackId);
                }
            }
            console.log("[html] onCallback:invoke success");
        } catch (error) {
            result = "error";
            console.log("[html] onCallback:invoke error:" + error);
        }
        console.log("[html] onCallback:end:callbackMap.size==" + _bind.callbackMap.size);
        return result;
    };

    _bind.browser = {
        versions: (function () {
            const u = navigator.userAgent;
            // const u = 'Mozilla/5.0 (Linux; Android 10; CLT-AL01 Build/HUAWEICLT-AL01; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/79.0.3945.116 Mobile Safari/537.36 statusBarHeight/27 ';
            const app = navigator.appVersion;
            const ios = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);

            // parse statusbarHeight start
            const statusbarKeyString = "statusBarHeight"; // " statusbarHeight/22 "
            const statusbarIndex = u.indexOf(statusbarKeyString);
            let statusbarHeight = parseInt(u.substr(statusbarIndex + statusbarKeyString.length + 1, 3).trim());
            statusbarHeight = isNaN(statusbarHeight) ? 0 : statusbarHeight;
            // parse statusbarHeight end

            console.log("statusbarHeight=" + statusbarHeight);

            return {
                webKit: u.indexOf("AppleWebKit") > -1, //苹果、谷歌内核
                statusBarHeight: statusbarHeight, //statusbarHeight
                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                ios: ios, //ios终端
                android: u.indexOf("Android") > -1 || u.indexOf("Adr") > -1 || u.indexOf("Linux") > -1, //android终端
                iPhone: u.indexOf("iPhone") > -1, //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf("iPad") > -1, //是否iPad
                weixin: u.indexOf("MicroMessenger") > -1, //是否微信 （2015-01-22新增）
                qq: u.match(/\sQQ/i) === " qq", //是否QQ
                isIphoneX: ios && ((window.screen.height === 812 && window.screen.width === 375) || (window.screen.height === 375 && window.screen.width === 812))
            };
        })(),
        language: (navigator.browserLanguage || navigator.language).toLowerCase(),
        toString: function () {
            return "\nwebKit:" + this.versions.webKit + "\nstatusBarHeight:" + this.versions.statusBarHeight + "\nmobile:" + this.versions.mobile + "\nios:" + this.versions.ios + "\nandroid:" + this.versions.android + "\niPhone:" + this.versions.iPhone + "\niPad:" + this.versions.iPad + "\nweixin:" + this.versions.weixin + "\nqq:" + this.versions.qq + "\nisIphoneX:" + this.versions.isIphoneX + "\niosVersion:" + this.versions.iosVersion + "\nandroidVersion:" + this.versions.androidVersion;
        }
    };

    _bind.goTo = function (url) {
        console.log("[Native:goTo] url: " + url);

        // 同时支持 ios UIWebview and WKWebview
        if (_bind.browser.versions.ios && window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.bridge) {
            // 此种调用方式为 WKWebview 独有
            window.webkit.messageHandlers.bridge.postMessage(url);
            return;
        }

        /* else if (_bind.browser.versions.android) {*/
        let div = document.createElement("div");
        // eslint-disable-next-line quotes
        div.innerHTML = '<iframe style="display: none;" src="' + url + '"/>';
        document.querySelector("body").appendChild(div);

        setTimeout(function () {
            document.querySelector("body").removeChild(div);
        }, 1000);
        /*}*/
    };

    console.log("navigator.userAgent=" + navigator.userAgent);
    console.log("navigator.appVersion=" + navigator.appVersion);
    console.log("navigator.browser=" + _bind.browser);
    console.log("window.screen.height=" + window.screen.height);
    console.log("window.screen.width=" + window.screen.width);

    return _bind;
})(window.bridge);
