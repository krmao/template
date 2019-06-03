@file:Suppress("unused")

package com.smart.library.util

import android.app.Activity
import android.support.annotation.UiThread
import android.text.TextUtils
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.TextView
import com.smart.library.base.animateAlphaToVisibility
import org.jetbrains.anko.forEachChild

@Suppress("MemberVisibilityCanBePrivate")
object STViewUtil {
    private val TAG = STViewUtil::class.java.simpleName

    @Volatile
    private var lastClickedTime: Long = System.currentTimeMillis()

    @UiThread
    @JvmStatic
    fun isDoubleClicked(): Boolean {
        val isDoubleClicked = System.currentTimeMillis() - lastClickedTime <= ViewConfiguration.getDoubleTapTimeout() // double check
        lastClickedTime = System.currentTimeMillis()
        return isDoubleClicked
    }

    @JvmStatic
    fun animateAlphaToVisibility(visibility: Int, duration: Long = 300, vararg views: View?) = views.forEach { it?.animateAlphaToVisibility(visibility, duration) }

    @JvmStatic
    fun performItemClick(listView: AbsListView?, position: Int) {
        listView?.performItemClick(listView.getChildAt(position), position, listView.getItemIdAtPosition(position))
    }

    @Suppress("DEPRECATION")
    fun checkOverLines(tv: TextView?, lines: Int?, callback: ((isTextOverLines: Boolean) -> Unit)?) {
        if (tv == null || lines == null || callback == null) {
            STLogUtil.e(TAG, "arguments may be null")
            return
        }
        if (tv.width > 0) {
            STLogUtil.d(TAG, "tv.width > 0 , call invoke")
            callback.invoke(tv.paint.measureText(tv.text.toString()) > (lines * (tv.width - tv.paddingLeft - tv.paddingRight)))
        } else {
            STLogUtil.d(TAG, "tv.width <= 0 , call addOnLayoutChangeListener")
            tv.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    tv.viewTreeObserver.removeOnPreDrawListener(this)
                    STLogUtil.d(TAG, "onLayoutChange, width:" + tv.width)
                    callback.invoke(tv.paint.measureText(tv.text.toString()) > (lines * (tv.width - tv.paddingLeft - tv.paddingRight)))
                    return true
                }
            })
        }
    }

    @JvmStatic
    fun getAllViews(activity: Activity?): MutableList<View> = getAllChildViews(activity?.getWindow()?.getDecorView())

    @JvmStatic
    fun getAllChildViews(view: View?): MutableList<View> {
        val allChildren = mutableListOf<View>()
        if (view is ViewGroup) {
            view.forEachChild {
                allChildren.add(it)
                allChildren.addAll(getAllChildViews(it))
            }
        }
        return allChildren
    }

    private var reactViewClass: Class<*>? = null
        get() {
            if (field == null) {
                try {
                    field = java.lang.Class.forName("com.facebook.react.ReactRootView")
                } catch (e: Exception) {
                }
            }
            return field
        }

    @JvmStatic
    fun isHaveReactView(activity: Activity?): Boolean = getAllChildViews(activity?.getWindow()?.getDecorView()).any { reactViewClass?.isInstance(it) ?: false }

    @JvmStatic
    fun isHaveWebView(activity: Activity?): Boolean = getAllChildViews(activity?.getWindow()?.getDecorView()).any { it is WebView }

}

/**
 * 如果 text 为空 隐藏 TextView
 */
fun TextView.setTextAndVisible(text: String?) {
    this.text = text
    this.visibility = if (TextUtils.isEmpty(text?.trim())) View.GONE else View.VISIBLE
}