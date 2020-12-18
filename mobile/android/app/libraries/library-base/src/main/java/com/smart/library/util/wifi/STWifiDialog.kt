package com.smart.library.util.wifi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.util.STWifiUtil

/**
 * package com.android.settings.wifi; @link{ http://androidxref.com/9.0.0_r3/xref/packages/apps/Settings/src/com/android/settings/wifi/ }
 */
@SuppressLint("InflateParams")
@Suppress("unused")
class STWifiDialog @JvmOverloads constructor(context: Context, private val scanResult: ScanResult, private val mode: Int = STWifiConfigUiBase.MODE_CONNECT, style: Int, private val hideSubmitButton: Boolean) : AlertDialog(context, style), STWifiConfigUiBase, DialogInterface.OnClickListener {

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
        configController.hideForgetButton()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        configController.updatePassword()
    }

    override fun dispatchSubmit() {
        onSubmit()
        dismiss()
    }

    private fun onForget() {
        /*if (WifiUtils.isNetworkLockedDown(getContext(), mAccessPoint.getConfig())) {
           RestrictedLockUtils.sendShowAdminSupportDetailsIntent(getContext(), RestrictedLockUtils.getDeviceOwner(getContext()));
           return;
        }*/

        // val accessPoint = dialog.controller.accessPoint
        // val wifiManager = getWifiManager(STInitializer.application())
        // if (accessPoint != null) {
        //     if (accessPoint.isSaved) {
        //         // wifiManager.forget(accessPoint.getConfig().networkId, null /* listener */);
        //     }
        // }
    }

    @SuppressLint("MissingPermission")
    private fun onSubmit() {
        STWifiUtil.connectWifi(
            application = STInitializer.application(),
            scanResult = scanResult,
            wifiConfiguration = getController().getConfig()
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
        fun createModal(context: Context, scanResult: ScanResult, mode: Int, style: Int = 0): STWifiDialog {
            return STWifiDialog(context, scanResult, mode, style, mode == STWifiConfigUiBase.MODE_VIEW /* hideSubmitButton*/)
        }
    }
}