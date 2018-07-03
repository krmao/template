@file:Suppress("unused")

package com.smart.library.util

import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AbsListView
import android.widget.TextView
import com.smart.library.base.animateAlphaToVisibility


object CXViewUtil {
    private val TAG = CXViewUtil::class.java.simpleName

    @JvmStatic
    fun animateAlphaToVisibility(visibility: Int, duration: Long = 300, vararg views: View?) = views.forEach { it?.animateAlphaToVisibility(visibility, duration) }

    @JvmStatic
    fun performItemClick(listView: AbsListView?, position: Int) {
        listView?.performItemClick(listView.getChildAt(position), position, listView.getItemIdAtPosition(position))
    }

    @Suppress("DEPRECATION")
    fun checkOverLines(tv: TextView?, lines: Int?, callback: ((isTextOverLines: Boolean) -> Unit)?) {
        if (tv == null || lines == null || callback == null) {
            CXLogUtil.e(TAG, "arguments may be null")
            return
        }
        if (tv.width > 0) {
            CXLogUtil.d(TAG, "tv.width > 0 , call invoke")
            callback.invoke(tv.paint.measureText(tv.text.toString()) > (lines * (tv.width - tv.paddingLeft - tv.paddingRight)))
        } else {
            // 一个view的有效requestLayout，会导致触发onGlobalLayout
            // http://blog.csdn.net/litefish/article/details/53886469
            /*tv.viewTreeObserver.addOnGlobalLayoutListener {
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        tv.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        callback.invoke(tv.paint.measureText(tv.text.toString()) > (lines * (tv.width - tv.paddingLeft - tv.paddingRight)))
                    }
                }
            }*/
            CXLogUtil.d(TAG, "tv.width <= 0 , call addOnLayoutChangeListener")
            tv.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    tv.viewTreeObserver.removeOnPreDrawListener(this)
                    CXLogUtil.d(TAG, "onLayoutChange, width:" + tv.width)
                    callback.invoke(tv.paint.measureText(tv.text.toString()) > (lines * (tv.width - tv.paddingLeft - tv.paddingRight)))
                    return true
                }
            })
//            tv.post {
//                CXLogUtil.d(TAG, "onLayoutChange, width:" + tv.width)
//                callback.invoke(tv.paint.measureText(tv.text.toString()) > (lines * (tv.width - tv.paddingLeft - tv.paddingRight)))
//            }
//            tv.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
//                override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
//                    tv.removeOnLayoutChangeListener(this)
//
//                }
//            })
        }
    }
}

/**
 * 如果 text 为空 隐藏 TextView
 */
fun TextView.setTextAndVisible(text: String?) {
    this.text = text
    this.visibility = if (TextUtils.isEmpty(text?.trim())) View.GONE else View.VISIBLE
}
