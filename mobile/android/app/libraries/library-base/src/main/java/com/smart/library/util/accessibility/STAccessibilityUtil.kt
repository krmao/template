package com.smart.library.util.accessibility

import android.content.Context
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.widget.Toast
import com.smart.library.util.STIntentUtil
import com.smart.library.util.STSystemUtil
import java.util.*

/**
 * https://github.com/fashare2015/ActivityTracker
 */
object STAccessibilityUtil {

    @JvmStatic
    @JvmOverloads
    fun checkAccessibility(context: Context?, autoOpenAccessibilitySettings: Boolean = false): Boolean {
        context ?: return false

        // 判断辅助功能是否开启
        if (!isAccessibilitySettingsOn(context)) {
            if (autoOpenAccessibilitySettings) {
                // 引导至辅助功能设置页面
                context.startActivity(STIntentUtil.getAccessibilitySettingsIntent())
                Toast.makeText(context, "请先开启 '${STSystemUtil.getAppName()}' 的辅助功能", Toast.LENGTH_LONG).show()
                STSystemUtil.closeSystemNotificationPanel()
            }
            return false
        }
        return true
    }

    private fun isAccessibilitySettingsOn(context: Context?): Boolean {
        context ?: return false

        var accessibilityEnabled = 0
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }
        if (accessibilityEnabled == 1) {
            val services = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (services != null) {
                return services.toLowerCase(Locale.getDefault()).contains(context.packageName.toLowerCase(Locale.getDefault()))
            }
        }
        return false
    }
}