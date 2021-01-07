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

/**
 * https://github.com/fashare2015/ActivityTracker
 */
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
                    ACTION_BROADCAST_RECEIVER_OPEN_ACCESSIBILITY -> {
                        if (STAccessibilityUtil.checkAccessibility(context = STInitializer.application(), autoOpenAccessibilitySettings = true)) {
                            STSystemUtil.closeSystemNotificationPanel()
                        }
                    }
                }
            }

            companion object {
                const val ACTION_BROADCAST_RECEIVER_OPEN_ACCESSIBILITY = "com.codesdancing.broadcast.receiver.accessibility.open"

                @JvmStatic
                @JvmOverloads
                fun createBroadcastReceiverOpenAccessibilityPendingIntent(context: Context? = STInitializer.application()): PendingIntent {
                    val intent = Intent(context, ActivityTrackerBroadcastReceiver::class.java)
                    intent.action = ACTION_BROADCAST_RECEIVER_OPEN_ACCESSIBILITY
                    return PendingIntent.getBroadcast(context, 0, intent, 0)
                }
            }
        }

        // 过滤掉下拉的系统通知面板, 否则每次查看通知显示的包名类名都是显示通知面板本身所在的包名类名
        private const val SYSTEM_NOTIFICATION_PANEL_PACKAGE_NAME = "com.android.systemui"
        private const val SYSTEM_NOTIFICATION_PANEL_CLASS_NAME = "android.widget.FrameLayout"
    }
}