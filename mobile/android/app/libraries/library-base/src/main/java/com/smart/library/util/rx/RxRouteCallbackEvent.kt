package com.smart.library.util.rx

import android.os.Bundle
import androidx.annotation.Keep
import com.smart.library.util.STLogUtil

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
@Suppress("unused")
//@Keep
open class RxRouteCallbackEvent {
    var id: String = ""
    var bundle: Bundle? = null

    init {
        STLogUtil.w(javaClass.name, "[路由返回事件]-> id='$id', bundle='$bundle'")
    }
}
