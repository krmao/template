package com.smart.library.util

import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.smart.library.R
import com.smart.library.base.HKBaseApplication

@Suppress("unused", "MemberVisibilityCanPrivate")
object HKToastUtil {
    private var toast: Toast? = null

    fun show(@StringRes strId: Int) {
        show(HKBaseApplication.INSTANCE.resources.getString(strId))
    }

    fun show(msg: String?) {
        show(msg, null)
    }

    fun showCenter(@StringRes strId: Int) {
        show(HKBaseApplication.INSTANCE.resources.getString(strId), Gravity.CENTER)
    }

    fun showCenter(msg: String?) {
        show(msg, Gravity.CENTER)
    }

    fun show(msg: String?, gravity: Int?) {
        show(msg, gravity, null)
    }

    fun show(msg: String?, gravity: Int?, duration: Int?) {
        if (!TextUtils.isEmpty(msg)) {
            toast?.cancel()
            toast = Toast(HKBaseApplication.INSTANCE)
            viewHolder.textView.text = msg
            toast?.view = viewHolder.rootView
            toast?.duration = duration ?: Toast.LENGTH_SHORT
            if (gravity != null)
                toast?.setGravity(gravity, 0, 0)
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
