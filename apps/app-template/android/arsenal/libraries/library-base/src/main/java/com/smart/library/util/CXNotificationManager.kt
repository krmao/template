@file:Suppress("DEPRECATION")


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.TaskStackBuilder
import com.smart.library.base.CXBaseApplication

@Suppress("MemberVisibilityCanBePrivate", "unused")
object CXNotificationManager {

    @JvmStatic
    fun getChannelId(notificationId: Int?): String? = if (notificationId == null) null else "channelId-$notificationId"

    @JvmStatic
    fun getChannelName(notificationId: Int?): String? = if (notificationId == null) null else "channelName-$notificationId"

    @JvmStatic
    fun getNotificationManager(): NotificationManager = CXBaseApplication.INSTANCE.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * @see notification flags
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
    fun showNotify(notificationId: Int, notification: Notification, channelId: String? = getChannelId(notificationId), channelName: String? = getChannelName(notificationId)) {

        val notificationManager = getNotificationManager()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(false) // 是否在桌面icon右上角展示小红点
            channel.setShowBadge(false) // 是否在久按桌面图标时显示此渠道的通知
            channel.enableVibration(false);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notification)

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
     */
    @JvmStatic
    @JvmOverloads
    @Deprecated("使用后回退栈有问题,且mainActivity 重启或者 finish")
    fun getPendingIntent(intent: Intent, parentActivityName: ComponentName? = null, requestCode: Int = 0, flags: Int = PendingIntent.FLAG_UPDATE_CURRENT): PendingIntent? {
        val stackBuilder = TaskStackBuilder.create(CXBaseApplication.INSTANCE)
        // parentActivityName?.let { stackBuilder.addParentStack(it) }
        stackBuilder.addNextIntent(intent)
        return stackBuilder.getPendingIntent(requestCode, flags)
    }
}
