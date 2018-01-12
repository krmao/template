package com.smart.library.widget.debug

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
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
import com.smart.library.base.HKBaseApplication
import com.smart.library.base.HKBaseFragment
import com.smart.library.base.HKConfig
import com.smart.library.util.*
import com.smart.library.util.rx.RxBus
import kotlinx.android.synthetic.main.hk_fragment_debug.*

@Suppress("unused")
open class HKDebugFragment : HKBaseFragment() {
    companion object {
        private val KEY_TESTING_LIST = "KEY_TESTING_LIST"
        private var URLList: MutableList<URLModel> = HKPreferencesUtil.getList(KEY_TESTING_LIST, URLModel::class.java)

        fun add(vararg URLModels: URLModel) {
            if (URLModels.isNotEmpty()) {
                URLModels
                    .filterNot { URLList.contains(it) }
                    .forEach { URLList.add(it) }
                save()
            }
        }

        fun add(label: String, url: String, isSelected: Boolean = false) {
            if (!TextUtils.isEmpty(label) && !TextUtils.isEmpty(url))
                add(URLModel(label, url, isSelected))
        }

        fun save() {
            HKPreferencesUtil.putList(KEY_TESTING_LIST, URLList)
        }

        fun getCurUrl(): String {
            val tmpUrlList = URLList
            var url: String = tmpUrlList.firstOrNull { it.selected }?.url ?: ""
            if (TextUtils.isEmpty(url) && tmpUrlList.size > 0) {
                tmpUrlList[0].selected = true
                URLList = tmpUrlList
                url = tmpUrlList[0].url
                save()
            }
            return url
        }

        fun showDebugNotification(notificationId: Int) {
            val builder = NotificationCompat.Builder(HKBaseApplication.INSTANCE, "channel-0")
                .setSmallIcon(R.drawable.hk_emo_im_happy)
                .setContentTitle("车享家环境切换")
                .setContentText("点击跳转到环境切换页面")
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setOngoing(true)
            HKNotificationManager.showNotifyToFragment(HKBaseApplication.INSTANCE, notificationId, Notification.FLAG_NO_CLEAR, builder, HKDebugFragment::class.java, Bundle(), PendingIntent.FLAG_CANCEL_CURRENT)
        }

        fun cancelDebugNotification(notificationId: Int) = HKNotificationManager.cancelNotify(HKBaseApplication.INSTANCE, notificationId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.hk_fragment_debug, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trace_cb.isChecked = HKConfig.ENABLE_TRACE_DEBUG
        trace_cb.setOnCheckedChangeListener { _, isChecked -> HKConfig.ENABLE_TRACE_DEBUG = isChecked }

        val adapter = DebugAdapter(URLList, activity)
        listView.adapter = adapter
        addCustom.setOnClickListener(View.OnClickListener {
            val newEntity = URLModel(editLabel.text.toString(), editUrl.text.toString(), false)
            if (TextUtils.isEmpty(newEntity.name)) {
                HKToastUtil.show("请填写标签")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(newEntity.url)) {
                HKToastUtil.show("请填写服务地址")
                return@OnClickListener
            }
            editLabel.text = null
            editUrl.text = null
            if (!URLList.contains(newEntity)) {
                URLList.add(newEntity)
                save()
                RxBus.post(ChangeEvent(newEntity))
                adapter.notifyDataSetChanged()
            }
            HKSystemUtil.hideKeyboard(activity)
        })

        clearCacheTV.setOnClickListener { HKIntentUtil.goToAppDetails(activity) }
    }

    class DebugAdapter(private var urlList: List<URLModel>, private val context: Context?) : BaseAdapter() {
        override fun getCount(): Int = urlList.size

        override fun getItem(position: Int): URLModel = urlList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
            val convertView = LayoutInflater.from(context).inflate(R.layout.hk_fragment_debug_item, parent, false)
            val radioButton = convertView.findViewById(R.id.radioButton) as RadioButton
            val textView = convertView.findViewById(R.id.textView) as TextView
            val urlEntity = getItem(position)
            radioButton.text = urlEntity.name
            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && !urlEntity.selected) {
                    urlEntity.selected = true
                    for (item in URLList) {
                        item.selected = item == urlEntity
                    }
                    save()
                    RxBus.post(ChangeEvent(urlEntity))
                    notifyDataSetChanged()
                }
            }
            radioButton.isChecked = urlEntity.selected
            textView.text = urlEntity.url
            textView.setOnClickListener { HKToastUtil.show("长按复制") }
            textView.setOnLongClickListener {
                HKSystemUtil.copyToClipboard(urlEntity.name, urlEntity.url)
                HKToastUtil.show("已复制")
                true
            }
            return convertView
        }

    }

    class ChangeEvent(var model: URLModel)

    class URLModel(var name: String, var url: String, var selected: Boolean) {
        override fun equals(other: Any?): Boolean = (other is URLModel) && !TextUtils.isEmpty(name) && name == other.name

        override fun toString(): String = "URLModel{name='$name', url='$url', selected=$selected}"

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + url.hashCode()
            result = 31 * result + selected.hashCode()
            return result
        }
    }
}
