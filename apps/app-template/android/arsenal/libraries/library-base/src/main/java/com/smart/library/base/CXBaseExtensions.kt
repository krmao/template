@file:Suppress("unused")

package com.smart.library.base

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.RequiresPermission
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import com.smart.library.util.CXChecksumUtil
import com.smart.library.util.CXValueUtil
import com.smart.library.util.CXViewUtil
import org.jetbrains.anko.AnkoAsyncContext
import java.util.concurrent.ConcurrentHashMap

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

/**
 * 如果 text 为空 隐藏 TextView
 */
fun TextView.setTextAndVisible(text: String?) {
    this.text = text
    this.visibility = if (TextUtils.isEmpty(text?.trim())) View.GONE else View.VISIBLE
}
fun String.md5(): String = CXChecksumUtil.genMD5Checksum(this)


fun Fragment.uiThread(fn: () -> Unit) {
    if (this.isDetached) return
    val activity = this.activity ?: return
    activity.runOnUiThread { fn() }
}

fun View.animateAlphaToVisibility(visibility: Int, duration: Long = 300) {
    animate().alpha(if (visibility == View.VISIBLE) 1.0f else 0.0f).setDuration(duration).setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            setVisibility(View.VISIBLE)
        }

        override fun onAnimationEnd(animation: Animator?) {
            setVisibility(visibility)
        }

        override fun onAnimationCancel(animation: Animator?) {
            setVisibility(visibility)
        }

        override fun onAnimationStart(animation: Animator?) {
            setVisibility(View.VISIBLE)
        }
    }).start()
}


// startActivityForResult with call back -->

@JvmOverloads
fun startActivityForResult(activity: Activity?, @RequiresPermission intent: Intent, requestCode: Int, options: Bundle? = null, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
    activity?.let {
        if (CXValueUtil.isValid(activity)) {
            if (activity is FragmentActivity) {
                activity.callbackFragmentV4?.startForResult(intent, requestCode, options, callback)
            } else {
                activity.callbackFragment?.startForResult(intent, requestCode, options, callback)
            }
        }
    }
}

private val Activity.callbackFragment: ActivityCallbackFragment?
    get() {
        return (fragmentManager?.findFragmentByTag(ActivityCallbackFragment.TAG) as ActivityCallbackFragment?) ?: ActivityCallbackFragment().apply {
            fragmentManager?.let {
                it.beginTransaction().add(this, ActivityCallbackFragment.TAG).commitAllowingStateLoss()
                it.executePendingTransactions()
            }
        }
    }
private val FragmentActivity.callbackFragmentV4: ActivityCallbackFragmentV4?
    get() {
        return (supportFragmentManager?.findFragmentByTag(ActivityCallbackFragment.TAG) as ActivityCallbackFragmentV4?) ?: ActivityCallbackFragmentV4().apply {
            supportFragmentManager?.let {
                it.beginTransaction().add(this, ActivityCallbackFragment.TAG).commitAllowingStateLoss()
                it.executePendingTransactions()
            }
        }
    }

internal class ActivityCallbackFragmentV4 : android.support.v4.app.Fragment() {
    companion object {
        const val TAG: String = "ActivityCallbackFragmentV4"
    }

    /**
     *
     * @see {@link https://stackoverflow.com/questions/698638/why-does-concurrenthashmap-prevent-null-keys-and-values}
     */
    private val callbackMap: MutableMap<Int, ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)?> = ConcurrentHashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startForResult(@RequiresPermission intent: Intent, requestCode: Int, options: Bundle?, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
        if (callback != null) callbackMap[requestCode] = callback
        startActivityForResult(intent, requestCode, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (callbackMap.containsKey(requestCode)) callbackMap.remove(requestCode)?.invoke(requestCode, resultCode, data)
    }
}

internal class ActivityCallbackFragment : android.app.Fragment() {
    companion object {
        const val TAG: String = "ActivityCallbackFragment"
    }

    /**
     *
     * @see {@link https://stackoverflow.com/questions/698638/why-does-concurrenthashmap-prevent-null-keys-and-values}
     */
    private val callbackMap: MutableMap<Int, ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)?> = ConcurrentHashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startForResult(@RequiresPermission intent: Intent, requestCode: Int, options: Bundle?, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
        if (callback != null) callbackMap[requestCode] = callback
        startActivityForResult(intent, requestCode, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (callbackMap.containsKey(requestCode)) callbackMap.remove(requestCode)?.invoke(requestCode, resultCode, data)
    }
}

// startActivityForResult with call back <--