package com.smart.library.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.widget.AbsListView
import com.smart.library.util.CXChecksumUtil
import com.smart.library.util.CXViewUtil
import org.jetbrains.anko.AnkoAsyncContext

/**
 * 全局基础扩展属性/方法
 */

/**
 * drawable to bitmap
 */
fun Drawable.toBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bmp
}

fun <T : android.support.v4.app.Fragment> AnkoAsyncContext<T>.fragmentUiThread(f: (T) -> Unit) {
    val fragment = weakRef.get() ?: return
    if (fragment.isDetached) return
    val activity = fragment.activity ?: return
    activity.runOnUiThread { f(fragment) }
}

fun <T : android.support.v4.app.Fragment> AnkoAsyncContext<T>.fragmentUiThreadWithContext(f: Context.(T) -> Unit) {
    val fragment = weakRef.get() ?: return
    if (fragment.isDetached) return
    val activity = fragment.activity ?: return
    activity.runOnUiThread { activity.f(fragment) }
}

fun AbsListView.performItemClick(position: Int) {
    CXViewUtil.performItemClick(this, position)
}

fun String.md5(): String = CXChecksumUtil.genMD5Checksum(this)
