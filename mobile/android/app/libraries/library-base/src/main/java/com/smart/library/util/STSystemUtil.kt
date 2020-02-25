package com.smart.library.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
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
import androidx.core.content.PermissionChecker
import com.smart.library.R
import com.smart.library.base.STBaseApplication
import com.smart.library.base.toBitmap

@Suppress("unused", "MemberVisibilityCanBePrivate", "DEPRECATION")
object STSystemUtil {

    @JvmStatic
    val SDK_INT: Int = Build.VERSION.SDK_INT
    @JvmStatic
    val versionCode: Int by lazy { getAppVersionCode() }
    @JvmStatic
    val versionName: String by lazy { getAppVersionName() }
    @JvmStatic
    val appName: String by lazy { getAppName() }
    @JvmStatic
    val appIcon: Int? by lazy { getAppIcon() }
    @JvmStatic
    val appBitmap: Bitmap? by lazy { getAppBitmap() }
    @JvmStatic
    val displayMetrics: DisplayMetrics
        get() = STBaseApplication.INSTANCE.resources.displayMetrics
    @JvmStatic
    val screenWidth: Int
        get() = displayMetrics.widthPixels
    @JvmStatic
    val screenHeight: Int
        get() = displayMetrics.heightPixels

    /**
     * If your application is targeting an API level before 23 (Android M) then both:ContextCompat.CheckSelfPermission and Context.checkSelfPermission doesn't work and always returns 0 (PERMISSION_GRANTED). Even if you run the application on Android 6.0 (API 23).
     */
    @JvmStatic
    fun checkSelfPermission(vararg permission: String): Boolean = permission.all {
        PermissionChecker.checkSelfPermission(
            STBaseApplication.INSTANCE,
            it
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    @JvmStatic
    fun sendKeyDownEventBack(context: Context?) = (context as? Activity)?.onKeyDown(
        KeyEvent.KEYCODE_BACK,
        KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
    )

    @JvmStatic
    fun sendKeyDownEvent(context: Context?, keyCode: Int) =
        (context as? Activity)?.onKeyDown(keyCode, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))

    @JvmStatic
    fun sendKeyUpEvent(context: Context?, keyCode: Int) =
        (context as? Activity)?.onKeyUp(keyCode, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))

    @JvmStatic
    fun sendKeyUpEventMenu(context: Context?) = (context as? Activity)?.onKeyUp(
        KeyEvent.KEYCODE_MENU,
        KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MENU)
    )

    @JvmStatic
    fun getPxFromDp(value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics)

    @JvmStatic
    fun getPxFromPx(value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, displayMetrics)

    @JvmStatic
    fun getDpFromPx(px: Int): Float = px / displayMetrics.density

    @JvmStatic
    fun getPxFromSp(value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, displayMetrics)

    @JvmStatic
    fun collapseStatusBar() =
        STBaseApplication.INSTANCE.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) // 收起下拉通知栏

    @JvmStatic
    fun hideKeyboard(view: View?) = view?.let { _view ->
        (_view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.let {
            if (it.isActive) it.hideSoftInputFromWindow(
                view.applicationWindowToken,
                0
            )
        }
    }

    @JvmStatic
    fun hideKeyboard(activity: Activity?) = activity?.currentFocus?.let { view ->
        (view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.let {
            if (it.isActive) it.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    @JvmStatic
    fun showKeyboard(view: View?) =
        (view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.showSoftInput(
            view,
            InputMethodManager.SHOW_FORCED
        )

    @JvmStatic
    fun toggleKeyboard(view: View?) =
        (view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.toggleSoftInput(
            0,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

    @JvmStatic
    fun isAppInstalled(packageName: String): Boolean = getPackageInfo(packageName) != null

    @JvmStatic
    fun getAppVersionCode(packageName: String = STBaseApplication.INSTANCE.packageName): Int =
        getPackageInfo(packageName)?.versionCode ?: 0

    @JvmStatic
    fun getAppVersionName(packageName: String = STBaseApplication.INSTANCE.packageName): String =
        getPackageInfo(packageName)?.versionName ?: ""

    @JvmStatic
    fun getAppName(packageName: String = STBaseApplication.INSTANCE.packageName): String {
        var appName = ""
        try {
            getPackageInfo(packageName)?.applicationInfo?.labelRes?.let {
                appName = STBaseApplication.INSTANCE.resources.getString(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return appName
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
    @JvmStatic
    fun getAppIcon(packageName: String = STBaseApplication.INSTANCE.packageName): Int? =
        getPackageInfo(packageName)?.applicationInfo?.icon

    @JvmStatic
    fun getAppMetaData(key: String): Any? = getApplicationInfo()?.metaData?.get(key)

    @JvmStatic
    fun getAppMetaDataForInt(key: String): Int? = getApplicationInfo()?.metaData?.getInt(key)

    /**
     * sdCardInfo[0]=总大小
     * sdCardInfo[1]=可用大小
     */
    @Suppress("DEPRECATION")
    @JvmStatic
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
    val isSdCardExist: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    @JvmStatic
    val sdCardPath: String?
        get() = if (isSdCardExist) Environment.getExternalStorageDirectory().absolutePath else null
    @JvmStatic
    val statusBarHeight: Int by lazy {
        var statusBarHeight = 0
        val resourceId = STBaseApplication.INSTANCE.resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) statusBarHeight =
            STBaseApplication.INSTANCE.resources.getDimensionPixelSize(resourceId)
        statusBarHeight
    }


    /**
     * 使得处于后台的 app 显示到前台
     */
    @JvmStatic
    fun bringAppToFront(activity: Activity?) {
        activity?.let {
            val notificationIntent =
                activity.packageManager.getLaunchIntentForPackage(activity.packageName)
            notificationIntent?.`package` = null // The golden row !!!
            notificationIntent?.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            activity.startActivity(notificationIntent)
            activity.overridePendingTransition(R.anim.st_fade_in, R.anim.st_fade_out)
        }
    }

    /**
     * 复制到剪贴板
     */
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun copyToClipboard(label: String, contentText: String): Boolean {
        return try {
            val cm =
                STBaseApplication.INSTANCE.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText(label, contentText)
            cm.setPrimaryClip(clip)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
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
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun hideSystemUI(window: Window, useImmersiveSticky: Boolean) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or if (useImmersiveSticky) View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY else View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    /**
     * This snippet shows the system bars. It does this by removing all the flags
     * except for the ones that make the content appear under the system bars.
     */
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun showSystemUI(window: Window) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    /**
     * android o appIcon 获取不到, 所以只提供 appBitmap
     */
    @JvmStatic
    fun getAppBitmap(packageName: String = STBaseApplication.INSTANCE.packageName): Bitmap? {
        var drawable: Drawable? = null
        try {
            drawable = STBaseApplication.INSTANCE.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (drawable == STBaseApplication.INSTANCE.packageManager.defaultActivityIcon) drawable =
            null
        return drawable?.toBitmap()
    }

    @JvmStatic
    fun getPackageInfo(packageName: String = STBaseApplication.INSTANCE.packageName): PackageInfo? {
        try {
            return STBaseApplication.INSTANCE.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        } catch (e: Exception) {
            STLogUtil.e("getPackageInfo failure, packageName=$packageName", e)
        }
        return null
    }

    @JvmStatic
    fun getApplicationInfo(packageName: String = STBaseApplication.INSTANCE.packageName): ApplicationInfo? {
        try {
            return STBaseApplication.INSTANCE.packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        } catch (e: Exception) {
            STLogUtil.e("getApplicationInfo failure, packageName=$packageName", e)
        }
        return null
    }
}
