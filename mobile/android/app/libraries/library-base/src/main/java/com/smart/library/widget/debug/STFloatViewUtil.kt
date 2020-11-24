package com.smart.library.widget.debug


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.STSystemUtil

/*
<!--悬浮窗-->
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
*/
@Suppress("DEPRECATION", "unused", "ClickableViewAccessibility", "PrivatePropertyName")
enum class STFloatViewUtil {
    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    private val KEY_LAST_X = "M_FLOAT_VIEW_LAST_X"
    private val KEY_LAST_Y = "M_FLOAT_VIEW_LAST_Y"
    private val KEY_HIDE_ALWAYS = "KEY_HIDE_ALWAYS"
    private val defaultWH = STSystemUtil.getPxFromDp(42f).toInt()
    private val defaultX = 0
    private val defaultY = defaultWH * 5

    private var floatView: ImageView = ImageView(STInitializer.application())
    private var windowManager: WindowManager
    private var windowLayoutParams: WindowManager.LayoutParams
    private var listener: View.OnClickListener? = null

    init {
        floatView.setImageResource(R.drawable.st_emo_im_happy)
        windowManager = STInitializer.application()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowLayoutParams = WindowManager.LayoutParams()
        windowLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
        windowLayoutParams.format = PixelFormat.RGBA_8888
        windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

        windowLayoutParams.width = defaultWH
        windowLayoutParams.height = defaultWH
        windowLayoutParams.gravity = Gravity.START or Gravity.TOP
        windowLayoutParams.x = STPreferencesUtil.getInt(KEY_LAST_X, defaultX) ?: 0
        windowLayoutParams.y = STPreferencesUtil.getInt(KEY_LAST_Y, defaultY) ?: 0
        floatView.setOnTouchListener(object : View.OnTouchListener {
            private var x: Float = 0.toFloat()
            private var y: Float = 0.toFloat()
            private var rawX: Float = 0.toFloat()
            private var rawY: Float = 0.toFloat()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        floatView.setImageResource(R.drawable.st_emo_im_foot_in_mouth)
                        x = event.x
                        y = event.y
                        rawX = event.rawX
                        rawY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        windowLayoutParams.x = (event.rawX - x).toInt()
                        windowLayoutParams.y = (event.rawY - STSystemUtil.statusBarHeight.toFloat() - y).toInt()
                        windowManager.updateViewLayout(v, windowLayoutParams)
                    }
                    MotionEvent.ACTION_UP -> {
                        floatView.setImageResource(R.drawable.st_emo_im_happy)
                        STPreferencesUtil.putInt(KEY_LAST_X, windowLayoutParams.x)
                        STPreferencesUtil.putInt(KEY_LAST_Y, windowLayoutParams.y)
                        if (Math.abs(event.rawX - rawX) < 5 && Math.abs(event.rawY - rawY) < 5) {
                            if (listener != null)
                                listener?.onClick(floatView)
                            else {
                                //jump
                            }
                        }
                    }
                }
                return true
            }
        })
    }

    private var isAwaysHide: Boolean
        get() = STPreferencesUtil.getBoolean(KEY_HIDE_ALWAYS, false) ?: true
        set(isAwaysHide) {
            STPreferencesUtil.putBoolean(KEY_HIDE_ALWAYS, isAwaysHide)
            if (isAwaysHide)
                hide()
        }

    fun setImageResource(@DrawableRes resId: Int) = floatView.setImageResource(resId)

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    fun show() = try {
        hide()
        if (isAwaysHide == true) {
        } else {
            windowManager.addView(floatView, windowLayoutParams)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }


    fun clearLocationCatch() {
        STPreferencesUtil.putInt(KEY_LAST_X, defaultX)
        STPreferencesUtil.putInt(KEY_LAST_Y, defaultY)
    }

    fun hide() = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!floatView.isAttachedToWindow) {
            } else windowManager.removeViewImmediate(floatView)
        } else {
            if (floatView.parent == null) {
            } else windowManager.removeViewImmediate(floatView)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
