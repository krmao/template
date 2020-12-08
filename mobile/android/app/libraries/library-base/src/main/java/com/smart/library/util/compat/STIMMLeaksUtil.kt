@file:Suppress("unused")

package com.smart.library.util.compat

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.ContextWrapper
import android.os.Looper
import android.os.MessageQueue
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.smart.library.STInitializer
import java.lang.reflect.Field
import java.lang.reflect.Method

@Deprecated("no need now")
@SuppressLint("DiscouragedPrivateApi")
object STIMMLeaksUtil {

    private val inputMethodManager = STInitializer.application()?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
    private var mServedViewField: Field? = null
    private var mHField: Field? = null
    private var finishInputLockedMethod: Method? = null
    private var focusInMethod: Method? = null

    init {
        try {
            mServedViewField = InputMethodManager::class.java.getDeclaredField("mServedView")
            mServedViewField?.isAccessible = true
            mHField = InputMethodManager::class.java.getDeclaredField("mServedView")
            mHField?.isAccessible = true
            finishInputLockedMethod = InputMethodManager::class.java.getDeclaredMethod("finishInputLocked")
            finishInputLockedMethod?.isAccessible = true
            focusInMethod = InputMethodManager::class.java.getDeclaredMethod("focusIn", View::class.java)
            focusInMethod?.isAccessible = true
        } catch (unexpected: NoSuchMethodException) {
            Log.e("IMMLeaks", "Unexpected reflection exception", unexpected)
        } catch (unexpected: NoSuchFieldException) {
            Log.e("IMMLeaks", "Unexpected reflection exception", unexpected)
        }
    }

    internal class ReferenceCleaner(
        private val inputMethodManager: InputMethodManager?,
        private val mHField: Field,
        private val mServedViewField: Field,
        private val finishInputLockedMethod: Method
    ) : MessageQueue.IdleHandler, View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalFocusChangeListener {

        override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
            if (newFocus == null) {
                return
            }
            oldFocus?.removeOnAttachStateChangeListener(this)
            Looper.myQueue().removeIdleHandler(this)
            newFocus.addOnAttachStateChangeListener(this)
        }

        override fun onViewAttachedToWindow(v: View) {}

        override fun onViewDetachedFromWindow(v: View) {
            v.removeOnAttachStateChangeListener(this)
            Looper.myQueue().removeIdleHandler(this)
            Looper.myQueue().addIdleHandler(this)
        }

        override fun queueIdle(): Boolean {
            clearInputMethodManagerLeak()
            return false
        }

        @Suppress("unused")
        private fun clearInputMethodManagerLeak() {
            try {
                val lock = mHField.get(inputMethodManager) ?: Any()
                // This is highly dependent on the InputMethodManager implementation.
                synchronized(lock) {
                    val servedView = mServedViewField.get(inputMethodManager) as View
                    val servedViewAttached = servedView.windowVisibility != View.GONE
                    if (servedViewAttached) {
                        // The view held by the IMM was replaced without a global focus change. Let's make
                        // sure we get notified when that view detaches.

                        // Avoid double registration.
                        servedView.removeOnAttachStateChangeListener(this)
                        servedView.addOnAttachStateChangeListener(this)
                    } else {
                        // servedView is not attached. InputMethodManager is being stupid!
                        val activity = extractActivity(servedView.context)
                        if (activity == null || activity.window == null) {
                            // Unlikely case. Let's finish the input anyways.
                            finishInputLockedMethod.invoke(inputMethodManager)
                        } else {
                            val decorView = activity.window.peekDecorView()
                            val windowAttached = decorView.windowVisibility != View.GONE
                            if (!windowAttached) {
                                finishInputLockedMethod.invoke(inputMethodManager)
                            } else {
                                decorView.requestFocusFromTouch()
                            }
                        }
                    }
                }
            } catch (unexpected: Exception) {
                Log.e("IMMLeaks", "Unexpected reflection exception", unexpected)
            }

        }

        private fun extractActivity(context: Context): Activity? {
            var tmpContext = context
            while (true) {
                when (tmpContext) {
                    is Application -> {
                        return null
                    }
                    is Activity -> {
                        return tmpContext
                    }
                    is ContextWrapper -> {
                        val baseContext = tmpContext.baseContext
                        // Prevent Stack Overflow.
                        if (baseContext === tmpContext) {
                            return null
                        }
                        tmpContext = baseContext
                    }
                    else -> {
                        return null
                    }
                }
            }
        }
    }

    /**
     * Fix for https://code.google.com/p/android/issues/detail?id=171190 .
     *
     *
     * When a view that has focus gets detached, we wait for the main thread to be idle and then
     * check if the InputMethodManager is leaking a view. If yes, we tell it that the decor view got
     * focus, which is what happens if you press home and come back from recent apps. This replaces
     * the reference to the detached view with a reference to the decor view.
     *
     *
     * Should be called from [Activity.onCreate] )}.
     */
    @Suppress("unused")
    fun fixFocusedViewLeak(activity: Activity) {
        // Don't know about other versions yet.
        if (mHField == null || mServedViewField == null || finishInputLockedMethod == null) {
            return
        }


        val cleaner = ReferenceCleaner(inputMethodManager, mHField!!, mServedViewField!!, finishInputLockedMethod!!)
        val rootView = activity.window.decorView.rootView
        val viewTreeObserver = rootView.viewTreeObserver
        viewTreeObserver.addOnGlobalFocusChangeListener(cleaner)
    }
}
