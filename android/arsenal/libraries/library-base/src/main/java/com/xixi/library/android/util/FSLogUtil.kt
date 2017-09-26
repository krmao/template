package com.xixi.library.android.util

import android.text.TextUtils
import android.util.Log
import com.xixi.library.android.base.FSBaseApplication
import com.xixi.library.android.base.FSConfig
import com.xixi.library.android.util.cache.FSCacheManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
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
 *      FSLogUtil.open()        使用之前打开开关[优先级是以包范围越大为主控制]
 *      FSLogUtil.openPage()    页面级别的控制，在所在模块级别开启的情况下才起作用，开启本页面日志
 *
 *          FSLogUtil.v()
 *          FSLogUtil.d()
 *          FSLogUtil.e()
 *
 *      FSLogUtil.closePage()   页面级别的控制，在所在模块级别开启的情况下才起作用，关闭本页面日志
 *      FSLogUtil.close()       使用之后关闭开关
 *
 * </p>
 */
@Suppress("unused")
object FSLogUtil {
    private val LINE_SEPARATOR = System.getProperty("line.separator") ?: "\n"
    private val PAGE_SUFFIX = "#_PAGE_#"

    private val MODULE_MAP_ASC by lazy { TreeMap<String, Boolean>(Comparator<String> { o1, o2 -> o2.compareTo(o1) }) }//升序,大模块优先级高
    private var debug = FSBaseApplication.DEBUG

    fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    fun open() {
        if (debug) {
            MODULE_MAP_ASC.put(getLocationPackageName(getStackTraceElement()), true)
        }
    }

    fun openPage() {
        if (debug) {
            val locationClassName = getLocationClassName(getStackTraceElement()) + PAGE_SUFFIX
            MODULE_MAP_ASC.put(locationClassName, true)
        }
    }

    fun close() {
        if (debug) {
            MODULE_MAP_ASC.put(getLocationPackageName(getStackTraceElement()), false)
        }
    }

    fun closePage() {
        if (debug) {
            val locationClassName = getLocationClassName(getStackTraceElement()) + PAGE_SUFFIX
            MODULE_MAP_ASC.put(locationClassName, false)
        }
    }

    private fun isModuleEnable(stackTraceElement: StackTraceElement?): Boolean {
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

    fun v(msg: String, throwable: Throwable? = null) {
        v(null, msg, throwable)
    }

    fun v(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.VERBOSE, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    fun d(msg: String, throwable: Throwable? = null) {
        d(null, msg, throwable)
    }

    fun d(tag: String? = null, msg: String) {
        d(tag, msg, null)
    }

    fun d(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.DEBUG, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    fun i(msg: String, throwable: Throwable? = null) {
        i(null, msg, throwable)
    }

    fun i(tag: String? = null, msg: String) {
        i(tag, msg, null)
    }

    fun i(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.INFO, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    fun w(msg: String, throwable: Throwable? = null) {
        w(null, msg, throwable)
    }

    fun w(tag: String? = null, msg: String) {
        w(tag, msg, null)
    }

    fun w(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.WARN, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    fun e(msg: String, throwable: Throwable? = null) {
        e(null, msg, throwable)
    }

    fun e(tag: String? = null, msg: String) {
        e(tag, msg, null)
    }

    fun e(tag: String? = null, msg: String, throwable: Throwable? = null) {
        if (debug) {
            val stackTraceElement: StackTraceElement? = getStackTraceElement()
            if (isModuleEnable(stackTraceElement)) {
                p(Log.ERROR, tag, msg, throwable, stackTraceElement)
            }
        }
    }

    fun getCacheDir(): File {
        val cacheDir = File(FSCacheManager.getCacheDir(), if (debug) FSConfig.NAME_LOG_DIR else FSChecksumUtil.genMD5Checksum(FSConfig.NAME_LOG_DIR))
        if (!cacheDir.exists())
            cacheDir.mkdirs()
        return cacheDir
    }

    fun write(msg: String, throwable: Throwable? = null) {
        write(null, msg, throwable)
    }

    fun write(msg: String, isForce: Boolean? = false) {
        write(null, msg, null, isForce)
    }

    fun write(tag: String? = null, msg: String, throwable: Throwable? = null, isForce: Boolean? = false) {
        if (debug || isForce ?: false) {
            FSFileUtil.writeTextToFile(p(Log.VERBOSE, tag, msg, throwable), throwable, File(getCacheDir(), FSConfig.NAME_NEW_LOG))
        }
    }

    private fun getStackTraceElement(): StackTraceElement? {
        val className = FSLogUtil::class.java.name
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

    private fun getLocation(stackTraceElement: StackTraceElement?): String {
        return "[" + getLocationClassName(stackTraceElement) + ":" + getLocationMethodName(stackTraceElement) + ":" + getLocationLineNumber(stackTraceElement) + "]"
    }

    private fun getLocationPackageName(stackTraceElement: StackTraceElement?): String {
        return Class.forName(stackTraceElement?.className).`package`.name ?: ""
    }

    private fun getLocationMethodName(stackTraceElement: StackTraceElement?): String {
        return stackTraceElement?.methodName ?: ""
    }

    private fun getLocationLineNumber(stackTraceElement: StackTraceElement?): Int {
        return stackTraceElement?.lineNumber ?: 0
    }

    private fun getLocationClassName(stackTraceElement: StackTraceElement?): String {
        return stackTraceElement?.className ?: ""
    }

    /**
     * json support
     */
    fun j(msg: String) {
        j(Log.DEBUG, null, msg)
    }

    fun j(tag: String? = null, msg: String) {
        j(Log.DEBUG, tag, msg)
    }

    fun j(level: Int, tag: String? = null, msg: String) {
        if (debug) {
            if (TextUtils.isEmpty(msg)) {
                p(level, tag, msg)
            } else {
                var message: String
                try {
                    if (msg.startsWith("{")) {
                        val jsonObject = JSONObject(msg)
                        message = jsonObject.toString(4)
                    } else if (msg.startsWith("[")) {
                        val jsonArray = JSONArray(msg)
                        message = jsonArray.toString(4)
                    } else {
                        message = msg
                    }
                } catch (e: JSONException) {
                    message = msg
                }
                p(level, tag, "╔═══════════════════════════════════════════════════════════════════════════════════════", false)
                val lines = message.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (line in lines) {
                    p(level, tag, "║ " + line, false)
                }
                p(level, tag, "╚═══════════════════════════════════════════════════════════════════════════════════════", false)
            }
        }
    }

    fun p(level: Int, tag: String? = null, message: String, throwable: Throwable? = null): String {
        return p(level, tag, message, throwable, null)
    }

    fun p(level: Int, tag: String? = null, message: String, throwable: Throwable? = null, stackTraceElement: StackTraceElement? = null): String {
        return p(level, tag, message, true, throwable, stackTraceElement)
    }

    fun p(level: Int, tag: String? = null, message: String, appendLocation: Boolean = true, throwable: Throwable? = null, stackTraceElement: StackTraceElement? = null): String {
        var _tag = tag
        var _msg = message
        if (debug) {
            val _stackTraceElement = stackTraceElement ?: getStackTraceElement()
            _tag = _tag ?: getLocationClassName(_stackTraceElement)
            if (appendLocation && !TextUtils.isEmpty(_msg)) {
                _msg += getLocation(_stackTraceElement)
            }
            when (level) {
                Log.VERBOSE -> Log.v(_tag, _msg, throwable)
                Log.DEBUG -> Log.d(_tag, _msg, throwable)
                Log.INFO -> Log.i(_tag, _msg, throwable)
                Log.WARN -> Log.w(_tag, _msg, throwable)
                Log.ERROR -> Log.e(_tag, _msg, throwable)
                Log.ASSERT -> Log.wtf(_tag, _msg, throwable)
                else -> Log.v(_tag, _msg, throwable)
            }

        }
        return _tag + ":" + _msg
    }
}
