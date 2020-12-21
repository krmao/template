package com.smart.library.util.wifi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.ScanResult
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.util.STWifiUtil

/**
 * package com.android.settings.wifi; @link{ http://androidxref.com/9.0.0_r3/xref/packages/apps/Settings/src/com/android/settings/wifi/ }
 */
@SuppressLint("InflateParams")
@Suppress("unused", "DEPRECATION")
class STWifiDialog @JvmOverloads constructor(context: Context, private val scanResult: ScanResult, private val mode: Int = STWifiConfigUiBase.MODE_CONNECT, style: Int, private val hideSubmitButton: Boolean, private val networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {}) : AlertDialog(context, style), STWifiConfigUiBase, DialogInterface.OnClickListener {

    private val view: View by lazy { layoutInflater.inflate(R.layout.wifi_dialog, null) }
    private lateinit var configController: STWifiConfigController


    override fun onCreate(savedInstanceState: Bundle?) {
        setView(view)
        setInverseBackgroundForced(true)
        // must init before super.onCreate(savedInstanceState), because setButton only works here!!!
        configController = STWifiConfigController(this, view, scanResult, mode)
        super.onCreate(savedInstanceState)
        if (hideSubmitButton) {
            configController.hideSubmitButton()
        } else {
            /* During creation, the submit button can be unavailable to determine
             * visibility. Right after creation, update button visibility */
            configController.enableSubmitIfAppropriate()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        configController.updatePassword()
    }

    fun connectResult(): STWifiUtil.ConnectionResult? = STWifiUtil.getCachedWifiModel(scanResult)?.connectionResult

    fun disconnect() {
        STWifiUtil.disconnectWifi(STInitializer.application(), connectResult = STWifiUtil.getCachedWifiModel(scanResult)?.connectionResult, removeWifi = true)
    }

    override fun dispatchSubmit() {
        onSubmit()
        dismiss()
    }

    private fun onForget() {
        STWifiUtil.disconnectWifi(application = STInitializer.application(), connectResult = STWifiUtil.getCachedWifiModel(scanResult)?.connectionResult, removeWifi = true)
    }

    @SuppressLint("MissingPermission")
    private fun onSubmit() {
        val wifiConfiguration = getController().getConfig()
        var connectResult: STWifiUtil.ConnectionResult? = null

        val finalNetworkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                networkCallback.onAvailable(network)
                STWifiUtil.cacheWifiModel(scanResult, wifiConfiguration, connectResult)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onUnavailable() {
                super.onUnavailable()
                networkCallback.onUnavailable()
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                super.onBlockedStatusChanged(network, blocked)
                networkCallback.onBlockedStatusChanged(network, blocked)
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                networkCallback.onCapabilitiesChanged(network, networkCapabilities)
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties)
                networkCallback.onLinkPropertiesChanged(network, linkProperties)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                networkCallback.onLosing(network, maxMsToLive)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                networkCallback.onLost(network)
            }
        }

        connectResult = STWifiUtil.connectWifi(
            application = STInitializer.application(),
            scanResult = scanResult,
            wifiConfiguration = wifiConfiguration,
            networkCallback = finalNetworkCallback
        )
    }

    override fun onClick(dialogInterface: DialogInterface, id: Int) {
        when (id) {
            BUTTON_SUBMIT -> onSubmit()
            BUTTON_FORGET -> onForget()
        }
    }

    override fun getMode(): Int = mode
    override fun getController(): STWifiConfigController = this.configController
    override fun setSubmitButton(text: CharSequence) = setButton(BUTTON_SUBMIT, text, this)
    override fun setForgetButton(text: CharSequence) = setButton(BUTTON_FORGET, text, this)
    override fun setCancelButton(text: CharSequence) = setButton(BUTTON_NEGATIVE, text, this)
    override fun getCancelButton(): Button? = getButton(BUTTON_NEGATIVE)
    override fun getForgetButton(): Button? = getButton(BUTTON_FORGET)
    override fun getSubmitButton(): Button? = getButton(BUTTON_SUBMIT)

    companion object {
        private const val BUTTON_SUBMIT = BUTTON_POSITIVE
        private const val BUTTON_FORGET = BUTTON_NEUTRAL

        /**
         * Creates a Dialog with no additional style. It displays as a dialog above the current
         * view.
         */
        @JvmStatic
        @JvmOverloads
        fun createModal(context: Context, scanResult: ScanResult, mode: Int, style: Int = 0, networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {}): STWifiDialog {
            var finalModel: Int = mode
            val isConnected: Boolean = (STWifiUtil.removeDoubleQuotes(STWifiUtil.getConnectedWifiSSID(application = STInitializer.application())) == scanResult.SSID) && (STWifiUtil.getConnectedWifiBSSID(application = STInitializer.application()) == scanResult.BSSID)
            if (isConnected) {
                if (finalModel == STWifiConfigUiBase.MODE_CONNECT) {
                    finalModel = STWifiConfigUiBase.MODE_VIEW
                }
            }
            return STWifiDialog(context, scanResult, finalModel, style, finalModel == STWifiConfigUiBase.MODE_VIEW /* hideSubmitButton*/, networkCallback = networkCallback)
        }
    }
}