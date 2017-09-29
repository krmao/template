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
module.exports = function (bindObj = null) {
    let _bind = (bindObj && bindObj instanceof Object) ? bindObj : {}

    _bind.onBackPressed = function () {
        console.log('[html] onBackPressed()')
    }

    _bind.onResume = function () {
        console.log('[html] onResume()')
    }

    _bind.onPause = function () {
        console.log('[html] onPause()')
    }

    _bind.onResult = function (dataString) {
        console.log('[html] onResult():dataString=' + dataString)
    }

    _bind.onNetworkStateChanged = function (available) {
        console.log('[html] onNetworkStateChanged():available=' + available)
    }

    /**
     * app is visible
     * @param visible true/false
     */
    _bind.onApplicationVisibleChanged = function (visible) {
        console.log('[html] onApplicationVisibleChanged():visible=' + visible)
    }
    return _bind
}
