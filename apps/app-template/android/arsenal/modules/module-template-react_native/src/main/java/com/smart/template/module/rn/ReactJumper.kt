package com.smart.template.module.rn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.smart.library.util.CXJsonUtil
import com.smart.library.util.CXLogUtil

/**
 * react native pages jumper
 */
@Suppress("unused")
object ReactJumper {

    @JvmStatic
    @JvmOverloads
    fun goTo(context: Context?, page: RNPages? = null, param: HashMap<String, Any?> = hashMapOf(), component: String = ReactManager.devSettingsManager.getDefaultStartComponent(), intentFlag: Int? = null, _requestCode: Int = 0, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
        var requestCode = _requestCode
        if (requestCode < 0 || requestCode > 65536) {
            CXLogUtil.e(ReactManager.TAG, "requestCode:$requestCode can only use lower 16 bits for requestCode")
            requestCode = 0
        }

        ReactActivity.startForResult(context, component, Bundle().apply {
            putString("page", page?.pageName ?: ReactManager.devSettingsManager.getDefaultStartComponentPage())
            putString("param", CXJsonUtil.toJson(param))
        }, intentFlag, requestCode, callback)
    }

    enum class RNPages(val pageName: String) {
        GOODS_DETAIL("GOODS_DETAIL")
    }
}