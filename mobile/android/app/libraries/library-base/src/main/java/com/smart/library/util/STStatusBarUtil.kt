package com.smart.library.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

@Suppress("SpellCheckingInspection", "unused", "MemberVisibilityCanBePrivate", "SameParameterValue")
object STStatusBarUtil {

    private const val ROM_FLYME = "FLYME"
    private const val ROM_MIUI = "MIUI"
    private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"

    private var sName: String? = null
    private var sVersion: String? = null

    val isTransparentStatusBarSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || isMiui || isFlyme

    private val isMiui: Boolean
        get() = check(ROM_MIUI)

    private val isFlyme: Boolean
        get() = check(ROM_FLYME)

    private fun check(rom: String): Boolean {
        if (sName != null) {
            return sName == rom
        }
        sVersion = getProp(KEY_VERSION_MIUI)
        if (!TextUtils.isEmpty(sVersion)) {
            sName = ROM_MIUI
        } else {
            sVersion = Build.DISPLAY
            if (sVersion?.toUpperCase(Locale.getDefault())?.contains(ROM_FLYME) == true) {
                sName = ROM_FLYME
            } else {
                sVersion = Build.UNKNOWN
                sName = Build.MANUFACTURER.toUpperCase(Locale.getDefault())
            }
        }
        return sName == rom
    }

    private fun getProp(name: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return line
    }

    /**
     * 设置状态栏文字颜色模式
     *
     * @param activity
     * @param dark     文字是否是黑色，true为黑色，false为白色
     */
    fun setStatusBarTextColor(activity: Activity, dark: Boolean) {
        if (isMiui) {
            miuiSetStatusBarLightMode(activity.window, dark)
        }

        if (isFlyme) {
            flymeSetStatusBarLightMode(activity.window, dark)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = activity.window.decorView
            var ui = decor.systemUiVisibility
            if (dark) {
                ui = ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                ui = ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decor.systemUiVisibility = ui
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity 设置的activity
     * @param color    状态栏颜色
     * @param alpha    透明度
     */
    @JvmOverloads
    fun setStatusBarBackgroundColor(activity: Activity?, @ColorInt color: Int, alpha: Int = 0) {
        activity?.window ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            activity.window.statusBarColor = calculateStatusColor(color, alpha)
        }
    }

    /**
     * 设置白色TitleBar状态栏颜色
     *
     * @param activity
     */
    fun setStatusBarWhiteWithDarkText(activity: Activity?) {
        if (activity == null || activity.window == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = Color.WHITE
            miuiSetStatusBarLightMode(activity.window, true)
            flymeSetStatusBarLightMode(activity.window, true)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarBackgroundColor(activity, Color.GRAY, 0)
        }
    }


    /**
     * Dialog设置状态栏透明
     *
     * @param dialog
     */
    fun setTranslucentStatusForDialog(dialog: Dialog?) {
        if (dialog == null)
            return
        val window = dialog.window ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    /**
     * 设置透明
     */
    fun setTransparentForWindow(activity: Activity?) {
        activity?.window ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    fun setTransparent(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || activity?.window == null) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        val parent = activity.findViewById<View>(android.R.id.content) as ViewGroup
        var i = 0
        val count = parent.childCount
        while (i < count) {
            val childView = parent.getChildAt(i)
            if (childView is ViewGroup) {
                childView.setFitsSystemWindows(true)
                childView.clipToPadding = true
            }
            i++
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * flyme系统 状态栏图标为深色 和 魅族特定的文字风格
     *
     * @param window
     * @param dark
     * @return
     */
    fun flymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 设置状态栏文字颜色模式
     * @param activity
     * @param dark 文字是否是黑色，true为黑色，false为白色
     */
    fun setStatusBarLightMode(activity: Activity, dark: Boolean) {
        if (isMiui) {
            miuiSetStatusBarLightMode(activity.window, dark)
        }
        if (isFlyme) {
            flymeSetStatusBarLightMode(activity.window, dark)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = activity.window.decorView
            var ui = decor.systemUiVisibility
            ui = if (dark) {
                ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decor.systemUiVisibility = ui
        }
    }

    /**
     * miui系统状态栏 深色字体
     *
     * @param window
     * @param dark
     * @return
     */
    @SuppressLint("PrivateApi")
    fun miuiSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }
}