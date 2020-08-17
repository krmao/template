@file:Suppress("unused", "DEPRECATION")

package com.smart.library.base

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import com.smart.library.util.STChecksumUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STViewUtil
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

fun Float.toPxFromDp(): Float {
    return STSystemUtil.getPxFromDp(this)
}

fun Int.toPxFromDp(): Int {
    return STSystemUtil.getPxFromDp(this.toFloat()).toInt()
}

fun Int.toDpFromPx(): Float {
    return STSystemUtil.getDpFromPx(this)
}

fun <T : Fragment> AnkoAsyncContext<T>.fragmentUiThread(f: (T) -> Unit) {
    val fragment = weakRef.get() ?: return
    if (fragment.isDetached) return
    val activity = fragment.activity ?: return
    activity.runOnUiThread { f(fragment) }
}

fun <T : Fragment> AnkoAsyncContext<T>.fragmentUiThreadWithContext(f: Context.(T) -> Unit) {
    val fragment = weakRef.get() ?: return
    if (fragment.isDetached) return
    val activity = fragment.activity ?: return
    activity.runOnUiThread { activity.f(fragment) }
}

fun AbsListView.performItemClick(position: Int) {
    STViewUtil.performItemClick(this, position)
}

/**
 * @param debug true 返回自身, false 返回 md5
 */
@JvmOverloads
fun String.md5(debug: Boolean = false): String = if (debug) this else STChecksumUtil.genMD5Checksum(this)

fun Fragment.uiThread(fn: () -> Unit) {
    if (this.isDetached) return
    val activity = this.activity ?: return
    activity.runOnUiThread { fn() }
}

@JvmOverloads
fun View.animateAlphaToVisibility(visibility: Int, duration: Long = 300, onAnimationEnd: (() -> Unit)? = null) {
    animate().alpha(if (visibility == View.VISIBLE) 1.0f else 0.0f).setDuration(duration).setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            setVisibility(View.VISIBLE)
        }

        override fun onAnimationEnd(animation: Animator?) {
            setVisibility(visibility)
            onAnimationEnd?.invoke()
        }

        override fun onAnimationCancel(animation: Animator?) {
            setVisibility(visibility)
            onAnimationEnd?.invoke()
        }

        override fun onAnimationStart(animation: Animator?) {
            setVisibility(View.VISIBLE)
        }
    }).start()
}

/**
 * 方法内加了 requestLayout
 * 会必然触发 onGlobalLayout 回调
 * @return view.height 并不会随着改变/显示/隐藏 虚拟导航栏而显示不同的值, 经测试一直都是 screenRealHeight, 还是需要调用通过 STSystemUtil.showSystemInfo(this) 去获取正确的高度
 */
fun View.ensureOnGlobalLayoutListener(onLayout: (view: View) -> Unit) {
    STLogUtil.w("ensureOnGlobalLayoutListener start")
    // 调用不会立即出发
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val viewTreeObserver: ViewTreeObserver = viewTreeObserver
            STLogUtil.w("ensureOnGlobalLayoutListener isAlive=${viewTreeObserver.isAlive}")
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                STLogUtil.w("ensureOnGlobalLayoutListener end")
                onLayout(this@ensureOnGlobalLayoutListener)
            }
        }
    })
    requestLayout() // 加 requestLayout 会必然触发 onGlobalLayout
}