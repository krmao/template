@file:Suppress("DEPRECATION")


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.smart.library.base.CXBaseApplication

/**
 * val intent = CXActivity.getNewTaskIntent(CXBaseApplication.INSTANCE, 0, CXDebugFragment::class.java)
 * // val pendingIntent = PendingIntent.getActivity(CXBaseApplication.INSTANCE, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT, null)
 * val pendingIntent = CXNotificationManager.getPendingIntent(intent, parentActivityName = ComponentName("com.smart.template", "com.smart.template.tab.HomeTabActivity"))
 * CXNotificationManager.showNotify(notificationId, Notification.FLAG_NO_CLEAR, builder, pendingIntent, channelId, channelName)
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object CXNotificationManager {

    /**
     * 获取 notification 所需的 pendingIntent
     *
     * @param intent 目标 activity 信息
     * @param parentActivityName 当程序退出的时候,点击通知栏跳转的目标 DestinationActivity 后,点击返回按钮回到的页面,如果是空则返回到系统桌面
     *              同时配置 AndroidManifest.xml
     *              <activity android:name=".DestinationActivity"
     *                        android:parentActivityName=".MainActivity"/>
     *
     * @param requestCode Private request code for the sender
     * @param flags May be
     *              {@link PendingIntent#FLAG_ONE_SHOT}, PendingIntent 只能被使用一次,且触发后自动取消,后面所有的尝试将无效
     *              {@link PendingIntent#FLAG_NO_CREATE}, PendingIntent 如果不存在,return null
     *              {@link PendingIntent#FLAG_CANCEL_CURRENT}, PendingIntent 如果已经存在, 则 new PendingIntent 之前会 先取消之前的那一个, 常用来更新数据, 不可以 new 一个新的 Intent
     *              {@link PendingIntent#FLAG_UPDATE_CURRENT}, PendingIntent 如果已经存在, 则继续使用, 但是数据会被替换成新的, 与 FLAG_CANCEL_CURRENT 的区别是 可以 new 一个新的 Intent
     *              {@link Intent#fillIn(Intent, int)}
     *
     * @see https://stackoverflow.com/questions/18934626/notification-destroying-and-creating-existing-activity
     */
    @JvmStatic
    @JvmOverloads
    fun getPendingIntent(intent: Intent, parentActivityClass: Class<out Activity>? = null, requestCode: Int = 0, flags: Int = PendingIntent.FLAG_UPDATE_CURRENT): PendingIntent? {
        val stackBuilder = TaskStackBuilder.create(CXBaseApplication.INSTANCE)
        // parentActivityClass?.let { stackBuilder.addParentStack(parentActivityClass) } // 这句代码导致 android o 以下版本的手机 mainActivity 换了一个新的实例
        stackBuilder.addNextIntent(intent)
        return stackBuilder.getPendingIntent(requestCode, flags)
    }

    @JvmStatic
    fun getChannelId(notificationId: Int?): String? = if (notificationId == null) null else "channelId-$notificationId"

    @JvmStatic
    fun getChannelName(notificationId: Int?): String? = if (notificationId == null) null else "channelName-$notificationId"

    @JvmStatic
    fun getNotificationManager(): NotificationManager = CXBaseApplication.INSTANCE.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * @param flags
     *      Notification.FLAG_SHOW_LIGHTS         //三色灯提醒，在使用三色灯提醒时候必须加该标志符
     *      Notification.FLAG_ONGOING_EVENT       //发起正在运行事件（活动中）
     *      Notification.FLAG_INSISTENT           //让声音、振动无限循环，直到用户响应 （取消或者打开）
     *      Notification.FLAG_ONLY_ALERT_ONCE     //发起Notification后，铃声和震动均只执行一次
     *      Notification.FLAG_AUTO_CANCEL         //用户单击通知后自动消失
     *      Notification.FLAG_NO_CLEAR            //只有全部清除时，Notification才会清除 ，不清除该通知(QQ的通知无法清除，就是用的这个)
     *      Notification.FLAG_FOREGROUND_SERVICE  //表示正在运行的服务
     */
    @JvmStatic
    @JvmOverloads
    @SuppressLint("ObsoleteSdkInt", "NewApi")
    fun showNotify(notificationId: Int, flags: Int = Notification.FLAG_AUTO_CANCEL, notificationBuilder: NotificationCompat.Builder, pendingIntent: PendingIntent? = null, channelId: String? = getChannelId(notificationId), channelName: String? = getChannelName(notificationId)) {

        if (pendingIntent != null) notificationBuilder.setContentIntent(pendingIntent) //在build 之前设置 !

        var notification: Notification? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = notificationBuilder.build()
        } else if (Build.VERSION.SDK_INT >= 11) {
            notification = notificationBuilder.notification
        }

        val notificationManager = getNotificationManager()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(false) // 是否在桌面icon右上角展示小红点
            channel.setShowBadge(true) // 是否在久按桌面图标时显示此渠道的通知
            channel.enableVibration(false);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel)
        }

        if (notification != null) {
            notification.flags = flags
            notificationManager.notify(notificationId, notification)
        }

    }

    @JvmStatic
    @JvmOverloads
    fun cancelNotify(notificationId: Int? = null, channelId: String? = getChannelId(notificationId)) {
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
}
