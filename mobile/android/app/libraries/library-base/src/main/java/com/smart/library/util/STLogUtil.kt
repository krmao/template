package com.smart.library.util

import android.text.TextUtils
import android.util.Log
import com.smart.library.STInitializer
import com.smart.library.util.cache.STCacheManager
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 具有模块控制的日志系统
 *
 * <p>
 *
 * 0:范围控制
 * 1:打印格式化后的 json
 * 2:写日志文件到 sdcard 缓存目录
 *
 *      STLogUtil.open()        使用之前打开开关[优先级是以包范围越大为主控制]
 *      STLogUtil.openPage()    页面级别的控制，在所在模块级别开启的情况下才起作用，开启本页面日志
 *
 *          STLogUtil.v()
 *          STLogUtil.d()
 *          STLogUtil.e()
 *
 *      STLogUtil.closePage()   页面级别的控制，在所在模块级别开启的情况下才起作用，关闭本页面日志
 *      STLogUtil.close()       使用之后关闭开关
 *
 * </p>
 */
/**
 * @deprerate
 *
 *      警告 当使用 clazz?.canonicalName 的时候
 *      如果在一个类的 complain object 里面的静态 成员变量 上加 监听 Delegates.observable ,则在 华为 PLK-UL00 型号的手机报出
 *      java.lang.IncompatibleClassChangeError
 *      at java.lang.Class.getCanonicalName
 *      华为荣耀6P 则没有任何问题
 *
 *      为了保险起见,不用 Delegates.observable监听 且不用 getCanonicalName,
 *      改用 set 方法 以及 getName 方法
 *
 */
@Suppress("unused", "MemberVisibilityCan", "MemberVisibilityCan", "MemberVisibilityCanPrivate")
object STLogUtil {
    private val LINE_SEPARATOR = System.getProperty("line.separator") ?: "\n"
    private const val PAGE_SUFFIX = "#_PAGE_#"
    private val MODULE_MAP_ASC by lazy { TreeMap<String, Boolean>(Comparator<String> { o1, o2 -> o2.compareTo(o1) }) }//升序,大模块优先级高

    @JvmStatic
    var debug = STInitializer.debug()

    private fun getNewLogName(): String = "log_" + SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date(System.currentTimeMillis())) + ".txt"

    @JvmStatic
    @JvmOverloads
    fun async(debugLog: Boolean = debug, doLog: () -> Unit): Disposable? {
        return if (debugLog) {
            Flowable.fromCallable { doLog() }.subscribeOn(Schedulers.io()).subscribe()
        } else {
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun sync(debugLog: Boolean = debug, doLog: () -> Unit) {
        if (debugLog) {
            doLog()
        }
    }

    @JvmStatic
    fun open() {
        if (debug) {
            MODULE_MAP_ASC[getLocationPackageName(getStackTraceElement())] = true
        }
    }

    @JvmStatic
    fun openPage() {
        if (debug) {
            val locationClassName = getLocationClassName(getStackTraceElement()) + PAGE_SUFFIX
            MODULE_MAP_ASC[locationClassName] = true
        }
    }

    @JvmStatic
    fun close() {
        if (debug) {
            MODULE_MAP_ASC[getLocationPackageName(getStackTraceElement())] = false
        }
    }

    @JvmStatic
    fun closePage() {
        if (debug) {
            val locationClassName = getLocationClassName(getStackTraceElement()) + PAGE_SUFFIX
            MODULE_MAP_ASC[locationClassName] = false
        }
    }

    @JvmStatic
    fun isModuleEnable(stackTraceElement: StackTraceElement?): Boolean {
        var isModuleEnable = debug
        if (debug) {
            val locationClassName = getLocationClassName(stackTraceElement) + PAGE_SUFFIX
            for ((key, value) in MODULE_MAP_ASC) {
                if (key.endsWith(PAGE_SUFFIX)) {
                    if (locationClassName == key) {
                        isModuleEnable = value
                        break
                    }
                } else if (locationClassName.contains(key)) {
                    isModuleEnable = value
                    if (!isModuleEnable)
                        break
                }
                //System.out.println("isPage:${key.endsWith(PAGE_SUFFIX)},\tlocationClassName:\t$locationClassName == key:$key\t=$isModuleEnable")
            }
            //System.out.println("isModuleEnable:${isModuleEnable}\tASC:" + MODULE_MAP_ASC)
        }
        return isModuleEnable
    }

    @JvmStatic
    fun v(msg: String, throwable: Throwable? = null) = v(null, msg, throwable)

    @JvmStatic
    fun v(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.VERBOSE, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    @JvmStatic
    fun d(msg: String, throwable: Throwable? = null) = d(null, msg, throwable)

    @JvmStatic
    fun d(tag: String? = null, msg: String) = d(tag, msg, null)

    @JvmStatic
    fun d(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.DEBUG, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    @JvmStatic
    fun i(msg: String, throwable: Throwable? = null) = i(null, msg, throwable)

    @JvmStatic
    fun i(tag: String? = null, msg: String) = i(tag, msg, null)

    @JvmStatic
    fun i(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.INFO, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    @JvmStatic
    fun w(msg: String, throwable: Throwable? = null) = w(null, msg, throwable)

    @JvmStatic
    fun w(tag: String? = null, msg: String) = w(tag, msg, null)

    @JvmStatic
    fun w(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.WARN, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    @JvmStatic
    fun e(msg: String, throwable: Throwable? = null) = e(null, msg, throwable)

    @JvmStatic
    fun e(tag: String? = null, msg: String) = e(tag, msg, null)

    @JvmStatic
    fun e(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.ERROR, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    @JvmStatic
    fun getCacheDir(): File {
        val cacheDir = File(STCacheManager.getCacheDir(), if (debug) STInitializer.configName?.appLogDirName ?: "log" else STChecksumUtil.genMD5ForCharSequence(STInitializer.config?.configName?.appLogDirName ?: "log"))
        if (!cacheDir.exists())
            cacheDir.mkdirs()
        return cacheDir
    }

    @JvmStatic
    fun write(msg: String, throwable: Throwable? = null) = write(null, msg, throwable)

    @JvmStatic
    fun write(msg: String, isForce: Boolean? = false) = write(null, msg, null, isForce)

    @JvmStatic
    fun write(tag: String? = null, msg: String, throwable: Throwable? = null, isForce: Boolean? = false) {
        if (debug || isForce == true) {
            STFileUtil.writeTextToFile(p(Log.VERBOSE, tag, msg, throwable), throwable, File(getCacheDir(), getNewLogName()))
        }
    }

    @JvmStatic
    fun getStackTraceElement(): StackTraceElement? {
        val className = STLogUtil::class.java.name
        var found = false
        for (trace in Thread.currentThread().stackTrace) {
            try {
                if (found) {
                    if (!trace.className.startsWith(className)) {
                        return trace
                    }
                } else if (trace.className.startsWith(className)) {
                    found = true
                }
            } catch (ignored: ClassNotFoundException) {
            }
        }
        return null
    }

    @JvmStatic
    fun getLocation(stackTraceElement: StackTraceElement?): String = "[" + getLocationClassName(stackTraceElement) + ":" + getLocationMethodName(stackTraceElement) + ":" + getLocationLineNumber(stackTraceElement) + "]"

    @JvmStatic
    fun getLocationPackageName(stackTraceElement: StackTraceElement?): String = Class.forName(stackTraceElement?.className ?: "").`package`?.name ?: ""

    @JvmStatic
    fun getLocationMethodName(stackTraceElement: StackTraceElement?): String = stackTraceElement?.methodName ?: ""

    @JvmStatic
    fun getLocationLineNumber(stackTraceElement: StackTraceElement?): Int = stackTraceElement?.lineNumber ?: 0

    @JvmStatic
    fun getLocationClassName(stackTraceElement: StackTraceElement?): String = stackTraceElement?.className ?: ""

    /**
     * json support
     */
    @JvmStatic
    fun j(msg: String) = j(Log.DEBUG, null, msg)

    @JvmStatic
    fun j(tag: String? = null, msg: String) = j(Log.DEBUG, tag, msg)

    @JvmStatic
    fun j(level: Int, tag: String? = null, msg: String) {
        if (debug) {
            if (TextUtils.isEmpty(msg)) {
                p(level, tag, msg)
            } else {
                val message: String
                message = try {
                    when {
                        msg.startsWith("{") -> {
                            val jsonObject = JSONObject(msg)
                            jsonObject.toString(4)
                        }
                        msg.startsWith("[") -> {
                            val jsonArray = JSONArray(msg)
                            jsonArray.toString(4)
                        }
                        else -> msg
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    msg
                }
                p(level, tag, "╔═══════════════════════════════════════════════════════════════════════════════════════", false)
                val lines = message.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (line in lines) {
                    p(level, tag, "║ $line", false)
                }
                p(level, tag, "╚═══════════════════════════════════════════════════════════════════════════════════════", false)
            }
        }
    }

    @JvmStatic
    fun p(level: Int, tag: String? = null, message: String, throwable: Throwable? = null): String =
        p(level, tag, message, throwable, null)

    @JvmStatic
    fun p(level: Int, tag: String? = null, message: String, throwable: Throwable? = null, stackTraceElement: StackTraceElement? = null): String =
        p(level, tag, message, false, throwable, stackTraceElement)

    @JvmStatic
    fun p(level: Int, tag: String? = null, message: String, appendLocation: Boolean = true, throwable: Throwable? = null, stackTraceElement: StackTraceElement? = null): String {
        var tmpTag = tag
        var tmpMsg = message
        if (debug) {
            val traceElement = stackTraceElement ?: getStackTraceElement()
            tmpTag = tmpTag ?: getLocationClassName(traceElement)
            if (appendLocation && !TextUtils.isEmpty(tmpMsg)) {
                tmpMsg += getLocation(traceElement)
            }
            when (level) {
                Log.VERBOSE -> Log.v(tmpTag, tmpMsg, throwable)
                Log.DEBUG -> Log.d(tmpTag, tmpMsg, throwable)
                Log.INFO -> Log.i(tmpTag, tmpMsg, throwable)
                Log.WARN -> Log.w(tmpTag, tmpMsg, throwable)
                Log.ERROR -> Log.e(tmpTag, tmpMsg, throwable)
                Log.ASSERT -> Log.wtf(tmpTag, tmpMsg, throwable)
                else -> Log.v(tmpTag, tmpMsg, throwable)
            }

        }
        return "$tmpTag:$tmpMsg"
    }
}
