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
import com.smart.library.STInitializer
import com.smart.library.reactnative.R
import com.smart.library.reactnative.RNConstant
import com.smart.library.reactnative.RNJumper
import com.smart.library.reactnative.RNInstanceManager
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import kotlinx.android.synthetic.main.rn_dev_settings_view.view.*

@SuppressLint("SetTextI18n")
class RNDevSettingsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.rn_dev_settings_view, this)

        dev_cb.isChecked = RNInstanceManager.devSettingsManager.devSettings?.isJSDevModeEnabled ?: STInitializer.debug()
        minify_cb.isChecked = RNInstanceManager.devSettingsManager.devSettings?.isJSMinifyEnabled ?: false

        deltas_cb.isChecked = RNInstanceManager.devSettingsManager.getJSBundleDeltas()
        fps_cb.isChecked = RNInstanceManager.devSettingsManager.getAnimationsDebug()
        profiler_cb.isChecked = RNInstanceManager.devSettingsManager.getStartSamplingProfilerOnInit()

        profiler_interval_et.setText(RNInstanceManager.devSettingsManager.getSamplingProfilerSampleInterval().toString())
        host_et.setText(RNInstanceManager.devSettingsManager.getDebugHttpHost())

        component_et.setText(RNInstanceManager.devSettingsManager.getDefaultStartComponent())
        page_et.setText(RNInstanceManager.devSettingsManager.getDefaultStartComponentPage())

        dev_cb.setOnCheckedChangeListener { _, isChecked ->
            RNInstanceManager.devSettingsManager.setJSDevModeDebug(isChecked)
        }
        minify_cb.setOnCheckedChangeListener { _, isChecked ->
            RNInstanceManager.devSettingsManager.setJSMinifyDebug(isChecked)
        }
        deltas_cb.setOnCheckedChangeListener { _, isChecked ->
            RNInstanceManager.devSettingsManager.setJSBundleDeltas(isChecked)
        }
        fps_cb.setOnCheckedChangeListener { _, isChecked ->
            RNInstanceManager.devSettingsManager.setAnimationsDebug(isChecked)
        }
        profiler_cb.setOnCheckedChangeListener { _, isChecked ->
            RNInstanceManager.devSettingsManager.setStartSamplingProfilerOnInit(isChecked)
        }

        profiler_interval_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val interval = s.toString().toInt()
                    RNInstanceManager.devSettingsManager.setSamplingProfilerSampleInterval(interval)
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
            clearHostAndSave()
        }
        host_tv.setOnLongClickListener {
            host_et.setText("10.32.33.16:5387")
            save()
            true
        }

        enableRemoteDebugAndJump.setOnClickListener {
            if (host_et.text.toString().trim().isBlank()) {
                host_et.setText("10.32.33.16:5387")
            }
            save()
            RNInstanceManager.reloadBundle { success: Boolean ->
                if (success) {
                    STToastUtil.show("reload bundle success")
                    val page: String = page_et.text.toString().trim()
                    if (page.isNotBlank()) {
                        STToastUtil.show("检测到 page:$page, 执行跳转")
                        RNJumper.goTo(STInitializer.application(), intentFlag = Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                } else {
                    STToastUtil.show("reload bundle failure")
                }
            }
        }

        disableRemoteDebug.setOnClickListener {
            clearHostAndSave()
            RNInstanceManager.reloadBundleAndDisableRemoteDebug { success: Boolean ->
                if (success) {
                    STToastUtil.show("reload bundle success")
                    val page: String = page_et.text.toString().trim()
                    if (page.isNotBlank()) {
                        STToastUtil.show("检测到 page:$page, 执行跳转")
                        RNJumper.goTo(STInitializer.application(), intentFlag = Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                } else {
                    STToastUtil.show("reload bundle failure")
                }
            }
        }

        show_rn_dev_dialog_tv.setOnClickListener {
            STSystemUtil.sendKeyDownEventBack(context)

            Looper.myQueue().addIdleHandler {
                RNInstanceManager.devSettingsManager.showDevOptionsDialog()
                false
            }

        }

        rn_info_tv.text = "VERSION_RN_BASE: ${STInitializer.rnBaseVersion()}\nVERSION_RN_CURRENT: ${RNConstant.VERSION_RN_CURRENT}\t(注意:'-1'代表在线调试, '0'代表无有效离线包并且初始化失败)"
    }

    private fun save() {
        RNInstanceManager.devSettingsManager.setDefaultStartComponent(component_et.text.toString().trim())
        RNInstanceManager.devSettingsManager.setDefaultStartComponentPage(page_et.text.toString().trim())

        val host = host_et.text.toString().trim().replace(" ", "").replace("．", ".").replace("：", ":")
        if (!RNInstanceManager.devSettingsManager.setDebugHttpHost(host)) {
            STToastUtil.show("保存配置成功, IP 保存失败, 请填写有效格式")
        } else {
            STToastUtil.show("保存配置成功")
        }
    }

    private fun clearHostAndSave() {
        host_et.setText("")
        save()
    }

}