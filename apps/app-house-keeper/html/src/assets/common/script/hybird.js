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
window.hybird = {}
module.export = (function (bindObj = null) {
    let _bind = (bindObj && bindObj instanceof Object) ? bindObj : {}

    _bind.callbackMap = new Map()

    _bind.onResume = function () {
        console.log('[html] onResume()')
    }

    _bind.onPause = function () {
        console.log('[html] onPause()')
    }

    _bind.onNetworkStateChanged = function (available) {
        console.log('[html] onNetworkStateChanged():available=' + available)
    }

    /**
     * @param args args[0]==hashcode 后面的为其它不定长参数
     */
    _bind.onCallback = function (...args) {
        console.log("[html] onCallback:start:callbackMap.size==" + _bind.callbackMap.size)
        console.log("[html] onCallback:start:args=" + args)
        let result = null
        try {
            let hashcode = args[0]
            let result = args.slice(1, args.length)
            console.log("[html] onCallback:invoke before: hashcode=" + hashcode + " , result=" + result)
            _bind.callbackMap.get(hashcode)(result)
            _bind.callbackMap.delete(hashcode)
            console.log("[html] onCallback:invoke success")
        } catch (error) {
            result = 'error';
            console.log("[html] onCallback:invoke error:" + error)
        }
        console.log("[html] onCallback:end:callbackMap.size==" + _bind.callbackMap.size)
        return result
    }

    //======================================================================

    _bind.showToast = function (message) {
        _bind.showToastWithDuration(message, null)
    }

    _bind.showToastWithDuration = function (message, duration) {
        _bind.invoke("showToast", [message, duration].filter(it => it !== undefined && it !== null))
    }

    _bind.test = function (message, duration, callback) {
        return _bind.invoke(callback, "showToast", "Hello Native ,Mao", 2000)
    }

    _bind.invoke = function (callback, ...args) {
        let className = "native"
        let methodName = args[0]
        var patamString = ""

        args.slice(1, args.length).forEach(
            function (value, index, array) {
                patamString += encodeURIComponent(array[index]) + (index < array.length - 1 ? "," : "")
            }
        )

        let hashcode = new Date().getTime()
        _bind.callbackMap.set(hashcode, callback)

        _bind.goTo("hybird://hybird:1234/" + className + "/" + methodName + "?params=" + patamString + "&hashcode=" + hashcode)
    }

    _bind.goTo = function (url) {
        console.log("[Native:goTo] url: " + url)

        var div = document.createElement("div");
        div.innerHTML = '<iframe style="display: none;" src="' + url + '"/>';
        document.querySelector("body").appendChild(div);
        setTimeout(function () {
            document.querySelector("body").removeChild(div);
        }, 1000);
    }

    _bind.browser = {
        versions: function () {
            var u = navigator.userAgent, app = navigator.appVersion;
            var ios = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)
            return {
                webKit: u.indexOf('AppleWebKit') > -1,                                                      //苹果、谷歌内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/),                                                 //是否为移动终端
                ios: ios,                                            //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1 || u.indexOf('Linux') > -1,     //android终端
                iPhone: u.indexOf('iPhone') > -1,                                                           //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1,                                                               //是否iPad
                weixin: u.indexOf('MicroMessenger') > -1,                                                   //是否微信 （2015-01-22新增）
                qq: u.match(/\sQQ/i) === " qq",                                                              //是否QQ
                isIphoneX: ios && ((window.screen.height === 812 && window.screen.width === 375) || (window.screen.height === 375 && window.screen.width === 812)),
            };
        }(),
        language: (navigator.browserLanguage || navigator.language).toLowerCase(),
        toString: function () {
            return "\nwebKit:" + this.versions.webKit +
                "\nmobile:" + this.versions.mobile +
                "\nios:" + this.versions.ios +
                "\nandroid:" + this.versions.android +
                "\niPhone:" + this.versions.iPhone +
                "\niPad:" + this.versions.iPad +
                "\nweixin:" + this.versions.weixin +
                "\nqq:" + this.versions.qq +
                "\nisIphoneX:" + this.versions.isIphoneX +
                "\niosVersion:" + this.versions.iosVersion +
                "\nandroidVersion:" + this.versions.androidVersion
        }
    }

    console.log("navigator.userAgent=" + navigator.userAgent)
    console.log("navigator.appVersion=" + navigator.appVersion)
    console.log("navigator.browser=" + _bind.browser)
    console.log("window.screen.height=" + window.screen.height)
    console.log("window.screen.width=" + window.screen.width)

    return _bind
})(window.hybird)
