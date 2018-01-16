package com.smart.library.util.rx

import android.app.Activity
import android.os.Bundle
import com.smart.library.util.CXLogUtil

/**
 * RxRouteEvent作为总线间的通信载体
 * 传递路由协议
 * activity  - 发起方context
 * id        - 发起方唯一标识
 * targetUrl - example://demo
 * bundle    - 数据载体
 */
@Suppress("unused", "MemberVisibilityCanPrivate")
open class RxRouteEvent {
    var activity: Activity? = null
    var targetUrl: String = ""
    var id: String = ""
    var bundle: Bundle? = null

    init {
        CXLogUtil.w(javaClass.name, "[路由事件]-> activity=${activity?.javaClass?.name}, id='$id', targetUrl='$targetUrl', bundle='$bundle'")
    }
}
