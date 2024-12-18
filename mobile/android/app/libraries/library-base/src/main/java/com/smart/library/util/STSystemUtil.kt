package com.smart.library.util

import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.PermissionChecker
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.base.toBitmap
import kotlin.math.sqrt

//@Keep
@Suppress("unused", "MemberVisibilityCanBePrivate", "DEPRECATION", "SpellCheckingInspection")
object STSystemUtil {

    @JvmStatic
    val SDK_INT: Int = Build.VERSION.SDK_INT

    @JvmStatic
    val SDK_RELEASE: String = Build.VERSION.RELEASE

    @JvmStatic
    fun displayMetrics(application: Application? = STInitializer.application()): DisplayMetrics? = application?.resources?.displayMetrics

    @JvmStatic
    fun displayRealMetrics(application: Application? = STInitializer.application()): DisplayMetrics? {
        val defaultDisplay: Display? = (application?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay
        val displayMetrics = DisplayMetrics()
        defaultDisplay?.getRealMetrics(displayMetrics)
        return displayMetrics
    }

    /**
     * 获取当前屏幕尺寸
     */
    @JvmStatic
    @JvmOverloads
    fun getScreenSize(application: Application? = STInitializer.application()): Float {
        val displayMetrics: DisplayMetrics? = displayRealMetrics(application)
        displayMetrics ?: return 0f

        val xdpi: Float = displayMetrics.xdpi
        val ydpi: Float = displayMetrics.ydpi
        val screenWidth: Int = displayMetrics.widthPixels

        val tmpWidth: Float = screenWidth / xdpi * (screenWidth / xdpi)
        val tmpHeight: Float = displayMetrics.heightPixels / ydpi * (screenWidth / xdpi)
        return sqrt(tmpWidth + tmpHeight)
    }

    /**
     * 获取屏幕信息
     *
     * 屏幕适配方案:
     * https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA (头条方案)
     * https://mp.weixin.qq.com/s/X-aL2vb4uEhqnLzU5wjc4Q (smallestWidth方案)
     */
    @JvmStatic
    @JvmOverloads
    fun getScreenInfo(application: Application? = STInitializer.application()): String {
        val width: Int = screenWidth(application)
        val height: Int = screenRealHeight(application)
        val density: Float = displayRealMetrics()?.density ?: 0f
        val size: Float = getScreenSize(application)
        return "(w:${width}px h:${height}px density:$density size=${size}英寸 \nSDK_INT:$SDK_INT SDK_RELEASE:$SDK_RELEASE)"
    }

    @JvmStatic
    @JvmOverloads
    fun screenWidth(application: Application? = STInitializer.application()): Int = displayMetrics(application)?.widthPixels ?: 0

    /**
     * 不包含虚拟导航栏的高度, 有的包含状态栏高度, 有的不包含状态栏高度
     *
     * 三星 S8-G9500 包含状态兰高度
     * 华为 P30 PRO 不包含状态栏高度
     *
     * 假定肯定不包含虚拟导航栏高度
     */
    @JvmStatic
    @JvmOverloads
    fun screenHeight(application: Application? = STInitializer.application()): Int = displayMetrics(application)?.heightPixels ?: 0

    /**
     * 三星 S8-G9500 分辨率2960×1440(存在多种虚拟导航栏)   screenHeight=2076 screenRealHeight=2220(状态栏+内容高度+大虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=144 screenContentHeightExcludeStatusAndNavigationBar=2004
     * 三星 S8-G9500 分辨率2960×1440(存在多种虚拟导航栏)   screenHeight=2175 screenRealHeight=2220(状态栏+内容高度+小虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=144 screenContentHeightExcludeStatusAndNavigationBar=2175
     *
     * VIVO X20A 分辨率2160×1080(存在虚拟导航栏)          screenHeight=2034 screenRealHeight=2160(状态栏+内容高度+虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=126 screenContentHeightExcludeStatusAndNavigationBar=1962
     * 华为 P20 (不存在虚拟导航栏)                        screenHeight=1439 screenRealHeight=1493(状态栏+内容高度)               statusBarHeight=54 navigationBarHeight=82  screenContentHeightExcludeStatusAndNavigationBar=1439
     */
    @JvmStatic
    @JvmOverloads
    fun screenRealHeight(application: Application? = STInitializer.application()): Int {
        return displayRealMetrics(application)?.heightPixels ?: 0
    }

    // 三星S8 G9500 小虚拟导航栏高度 45但是navigationBarHeight=144/大虚拟导航栏高度为navigationBarHeight=144
    private const val S8_G9500_MIN_VIRTUAL_NAVIGATION_BAR_HEIGHT = 45

    /**
     * 受机型不同影响, 比如三星 S8-G9500 存在小/大两种虚拟导航栏
     * 推荐使用 @see getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged
     */
    @JvmStatic
    @Deprecated(message = "受机型不同影响, 比如三星 S8-G9500 存在小/大两种虚拟导航栏")
    fun screenContentHeightExcludeStatusAndNavigationBar(): Int? {
        val screenHeight = screenHeight()
        val screenRealHeight = screenRealHeight()
        val statusBarHeight = statusBarHeight()
        @Suppress("DEPRECATION") val navigationBarHeight = navigationBarHeight()

        val heightDelta: Int = screenRealHeight - screenHeight

        val contentHeight = if ((heightDelta == 0) // 等于 0 说明 screenHeight 一定包含状态栏
            || (heightDelta > 0 && heightDelta == navigationBarHeight) // 相差虚拟导航栏高度, 说明包含状态栏, 因为 screenRealHeight 肯定包含状态栏
            || (heightDelta == S8_G9500_MIN_VIRTUAL_NAVIGATION_BAR_HEIGHT) // 相差不等于状态栏也不等于虚拟导航栏高度, 一种情况: 三星S8 G9500 小虚拟导航栏高度 45但是navigationBarHeight=144/大虚拟导航栏高度为navigationBarHeight=144
        ) {
            screenHeight - statusBarHeight
        } else {
            screenHeight
        }

        return contentHeight
    }

    @JvmStatic
    fun statusBarHeight(): Int {
        val resources: Resources = Resources.getSystem()
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) statusBarHeight = resources.getDimensionPixelSize(resourceId)
        return statusBarHeight
    }

    /**
     * 三星 S8-G9500 不同高度的虚拟导航栏均返回高度 144, 所以该变量是不推荐使用的
     *
     * 三星 S8-G9500 分辨率2960×1440(存在多种虚拟导航栏)   screenHeight=2076 screenRealHeight=2220(状态栏+内容高度+大虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=144 screenContentHeightExcludeStatusAndNavigationBar=2004
     * 三星 S8-G9500 分辨率2960×1440(存在多种虚拟导航栏)   screenHeight=2175 screenRealHeight=2220(状态栏+内容高度+小虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=144 screenContentHeightExcludeStatusAndNavigationBar=2175
     *
     * VIVO X20A 分辨率2160×1080(存在虚拟导航栏)          screenHeight=2034 screenRealHeight=2160(状态栏+内容高度+虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=126 screenContentHeightExcludeStatusAndNavigationBar=1962
     * 华为 P20 (不存在虚拟导航栏)                        screenHeight=1439 screenRealHeight=1493(状态栏+内容高度)               statusBarHeight=54 navigationBarHeight=82  screenContentHeightExcludeStatusAndNavigationBar=1439
     * 华为 P30 PRO 2340*1080(存在虚拟导航栏)            screenHeight=2147 screenRealHeight=2340(状态栏+内容高度+虚拟导航栏高度)   statusBarHeight=75 navigationBarHeight=118  screenContentHeightExcludeStatusAndNavigationBar=2072 navigationBarHeightByLogic=193
     */
    @JvmStatic
    @Deprecated(message = "三星 S8-G9500 不同高度的虚拟导航栏均返回高度 144, 所以该变量是不推荐使用的")
    fun navigationBarHeight(): Int {
        val resources: Resources = Resources.getSystem()
        var navigationBarHeight = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        return navigationBarHeight
    }

    /**
     * 获取 当前虚拟导航栏高度
     * 经过测试 activity onCreate/onStart/onResume/onAttachedToWindow 均失效
     * 唯有 activity onWindowFocusChanged 测试成功, onWindowFocusChanged 切换 app 隐藏/显示都会额外触发一次, 如果切换到设置页面切换虚拟导航栏模式(触发 onConfigurationChanged/onApplyWindowInsetsListener)再回到 app, 会在 onWindowFocusChanged 触发之前触发一次onApplyWindowInsetsListener
     *
     * 1: 在 onWindowFocusChanged 之后执行
     * 2: 受到 虚拟键盘显示/隐藏 影响
     * 3: 受到 虚拟导航栏显示/隐藏 影响
     * 4: top 值受到 fullScreen 以及 状态栏影响, FLAG_FULLSCREEN or windowFullscreen==true 始终为0, 否则受到 MATCH_PARENT/WRAP_CONTENT 影响, 不过这里只获取高度, 不用理会
     *
     * @return null or 当前虚拟导航栏高度
     *
     * @see {https://www.cnblogs.com/ldq2016/p/6835366.html}
     *
     * 在 app 运行期间, 有的手机比如 华为P20屏幕内三键导航, 可以点击按钮显示/隐藏虚拟导航栏(不触发 onConfigurationChanged, 触发 onApplyWindowInsetsListener), 可以通过以下方法监听, 重新获取高度
     * 注意: insets.systemWindowInsetBottom 无效, 华为 P20 无论隐藏/显示虚拟导航栏, 都显示为 0, 需要通过 getVisibleNavigationBarHeightOnWindowFocusChanged 重新计算
     * ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view: View?, insets: WindowInsetsCompat ->
     *     val navigationBarHeight = insets.systemWindowInsetBottom // 无效, 华为 P20 无论隐藏/显示虚拟导航栏, 都显示为 0, 需要通过 getVisibleNavigationBarHeightOnWindowFocusChanged 重新计算
     *     STLogUtil.d("home", "activity: onApplyWindowInsetsListener navigationBarHeight=$navigationBarHeight, view=$view")
     *     STSystemUtil.showSystemInfo(this@HomeTabActivity)
     *
     *     insets
     * }
     */
    fun getVisibleNavigationBarHeightOnWindowFocusChanged(activity: Activity?): Int? {
        val height = getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(activity)
        return if (height != null) screenRealHeight() - height else null
    }

    /**
     * 获取 状态栏高度+内容高度, 不包含虚拟导航栏高度
     * 经过测试 activity onCreate/onStart/onResume/onAttachedToWindow 均失效
     * 唯有 activity onWindowFocusChanged 测试成功, onWindowFocusChanged 切换 app 隐藏/显示都会额外触发一次, 如果切换到设置页面切换虚拟导航栏模式(触发 onConfigurationChanged/onApplyWindowInsetsListener)再回到 app, 会在 onWindowFocusChanged 触发之前触发一次onApplyWindowInsetsListener
     *
     * 1: 在 onWindowFocusChanged 之后执行
     * 2: 受到 虚拟键盘显示/隐藏 影响
     * 3: 受到 虚拟导航栏显示/隐藏 影响
     * 4: top 值受到 fullScreen 以及 状态栏影响, FLAG_FULLSCREEN or windowFullscreen==true 始终为0, 否则受到 MATCH_PARENT/WRAP_CONTENT 影响, 不过这里只获取高度, 不用理会
     *
     * @return null or 状态栏高度+内容高度, 不包含虚拟导航栏高度
     *
     * @see {https://www.cnblogs.com/ldq2016/p/6835366.html}
     *
     * 在 app 运行期间, 有的手机比如 华为P20屏幕内三键导航, 可以点击按钮显示/隐藏虚拟导航栏(不触发 onConfigurationChanged, 触发 onApplyWindowInsetsListener), 可以通过以下方法监听, 重新获取高度
     * 注意: insets.systemWindowInsetBottom 无效, 华为 P20 无论隐藏/显示虚拟导航栏, 都显示为 0, 需要通过 getVisibleNavigationBarHeightOnWindowFocusChanged 重新计算
     * ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view: View?, insets: WindowInsetsCompat ->
     *     val navigationBarHeight = insets.systemWindowInsetBottom // 无效, 华为 P20 无论隐藏/显示虚拟导航栏, 都显示为 0, 需要通过 getVisibleNavigationBarHeightOnWindowFocusChanged 重新计算
     *     STLogUtil.d("home", "activity: onApplyWindowInsetsListener navigationBarHeight=$navigationBarHeight, view=$view")
     *     STSystemUtil.showSystemInfo(this@HomeTabActivity)
     *
     *     insets
     * }
     */
    fun getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(activity: Activity?): Int? {
        val windowsVisibleDisplayFrameSize = Rect()
        activity?.window?.decorView?.getWindowVisibleDisplayFrame(windowsVisibleDisplayFrameSize)
        val height = windowsVisibleDisplayFrameSize.bottom
        return if (height <= (screenHeight() - statusBarHeight() - navigationBarHeight())) return null else height // 杜绝由于软键盘的展开/收起导致的计算错误
    }


    /**
     * If your application is targeting an API level before 23 (Android M) then both:ContextCompat.CheckSelfPermission and Context.checkSelfPermission doesn't work and always returns 0 (PERMISSION_GRANTED). Even if you run the application on Android 6.0 (API 23).
     */
    @JvmStatic
    fun checkSelfPermission(vararg permission: String): Boolean = permission.all {
        val context = STInitializer.application()
        context ?: return false
        PermissionChecker.checkSelfPermission(context, it) == PermissionChecker.PERMISSION_GRANTED
    }

    @JvmStatic
    fun sendKeyDownEventBack(context: Context?) = (context as? Activity)?.onKeyDown(KeyEvent.KEYCODE_BACK, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))

    @JvmStatic
    fun sendKeyDownEvent(context: Context?, keyCode: Int) = (context as? Activity)?.onKeyDown(keyCode, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))

    @JvmStatic
    fun sendKeyUpEvent(context: Context?, keyCode: Int) = (context as? Activity)?.onKeyUp(keyCode, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))

    @JvmStatic
    fun sendKeyUpEventMenu(context: Context?) = (context as? Activity)?.onKeyUp(KeyEvent.KEYCODE_MENU, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MENU))

    @JvmStatic
    fun getPxFromDp(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics(STInitializer.application())) + 0.5f

    @JvmStatic
    fun getPxFromPx(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, displayMetrics(STInitializer.application()))

    @JvmStatic
    fun getDpFromPx(px: Int): Float = px / (displayMetrics(STInitializer.application())?.density ?: 1f)

    @JvmStatic
    fun getPxFromSp(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, displayMetrics(STInitializer.application()))

    @JvmStatic
    fun collapseStatusBar() = STInitializer.application()?.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) // 收起下拉通知栏

    @JvmStatic
    @JvmOverloads
    fun getInputMethodManager(context: Context? = STInitializer.application()): InputMethodManager? {
        return context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    /*
     * 注意: 如果 dialog 包含一个或者多个EditText, 点击外部(canceledOnTouchOutside==true)不会隐藏已经显示的输入法弹框, 且通过点击取消按钮必须先隐藏输入法弹框再延时 dismiss, 因为在 onCancel/onDismiss 中 dialog?.currentFocus?.windowToken 必然已经是 null, 且 inputMethodManager?.isActive 必然是 false
     * ----> canceledOnTouchOutside = false
     * ----> binding.cancelBtn.setOnClickListener {
     *          STSystemUtil.hideKeyboardFromDialogBeforeDismiss(dialog)
     *          binding.root.postDelayed({ dialog?.dismiss() }, 200)
     *       }
     */
    @JvmStatic
    @JvmOverloads
    fun hideKeyboardFromDialogBeforeDismiss(context: Context? = STInitializer.application(), dialog: Dialog? = null) {
        val inputMethodManager: InputMethodManager? = getInputMethodManager(context)
        if (dialog == null) {
            if (inputMethodManager?.isActive == true) {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } else {
            inputMethodManager?.hideSoftInputFromWindow(dialog.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun isKeyboardActive(context: Context? = STInitializer.application(), view: View? = null): Boolean = if (view == null) getInputMethodManager(context)?.isActive == true else getInputMethodManager(context)?.isActive(view) == true

    @JvmStatic
    fun hideKeyboard(view: View?) = view?.let { _view ->
        val inputMethodManager: InputMethodManager? = getInputMethodManager(_view.context)
        if (inputMethodManager?.isActive(view) == true) inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    @JvmStatic
    fun hideKeyboard(activity: Activity?) = hideKeyboard(activity?.currentFocus)

    @JvmStatic
    fun showKeyboard(view: View?) = getInputMethodManager(view?.context)?.showSoftInput(view, InputMethodManager.SHOW_FORCED)

    @JvmStatic
    fun toggleKeyboard(view: View?) = getInputMethodManager(view?.context)?.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)

    @JvmStatic
    fun isAppInstalled(application: Application?, packageName: String): Boolean = getPackageInfo(application, packageName) != null

    @JvmStatic
    fun getAppVersionCode(application: Application?, packageName: String? = application?.packageName): Int = getPackageInfo(application, packageName)?.versionCode ?: 0

    @JvmStatic
    fun getAppVersionName(application: Application?, packageName: String? = application?.packageName): String = getPackageInfo(application, packageName)?.versionName ?: ""

    @JvmStatic
    @JvmOverloads
    fun getAppName(application: Application? = STInitializer.application(), packageName: String? = application?.packageName): String {
        var appName = ""
        try {
            getPackageInfo(application, packageName)?.applicationInfo?.labelRes?.let {
                appName = STInitializer.application()?.resources?.getString(it) ?: ""
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
    fun getAppIcon(application: Application?, packageName: String? = application?.packageName): Int? = getPackageInfo(application, packageName)?.applicationInfo?.icon

    @JvmStatic
    fun getAppMetaData(applicationInfo: ApplicationInfo?, key: String): Any? = applicationInfo?.metaData?.get(key)

    @JvmStatic
    fun getAppMetaDataForInt(applicationInfo: ApplicationInfo?, key: String): Int? = applicationInfo?.metaData?.getInt(key)

    /**
     * sdCardInfo[0]=总大小
     * sdCardInfo[1]=可用大小
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    fun getSDCardMemory(): LongArray {
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

    @Suppress("DEPRECATION")
    @JvmStatic
    fun isSDCardAvailableSize(): Boolean {
        return try {
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                val path = Environment.getExternalStorageDirectory() // 取得sdcard文件路径
                val stat = StatFs(path.path)
                val blockSize = stat.blockSize.toLong()
                val availableBlocks = stat.availableBlocks.toLong()
                availableBlocks * blockSize > 30 * 1024 * 1024
            } else {
                false
            }
        } catch (ex: Exception) {
            false
        }
    }

    @JvmStatic
    fun isSDCardExist(): Boolean = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    @JvmStatic
    fun getSDCardPath(): String? = if (isSDCardExist()) Environment.getExternalStorageDirectory().absolutePath else null

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
            activity.overridePendingTransition(R.anim.st_anim_fade_enter, R.anim.st_anim_fade_exit)
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
                STInitializer.application()?.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
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
    fun getAppBitmap(application: Application?, packageName: String? = application?.packageName): Bitmap? {
        var drawable: Drawable? = null
        try {
            drawable = application?.packageManager?.getApplicationIcon(packageName ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (drawable == application?.packageManager?.defaultActivityIcon) drawable =
            null
        return drawable?.toBitmap()
    }

    @JvmStatic
    fun getPackageInfo(application: Application?, packageName: String?): PackageInfo? {
        try {
            return application?.packageManager?.getPackageInfo(packageName ?: "", PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            STLogUtil.e("getPackageInfo failure, packageName=$packageName", e)
        }
        return null
    }

    @JvmStatic
    fun getApplicationInfo(application: Application?, packageName: String? = application?.packageName): ApplicationInfo? {
        try {
            return application?.packageManager?.getApplicationInfo(packageName ?: "", PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            STLogUtil.e("getApplicationInfo failure, packageName=$packageName", e)
        }
        return null
    }

    @JvmStatic
    @JvmOverloads
    fun isAppOnTestEnvironment(doSomethingOnYes: (() -> Unit)? = null): Boolean {
        if (STInitializer.debug()) {
            doSomethingOnYes?.invoke()
            return true
        }
        return false
    }

    /**
     * 测量一行文本宽度, 多行不起作用
     * textStyle(normal/bold/italic) 影响宽度计算
     *
     * Paint.measureText和Paint.getTextBounds返回的宽度之间略有差异
     * > measureText 返回的宽度包括字形的 advanceX 值，该值填充字符串的开始和结尾, 即包含字形所需的空间
     * > getTextBounds 返回的 Rect 宽度没有此填充，因为边界是紧密包裹文本的Rect, 即所需要的最小尺寸
     *
     * @see {@link https://stackoverflow.com/a/42091739/4348530}
     *
     * @example STSystemUtil.measuringTextWidth(text, 16f.toPxFromDp(), typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
     */
    @Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
    fun measuringTextWidth(text: String?, textSizePx: Float, typeface: Typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)): Float {
        if (text == null || text.isEmpty()) return 0f

        val paint = Paint()
        paint.isAntiAlias = true
        paint.textSize = textSizePx
        paint.typeface = typeface
        paint.color = Color.BLACK

        var widthByGetTextWidths = 0f

        val textLength: Int = text.length
        val textWidths = FloatArray(textLength)

        paint.getTextWidths(text, textWidths)

        for (index: Int in 0 until textLength) {
            widthByGetTextWidths += Math.ceil(textWidths[index].toDouble()).toFloat()
        }

        STLogUtil.w("[SYS] measuringTextWidth widthByGetTextWidths=$widthByGetTextWidths, widthByMeasureText=${paint.measureText(text)}")
        return widthByGetTextWidths
    }

    /**
     * 测量一行或者多行文本的高度, 每个字符宽度相加
     * textStyle(normal/bold/italic) 不影响高度计算
     *
     * 务必设置以下参数
     *
     * android:includeFontPadding="false"
     * android:textSize="16dp"
     * android:lineHeight="16dp"
     * android:lineSpacingExtra="0dp"
     * android:lineSpacingMultiplier="1"
     * android:textStyle="normal"
     * android:layout_margin="0dp"
     * android:padding="0dp"
     *
     * option:
     * android:fontFamily="sans-serif"
     * textPaint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
     */
    @Deprecated(message = "不同机型不精确, 获取到近似值后最好直接 textView.setLayoutParams 这样就精准了")
    fun measuringMultiLineTextHeight(text: String?, textSizePx: Float, widthPx: Float): Float {
        if (text == null || text.isEmpty()) return 0f

        val textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textSize = textSizePx
        textPaint.color = Color.BLACK
        // textPaint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)

        val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        val spacingMultiplier = 1f
        val spacingAddition = 0f
        val includePadding = false

        val staticLayout = StaticLayout(text, textPaint, widthPx.toInt(), alignment, spacingMultiplier, spacingAddition, includePadding)
        val heightByStaticLayoutOrigin: Float = staticLayout.height.toFloat()
        var heightByStaticLayout: Float = heightByStaticLayoutOrigin

        when {
            IS_HUAWEI -> { // 华为手机存在偏差
                heightByStaticLayout = heightByStaticLayoutOrigin - (if (staticLayout.lineCount <= 1) 0f else (0 + (staticLayout.lineCount - 3f) * 3f))
            }
            IS_SAMSUNG -> { // 三星手机存在偏差
                // SM-A9080 从第二行开始逐行比真实值缺少 17 22 27 32
                heightByStaticLayout = heightByStaticLayoutOrigin + (if (staticLayout.lineCount <= 1) 0f else (12 + (staticLayout.lineCount - 1f) * 5f))
            }
            IS_ONEPLUS -> { // 一加手机存在偏差
                // ONEPLUS A5010 从第二行开始逐行比真实值增加 1 5 9 13 17
                heightByStaticLayout = heightByStaticLayoutOrigin - (if (staticLayout.lineCount <= 1) 0f else (1 + (staticLayout.lineCount - 2f) * 4f))
            }
        }

        //region singleLineHeightByBounds
        val bounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, bounds)
        val singleLineHeightByBounds = bounds.height()
        //endregion

        //region singleLineHeightByAverage
        val fm: Paint.FontMetrics = textPaint.fontMetrics
        val singleLineHeightByFontMetrics: Float = fm.descent - fm.ascent
        val singleLineHeightByAverage = heightByStaticLayout / staticLayout.lineCount.toFloat()
        //endregion

        //region heightByCalculate
        val singleLineHeightByCalculate = round(textPaint.getFontMetricsInt(null) * spacingMultiplier + spacingAddition)
        val heightByCalculate = singleLineHeightByCalculate * staticLayout.lineCount
        //endregion

        STLogUtil.w("[SYS] measuringMultiLineTextHeight heightByStaticLayout=$heightByStaticLayout, heightByStaticLayoutOrigin=$heightByStaticLayoutOrigin, lineCount=${staticLayout.lineCount}, singleLineHeightByFontMetrics=$singleLineHeightByFontMetrics, singleLineHeightByBounds=$singleLineHeightByBounds, singleLineHeightByAverage=$singleLineHeightByAverage, singleLineHeightByCalculate=$singleLineHeightByCalculate, heightByCalculate=$heightByCalculate")
        return heightByStaticLayout
    }

    /**
     * 设置 activity full screen, 并且扩展布局显示到刘海区域
     */
    @JvmStatic
    fun setActivityFullScreenAndExpandLayout(activity: Activity) {
        val window: Window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // 设置页面全屏显示 务必继承 FragmentActivity
        if (Build.VERSION.SDK_INT >= 28) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val layoutParams = window.attributes
            val layoutParamsClass: Class<*> = layoutParams.javaClass
            try {
                val field = layoutParamsClass.getDeclaredField("layoutInDisplayCutoutMode")
                field.isAccessible = true
                field[layoutParams] = 1
                // 设置页面延伸到刘海区显示
                window.attributes = layoutParams
            } catch (e: Exception) {
                STLogUtil.e("splash", e)
            }
        }
    }

    @JvmStatic
    @JvmOverloads
    fun closeSystemNotificationPanel(context: Context? = STInitializer.application()) {
        context?.sendBroadcast(STIntentUtil.getBroadcastReceiverIntentForCloseSystemNotificationPanel())
    }

    private fun round(value: Float): Int {
        val lx = (value * (65536 * 256f)).toLong()
        return (lx + 0x800000 shr 24).toInt()
    }

    /**
     * 生产者; 制造者; 生产商;
     * huawei/meizu/xiaomi/vivo
     */
    @JvmStatic
    val MANUFACTURER: String by lazy { Build.MANUFACTURER }

    @JvmStatic
    val IS_HUAWEI: Boolean by lazy { "huawei".equals(MANUFACTURER, ignoreCase = true) }

    @JvmStatic
    val IS_MEIZU: Boolean by lazy { "meizu".equals(MANUFACTURER, ignoreCase = true) }

    @JvmStatic
    val IS_XIAOMI: Boolean by lazy { "xiaomi".equals(MANUFACTURER, ignoreCase = true) }

    @JvmStatic
    val IS_VIVO: Boolean by lazy { "vivo".equals(MANUFACTURER, ignoreCase = true) }

    @JvmStatic
    val IS_SAMSUNG: Boolean by lazy { "samsung".equals(MANUFACTURER, ignoreCase = true) }

    @JvmStatic
    val IS_ONEPLUS: Boolean by lazy { "oneplus".equals(MANUFACTURER, ignoreCase = true) }

    fun showSystemInfo(activity: Activity?) {
        STLogUtil.sync {
            STLogUtil.d("[SYS] ======================================================================================")
            STLogUtil.d("[SYS] appName=${getAppName(STInitializer.application())}, versionName=${getAppVersionName(STInitializer.application())}, versionCode=${getAppVersionCode(STInitializer.application())}, SDK_INT=$SDK_INT")
            STLogUtil.d("[SYS] MANUFACTURER=${MANUFACTURER}, IS_HUAWEI=$IS_HUAWEI, IS_MEIZU=$IS_MEIZU, IS_XIAOMI=$IS_XIAOMI, IS_VIVO=$IS_VIVO, IS_SAMSUNG=$IS_SAMSUNG, IS_ONEPLUS=$IS_ONEPLUS")
            STLogUtil.d("[SYS] screenWidth=${screenWidth()}, screenHeight=${screenHeight()}, screenRealHeight=${screenRealHeight()}, screenContentHeightExcludeStatusAndNavigationBar=${screenContentHeightExcludeStatusAndNavigationBar()}")
            STLogUtil.d("[SYS] statusBarHeight=${statusBarHeight()}, navigationBarHeight=${navigationBarHeight()}")
            STLogUtil.d("[SYS] getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged=${getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(activity)}")
            STLogUtil.d("[SYS] getVisibleNavigationBarHeightOnWindowFocusChanged=${getVisibleNavigationBarHeightOnWindowFocusChanged(activity)}")
            STLogUtil.d("[SYS] ======================================================================================")
        }
    }
}
