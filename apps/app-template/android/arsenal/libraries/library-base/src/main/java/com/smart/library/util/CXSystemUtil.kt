@file:Suppress("DEPRECATION")

package com.smart.library.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.smart.library.R
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.toBitmap
import org.jetbrains.annotations.Nullable

/**
 * 所有与系统相关的方法
 */
@Suppress("unused")
object CXSystemUtil {

    val isSdCardExist: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()


    val sdCardPath: String?
        get() {
            if (isSdCardExist)
                return Environment.getExternalStorageDirectory().absolutePath
            return null
        }

    /**
     * sdCardInfo[0]=总大小
     * sdCardInfo[1]=可用大小
     */
    val sdCardMemory: LongArray
        get() {
            val sdCardInfo = LongArray(2)
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                val sdcardDir = Environment.getExternalStorageDirectory()
                val sf = StatFs(sdcardDir.path)
                val bSize = sf.blockSize.toLong()
                val bCount = sf.blockCount.toLong()
                val availBlocks = sf.availableBlocks.toLong()

                sdCardInfo[0] = bSize * bCount
                sdCardInfo[1] = bSize * availBlocks
            }
            return sdCardInfo
        }

    @JvmStatic
    fun sendKeyDownEventBack(context: Context?) {
        if (context is Activity) {
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
            context.onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent)
        }
    }

    @JvmStatic
    fun sendKeyDownEvent(context: Context?, keyCode: Int) {
        if (context is Activity) {
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
            context.onKeyDown(keyCode, keyEvent)
        }
    }

    @JvmStatic
    fun sendKeyUpEvent(context: Context?, keyCode: Int) {
        if (context is Activity) {
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
            context.onKeyUp(keyCode, keyEvent)
        }
    }

    @JvmStatic
    fun sendKeyUpEventMenu(context: Context?) {
        if (context is Activity) {
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MENU)
            context.onKeyUp(KeyEvent.KEYCODE_MENU, keyEvent)
        }
    }

    /**
     * 使得处于后台的 app 显示到前台
     */
    @JvmStatic
    fun bringAppToFront(activity: Activity?) {
        activity?.let {
            val notificationIntent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
            notificationIntent.`package` = null // The golden row !!!
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            activity.startActivity(notificationIntent)
            activity.overridePendingTransition(R.anim.cx_fade_in, R.anim.cx_fade_out)
        }
    }

    /***
     * 隐藏软键盘
     */
    fun hideKeyboard(view: View?) {
        if (view != null) {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive)
                inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

    /***
     * 隐藏软键盘
     */
    fun hideKeyboard(activity: Activity?) {
        val view = activity?.currentFocus
        if (view != null) {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive)
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /***
     * 显示软键盘
     */
    fun showKeyboard(v: View) {
        val inputMethodManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED)
    }

    /***
     * 隐藏/显示软键盘
     */
    fun toggleKeyboard(v: View) {
        val inputMethodManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    val displayMetrics: DisplayMetrics
        get() = CXBaseApplication.INSTANCE.resources.displayMetrics

    val screenWidth: Int
        get() = CXBaseApplication.INSTANCE.resources.displayMetrics.widthPixels

    val screenHeight: Int
        get() = CXBaseApplication.INSTANCE.resources.displayMetrics.heightPixels


    fun getPxFromDp(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics)
    }

    fun getPxFromPx(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, displayMetrics)
    }

    fun getPxFromSp(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, displayMetrics)
    }

    val statusBarHeight: Int
        get() {
            var statusBarHeight = 0
            val resourceId = CXBaseApplication.INSTANCE.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = CXBaseApplication.INSTANCE.resources.getDimensionPixelSize(resourceId)
            }
            return statusBarHeight
        }

    /**
     * 复制到剪贴板
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun copyToClipboard(label: String, contentText: String): Boolean {
        try {
            val cm = CXBaseApplication.INSTANCE.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText(label, contentText)
            cm.primaryClip = clip
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    /*
    https://developer.android.com/training/system-ui/immersive.html#sticky
    https://developer.android.com/training/system-ui/navigation.html

    This snippet hides the system bars.

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            MSystemUtil.hideSystemUI(getWindow(),false);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.launch_activity);


        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (hasFocus) {
                MSystemUtil.hideSystemUI(getWindow(),false);
            }
        }

     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun hideSystemUI(window: Window, useImmersiveSticky: Boolean) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            if (useImmersiveSticky) View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY else View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun showSystemUI(window: Window) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    val SDK_INT: Int
        get() = Build.VERSION.SDK_INT

    val versionCode: Int
        get() {
            var versionCode = 0
            try {
                versionCode = CXBaseApplication.INSTANCE.packageManager.getPackageInfo(CXBaseApplication.INSTANCE.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionCode
        }

    val versionName: String?
        get() {
            var versionName: String? = null
            try {
                versionName = CXBaseApplication.INSTANCE.packageManager.getPackageInfo(CXBaseApplication.INSTANCE.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionName
        }

    val appName: String
        get() {
            var name = ""
            try {
                name = CXBaseApplication.INSTANCE.resources.getString(CXBaseApplication.INSTANCE.applicationInfo.labelRes)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return name
        }

    /**
     * android 图标尺寸大小
     * http://iconhandbook.co.uk/reference/chart/android/
     *
     * 如果 icon 是 vector xml 则会出现严重问题
     * android.app.RemoteServiceException: Bad notification posted from package
     * 详见:
     * https://stackoverflow.com/a/40243036/4348530
     * 建议使用 png
     */
    val appIcon: Int?
        get() {
            var icon: Int? = null
            try {
                icon = CXBaseApplication.INSTANCE.applicationInfo.icon
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            if (icon == null || icon <= 0) {
                icon = null
            }
            return icon
        }

    /**
     * "android o appIcon 获取不到"
     * 所以只提供 appBitmap
     */
    val appBitmap: Bitmap?
        get() {
            var drawable: Drawable? = null
            try {
                drawable = CXBaseApplication.INSTANCE.packageManager.getApplicationIcon(CXBaseApplication.INSTANCE.packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            if (drawable == CXBaseApplication.INSTANCE.packageManager.defaultActivityIcon) {
                drawable = null
            }
            return drawable?.toBitmap()
        }

    @Nullable
    fun getAppMetaData(key: String): Any? {
        var metaData: Any? = null
        try {
            val info = CXBaseApplication.INSTANCE.packageManager.getApplicationInfo(CXBaseApplication.INSTANCE.packageName, PackageManager.GET_META_DATA)
            metaData = info.metaData.get(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return metaData
    }

    fun getAppMetaDataForInt(key: String): Int {
        var metaData = 0
        try {
            val info = CXBaseApplication.INSTANCE.packageManager.getApplicationInfo(CXBaseApplication.INSTANCE.packageName, PackageManager.GET_META_DATA)
            metaData = info.metaData.getInt(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return metaData
    }

    fun isAppInstalled(context: Context?, pkgName: String): Boolean {
        if (context != null) {
            try {
                context.packageManager.getPackageInfo(pkgName, PackageManager.PERMISSION_GRANTED)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

        }
        return false
    }

    /**
     * 收起下拉通知栏
     */
    @JvmStatic
    fun collapseStatusBar() {
        CXBaseApplication.INSTANCE.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }
}
