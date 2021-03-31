package com.smart.library.widget.debug

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.base.STBaseFragment
import com.smart.library.util.*
import com.smart.library.util.accessibility.STAccessibilityUtil
import com.smart.library.util.accessibility.STActivityTrackerService
import com.smart.library.util.rx.RxBus
import kotlinx.android.synthetic.main.st_fragment_debug.*

@Keep
@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
open class STDebugFragment : STBaseFragment() {

    @SuppressLint("CheckResult")
    companion object {
        private const val KEY_CUSTOM_LIST = "KEY_CUSTOM_LIST"
        private var hostList: MutableList<HostModel> = STPreferencesUtil.getList(KEY_CUSTOM_LIST, HostModel::class.java)

        @JvmStatic
        var ENABLE_TRACE_DEBUG = false //开启埋点调试开关，执行 toast 提示 以及 log 打印, 只有debug包可用

        @JvmStatic
        var ENABLE_ACTIVITY_INFO_DEBUG = false //开启页面信息提示开关, 只有debug包可用

        @JvmStatic
        val NOTIFICATION_DEFAULT_DEBUG_CHANNEL_ID: Int = 100000000

        @JvmStatic
        val NOTIFICATION_DEFAULT_SUMMARY_GROUP_KEY: String = "NOTIFICATION_DEFAULT_GROUP_KEY"

        @JvmStatic
        val NOTIFICATION_DEFAULT_SUMMARY_GROUP_TEXT: String = "SETTINGS"

        @JvmStatic
        val NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID: Int = 200000000

        @JvmStatic
        val NOTIFICATION_DEFAULT_CHANNEL_GROUP_ID: String = NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID.toString()

        @JvmStatic
        val actionList: MutableList<NotificationCompat.Action> = arrayListOf()

        @JvmStatic
        val childViewList: MutableList<Class<out View>> = arrayListOf()

        @JvmStatic
        fun addHost(vararg hostModels: HostModel) {
            if (hostModels.isNotEmpty()) {
                hostModels
                    .filterNot { hostList.contains(it) }
                    .forEach { hostList.add(it) }
                saveHostList()
            }
        }

        @JvmStatic
        fun addHost(label: String, url: String, isSelected: Boolean = false) {
            if (!TextUtils.isEmpty(label) && !TextUtils.isEmpty(url))
                addHost(HostModel(label, url, isSelected))
        }

        @JvmStatic
        private fun saveHostList() {
            STPreferencesUtil.putList(KEY_CUSTOM_LIST, hostList)
        }

        @JvmStatic
        fun getCurrentHost(): HostModel? {
            val tmpUrlList = hostList
            var currentSelectedModel: HostModel? = tmpUrlList.firstOrNull { it.isSelected }
            if (currentSelectedModel == null && tmpUrlList.size > 0) {
                tmpUrlList[0].isSelected = true
                hostList = tmpUrlList
                currentSelectedModel = tmpUrlList[0]
                saveHostList()
            }
            return currentSelectedModel
        }

        private var isDebugNotificationShowingNow: Boolean = false

        @SuppressLint("NewApi", "CheckResult")
        @JvmStatic
        fun showDebugNotification(title: String = "${STSystemUtil.getAppName(STInitializer.application())} 调试助手", subTitle: String = "点击跳转到调试界面") {
            isDebugNotificationShowingNow = true

            //========== ======================================================================== ==========
            //========== notification example start
            //========== ======================================================================== ==========

            // notification
            val notificationId = NOTIFICATION_DEFAULT_DEBUG_CHANNEL_ID
            val channelId: String = STNotificationManager.getChannelId(notificationId)
            val channelName = "在通知栏上显示程式调试入口"
            val smallIcon = android.R.drawable.ic_menu_myplaces

            // notification group ( 注意: 通知组只有 sdk >= 24 support )
            // 注意 summaryGroupId 与 notificationId 不能相同, 否则会出现没有合并到一个组的意外情况
            val summaryGroupId = NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID
            val summaryGroupText = NOTIFICATION_DEFAULT_SUMMARY_GROUP_TEXT
            val summaryGroupKey = NOTIFICATION_DEFAULT_SUMMARY_GROUP_KEY

            // channel group
            val channelGroupId = NOTIFICATION_DEFAULT_CHANNEL_GROUP_ID
            val channelGroupName = "DEBUG"

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

            val context = STInitializer.application()
            context ?: return
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcon)
                .setColor(Color.parseColor("#4E6A78"))
                .setColorized(true)
                // .setLargeIcon(STSystemUtil.getAppBitmap(STInitializer.application()))
                .setContentTitle(title)
                .setContentText(subTitle) // set content text to support devices running API level < 24
                // .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)  // set the intent that will fire when the user taps the notification
                .setOngoing(true)
                .setGroup(summaryGroupKey) // specify which group this notification belongs to
                .setAutoCancel(false) // automatically removes the notification when the user taps it

            // 如果没有辅助权限功能, 增加跳转辅助权限设置页面
            if (!STAccessibilityUtil.checkAccessibility(context = STInitializer.application(), autoOpenAccessibilitySettings = false)) {
                builder.addAction(android.R.drawable.ic_menu_info_details, "开启辅助权限", STActivityTrackerService.Companion.ActivityTrackerBroadcastReceiver.createBroadcastReceiverOpenAccessibilityPendingIntent())
            }

            actionList.forEach { builder.addAction(it) }

            val notificationManager = STNotificationManager.getNotificationManager()
            notificationManager ?: return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 首先设置通知渠道组
                val channelGroup = NotificationChannelGroup(channelGroupId, channelGroupName)
                if (notificationManager.notificationChannelGroups?.filter { it == channelGroup }?.size ?: 0 > 1) {
                    notificationManager.deleteNotificationChannelGroup(channelGroupId)
                }
                if (notificationManager.notificationChannelGroups?.contains(channelGroup) != true) {
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

            val summaryNotification = builder
                // build summary info into InboxStyle template
                .setStyle(
                    NotificationCompat.InboxStyle()
                        .addLine(subTitle)
                        .setBigContentTitle(title)
                        .setSummaryText(summaryGroupText)
                )
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
            isDebugNotificationShowingNow = false

            val summaryGroupId = NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID
            val notificationId = NOTIFICATION_DEFAULT_DEBUG_CHANNEL_ID
            val channelId: String = STNotificationManager.getChannelId(notificationId)
            STNotificationManager.cancelNotify(notificationId, channelId)
            STNotificationManager.cancelNotify(summaryGroupId)
        }

        init {
            RxBus.toObservable(STActivityTrackerService.ActivityChangedEvent::class.java).subscribe { changeEvent ->
                STLogUtil.w(STActivityTrackerService.TAG, "onSubscribe isDebugNotificationShowingNow=$isDebugNotificationShowingNow, changeEvent=$changeEvent")
                if (isDebugNotificationShowingNow) {
                    showDebugNotification(changeEvent.packageName, changeEvent.className.substringAfterLast("."))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.st_fragment_debug, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childViewList.forEach { addChildView(it.getConstructor(Context::class.java).newInstance(context)) }

        activity_info_cb.isChecked = ENABLE_ACTIVITY_INFO_DEBUG
        activity_info_cb.setOnCheckedChangeListener { _, isChecked -> ENABLE_ACTIVITY_INFO_DEBUG = isChecked }

        trace_cb.isChecked = ENABLE_TRACE_DEBUG
        trace_cb.setOnCheckedChangeListener { _, isChecked -> ENABLE_TRACE_DEBUG = isChecked }

        val adapter = DebugAdapter(hostList, activity)
        listView.adapter = adapter
        addCustom.setOnClickListener(View.OnClickListener {
            val newEntity = HostModel(editLabel.text.toString(), editUrl.text.toString(), false)
            if (TextUtils.isEmpty(newEntity.label)) {
                STToastUtil.show("请填写标签")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(newEntity.url)) {
                STToastUtil.show("请填写服务地址")
                return@OnClickListener
            }
            editLabel.text = null
            editUrl.text = null
            if (!hostList.contains(newEntity)) {
                hostList.add(newEntity)
                saveHostList()
                RxBus.post(HostChangeEvent(newEntity))
                adapter.notifyDataSetChanged()
            }
            STSystemUtil.hideKeyboard(activity)
        })

        clearCacheTV.setOnClickListener { STIntentUtil.goToAppDetails(activity) }
    }

    fun getChildCount(): Int {
        return container_view_group.childCount
    }

    @JvmOverloads
    fun addChildView(child: View, index: Int = -1, params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)) {
        container_view_group?.addView(child, index, params)
    }

    private class DebugAdapter(var list: List<HostModel>, private val context: Context?) : BaseAdapter() {
        override fun getCount(): Int = list.size

        override fun getItem(position: Int): HostModel = list[position]

        override fun getItemId(position: Int): Long = position.toLong()

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
            val convertView = LayoutInflater.from(context).inflate(R.layout.st_fragment_debug_item, parent, false)
            val radioButton = convertView.findViewById<RadioButton>(R.id.radioButton)
            val textView = convertView.findViewById<TextView>(R.id.textView)
            val urlEntity = getItem(position)
            radioButton.text = urlEntity.label
            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && !urlEntity.isSelected) {
                    urlEntity.isSelected = true
                    for (item in hostList) {
                        item.isSelected = item == urlEntity
                    }
                    saveHostList()
                    RxBus.post(HostChangeEvent(urlEntity))
                    notifyDataSetChanged()
                }
            }
            radioButton.isChecked = urlEntity.isSelected
            textView.text = urlEntity.url
            textView.setOnClickListener { STToastUtil.show("长按复制") }
            textView.setOnLongClickListener {
                STSystemUtil.copyToClipboard(urlEntity.label, urlEntity.url)
                STToastUtil.show("已复制")
                true
            }
            return convertView
        }

    }

    @Keep
    data class HostChangeEvent(var hostModel: HostModel)

    @Keep
    data class HostModel(var label: String, var url: String, var isSelected: Boolean) {
        override fun equals(other: Any?): Boolean {
            return if (other is HostModel) !TextUtils.isEmpty(label) && label == other.label else false
        }

        override fun hashCode(): Int {
            var result = label.hashCode()
            result = 31 * result + url.hashCode()
            result = 31 * result + isSelected.hashCode()
            return result
        }
    }
}
