package com.smart.library.widget.debug

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.TextView
import com.smart.library.R
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.CXBaseFragment
import com.smart.library.base.CXConfig
import com.smart.library.util.*
import com.smart.library.util.rx.RxBus
import kotlinx.android.synthetic.main.cx_fragment_debug.*


@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
open class CXDebugFragment : CXBaseFragment() {

    companion object {
        private const val KEY_CUSTOM_LIST = "KEY_CUSTOM_LIST"
        private var hostList: MutableList<HostModel> = CXPreferencesUtil.getList(KEY_CUSTOM_LIST, HostModel::class.java)

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
            CXPreferencesUtil.putList(KEY_CUSTOM_LIST, hostList)
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
            // 注意 summaryGroupId 与 notificationId 不能相同, 否则会出现没有合并到一个组的意外情况
            val summaryGroupId = CXConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID
            val summaryGroupText = CXConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_TEXT
            val summaryGroupKey = CXConfig.NOTIFICATION_DEFAULT_SUMMARY_GROUP_KEY

            // channel group
            val channelGroupId = CXConfig.NOTIFICATION_DEFAULT_CHANNEL_GROUP_ID
            val channelGroupName = CXConfig.NOTIFICATION_DEFAULT_CHANNEL_GROUP_NAME

            CXLogUtil.e("notification", "notificationId=$notificationId")
            CXLogUtil.e("notification", "channelId=$summaryGroupId")
            CXLogUtil.e("notification", "summaryGroupId=$summaryGroupId")
            CXLogUtil.e("notification", "summaryGroupText=$summaryGroupText")
            CXLogUtil.e("notification", "summaryGroupKey=$summaryGroupKey")
            CXLogUtil.e("notification", "channelGroupId=$channelGroupId")
            CXLogUtil.e("notification", "channelGroupName=$channelGroupName")

            val intent = Intent(CXBaseApplication.INSTANCE, CXDebugActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

            val pendingIntent = PendingIntent.getActivity(CXBaseApplication.INSTANCE, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.cx_fragment_debug, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trace_cb.isChecked = CXConfig.ENABLE_TRACE_DEBUG
        trace_cb.setOnCheckedChangeListener { _, isChecked -> CXConfig.ENABLE_TRACE_DEBUG = isChecked }

        val adapter = DebugAdapter(hostList, activity)
        listView.adapter = adapter
        addCustom.setOnClickListener(View.OnClickListener {
            val newEntity = HostModel(editLabel.text.toString(), editUrl.text.toString(), false)
            if (TextUtils.isEmpty(newEntity.label)) {
                CXToastUtil.show("请填写标签")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(newEntity.url)) {
                CXToastUtil.show("请填写服务地址")
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
            CXSystemUtil.hideKeyboard(activity)
        })

        clearCacheTV.setOnClickListener { CXIntentUtil.goToAppDetails(activity) }
    }

    private class DebugAdapter(var list: List<HostModel>, private val context: Context?) : BaseAdapter() {
        override fun getCount(): Int = list.size

        override fun getItem(position: Int): HostModel = list[position]

        override fun getItemId(position: Int): Long = position.toLong()

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
            val convertView = LayoutInflater.from(context).inflate(R.layout.cx_fragment_debug_item, parent, false)
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
            textView.setOnClickListener { CXToastUtil.show("长按复制") }
            textView.setOnLongClickListener {
                CXSystemUtil.copyToClipboard(urlEntity.label, urlEntity.url)
                CXToastUtil.show("已复制")
                true
            }
            return convertView
        }

    }

    data class HostChangeEvent(var hostModel: HostModel)

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
