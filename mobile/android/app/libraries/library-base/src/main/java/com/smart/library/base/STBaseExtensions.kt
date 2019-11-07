@file:Suppress("unused", "DEPRECATION")

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
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AbsListView
import com.smart.library.util.*
import org.jetbrains.anko.AnkoAsyncContext
import java.util.*
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

fun Float.toPxFromDp(): Float {
    return STSystemUtil.getPxFromDp(this)
}

fun Int.toPxFromDp(): Int {
    return STSystemUtil.getPxFromDp(this.toFloat()).toInt()
}

fun Int.toDpFromPx(): Float {
    return STSystemUtil.getDpFromPx(this)
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

fun View.setOnLayoutListener(onLayout: (view: View) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val viewTreeObserver: ViewTreeObserver = viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                onLayout(this@setOnLayoutListener)
            }
        }
    })
}


// startActivityForResult with call back -->

/**
 * @param options Additional options for how the Activity should be started.
 * May be null if there are no options.  See {@link android.app.ActivityOptions}
 * for how to build the Bundle supplied here; there are no supported definitions
 * for building it manually.
 */
@JvmOverloads
fun startActivityForResult(activity: Activity?, @RequiresPermission intent: Intent, requestCode: Int, options: Bundle? = null, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
    activity?.let {
        if (STValueUtil.isValid(activity)) {
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
        val tmpFragmentManager = fragmentManager
        if (tmpFragmentManager != null) {
            var fragment = tmpFragmentManager.findFragmentByTag(ActivityCallbackFragment.TAG) as ActivityCallbackFragment?

            // remove unused fragment
            if (fragment?.isRemoving == true) {
                tmpFragmentManager.beginTransaction()?.remove(fragment)?.commitAllowingStateLoss()
                tmpFragmentManager.executePendingTransactions()
                fragment = null
            }

            if (fragment == null) {
                fragment = ActivityCallbackFragment()
                tmpFragmentManager.beginTransaction().add(fragment, ActivityCallbackFragment.TAG).commitAllowingStateLoss()
                tmpFragmentManager.executePendingTransactions()
                return fragment
            } else {
                return fragment
            }
        } else {
            return null
        }
    }
private val FragmentActivity.callbackFragmentV4: ActivityCallbackFragmentV4?
    get() {
        val tmpFragmentManager = supportFragmentManager
        if (tmpFragmentManager != null) {
            var fragment = tmpFragmentManager.findFragmentByTag(ActivityCallbackFragmentV4.TAG) as ActivityCallbackFragmentV4?

            // remove unused fragment
            if (fragment?.isRemoving == true) {
                tmpFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
                tmpFragmentManager.executePendingTransactions()
                fragment = null
            }

            if (fragment == null) {
                fragment = ActivityCallbackFragmentV4()
                tmpFragmentManager.beginTransaction().add(fragment, ActivityCallbackFragmentV4.TAG).commitAllowingStateLoss()
                tmpFragmentManager.executePendingTransactions()
                return fragment
            } else {
                return fragment
            }
        } else {
            return null
        }
    }

internal data class StartForResultModel(val intent: Intent, val requestCode: Int, val options: Bundle?)

internal class ActivityCallbackFragmentV4 : android.support.v4.app.Fragment() {
    companion object {
        const val TAG: String = "ActivityCallbackFragmentV4"
    }

    /**
     *
     * @see {@link https://stackoverflow.com/questions/698638/why-does-concurrenthashmap-prevent-null-keys-and-values}
     */
    private val callbackMap: MutableMap<Int, ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)?> = ConcurrentHashMap()
    private val startForResultList: Vector<StartForResultModel> = Vector()

    @Volatile
    private var isAttachedToActivity: Boolean = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        STLogUtil.w("onAttach to Activity")

        isAttachedToActivity = true

        startForResultList.forEach {
            if (activity != null && !isDetached) {
                startActivityForResult(it.intent, it.requestCode, it.options)
            } else {
                STLogUtil.e("startActivityForResult failure, not attached to Activity")
            }
        }
        startForResultList.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @Synchronized
    fun startForResult(@RequiresPermission intent: Intent, requestCode: Int, options: Bundle?, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
        if (callback != null) callbackMap[requestCode] = callback
        if (isAttachedToActivity) {
            if (activity != null && !isDetached) {
                startActivityForResult(intent, requestCode, options)
            } else {
                STLogUtil.e("startActivityForResult failure, not attached to Activity")
            }
        } else {
            STLogUtil.w("wait attached to Activity")
            startForResultList.addElement(StartForResultModel(intent, requestCode, options))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isAttachedToActivity = false
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
    private val startForResultList: Vector<StartForResultModel> = Vector()

    @Volatile
    private var isAttachedToActivity: Boolean = false

    /**
     * 备注
     * VERSION_CODES < M (23 - Android 6.0) 时，执行onAttach(Activity)
     * VERSION_CODES >= M (23 - Android 6.0) 时，执行onAttach(Context)
     * V4 无此问题
     */
    @Suppress("OverridingDeprecatedMember")
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        callOnAttach()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callOnAttach()
    }

    private fun callOnAttach() {
        isAttachedToActivity = true
        startForResultList.forEach {
            if (activity != null && !isDetached) {
                startActivityForResult(it.intent, it.requestCode, it.options)
            } else {
                STLogUtil.e("startActivityForResult failure, not attached to Activity")
            }
        }
        startForResultList.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @Synchronized
    fun startForResult(@RequiresPermission intent: Intent, requestCode: Int, options: Bundle?, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
        if (callback != null) callbackMap[requestCode] = callback
        if (isAttachedToActivity) {
            if (activity != null && !isDetached) {
                startActivityForResult(intent, requestCode, options)
            } else {
                STLogUtil.e("startActivityForResult failure, not attached to Activity")
            }
        } else {
            STLogUtil.w("wait attached to Activity")
            startForResultList.addElement(StartForResultModel(intent, requestCode, options))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isAttachedToActivity = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (callbackMap.containsKey(requestCode)) callbackMap.remove(requestCode)?.invoke(requestCode, resultCode, data)
    }
}

// startActivityForResult with call back <--