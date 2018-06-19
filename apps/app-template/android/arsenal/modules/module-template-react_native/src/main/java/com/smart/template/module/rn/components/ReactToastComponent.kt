package com.smart.template.module.rn.components

import android.view.Gravity
import android.widget.Toast
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.smart.library.util.CXToastUtil

class ReactToastComponent(reactContext: ReactApplicationContext?) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "ToastUtil"
    }

    @ReactMethod
    fun show(msg: String?) {
        CXToastUtil.show(msg = msg)
    }


    @ReactMethod
    fun showWithDuration(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        CXToastUtil.show(msg = msg, duration = duration)
    }

    @ReactMethod
    fun showWithDurationAndGravity(msg: String?, toastGravity: Int = Gravity.BOTTOM, duration: Int = Toast.LENGTH_SHORT) {
        CXToastUtil.show(msg = msg, toastGravity = toastGravity, duration = duration)
    }

    override fun getConstants(): MutableMap<String, Any> {
        return mutableMapOf(
                "LENGTH_SHORT" to Toast.LENGTH_SHORT,
                "LENGTH_LONG" to Toast.LENGTH_LONG,
                "BOTTOM" to Gravity.BOTTOM,
                "TOP" to Gravity.TOP,
                "START" to Gravity.START,
                "END" to Gravity.END,
                "CENTER" to Gravity.CENTER,
                "CENTER_VERTICAL" to Gravity.CENTER_VERTICAL,
                "CENTER_HORIZONTAL" to Gravity.CENTER_HORIZONTAL
        )
    }

    override fun canOverrideExistingModule(): Boolean {
        return true
    }
}
