package com.smart.library.util


import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.smart.library.R
import com.smart.library.base.HKBaseApplication

@SuppressLint("InflateParams")
@Suppress("unused", "MemberVisibilityCanPrivate")
object HKToastUtil {
    private var toast: Toast? = null


    @JvmOverloads
    @JvmStatic
    fun show(@StringRes strId: Int, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        show(msg = HKBaseApplication.INSTANCE.resources.getString(strId), textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun showTop(@StringRes strId: Int, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        showTop(msg = HKBaseApplication.INSTANCE.resources.getString(strId), textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun showTop(msg: String?, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        show(msg = msg, toastGravity = Gravity.BOTTOM, textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun showCenter(@StringRes strId: Int, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        showCenter(msg = HKBaseApplication.INSTANCE.resources.getString(strId), textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun showCenter(msg: String?, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        show(msg = msg, toastGravity = Gravity.CENTER, textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun show(msg: String?, toastGravity: Int = Gravity.BOTTOM, duration: Int = Toast.LENGTH_SHORT, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        if (!TextUtils.isEmpty(msg)) {
            if (cancelLastImmediately)
                toast?.cancel()
            toast = Toast(HKBaseApplication.INSTANCE)
            viewHolder.textView.text = msg
            viewHolder.textView.gravity = textGravity
            toast?.view = viewHolder.rootView
            toast?.duration = duration
            toast?.setGravity(toastGravity, xOffset, yOffset)
            toast?.show()
        }
    }

    private class ViewHolder(val rootView: View) {
        val textView: TextView by lazy { rootView.findViewById(R.id.message) as TextView }
    }

    private val viewHolder: ViewHolder by lazy {
        ViewHolder(LayoutInflater.from(HKBaseApplication.INSTANCE).inflate(R.layout.hk_widget_transient_notification, null))
    }
}
