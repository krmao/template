package com.smart.library.util

import android.annotation.TargetApi
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.Keep
import androidx.core.app.TaskStackBuilder
import com.smart.library.STInitializer


/* 必备操作一:配置

    <!-- [notification] HomeTabActivity is the parent for STDebugActivity -->
    <activity
        android:name=".widget.debug.STDebugActivity"
        android:excludeFromRecents="true"
        android:launchMode="singleTask"
        android:parentActivityName="com.smart.template.tab.HomeTabActivity"
        android:screenOrientation="portrait"
        android:taskAffinity=""
        android:theme="@style/STAppTheme.Normal.Translucent"
        android:windowSoftInputMode="stateHidden|adjustResize" />

 */

/* 必备操作二:代码

    @SuppressLint("NewApi")
    @JvmStatic
    fun showDebugNotification() {

        //========== ======================================================================== ==========
        //========== notification example start
        //========== ======================================================================== ==========

        // notification
        val title = "${STSystemUtil.appName} 调试助手"
        val text = "点击跳转到调试界面"
        val notificationId = STConfig.NOTIFICATION_DEFAULT_DEBUG_CHANNEL_ID
        val channelId: String = STNotificationManager.getChannelId(notificationId)
        val channelName = "在通知栏上显示程式调试入口"
        val smallIcon = STConfig.NOTIFICATION_ICON_SMALL

        // notification group ( 注意: 通知组只有 sdk >= 24 support )
        // 注意 summaryGroupId 与 notificationId 不能相同, 否则会出现没有合并到一个组的意外情况
        val summaryGroupId = STConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID
        val summaryGroupText = STConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_TEXT
        val summaryGroupKey = STConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_KEY

        // channel group
        val channelGroupId = STConfig.NOTIFICATION_DEFAULT_CHANNEL_GROUP_ID
        val channelGroupName = STConfig.NOTIFICATION_DEFAULT_CHANNEL_GROUP_NAME

        STLogUtil.e("notification", "notificationId=$notificationId")
        STLogUtil.e("notification", "channelId=$summaryGroupId")
        STLogUtil.e("notification", "summaryGroupId=$summaryGroupId")
        STLogUtil.e("notification", "summaryGroupText=$summaryGroupText")
        STLogUtil.e("notification", "summaryGroupKey=$summaryGroupKey")
        STLogUtil.e("notification", "channelGroupId=$channelGroupId")
        STLogUtil.e("notification", "channelGroupName=$channelGroupName")

        val intent = Intent(STInitializer.application(), STDebugActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(STInitializer.application(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(STInitializer.application(), channelId)
            .setSmallIcon(smallIcon)
            .setLargeIcon(STSystemUtil.appBitmap)
            .setContentTitle(title)
            .setContentText(text) // set content text to support devices running API level < 24
            // .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)  // set the intent that will fire when the user taps the notification
            .setOngoing(true)
            .setGroup(summaryGroupKey) // specify which group this notification belongs to
            .setAutoCancel(false) // automatically removes the notification when the user taps it

        val notificationManager = STNotificationManager.getNotificationManager()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 首先设置通知渠道组
            val channelGroup = NotificationChannelGroup(channelGroupId, channelGroupName)
            if (notificationManager.notificationChannelGroups.filter { it == channelGroup }.size > 1) {
                notificationManager.deleteNotificationChannelGroup(channelGroupId)
            }
            if (!notificationManager.notificationChannelGroups.contains(channelGroup)) {
                notificationManager.createNotificationChannelGroup(channelGroup)
            }

            // IMPORTANCE_HIGH      (紧急-发出声音并显示为提醒通知)
            // IMPORTANCE_DEFAULT   (高级-发出声音)
            // IMPORTANCE_LOW       (中等-没有声音)
            // IMPORTANCE_MIN       (低级-无声音并且不会出现在状态栏中)
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            channel.enableLights(false) // 是否在桌面icon右上角展示小红点
            channel.setShowBadge(false) // 是否在久按桌面图标时显示此渠道的通知
            channel.enableVibration(false)
            channel.setSound(null, null)
            channel.group = channelGroupId // 设置渠道组的归属关系


            if (notificationManager.notificationChannels.filter { it == channel }.size > 1) {
                notificationManager.deleteNotificationChannel(channelId)
            }
            if (!notificationManager.notificationChannels.contains(channel)) {
                notificationManager.createNotificationChannel(channel)
            }
        }

        val notification = builder.build()
        notificationManager.notify(notificationId, notification)

        //========== notification group

        val summaryNotification = NotificationCompat.Builder(STInitializer.application(), channelId)
            .setSmallIcon(smallIcon)
            .setLargeIcon(STSystemUtil.appBitmap)
            .setContentTitle(title)
            .setContentText(text) // set content text to support devices running API level < 24
            // .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)  // set the intent that will fire when the user taps the notification
            .setOngoing(true)
            .setGroup(summaryGroupKey) // specify which group this notification belongs to
            .setAutoCancel(false) // automatically removes the notification when the user taps it
            // build summary info into InboxStyle template
            .setStyle(NotificationCompat.InboxStyle()
                .addLine(text)
                .setBigContentTitle(title)
                .setSummaryText(summaryGroupText))
            .setGroup(summaryGroupKey) // specify which group this notification belongs to
            .setGroupSummary(true) // set this notification as the summary for the group
            .build()

        notificationManager.notify(summaryGroupId, summaryNotification)
        //========== ======================================================================== ==========
        //========== notification example end
        //========== ======================================================================== ==========

    }

    @JvmStatic
    fun cancelDebugNotification() {
        val notificationId = STConfig.NOTIFICATION_DEFAULT_DEBUG_CHANNEL_ID
        val channelId: String = STNotificationManager.getChannelId(notificationId)
        STNotificationManager.cancelNotify(notificationId, channelId)
    }
 */

/**
 * @see @link{https://developer.android.com/training/notify-user/navigation.html}
 * @see @link{https://developer.android.com/training/notify-user/navigation.html#ExtendedNotification}
 * @see @link{https://developer.android.com/training/notify-user/build-notification.html#SimpleNotification}
 * @see @link{https://developer.android.com/training/notify-user/channels.html}
 * @see @link{https://developer.android.com/training/notify-user/group.html}
 * @see @link{https://developer.android.com/training/notify-user/channels.html#CreateChannelGroup}
 *
 * 通知栏小图标必须为纯透明背景,白色 icon 内容
 * @see @link{https://blog.csdn.net/u013706904/article/details/51912634}
 * @see @link{https://stackoverflow.com/questions/30795431/icon-not-displaying-in-notification-white-square-shown-instead}
 */
//@Keep
@Suppress("MemberVisibilityCanBePrivate", "unused")
object STNotificationManager {

    @JvmStatic
    fun getChannelId(notificationId: Int): String = "NOTIFICATION_CHANNEL_ID_$notificationId"

    @JvmStatic
    fun getChannelName(notificationId: Int?): String? = if (notificationId == null) null else "NOTIFICATION_CHANNEL_NAME_$notificationId"

    @JvmStatic
    fun getNotificationManager(): NotificationManager? = STInitializer.application()?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    @JvmStatic
    @JvmOverloads
    fun cancelNotify(notificationId: Int? = null, channelId: String? = if (notificationId == null) null else getChannelId(notificationId)) {
        val notificationManager = getNotificationManager()
        if (notificationId == null) {
            notificationManager?.cancelAll()
        } else {
            notificationManager?.cancel(notificationId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channelId?.let { notificationManager?.deleteNotificationChannel(channelId) }
            }
        }
    }

    /**
     * 获取 notification 所需的 pendingIntent
     *
     * @param intent 目标 activity 信息
     * @param requestCode Private request code for the sender
     * @param flags May be
     *              {@link PendingIntent#FLAG_ONE_SHOT}, PendingIntent 只能被使用一次,且触发后自动取消,后面所有的尝试将无效
     *              {@link PendingIntent#FLAG_NO_CREATE}, PendingIntent 如果不存在,return null
     *              {@link PendingIntent#FLAG_CANCEL_CURRENT}, PendingIntent 如果已经存在, 则 new PendingIntent 之前会 先取消之前的那一个, 常用来更新数据, 不可以 new 一个新的 Intent
     *              {@link PendingIntent#FLAG_UPDATE_CURRENT}, PendingIntent 如果已经存在, 则继续使用, 但是数据会被替换成新的, 与 FLAG_CANCEL_CURRENT 的区别是 可以 new 一个新的 Intent
     *              {@link Intent#fillIn(Intent, int)}
     */
    @JvmStatic
    @Deprecated("使用后回退栈有问题,且mainActivity 重启或者 finish")
    private fun getPendingIntent(intent: Intent, requestCode: Int = 0, flags: Int = PendingIntent.FLAG_UPDATE_CURRENT): PendingIntent? {
        val context = STInitializer.application()
        context ?: return null
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(intent)
        return stackBuilder.getPendingIntent(requestCode, flags)
    }

    /**
     * 跳转到该 channel 的通知设置页面
     */
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.O)
    fun goToNotificationChannelSettings(context: Context, channelId: String) {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
        context.startActivity(intent)
    }
}
