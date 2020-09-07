package com.smart.library.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
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

    /**
     * 不包含虚拟导航栏的高度, 有的包含状态栏高度, 有的不包含状态栏高度
     *
     * 三星 S8-G9500 包含状态兰高度
     * 华为 P30 PRO 不包含状态栏高度
     *
     * 假定肯定不包含虚拟导航栏高度
     */
    @JvmStatic
    val screenHeight: Int by lazy { STBaseApplication.INSTANCE.resources.displayMetrics.heightPixels }

    /**
     * 三星 S8-G9500 分辨率2960×1440(存在多种虚拟导航栏)   screenHeight=2076 screenRealHeight=2220(状态栏+内容高度+大虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=144 screenContentHeightExcludeStatusAndNavigationBar=2004
     * 三星 S8-G9500 分辨率2960×1440(存在多种虚拟导航栏)   screenHeight=2175 screenRealHeight=2220(状态栏+内容高度+小虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=144 screenContentHeightExcludeStatusAndNavigationBar=2175
     *
     * VIVO X20A 分辨率2160×1080(存在虚拟导航栏)          screenHeight=2034 screenRealHeight=2160(状态栏+内容高度+虚拟导航栏高度)  statusBarHeight=72 navigationBarHeight=126 screenContentHeightExcludeStatusAndNavigationBar=1962
     * 华为 P20 (不存在虚拟导航栏)                        screenHeight=1439 screenRealHeight=1493(状态栏+内容高度)               statusBarHeight=54 navigationBarHeight=82  screenContentHeightExcludeStatusAndNavigationBar=1439
     */
    @JvmStatic
    val screenRealHeight: Int by lazy {
        val defaultDisplay: Display? = (STBaseApplication.INSTANCE.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay
        val displayMetrics = DisplayMetrics()
        defaultDisplay?.getRealMetrics(displayMetrics)
        displayMetrics.heightPixels
    }

    // 三星S8 G9500 小虚拟导航栏高度 45但是navigationBarHeight=144/大虚拟导航栏高度为navigationBarHeight=144
    private const val S8_G9500_MIN_VIRTUAL_NAVIGATION_BAR_HEIGHT = 45

    /**
     * 受机型不同影响, 比如三星 S8-G9500 存在小/大两种虚拟导航栏
     * 推荐使用 @see getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged
     */
    @JvmStatic
    @Deprecated(message = "受机型不同影响, 比如三星 S8-G9500 存在小/大两种虚拟导航栏")
    val screenContentHeightExcludeStatusAndNavigationBar: Int by lazy {
        val screenHeight = screenHeight
        val screenRealHeight = screenRealHeight
        val statusBarHeight = statusBarHeight
        @Suppress("DEPRECATION") val navigationBarHeight = navigationBarHeight

        val heightDelta: Int = screenRealHeight - screenHeight

        val contentHeight = if ((heightDelta == 0) // 等于 0 说明 screenHeight 一定包含状态栏
            || (heightDelta > 0 && heightDelta == navigationBarHeight) // 相差虚拟导航栏高度, 说明包含状态栏, 因为 screenRealHeight 肯定包含状态栏
            || (heightDelta == S8_G9500_MIN_VIRTUAL_NAVIGATION_BAR_HEIGHT) // 相差不等于状态栏也不等于虚拟导航栏高度, 一种情况: 三星S8 G9500 小虚拟导航栏高度 45但是navigationBarHeight=144/大虚拟导航栏高度为navigationBarHeight=144
        ) {
            screenHeight - statusBarHeight
        } else {
            screenHeight
        }

        contentHeight
    }

    @JvmStatic
    val statusBarHeight: Int by lazy {
        var statusBarHeight = 0
        val resourceId = STBaseApplication.INSTANCE.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) statusBarHeight = STBaseApplication.INSTANCE.resources.getDimensionPixelSize(resourceId)
        statusBarHeight
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
    val navigationBarHeight: Int by lazy {
        var navigationBarHeight = 0
        val resourceId = STBaseApplication.INSTANCE.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) navigationBarHeight = STBaseApplication.INSTANCE.resources.getDimensionPixelSize(resourceId)
        navigationBarHeight
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
        return if (height != null) screenRealHeight - height else null
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
        return if (height <= (screenHeight - statusBarHeight - navigationBarHeight)) return null else height // 杜绝由于软键盘的展开/收起导致的计算错误
    }


    /**
     * If your application is targeting an API level before 23 (Android M) then both:ContextCompat.CheckSelfPermission and Context.checkSelfPermission doesn't work and always returns 0 (PERMISSION_GRANTED). Even if you run the application on Android 6.0 (API 23).
     */
    @JvmStatic
    fun checkSelfPermission(vararg permission: String): Boolean = permission.all {
        PermissionChecker.checkSelfPermission(STBaseApplication.INSTANCE, it) == PermissionChecker.PERMISSION_GRANTED
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
    fun getPxFromDp(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics) + 0.5f

    @JvmStatic
    fun getPxFromPx(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, displayMetrics)

    @JvmStatic
    fun getDpFromPx(px: Int): Float = px / displayMetrics.density

    @JvmStatic
    fun getPxFromSp(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, displayMetrics)

    @JvmStatic
    fun collapseStatusBar() = STBaseApplication.INSTANCE.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) // 收起下拉通知栏

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
    fun showKeyboard(view: View?) = (view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.showSoftInput(view, InputMethodManager.SHOW_FORCED)

    @JvmStatic
    fun toggleKeyboard(view: View?) = (view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)

    @JvmStatic
    fun isAppInstalled(packageName: String): Boolean = getPackageInfo(packageName) != null

    @JvmStatic
    fun getAppVersionCode(packageName: String = STBaseApplication.INSTANCE.packageName): Int = getPackageInfo(packageName)?.versionCode ?: 0

    @JvmStatic
    fun getAppVersionName(packageName: String = STBaseApplication.INSTANCE.packageName): String = getPackageInfo(packageName)?.versionName ?: ""

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
    fun getAppIcon(packageName: String = STBaseApplication.INSTANCE.packageName): Int? = getPackageInfo(packageName)?.applicationInfo?.icon

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

    @JvmStatic
    @JvmOverloads
    fun isAppOnTestEnvironment(doSomethingOnYes: (() -> Unit)? = null): Boolean {
        if (STBaseApplication.DEBUG) {
            doSomethingOnYes?.invoke()
            return true
        }
        return false
    }

    /**
     * 测量文本宽度
     *
     * Paint.measureText和Paint.getTextBounds返回的宽度之间略有差异
     * > measureText 返回的宽度包括字形的 advanceX 值，该值填充字符串的开始和结尾, 即包含字形所需的空间
     * > getTextBounds 返回的 Rect 宽度没有此填充，因为边界是紧密包裹文本的Rect, 即所需要的最小尺寸
     *
     * @see {@link https://stackoverflow.com/a/42091739/4348530}
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
     * 测量多行文本的高度, 每个字符宽度相加
     */
    fun measuringMultiLineTextHeight(text: String?, textSizePx: Float, widthPx: Float, typeface: Typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)): Float {
        if (text == null || text.isEmpty()) return 0f

        val textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textSize = textSizePx
        textPaint.typeface = typeface
        textPaint.color = Color.BLACK

        val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        val spacingMultiplier = 1f
        val spacingAddition = 0f
        val includePadding = false

        val staticLayout = StaticLayout(text, textPaint, widthPx.toInt(), alignment, spacingMultiplier, spacingAddition, includePadding)

        val heightByStaticLayout: Float = staticLayout.height.toFloat()
        STLogUtil.w("[SYS] measuringMultiLineTextHeight heightByStaticLayout=$heightByStaticLayout")
        return heightByStaticLayout
    }

    fun showSystemInfo(activity: Activity?) {
        STLogUtil.sync {
            STLogUtil.d("[SYS] ======================================================================================")
            STLogUtil.d("[SYS] appName=$appName, versionName=$versionName, versionCode=$versionCode, SDK_INT=$SDK_INT")
            STLogUtil.d("[SYS] screenWidth=$screenWidth, screenHeight=$screenHeight, screenRealHeight=$screenRealHeight, screenContentHeightExcludeStatusAndNavigationBar=$screenContentHeightExcludeStatusAndNavigationBar")
            STLogUtil.d("[SYS] statusBarHeight=$statusBarHeight, navigationBarHeight=$navigationBarHeight")
            STLogUtil.d("[SYS] getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged=${getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(activity)}")
            STLogUtil.d("[SYS] getVisibleNavigationBarHeightOnWindowFocusChanged=${getVisibleNavigationBarHeightOnWindowFocusChanged(activity)}")
            STLogUtil.d("[SYS] ======================================================================================")
        }
    }
}
