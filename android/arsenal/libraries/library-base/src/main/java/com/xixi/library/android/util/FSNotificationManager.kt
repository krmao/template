@file:Suppress("DEPRECATION")

package com.xixi.library.android.util

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat

import com.xixi.library.android.base.FSActivity


object FSNotificationManager {
    fun showNotify(context: Context, notificationId: Int, notificationFlags: Int, notificationBuilder: NotificationCompat.Builder) {
        showNotify(context, notificationId, notificationFlags, notificationBuilder, null)
    }

    fun showNotifyToActivity(context: Context, notificationId: Int, notificationFlags: Int, notificationBuilder: NotificationCompat.Builder, intent: Intent) {
        showNotify(context, notificationId, notificationFlags, notificationBuilder, PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT))
    }

    fun showNotifyToActivity(context: Context, notificationId: Int, notificationFlags: Int, notificationBuilder: NotificationCompat.Builder, intent: Intent, pendingIntentFlags: Int) {
        showNotify(context, notificationId, notificationFlags, notificationBuilder, PendingIntent.getActivity(context, notificationId, intent, pendingIntentFlags))
    }

    fun showNotifyToActivity(context: Context, notificationId: Int, notificationFlags: Int, notificationBuilder: NotificationCompat.Builder, toActivity: Class<out Activity>, bundle: Bundle?, pendingIntentFlags: Int) {
        val intent = Intent(context, toActivity)
        if (bundle != null)
            intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        showNotify(context, notificationId, notificationFlags, notificationBuilder, PendingIntent.getActivity(context, notificationId, intent, pendingIntentFlags))
    }

    fun showNotifyToFragment(context: Context, notificationId: Int, notificationFlags: Int, notificationBuilder: NotificationCompat.Builder, fragmentClass: Class<out Fragment>, bundle: Bundle?) {
        val intent = FSActivity.getNewTaskIntent(context, notificationId, fragmentClass, bundle)
        if (bundle != null)
            intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        showNotify(context, notificationId, notificationFlags, notificationBuilder, PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT))
    }

    fun showNotifyToFragment(context: Context, notificationId: Int, notificationFlags: Int, notificationBuilder: NotificationCompat.Builder, fragmentClass: Class<out Fragment>, bundle: Bundle?, pendingIntentFlags: Int) {
        val intent = FSActivity.getNewTaskIntent(context, notificationId, fragmentClass, bundle)
        if (bundle != null)
            intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        showNotify(context, notificationId, notificationFlags, notificationBuilder, PendingIntent.getActivity(context, notificationId, intent, pendingIntentFlags))
    }

    fun showNotify(context: Context, notificationId: Int, notificationFlags: Int, notificationBuilder: NotificationCompat.Builder, pendingIntent: PendingIntent?) {
        if (pendingIntent != null)
            notificationBuilder.setContentIntent(pendingIntent)
        // 通过通知管理器来发起通知。如果id不同，则每click，在status那里增加一个提示
        var notification: Notification? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = notificationBuilder.build()
        } else if (android.os.Build.VERSION.SDK_INT >= 11) {
            notification = notificationBuilder.notification
        }
        if (notification != null) {
            notification.flags = notificationFlags
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(notificationId, notification)
        }
    }

    fun cancelNotify(context: Context, notificationId: Int) {
        cancelNotify(context, null, notificationId)
    }

    fun cancelNotify(context: Context, tag: String?, notificationId: Int) {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(tag, notificationId)
    }

    fun cancelAllNotify(context: Context) {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
    }
}
