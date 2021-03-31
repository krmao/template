package com.smart.library.util

import android.os.Handler
import android.os.Looper
import androidx.annotation.Keep
import java.util.concurrent.*

//@Keep
@Suppress("unused")
object STThreadUtils {

    @JvmStatic
    var mainHandler: Handler? = null
        get() {
            if (field == null) {
                field = Handler(Looper.getMainLooper())
            }
            return field
        }
        private set

    private var proxy: TaskHandleProxy? = null
    fun setTaskProxy(taskProxy: TaskHandleProxy?) {
        proxy = taskProxy
    }

    @JvmStatic
    fun runOnBackgroundThread(runnable: Runnable) {
        if (proxy != null) {
            proxy?.proxy(runnable)
        } else {
            WorkHolder.workService.execute(runnable)
        }
    }

    @JvmStatic
    fun runOnBackgroundThread(runnable: Runnable, delay: Long) {
        WorkHolder.delayWorkService.schedule(runnable, delay, TimeUnit.MILLISECONDS)
    }

    @JvmStatic
    fun runOnIOThread(runnable: Runnable) {
        IoHolder.ioService.execute(runnable)
    }

    @JvmStatic
    fun runOnTimerThread(runnable: Runnable, delay: Long) {
        WorkHolder.timerService.schedule(runnable, delay, TimeUnit.MILLISECONDS)
    }

    private fun internalRunOnUiThread(runnable: Runnable, delayMillis: Long) {
        mainHandler?.postDelayed(runnable, delayMillis)
    }

    @JvmStatic
    fun post(runnable: Runnable) {
        mainHandler?.post(runnable)
    }

    @JvmStatic
    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        mainHandler?.postDelayed(runnable, delayMillis)
    }

    @JvmStatic
    fun runOnUiThread(runnable: Runnable) {
        internalRunOnUiThread(runnable, 0)
    }

    @JvmStatic
    fun runOnUiThread(runnable: Runnable, delayMillis: Long) {
        internalRunOnUiThread(runnable, delayMillis)
    }

    @JvmStatic
    fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    @JvmStatic
    fun removeCallback(runnable: Runnable) {
        mainHandler?.removeCallbacks(runnable)
    }

    private object IoHolder {
        var ioService: ExecutorService = ThreadPoolExecutor(0, 1024, 5L, TimeUnit.SECONDS, SynchronousQueue())
    }

    private object WorkHolder {
        var workService: ThreadPoolExecutor = ThreadPoolExecutor(10, Int.MAX_VALUE, 3L, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>(), ThreadFactory { r -> Thread(r, "ThreadUtils workService") })
        var delayWorkService: ScheduledExecutorService = Executors.newScheduledThreadPool(2) { r -> Thread(r, "ThreadUtils delayWorkService") }
        var timerService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { r -> Thread(r, "ThreadUtils timerService") }

        init {
            workService.allowCoreThreadTimeOut(true)
        }
    }

    interface TaskHandleProxy {
        fun proxy(runnable: Runnable)
    }
}