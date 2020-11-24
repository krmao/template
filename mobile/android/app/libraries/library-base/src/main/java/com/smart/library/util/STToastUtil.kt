package com.smart.library.util


import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.smart.library.R
import com.smart.library.STInitializer

@SuppressLint("InflateParams")
@Suppress("unused", "MemberVisibilityCanPrivate", "StaticFieldLeak", "MemberVisibilityCanBePrivate")
object STToastUtil {
    private var toast: Toast? = null

    @JvmOverloads
    @JvmStatic
    fun show(@StringRes strId: Int, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        show(msg = STInitializer.application()?.resources?.getString(strId), textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun showTop(@StringRes strId: Int, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        showTop(msg = STInitializer.application()?.resources?.getString(strId), textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun showTop(msg: String?, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        show(msg = msg, toastGravity = Gravity.TOP, textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun showCenter(@StringRes strId: Int, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        showCenter(msg = STInitializer.application()?.resources?.getString(strId), textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @JvmOverloads
    @JvmStatic
    fun showCenter(msg: String?, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        show(msg = msg, toastGravity = Gravity.CENTER, textGravity = textGravity, cancelLastImmediately = cancelLastImmediately, xOffset = xOffset, yOffset = yOffset)
    }

    @Suppress("UsePropertyAccessSyntax")
    @JvmOverloads
    @JvmStatic
    fun show(msg: String?, toastGravity: Int = Gravity.BOTTOM, duration: Int = Toast.LENGTH_SHORT, textGravity: Int = Gravity.CENTER_HORIZONTAL, cancelLastImmediately: Boolean = true, xOffset: Int = 0, yOffset: Int = 0) {
        if (msg?.isNotBlank() == true) {
            if (cancelLastImmediately)
                toast?.cancel()
            toast = Toast(STInitializer.application())
            val viewHolder = ViewHolder(LayoutInflater.from(STInitializer.application()).inflate(R.layout.st_widget_transient_notification, null))
            viewHolder.textView.text = msg
            viewHolder.textView.gravity = textGravity
            toast?.setView(viewHolder.rootView)
            toast?.setDuration(duration)
            toast?.setGravity(toastGravity, xOffset, yOffset)
            toast?.show()
        }
    }

    class ViewHolder(val rootView: View) {
        val textView: TextView by lazy { rootView.findViewById(R.id.message) as TextView }
    }
}
