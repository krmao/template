package com.smart.library.reactnative

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.smart.library.reactnative.RNInstanceManager.TAG
import com.smart.library.util.STLogUtil

/**
 * react native pages jumper
 */
@Suppress("unused")
object RNJumper {

    @JvmStatic
    @JvmOverloads
    fun goTo(context: Context?, pageName: String = RNInstanceManager.devSettingsManager.getDefaultStartComponentPage(), paramJsonObjectString: String = "{}", component: String = RNInstanceManager.devSettingsManager.getDefaultStartComponent(), intentFlag: Int? = null, _requestCode: Int = 0, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
        var requestCode = _requestCode
        if (requestCode < 0 || requestCode > 65536) {
            STLogUtil.e(TAG, "requestCode:$requestCode can only use lower 16 bits for requestCode")
            requestCode = 0
        }

        STLogUtil.w(TAG, "ReactJumper.goTo component:$component, page:$pageName, param:$paramJsonObjectString")

        RNActivity.startForResult(context, component, Bundle().apply {
            putString("page", pageName)
            putString("param", paramJsonObjectString)
        }, intentFlag)
    }

    const val HOME = "home"
}