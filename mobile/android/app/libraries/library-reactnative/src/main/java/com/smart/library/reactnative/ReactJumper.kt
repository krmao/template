package com.smart.library.reactnative

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.smart.library.reactnative.ReactManager.TAG
import com.smart.library.util.STLogUtil

/**
 * react native pages jumper
 */
@Suppress("unused")
object ReactJumper {

    @JvmStatic
    @JvmOverloads
    fun goTo(context: Context?, pageName: String = ReactManager.devSettingsManager.getDefaultStartComponentPage(), paramJsonObjectString: String = "{}", component: String = ReactManager.devSettingsManager.getDefaultStartComponent(), intentFlag: Int? = null, _requestCode: Int = 0, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
        var requestCode = _requestCode
        if (requestCode < 0 || requestCode > 65536) {
            STLogUtil.e(TAG, "requestCode:$requestCode can only use lower 16 bits for requestCode")
            requestCode = 0
        }

        STLogUtil.w(TAG, "ReactJumper.goTo component:$component, page:$pageName, param:$paramJsonObjectString")

        ReactActivity.startForResult(context, component, Bundle().apply {
            putString("page", pageName)
            putString("param", paramJsonObjectString)
        }, intentFlag)
    }

    const val HOME = "home"
}