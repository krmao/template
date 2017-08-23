package com.xixi.library.android.util.rx

import android.os.Bundle
import com.xixi.library.android.util.FSLogUtil

/**
 * <pre>
 *     desc   : 挂在总线上的模块处理完需要回应，推送此Event
 *              id     -> RxRouteEvent.id
 *              bundle -> 返回数据载体
 *     author : Arthur
 *     since  : 2017/07/11
 *     version: 1.0
 * </pre>
 */
open class RxRouteCallbackEvent {
    var id: String = ""
    var bundle: Bundle? = null

    init {
        FSLogUtil.w(javaClass.name, "[路由返回事件]-> id='$id', bundle='$bundle'")
    }
}
