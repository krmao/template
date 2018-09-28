package com.smart.library.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.smart.library.R
import com.smart.library.base.CXBaseApplication
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

/**
 * 即使通知权限被禁用, 也可以显示 Toast
 *
 * @see CXToastUtil.enableCustomToast
 * @link https://www.jianshu.com/p/a5397a11a684
 */
@Suppress("MemberVisibilityCanBeprotected", "unused", "MemberVisibilityCanBePrivate")
open class CXToast {

    companion object {
        protected val TAG: String = CXToast::class.java.name
        protected const val SHORT_DURATION_TIMEOUT: Int = 2000
        protected const val LONG_DURATION_TIMEOUT: Int = 3500

        var debug = false

        protected val windowManager: WindowManager by lazy { CXBaseApplication.INSTANCE.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
        protected val handler by lazy { Handler(Looper.getMainLooper()) }
        protected val queue by lazy { LinkedBlockingQueue<CXToast>() }
        protected var atomicInteger = AtomicInteger(0)

        protected fun log(msg: String) {
            if (debug) CXLogUtil.d(TAG, msg)
        }

        protected val activeTask = object : Runnable {
            override fun run() {
                log("activeTask run, ${atomicInteger.get()}, queue:${queue.size}")
                val tmpToast = queue.peek()
                if (tmpToast == null) {
                    atomicInteger.decrementAndGet()
                    log("activeTask tmpToast == null, ${atomicInteger.get()}, queue:${queue.size}")
                } else {
                    log("activeTask tmpToast != null, ${atomicInteger.get()}, queue:${queue.size}")
                    handler.post(tmpToast.showTask)
                    handler.postDelayed(tmpToast.hideTask, tmpToast.duration.toLong())
                    handler.postDelayed(this, tmpToast.duration.toLong())
                }
            }
        }

        @JvmStatic
        fun cancelAll(callback: () -> Unit?) {
            synchronized(CXToast) {
                log("cancelAll start, ${atomicInteger.get()}, queue:${queue.size}")
                handler.removeCallbacksAndMessages(null)
                handler.post {
                    queue.forEach { it.removeView() }
                    queue.clear()
                    atomicInteger.set(0)
                    log("cancelAll end, ${atomicInteger.get()}, queue:${queue.size}")
                    callback.invoke()
                }
            }
        }

        @JvmStatic
        @JvmOverloads
        fun makeText(msg: String, duration: Int = Toast.LENGTH_SHORT, toastGravity: Int = Gravity.BOTTOM, textGravity: Int = Gravity.CENTER_HORIZONTAL, xOffset: Int = 0, yOffset: Int = 0): CXToast {
            return CXToast().setText(msg, textGravity).setDuration(duration).setGravity(toastGravity, xOffset, yOffset)
        }
    }


    protected var view: View? = null
    protected var duration: Int = 0
    protected val showTask = Runnable { log("showTask, ${atomicInteger.get()}, queue:${queue.size}"); view?.let { removeView();addView() }; }
    protected val hideTask = Runnable { log("hideTask, ${atomicInteger.get()}, queue:${queue.size}"); view?.let { removeView();queue.poll() };view = null; }

    protected val layoutParams: WindowManager.LayoutParams  by lazy {
        WindowManager.LayoutParams().apply {
            height = WindowManager.LayoutParams.WRAP_CONTENT
            width = WindowManager.LayoutParams.WRAP_CONTENT
            format = PixelFormat.TRANSLUCENT
            windowAnimations = android.R.style.Animation_Toast
            @Suppress("DEPRECATION")
            type = WindowManager.LayoutParams.TYPE_TOAST
            title = "Toast"
            flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        }
    }

    protected fun addView() = view?.let { windowManager.addView(it, layoutParams) }
    protected fun removeView() = view?.let { if (it.parent != null) windowManager.removeView(it) } // note: checking parent() just to make sure the view has  been added...  i have seen cases where we get here when the view isn't yet added, so let's try not to crash.

    @JvmOverloads
    fun setGravity(gravity: Int, xOffset: Int = 0, yOffset: Int = 0): CXToast {
        val finalGravity: Int = if (Build.VERSION.SDK_INT >= 17) Gravity.getAbsoluteGravity(gravity, view!!.context.resources.configuration.layoutDirection) else gravity
        layoutParams.gravity = finalGravity
        if (finalGravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.FILL_HORIZONTAL) layoutParams.horizontalWeight = 1.0f
        if (finalGravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.FILL_VERTICAL) layoutParams.verticalWeight = 1.0f
        layoutParams.y = yOffset
        layoutParams.x = xOffset
        return this
    }

    fun setDuration(durationMillis: Int): CXToast {
        when (durationMillis) {
            Toast.LENGTH_SHORT -> duration = SHORT_DURATION_TIMEOUT
            Toast.LENGTH_LONG -> duration = LONG_DURATION_TIMEOUT
            else -> duration = if (durationMillis < 0) 0 else durationMillis
        }
        return this
    }

    fun setMargin(horizontalMargin: Float, verticalMargin: Float): CXToast {
        layoutParams.horizontalMargin = horizontalMargin
        layoutParams.verticalMargin = verticalMargin
        return this
    }

    /**
     * 与 setText  不能同时使用
     */
    fun setView(view: View): CXToast {
        this.view = view
        return this
    }

    /**
     * 与 setView  不能同时使用
     */
    @JvmOverloads
    @SuppressLint("InflateParams")
    fun setText(text: String, textGravity: Int = Gravity.CENTER_HORIZONTAL): CXToast {
        val viewHolder = CXToastUtil.ViewHolder(LayoutInflater.from(CXBaseApplication.INSTANCE).inflate(R.layout.cx_widget_transient_notification, null))
        viewHolder.textView.text = text
        viewHolder.textView.gravity = textGravity
        setView(viewHolder.rootView)
        return this
    }

    fun show() {
        synchronized(CXToast) {
            log("show, ${atomicInteger.get()}, queue:${queue.size}")
            queue.offer(this)
            if (0 == atomicInteger.get()) {
                atomicInteger.incrementAndGet()
                handler.post(activeTask)
                log("show post, ${atomicInteger.get()}, queue:${queue.size}")
            }
        }
    }

    fun cancel() {
        synchronized(CXToast) {
            if (0 == atomicInteger.get() && queue.isEmpty()) return
            if (this == queue.peek()) {
                handler.removeCallbacks(activeTask)
                handler.post(hideTask)
                handler.post(activeTask)
            }
        }
    }
}