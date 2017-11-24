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
    fun show(@StringRes strId: Int, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true) {
        show(HKBaseApplication.INSTANCE.resources.getString(strId), textGravity, cancelLastImmediately)
    }

    @JvmOverloads
    @JvmStatic
    fun show(msg: String?, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true) {
        show(msg, null, textGravity, cancelLastImmediately)
    }

    @JvmOverloads
    @JvmStatic
    fun showCenter(@StringRes strId: Int, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true) {
        show(HKBaseApplication.INSTANCE.resources.getString(strId), Gravity.CENTER, textGravity, cancelLastImmediately)
    }

    @JvmOverloads
    @JvmStatic
    fun showCenter(msg: String?, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true) {
        show(msg, Gravity.CENTER, textGravity, cancelLastImmediately)
    }

    @JvmOverloads
    @JvmStatic
    fun show(msg: String?, toastGravity: Int?, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true) {
        show(msg, toastGravity, null, textGravity, cancelLastImmediately)
    }

    @JvmOverloads
    @JvmStatic
    fun show(msg: String?, toastGravity: Int?, duration: Int?, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true) {
        if (!TextUtils.isEmpty(msg)) {
            if (cancelLastImmediately)
                toast?.cancel()
            toast = Toast(HKBaseApplication.INSTANCE)
            viewHolder.textView.text = msg
            viewHolder.textView.gravity = textGravity
            toast?.view = viewHolder.rootView
            toast?.duration = duration ?: Toast.LENGTH_SHORT
            if (toastGravity != null)
                toast?.setGravity(toastGravity, 0, 0)
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
