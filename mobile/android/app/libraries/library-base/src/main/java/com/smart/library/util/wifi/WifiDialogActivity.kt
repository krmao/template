package com.smart.library.util.wifi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.wifi.ScanResult
import android.os.Bundle
import com.smart.library.STInitializer.application
import com.smart.library.util.STLogUtil
import com.smart.library.util.STLogUtil.w
import com.smart.library.util.STWifiUtil.connectWifi
import com.smart.library.util.STWifiUtil.getWifiManager
import com.smart.library.util.wifi.WifiDialog.WifiDialogListener

/**
 * package com.android.settings.wifi; @link{ http://androidxref.com/9.0.0_r3/xref/packages/apps/Settings/src/com/android/settings/wifi/ }
 */
class WifiDialogActivity : Activity(), WifiDialogListener, DialogInterface.OnDismissListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = intent
        super.onCreate(savedInstanceState)
        val scanResult = intent.getParcelableExtra<ScanResult>(KEY_SCAN_RESULT)
        var accessPoint: AccessPoint? = null
        if (scanResult != null) {
            accessPoint = AccessPoint(scanResult)
        }
        val dialog = WifiDialog.createModal(this, this, accessPoint, WifiConfigUiBase.MODE_CONNECT)
        dialog.show()
        dialog.setOnDismissListener(this)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onForget(dialog: WifiDialog) {
        val accessPoint = dialog.controller.accessPoint
        val wifiManager = getWifiManager(application())
        if (accessPoint != null) {
            if (!accessPoint.isSaved) {
                // Should not happen, but a monkey seems to trigger it
            } else {
                // wifiManager.forget(accessPoint.getConfig().networkId, null /* listener */); // todo
            }
        }
        finish()
    }

    @SuppressLint("MissingPermission")
    override fun onSubmit(dialog: WifiDialog) {
        val wifiConfiguration = dialog.controller.config
        if (wifiConfiguration != null) {
            w("[wifi]", "config!=null wifiConfiguration=$wifiConfiguration")
            connectWifi(
                application = application(),
                wifiConfiguration = wifiConfiguration,
                networkCallback = null
            )
        } else {
            w("[wifi]", "config==null config=$wifiConfiguration")
        }
        //}
        finish()
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        finish()
    }

    companion object {
        private const val TAG = "WifiDialogActivity"
        const val KEY_SCAN_RESULT = "KEY_SCAN_RESULT"

        @JvmStatic
        fun goTo(context: Context?, scanResult: ScanResult?) {
            /**
             * SSID: DEV, BSSID: 80:38:bc:1e:2e:b1, capabilities: [WPA2-EAP-CCMP][RSN-EAP-CCMP][K][V][ESS], level: -37, frequency: 5200, timestamp: 82753649303,
             * distance: ?(cm), distanceSd: ?(cm), passpoint: no, ChannelBandwidth: 0, centerFreq0: 0, centerFreq1: 0, 80211mcResponder: is not supported,
             * Carrier AP: no, Carrier AP EAP Type: -1, Carrier name: null, Radio Chain Infos: null
             */
            STLogUtil.w(TAG, "scanResult=$scanResult")
            context?.startActivity(Intent(context, WifiDialogActivity::class.java).apply {
                putExtra(KEY_SCAN_RESULT, scanResult)
            })
        }
    }
}