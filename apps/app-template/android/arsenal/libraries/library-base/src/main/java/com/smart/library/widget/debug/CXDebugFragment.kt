package com.smart.library.widget.debug

import CXNotificationManager
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.CXBaseFragment
import com.smart.library.base.CXConfig
import com.smart.library.util.CXIntentUtil
import com.smart.library.util.CXPreferencesUtil
import com.smart.library.util.CXSystemUtil
import com.smart.library.util.CXToastUtil
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

        @JvmOverloads
        @JvmStatic
        fun showDebugNotification(notificationId: Int, title: String = "${CXSystemUtil.appName} 调试助手", text: String = "点击跳转到调试界面", smallIcon: Int = R.drawable.cx_emo_im_happy, parentActivityClass: Class<out Activity>? = null, channelId: String = CXNotificationManager.getChannelId(notificationId) ?: "", channelName: String = "在通知栏上显示程式调试入口") {

            val builder = NotificationCompat.Builder(CXBaseApplication.INSTANCE, channelId)
                .setSmallIcon(smallIcon)
                .setLargeIcon(CXSystemUtil.appBitmap)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setOngoing(true)

            val intent = CXActivity.getNewTaskIntent(CXBaseApplication.INSTANCE, 0, CXDebugFragment::class.java)
             val pendingIntent = CXNotificationManager.getPendingIntent(intent, parentActivityClass = parentActivityClass, flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
//            val pendingIntent = PendingIntent.getActivity(CXBaseApplication.INSTANCE, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            val pendingIntent = PendingIntent.getActivity(CXBaseApplication.INSTANCE, 0, intent, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            CXNotificationManager.showNotify(notificationId, Notification.FLAG_NO_CLEAR, builder, pendingIntent, channelId, channelName)
        }

        @JvmStatic
        fun cancelDebugNotification(notificationId: Int) {
            CXNotificationManager.cancelNotify(notificationId)
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
