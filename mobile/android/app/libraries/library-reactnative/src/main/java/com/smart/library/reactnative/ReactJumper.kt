package com.smart.library.reactnative

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil

/**
 * react native pages jumper
 */
@Suppress("unused")
object ReactJumper {

    @JvmStatic
    @JvmOverloads
    fun goTo(context: Context?, pageName: String = ReactManager.devSettingsManager.getDefaultStartComponentPage(), param: HashMap<String, Any?> = hashMapOf(), component: String = ReactManager.devSettingsManager.getDefaultStartComponent(), intentFlag: Int? = null, _requestCode: Int = 0, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
        var requestCode = _requestCode
        if (requestCode < 0 || requestCode > 65536) {
            STLogUtil.e(ReactManager.TAG, "requestCode:$requestCode can only use lower 16 bits for requestCode")
            requestCode = 0
        }

        ReactActivity.startForResult(context, component, Bundle().apply {
            putString("page", pageName)
            putString("param", STJsonUtil.toJson(param))
        }, intentFlag, requestCode, callback)
    }

    const val HOME = "home"
}