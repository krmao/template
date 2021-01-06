package com.smart.library.util.accessibility

import android.accessibilityservice.AccessibilityService
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.rx.RxBus

class STActivityTrackerService : AccessibilityService() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        STLogUtil.d(TAG, "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        STLogUtil.d(TAG, "onAccessibilityEvent packageName=${event.packageName}, className=${event.className}")
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName: String? = event.packageName?.toString()
            val className: String? = event.className?.toString()
            if (!packageName.isNullOrBlank() && !className.isNullOrBlank() && packageName != SYSTEM_NOTIFICATION_PANEL_PACKAGE_NAME && className != SYSTEM_NOTIFICATION_PANEL_CLASS_NAME) {
                RxBus.post(ActivityChangedEvent(packageName, className.toString()))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        STLogUtil.d(TAG, "onDestroy")
    }

    data class ActivityChangedEvent(val packageName: String, val className: String)

    companion object {
        const val TAG = "[ActivityTrackerService]"

        class ActivityTrackerBroadcastReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    ACTION_BROADCAST_RECEIVER_ACTIVITY_TRACKER_START -> {
                        if (STAccessibilityUtil.checkAccessibility(context = STInitializer.application())) {
                            startService()
                        }
                        return
                    }
                    ACTION_BROADCAST_RECEIVER_ACTIVITY_TRACKER_STOP -> {
                        stopService()
                    }
                }
            }

            companion object {
                const val ACTION_BROADCAST_RECEIVER_ACTIVITY_TRACKER_START = "com.codesdancing.broadcast.receiver.activity.tracker.start"
                const val ACTION_BROADCAST_RECEIVER_ACTIVITY_TRACKER_STOP = "com.codesdancing.broadcast.receiver.activity.tracker.stop"

                @JvmStatic
                @JvmOverloads
                fun createBroadcastReceiverStartPendingIntent(context: Context? = STInitializer.application()): PendingIntent {
                    return createBroadcastReceiverPendingIntent(context, ACTION_BROADCAST_RECEIVER_ACTIVITY_TRACKER_START)
                }

                @JvmStatic
                @JvmOverloads
                fun createBroadcastReceiverStopPendingIntent(context: Context? = STInitializer.application()): PendingIntent {
                    return createBroadcastReceiverPendingIntent(context, ACTION_BROADCAST_RECEIVER_ACTIVITY_TRACKER_STOP)
                }

                @JvmStatic
                @JvmOverloads
                fun createBroadcastReceiverPendingIntent(context: Context? = STInitializer.application(), actionName: String): PendingIntent {
                    val intent = Intent(context, ActivityTrackerBroadcastReceiver::class.java)
                    intent.action = actionName
                    return PendingIntent.getBroadcast(context, 0, intent, 0)
                }
            }
        }

        private var serviceIntent: Intent? = null

        // 过滤掉下拉的系统通知面板, 否则每次查看通知显示的包名类名都是显示通知面板本身所在的包名类名
        private const val SYSTEM_NOTIFICATION_PANEL_PACKAGE_NAME = "com.android.systemui"
        private const val SYSTEM_NOTIFICATION_PANEL_CLASS_NAME = "android.widget.FrameLayout"

        @JvmStatic
        @JvmOverloads
        fun startService(context: Context? = STInitializer.application()) {
            if (context != null) {
                stopService(context)
                serviceIntent = Intent(context, STActivityTrackerService::class.java)
                context.startService(serviceIntent)
            }
        }

        @JvmStatic
        @JvmOverloads
        fun stopService(context: Context? = STInitializer.application()) {
            val intent: Intent? = serviceIntent
            if (context != null && intent != null) {
                context.stopService(intent)
                serviceIntent = null
            }
        }
    }
}