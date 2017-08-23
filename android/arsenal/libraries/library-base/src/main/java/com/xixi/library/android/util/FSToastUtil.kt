package com.xixi.library.android.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.xixi.library.R
import com.xixi.library.android.base.CXBaseApplication

@Suppress("unused")
object CXToastUtil {
    private class ViewHolder(var contentLayout: View, var textView: TextView)

    private var toast: Toast? = null
    private var mToastCount = 5
    private val DP_5 = CXSystemUtil.getPxFromDp(5f).toInt()

    fun show(strId: Int) {
        show(CXBaseApplication.INSTANCE.resources.getString(strId))
    }

    fun show(msg: String, defaultStr: String) {
        show(if (TextUtils.isEmpty(msg)) defaultStr else msg)
    }

    @SuppressLint("ShowToast")
    fun show(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            if (toast == null) {
                toast = Toast.makeText(CXBaseApplication.INSTANCE, msg, Toast.LENGTH_SHORT)
                toast!!.view = toast!!.view
                toast!!.duration = Toast.LENGTH_SHORT
            } else {
                toast!!.setText(msg)
            }
            toast!!.show()
        }
    }

    fun showDefault(msg: String) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(CXBaseApplication.INSTANCE, msg, Toast.LENGTH_SHORT).show()
    }


    fun showMore(message: String) {
        if (TextUtils.isEmpty(message))
            return
        val toast = Toast(CXBaseApplication.INSTANCE)
        val viewHolder = toastLayout
        if (viewHolder != null) {
            viewHolder.textView.text = message
            toast.view = viewHolder.contentLayout
            toast.duration = computeShowTime(viewHolder.textView, message)
            val averageDip = CXSystemUtil.screenHeight / 7
            when (mToastCount) {
                5 -> toast.setGravity(Gravity.TOP, 0, 0)
                4 -> toast.setGravity(Gravity.TOP, 0, averageDip)
                3 -> toast.setGravity(Gravity.TOP, 0, 2 * averageDip)
                2 -> toast.setGravity(Gravity.TOP, 0, 3 * averageDip)
                1 -> toast.setGravity(Gravity.TOP, 0, 4 * averageDip)
                else -> toast.setGravity(Gravity.CENTER, 0, 0)
            }
            toast.show()
            mToastCount--
        }
    }

    private fun computeShowTime(tv: TextView, msg: String): Int {
        val paint = tv.paint ?: return Toast.LENGTH_SHORT
        val maxWidth = (CXSystemUtil.screenWidth - CXSystemUtil.getPxFromDp(50f)).toInt()
        val msgLength = paint.measureText(msg)
        return if (msgLength > maxWidth) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    }


    private val toastLayout: ViewHolder?
        get() {
            val contentLayout = LinearLayout(CXBaseApplication.INSTANCE)
            contentLayout.setBackgroundResource(R.drawable.fs_toast)
            val textView = TextView(CXBaseApplication.INSTANCE)
            textView.setShadowLayer(2.75f, 1f, 1f, Color.parseColor("#BB000000"))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            @Suppress("DEPRECATION")
            textView.setTextColor(CXBaseApplication.INSTANCE.resources.getColor(android.R.color.background_light))
            textView.setPadding(DP_5, DP_5, DP_5, DP_5)
            contentLayout.addView(textView)
            return ViewHolder(contentLayout, textView)
        }
}
