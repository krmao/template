package com.smart.library.reactnative.dev

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.smart.library.base.STBaseApplication
import com.smart.library.reactnative.R
import com.smart.library.reactnative.ReactConstant
import com.smart.library.reactnative.ReactJumper
import com.smart.library.reactnative.ReactManager
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import kotlinx.android.synthetic.main.rn_dev_settings_view.view.*

@SuppressLint("SetTextI18n")
class ReactDevSettingsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.rn_dev_settings_view, this)

        dev_cb.isChecked = ReactManager.devSettingsManager.devSettings?.isJSDevModeEnabled ?: STBaseApplication.DEBUG
        minify_cb.isChecked = ReactManager.devSettingsManager.devSettings?.isJSMinifyEnabled ?: false

        deltas_cb.isChecked = ReactManager.devSettingsManager.getJSBundleDeltas()
        fps_cb.isChecked = ReactManager.devSettingsManager.getAnimationsDebug()
        profiler_cb.isChecked = ReactManager.devSettingsManager.getStartSamplingProfilerOnInit()

        profiler_interval_et.setText(ReactManager.devSettingsManager.getSamplingProfilerSampleInterval().toString())
        host_et.setText(ReactManager.devSettingsManager.getDebugHttpHost())

        component_et.setText(ReactManager.devSettingsManager.getDefaultStartComponent())
        page_et.setText(ReactManager.devSettingsManager.getDefaultStartComponentPage())

        dev_cb.setOnCheckedChangeListener { _, isChecked ->
            ReactManager.devSettingsManager.setJSDevModeDebug(isChecked)
        }
        minify_cb.setOnCheckedChangeListener { _, isChecked ->
            ReactManager.devSettingsManager.setJSMinifyDebug(isChecked)
        }
        deltas_cb.setOnCheckedChangeListener { _, isChecked ->
            ReactManager.devSettingsManager.setJSBundleDeltas(isChecked)
        }
        fps_cb.setOnCheckedChangeListener { _, isChecked ->
            ReactManager.devSettingsManager.setAnimationsDebug(isChecked)
        }
        profiler_cb.setOnCheckedChangeListener { _, isChecked ->
            ReactManager.devSettingsManager.setStartSamplingProfilerOnInit(isChecked)
        }

        profiler_interval_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val interval = s.toString().toInt()
                    ReactManager.devSettingsManager.setSamplingProfilerSampleInterval(interval)
                } catch (e: NumberFormatException) {
                    STToastUtil.show("请输入正整数")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        clear_host_iv.setOnClickListener {
            host_et.setText("")
            save_tv.callOnClick()
        }
        host_tv.setOnLongClickListener {
            host_et.setText("10.47.62.17:8081")
            save_tv.callOnClick()
            true
        }

        save_tv.setOnClickListener {
            if (ReactManager.debug) {
                ReactManager.devSettingsManager.setDefaultStartComponent(component_et.text.toString().trim())
                ReactManager.devSettingsManager.setDefaultStartComponentPage(page_et.text.toString().trim())

                val host = host_et.text.toString().trim().replace(" ", "").replace("．", ".").replace("：", ":")
                if (!ReactManager.devSettingsManager.setDebugHttpHost(host)) {
                    STToastUtil.show("保存配置成功, IP 保存失败, 请填写有效格式")
                } else {
                    STToastUtil.show("保存配置成功")
                }
            } else {
                STToastUtil.show("保存配置失败, 当前非 debug 环境")
            }
        }

        start_tv.setOnClickListener {
            STSystemUtil.sendKeyDownEventBack(context)

            Looper.myQueue().addIdleHandler {
                ReactJumper.goTo(STBaseApplication.INSTANCE, intentFlag = Intent.FLAG_ACTIVITY_NEW_TASK)
                false
            }
        }

        back_to_offline_tv.setOnClickListener {
            STSystemUtil.sendKeyDownEventBack(context)

            ReactManager.reloadBundleFromOnlineToOffline()
        }

        show_rn_dev_dialog_tv.setOnClickListener {
            STSystemUtil.sendKeyDownEventBack(context)

            Looper.myQueue().addIdleHandler {
                ReactManager.devSettingsManager.showDevOptionsDialog()
                false
            }

        }

        rn_info_tv.text = "VERSION_RN_BASE: ${ReactConstant.VERSION_RN_BASE}\nVERSION_RN_CURRENT: ${ReactConstant.VERSION_RN_CURRENT}\t(注意:'-1'代表在线调试, '0'代表无有效离线包并且初始化失败)"
    }

}