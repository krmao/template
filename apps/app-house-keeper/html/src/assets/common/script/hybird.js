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
            let params = args.slice(1, args.length)
            console.log("[html] onCallback:invoke before: params:(" + params + ") , hashcode=" + hashcode)
            result = _bind.callbackMap.get(hashcode)(params)
            _bind.callbackMap.delete(hashcode)
            console.log("[html] onCallback:invoke success:result= " + result)
        } catch (error) {
            result = 'error';
            console.log("[html] onCallback:invoke error:result= " + result + " error= " + error)
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
    return _bind
})(window.hybird)
