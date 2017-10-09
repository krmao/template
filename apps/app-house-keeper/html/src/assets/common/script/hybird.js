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

    _bind.onResume = function () {
        console.log('[html] onResume()')
    }

    _bind.onPause = function () {
        console.log('[html] onPause()')
    }

    _bind.onNetworkStateChanged = function (available) {
        console.log('[html] onNetworkStateChanged():available=' + available)
    }
    return _bind
}
