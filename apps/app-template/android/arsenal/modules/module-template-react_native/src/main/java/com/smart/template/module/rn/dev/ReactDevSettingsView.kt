package com.smart.template.module.rn.dev

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXSystemUtil
import com.smart.library.util.CXToastUtil
import com.smart.template.module.rn.R
import com.smart.template.module.rn.ReactActivity
import com.smart.template.module.rn.ReactJumper
import com.smart.template.module.rn.ReactManager
import kotlinx.android.synthetic.main.rn_dev_settings_view.view.*

class ReactDevSettingsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.rn_dev_settings_view, this)

        dev_cb.isChecked = ReactManager.devSettingsManager.devSettings?.isJSDevModeEnabled ?: CXBaseApplication.DEBUG
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
                    CXToastUtil.show("请输入正整数")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        host_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ReactManager.devSettingsManager.setDebugHttpHost(s.toString())
                //if (!ReactManager.devSettingsManager.setDebugHttpHost(s.toString())) {
                //    CXToastUtil.show("设置 ip & port 失败, 正确的格式为 0.0.0.0:8081")
                //}
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        reload_tv.setOnClickListener {
            CXSystemUtil.sendKeyDownEventBack(context)

            Looper.myQueue().addIdleHandler {
                ReactManager.devSettingsManager.reload()
                false
            }
        }

        component_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ReactManager.devSettingsManager.setDefaultStartComponent(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        page_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ReactManager.devSettingsManager.setDefaultStartComponentPage(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        start_tv.setOnClickListener {
            CXSystemUtil.sendKeyDownEventBack(context)

            Looper.myQueue().addIdleHandler {
                ReactJumper.goTo(context, intentFlag = Intent.FLAG_ACTIVITY_NEW_TASK)
                false
            }
        }

        show_rn_dev_dialog_tv.setOnClickListener {
            CXSystemUtil.sendKeyDownEventBack(context)

            Looper.myQueue().addIdleHandler {
                ReactManager.devSettingsManager.showDevOptionsDialog()
                false
            }

        }
    }

}
