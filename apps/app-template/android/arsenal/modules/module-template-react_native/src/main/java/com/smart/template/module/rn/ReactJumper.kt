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
    fun goTo(context: Context?, component: String = ReactManager.devSettingsManager.getDefaultStartComponent(), pageName: String = ReactManager.devSettingsManager.getDefaultStartComponentPage(), param: Bundle = Bundle(), intentFlag: Int? = null) {
        ReactActivity.start(context, component, Bundle().apply {
            putString("pageName", pageName)
            putBundle("param", param)
        }, intentFlag)
    }
}