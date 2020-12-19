package com.smart.template.home.test

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.ScanResult
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.smart.library.STInitializer
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STDialogManager
import com.smart.library.util.STLogUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.STWifiUtil
import com.smart.library.util.STWifiUtil.getSecurityString
import com.smart.library.util.wifi.STWifiConfigUiBase
import com.smart.library.util.wifi.STWifiDialog
import com.smart.library.widget.recyclerview.STDividerItemDecoration
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import kotlinx.android.synthetic.main.final_wifi_fragment.*
import kotlinx.android.synthetic.main.final_wifi_fragment_item.view.*

class FinalWifiFragment : STBaseFragment() {

    private val loadingDialog: Dialog? by lazy { STDialogManager.createLoadingDialog(context) }
    private val dataList: MutableList<ScanResult> = arrayListOf()
    private var networkId: Int? = null
    private val application: Application by lazy { STInitializer.application()!! }
    private val connectivityManager: ConnectivityManager? by lazy { STWifiUtil.getConnectivityManager(application) }
    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                STLogUtil.d(TAG, "onUnavailable")
            }

            @Suppress("DEPRECATION")
            override fun onAvailable(network: Network) {
                STLogUtil.d(TAG, "onAvailable network=$network")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager?.bindProcessToNetwork(network)
                } else {
                    ConnectivityManager.setProcessDefaultNetwork(network)
                }
            }
        }
    }
    private val adapter: STRecyclerViewAdapter<ScanResult, STRecyclerViewAdapter.ViewHolder> by lazy {
        object : STRecyclerViewAdapter<ScanResult, STRecyclerViewAdapter.ViewHolder>(context, dataList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(layoutInflater.inflate(R.layout.final_wifi_fragment_item, parent, false))
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val scanResult: ScanResult = dataList[position]
                holder.itemView.nameTv.text = scanResult.SSID
                holder.itemView.bssIdTv.text = "bssid:${scanResult.BSSID}"
                holder.itemView.frequencyTv.text = "frequency:${scanResult.frequency}"
                holder.itemView.levelTv.text = "level:${scanResult.level}"
                holder.itemView.securityTypeTv.text = scanResult.capabilities + "(${getSecurityString(scanResult)})"
                holder.itemView.signalStrengthTv.text = STWifiUtil.getSpeedLabel(context, scanResult)
                holder.itemView.setOnClickListener {
                    showCustomViewDialog(scanResult, BottomSheet(LayoutMode.WRAP_CONTENT))
                }
                holder.itemView.setOnLongClickListener {
                    val finalContext = context
                    if (finalContext != null) {
                        STWifiDialog.createModal(finalContext, scanResult, STWifiConfigUiBase.MODE_CONNECT).show()
                    }
                    true
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.final_wifi_fragment, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(STDividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        disableWifiBtn.setOnClickListener {
            STWifiUtil.ensurePermissions(activity) {
                if (it) {
                    STWifiUtil.setWifiEnabled(application, false)
                }
            }
        }
        enableWifiBtn.setOnClickListener {
            STWifiUtil.ensurePermissions(activity) {
                if (it) {
                    STWifiUtil.setWifiEnabled(application, true)
                }
            }
        }

        disconnectBtn.setOnClickListener {
            STWifiUtil.disconnectWifi(application, networkId = networkId, networkCallback = networkCallback, removeWifi = true)
        }

        scanBtn.setOnClickListener {
            STLogUtil.i(TAG, "scan button clicked")
            STWifiUtil.ensurePermissions(activity) { allPermissionsGranted ->
                STLogUtil.i(TAG, "allPermissionsGranted=$allPermissionsGranted")
                if (allPermissionsGranted) {
                    if (STWifiUtil.checkIfNeedOpenLocationSettings(application)) {
                        return@ensurePermissions
                    }
                    loadingDialog?.show()

                    if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        STLogUtil.i(TAG, "checkSelfPermission success")
                        STWifiUtil.scanWifi(application, scanResultsListener = object : STWifiUtil.ScanResultsListener {
                            override fun onScanResults(scanResults: MutableList<ScanResult>) {
                                loadingDialog?.dismiss()
                                if (scanResults.isEmpty()) {
                                    STLogUtil.i(TAG, "SCAN RESULTS IT'S EMPTY")
                                    return
                                }
                                STLogUtil.i(TAG, "GOT SCAN RESULTS $scanResults")
                                adapter.removeAll()
                                adapter.add(scanResults.sortedByDescending { it.level })
                            }
                        })
                    } else {
                        STLogUtil.i(TAG, "checkSelfPermission failure")
                    }
                } else {
                    STToastUtil.show("need permission!")
                }
            }
        }
        scanBtn.callOnClick()
    }

    @SuppressLint("SetTextI18n", "MissingPermission", "NewApi")
    private fun showCustomViewDialog(scanResult: ScanResult, dialogBehavior: DialogBehavior = ModalDialog) {
        val context = context
        context ?: return

        val dialog: MaterialDialog = MaterialDialog(context, dialogBehavior).show {
            title(R.string.googleWifi)
            customView(R.layout.final_wifi_fragment_dialog, scrollable = true, horizontalPadding = true)
            positiveButton(R.string.connect) { dialog ->
                // Pull the password out of the custom view when the positive button is pressed
                val identityInput: EditText = dialog.getCustomView().findViewById(R.id.identity)
                val passwordInput: EditText = dialog.getCustomView().findViewById(R.id.password)
                val identity = identityInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                STLogUtil.w(TAG, "Build.VERSION.SDK_INT=${Build.VERSION.SDK_INT}")
                STLogUtil.w(TAG, "Build.VERSION_CODES.Q=${Build.VERSION_CODES.Q}")

                STLogUtil.w(TAG, "android < 10 connect wifi")

                networkId = STWifiUtil.connectWifi(
                    application = application,
                    scanResult = scanResult,
                    identity = identity,
                    password = password,
                    networkCallback = networkCallback
                )
            }
            negativeButton(android.R.string.cancel)
            lifecycleOwner(this@FinalWifiFragment)
            debugMode(false)
        }
        // Setup custom view content
        val customView = dialog.getCustomView()
        val signalStrengthTV: TextView = customView.findViewById(R.id.signalStrengthTV)
        val securityTV: TextView = customView.findViewById(R.id.securityTV)
        val ssidTV: TextView = customView.findViewById(R.id.ssidTV)
        signalStrengthTV.text = STWifiUtil.getSpeedLabel(context, scanResult)
        ssidTV.text = scanResult.SSID
        securityTV.text = getSecurityString(scanResult)
        val passwordInput: EditText = customView.findViewById(R.id.password)
        val showPasswordCheck: CheckBox = customView.findViewById(R.id.showPassword)
        showPasswordCheck.setOnCheckedChangeListener { _, isChecked ->
            passwordInput.inputType = if (!isChecked) InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT
            passwordInput.transformationMethod = if (!isChecked) PasswordTransformationMethod.getInstance() else null
        }
        STLogUtil.w(TAG, "scanResult=$scanResult")
    }

    @Suppress("RedundantOverride")
    override fun onDestroy() {
        super.onDestroy()
        // STWifiUtil.disconnectWifiAndroidQ(application, connectivityManager, onNetworkCallback)
    }

    companion object {
        private const val TAG = "[wifi]"

        @JvmStatic
        fun goTo(context: Context?) {
            STActivity.startActivity(context, FinalWifiFragment::class.java)
        }
    }
}