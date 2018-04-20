@file:Suppress("DEPRECATION")


import android.annotation.TargetApi
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.support.v4.app.TaskStackBuilder
import com.smart.library.base.CXBaseApplication


/* 必备操作一:配置

    <!-- [notification] HomeTabActivity is the parent for CXDebugActivity -->
    <activity
        android:name=".widget.debug.CXDebugActivity"
        android:excludeFromRecents="true"
        android:launchMode="singleTask"
        android:parentActivityName="com.smart.template.tab.HomeTabActivity"
        android:screenOrientation="portrait"
        android:taskAffinity=""
        android:theme="@style/CXAppTheme.Translucent"
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
        val title = "${CXSystemUtil.appName} 调试助手"
        val text = "点击跳转到调试界面"
        val notificationId = CXConfig.NOTIFICATION_DEFAULT_DEBUG_CHANNEL_ID
        val channelId: String = CXNotificationManager.getChannelId(notificationId)
        val channelName = "在通知栏上显示程式调试入口"
        val smallIcon = CXConfig.NOTIFICATION_ICON_SMALL

        // notification group ( 注意: 通知组只有 sdk >= 24 support )
        val summaryGroupId = CXConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID
        val summaryGroupText = CXConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_TEXT
        val summaryGroupKey = CXConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_KEY

        // channel group
        val channelGroupId = CXConfig.NOTIFICATION_DEFAULT_CHANNEL_GROUP_ID
        val channelGroupName = CXConfig.NOTIFICATION_DEFAULT_CHANNEL_GROUP_NAME

        val intent = Intent(CXBaseApplication.INSTANCE, CXDebugActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(CXBaseApplication.INSTANCE, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        // val pendingIntent = CXNotificationManager.getPendingIntent(intent, flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)

        val builder = NotificationCompat.Builder(CXBaseApplication.INSTANCE, channelId)
            .setSmallIcon(smallIcon)
            .setLargeIcon(CXSystemUtil.appBitmap)
            .setContentTitle(title)
            .setContentText(text) // set content text to support devices running API level < 24
            // .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)  // set the intent that will fire when the user taps the notification
            .setOngoing(true)
            .setGroup(summaryGroupKey) // specify which group this notification belongs to
            .setAutoCancel(false) // automatically removes the notification when the user taps it

        val notificationManager = CXNotificationManager.getNotificationManager()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 首先设置通知渠道组
            notificationManager.createNotificationChannelGroup(NotificationChannelGroup(channelGroupId, channelGroupName))

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
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notificationId, notification)
        // notificationManager.notify(2, notification) // 创建更多不同 id 的 notification 会归并到 group 里面
        // notificationManager.notify(3, notification)

        //========== notification group

        val summaryNotification = builder
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
        val notificationId = CXConfig.NOTIFICATION_DEFAULT_DEBUG_CHANNEL_ID
        val channelId: String = CXNotificationManager.getChannelId(notificationId)
        CXNotificationManager.cancelNotify(notificationId, channelId)
    }
 */

/**
 * @see https://developer.android.com/training/notify-user/navigation.html
 * @see https://developer.android.com/training/notify-user/navigation.html#ExtendedNotification
 * @see https://developer.android.com/training/notify-user/build-notification.html#SimpleNotification
 * @see https://developer.android.com/training/notify-user/channels.html
 * @see https://developer.android.com/training/notify-user/group.html
 * @see https://developer.android.com/training/notify-user/channels.html#CreateChannelGroup
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object CXNotificationManager {

    @JvmStatic
    fun getChannelId(notificationId: Int): String = "NOTIFICATION_CHANNEL_ID_$notificationId"

    @JvmStatic
    fun getChannelName(notificationId: Int?): String? = if (notificationId == null) null else "NOTIFICATION_CHANNEL_NAME_$notificationId"

    @JvmStatic
    fun getNotificationManager(): NotificationManager = CXBaseApplication.INSTANCE.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @JvmStatic
    @JvmOverloads
    fun cancelNotify(notificationId: Int? = null, channelId: String? = if (notificationId == null) null else getChannelId(notificationId)) {
        val notificationManager = getNotificationManager()
        if (notificationId == null) {
            notificationManager.cancelAll()
        } else {
            notificationManager.cancel(notificationId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channelId?.let { notificationManager.deleteNotificationChannel(channelId) }
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
    @JvmOverloads
    @Deprecated("使用后回退栈有问题,且mainActivity 重启或者 finish")
    fun getPendingIntent(intent: Intent, requestCode: Int = 0, flags: Int = PendingIntent.FLAG_UPDATE_CURRENT): PendingIntent? {
        val stackBuilder = TaskStackBuilder.create(CXBaseApplication.INSTANCE)
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
