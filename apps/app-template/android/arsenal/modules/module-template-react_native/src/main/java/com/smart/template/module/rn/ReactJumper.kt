package com.smart.template.module.rn

import android.content.Context
import android.os.Bundle

/**
 * react native pages jumper
 */
@Suppress("unused")
object ReactJumper {

    @JvmStatic
    @JvmOverloads
    fun goTo(context: Context?, pageName: String = ReactManager.devSettingsManager.getDefaultStartComponentPage(), param: Bundle = Bundle(), component: String = ReactManager.devSettingsManager.getDefaultStartComponent(), intentFlag: Int? = null) {
        ReactActivity.start(context, component, Bundle().apply {
            putString("pageName", pageName)
            putBundle("param", param)
        }, intentFlag)
    }
}