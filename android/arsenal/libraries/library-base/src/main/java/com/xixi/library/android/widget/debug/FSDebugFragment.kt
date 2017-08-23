package com.xixi.library.android.widget.debug

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.TextView
import com.xixi.library.R
import com.xixi.library.android.base.CXBaseApplication
import com.xixi.library.android.base.CXBaseFragment
import com.xixi.library.android.util.*
import com.xixi.library.android.util.rx.RxBus
import kotlinx.android.synthetic.main.fs_fragment_debug.*

@Suppress("unused")
open class CXDebugFragment : CXBaseFragment() {
    companion object {
        val KEY_CUSTOM_LIST = "KEY_CUSTOM_LIST"
        private var serverList: MutableList<ServerModel> = CXPreferencesUtil.getList(KEY_CUSTOM_LIST, ServerModel::class.java)

        fun addUrl(vararg serverModels: ServerModel) {
            if (serverModels.isNotEmpty()) {
                for (tmpUrlEntity in serverModels)
                    if (!serverList.contains(tmpUrlEntity))
                        serverList.add(tmpUrlEntity)
                saveUrlList()
            }
        }

        fun addUrl(label: String, url: String, isSelected: Boolean = false) {
            if (!TextUtils.isEmpty(label) && !TextUtils.isEmpty(url))
                addUrl(ServerModel(label, url, isSelected))
        }

        fun saveUrlList() {
            CXPreferencesUtil.putList(KEY_CUSTOM_LIST, serverList)
        }

        fun getCurrentUrl(): String {
            val tmpUrlList = serverList
            var url: String = tmpUrlList.firstOrNull { it.isSelected }?.url ?: ""
            if (TextUtils.isEmpty(url) && tmpUrlList.size > 0) {
                tmpUrlList[0].isSelected = true
                serverList = tmpUrlList
                url = tmpUrlList[0].url
                saveUrlList()
            }
            return url
        }

        fun showDebugNotification(notificationId: Int) {
            val builder = android.support.v7.app.NotificationCompat.Builder(CXBaseApplication.INSTANCE)
                    .setSmallIcon(R.drawable.fs_emo_im_happy)
                    .setContentTitle("车享家环境切换")
                    .setContentText("点击跳转到环境切换页面")
                    .setAutoCancel(false)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setOngoing(true)
            CXNotificationManager.showNotifyToFragment(CXBaseApplication.INSTANCE, notificationId, Notification.FLAG_NO_CLEAR, builder, CXDebugFragment::class.java, Bundle(), PendingIntent.FLAG_CANCEL_CURRENT)
        }

        fun cancelDebugNotification(notificationId: Int) {
            CXNotificationManager.cancelNotify(CXBaseApplication.INSTANCE, notificationId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fs_fragment_debug, container, false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DebugAdapter(serverList, activity)
        listView.adapter = adapter
        addCustom.setOnClickListener(View.OnClickListener {
            val newEntity = ServerModel(editLabel.text.toString(), editUrl.text.toString(), false)
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
            if (!serverList.contains(newEntity)) {
                serverList.add(newEntity)
                saveUrlList()
                RxBus.post(ServerChangeEvent(newEntity))
                adapter.notifyDataSetChanged()
            }
            CXSystemUtil.hideKeyboard(activity)
        })

        clearCacheTV.setOnClickListener { CXIntentUtil.goToAppDetails(activity) }
    }

    class DebugAdapter(var list: List<ServerModel>, private val context: Context) : BaseAdapter() {
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): ServerModel {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
            val convertView = LayoutInflater.from(context).inflate(R.layout.fs_fragment_debug_item, parent, false)
            val radioButton = convertView.findViewById(R.id.radioButton) as RadioButton
            val textView = convertView.findViewById(R.id.textView) as TextView
            val urlEntity = getItem(position)
            radioButton.text = urlEntity.label
            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && !urlEntity.isSelected) {
                    urlEntity.isSelected = true
                    for (item in serverList) {
                        item.isSelected = item == urlEntity
                    }
                    saveUrlList()
                    RxBus.post(ServerChangeEvent(urlEntity))
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

    class ServerChangeEvent(var serverModel: ServerModel)

    class ServerModel(var label: String, var url: String, var isSelected: Boolean) {
        override fun equals(other: Any?): Boolean {
            if (other is ServerModel) {
                return !TextUtils.isEmpty(label) && label == other.label
            } else {
                return false
            }
        }

        override fun toString(): String {
            return "ServerModel{label='$label', url='$url', isSelected=$isSelected}"
        }

        override fun hashCode(): Int {
            var result = label.hashCode()
            result = 31 * result + url.hashCode()
            result = 31 * result + isSelected.hashCode()
            return result
        }
    }
}
