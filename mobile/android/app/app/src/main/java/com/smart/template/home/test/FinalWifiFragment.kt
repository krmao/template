package com.smart.template.home.test

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.*
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.smart.library.util.rx.permission.RxPermissions
import com.smart.library.widget.recyclerview.STDividerItemDecoration
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import com.thanosfisherman.wifiutils.LocationUtils
import com.thanosfisherman.wifiutils.Logger
import com.thanosfisherman.wifiutils.WifiConnectorBuilder
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionErrorCode
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionSuccessListener
import com.thanosfisherman.wifiutils.wifiScan.ScanResultsListener
import kotlinx.android.synthetic.main.final_wifi_fragment.*
import kotlinx.android.synthetic.main.final_wifi_fragment_item.view.*
import java.util.*


class FinalWifiFragment : STBaseFragment() {

    private val loadingDialog by lazy { STDialogManager.createLoadingDialog(context) }
    private val dataList: MutableList<ScanResult> = arrayListOf()
    private val application: Application by lazy { STInitializer.application()!! }
    private val connectivityManager: ConnectivityManager by lazy { application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    private val onNetworkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                STLogUtil.d(TAG, "onUnavailable")
            }

            @Suppress("DEPRECATION")
            override fun onAvailable(network: Network) {
                STLogUtil.d(TAG, "onAvailable network=$network")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(network);
                } else {
                    ConnectivityManager.setProcessDefaultNetwork(network);
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
                val itemData: ScanResult = dataList[position]
                holder.itemView.nameTv.text = itemData.SSID
                holder.itemView.bssIdTv.text = "bssid:${itemData.BSSID}"
                holder.itemView.frequencyTv.text = "frequency:${itemData.frequency}"
                holder.itemView.levelTv.text = "level:${itemData.level}"
                holder.itemView.securityTypeTv.text = getSecurityType(itemData.capabilities)
                holder.itemView.signalStrengthTv.text = getSignalStrengthType(WifiManager.calculateSignalLevel(itemData.level, 3))
                holder.itemView.setOnClickListener {
                    showCustomViewDialog(itemData, BottomSheet(LayoutMode.WRAP_CONTENT))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.final_wifi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(STDividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        WifiUtils.forwardLog(object : Logger {
            override fun log(priority: Int, tag: String?, message: String?) {
                STLogUtil.w(TAG, "priority=$priority, message=$message")
            }
        })

        disconnectBtn.setOnClickListener {
            val application = STInitializer.application()
            if (application != null) {
                WifiUtils.withContext(application)
                    .disconnect(object : DisconnectionSuccessListener {
                        override fun success() {
                            Toast.makeText(context, "Disconnect success!", Toast.LENGTH_SHORT).show()
                        }

                        override fun failed(errorCode: DisconnectionErrorCode) {
                            Toast.makeText(context, "Failed to disconnect: $errorCode", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }

        scanBtn.setOnClickListener {
            ensurePermissions(activity) { allPermissionsGranted ->
                if (allPermissionsGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                        if (LocationUtils.checkLocationAvailability(application) == LocationUtils.LOCATION_DISABLED) {
                            STLogUtil.e(TAG, "没有开启GPS定位")
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        }
                    }
                    loadingDialog?.show()
                    WifiUtils.withContext(application).scanWifi(object : ScanResultsListener {
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
                    }).start()
                } else {
                    STToastUtil.show("need permission!")
                }
            }
        }
        scanBtn.callOnClick()
    }

    // android9.0以上需要申请定位权限
    // android10.0需要申请新添加的隐私权限ACCESS_FINE_LOCATION详情见android官方10.0重大隐私权变更，如果还需要后台获取或者使用wifi api则还需要申请后台使用定位权限ACCESS_BACKGROUND_LOCATION
    // https://developer.android.google.cn/about/versions/10/privacy/
    private fun ensurePermissions(activity: Activity?, callback: (allPermissionsGranted: Boolean) -> Unit) {
        RxPermissions.ensurePermissions(activity, callback, android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.CHANGE_WIFI_STATE, android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(onNetworkCallback)
    }

    @SuppressLint("SetTextI18n")
    private fun showCustomViewDialog(scanResult: ScanResult, dialogBehavior: DialogBehavior = ModalDialog) {
        val context = context
        context ?: return

        val dialog: MaterialDialog = MaterialDialog(context, dialogBehavior).show {
            title(R.string.googleWifi)
            customView(R.layout.final_wifi_fragment_dialog, scrollable = true, horizontalPadding = true)
            positiveButton(R.string.connect) { dialog ->
                // Pull the password out of the custom view when the positive button is pressed
                val passwordInput: EditText = dialog.getCustomView().findViewById(R.id.password)
                val passwordString = passwordInput.text.toString().trim()
                val securityType = getSecurityType(scanResult.capabilities)
                STLogUtil.w(TAG, "Build.VERSION.SDK_INT=${Build.VERSION.SDK_INT}")
                STLogUtil.w(TAG, "Build.VERSION_CODES.Q=${Build.VERSION_CODES.Q}")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    STLogUtil.w(TAG, "android >= 10 requestNetwork")
                    // https://stackoverflow.com/questions/58769623/android-10-api-29-how-to-connect-the-phone-to-a-configured-network
                    val networkRequest: NetworkRequest = NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) //创建的是WIFI网络
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED) //网络不受限
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED) //信任网络，增加这个连个参数让设备连接wifi之后还联网
                        .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .setNetworkSpecifier(
                            WifiNetworkSpecifier.Builder()
                                .setSsid(scanResult.SSID)
                                .setBssid(MacAddress.fromString(scanResult.BSSID))
                                .setWpa2Passphrase(passwordString)
                                .build()
                        )
                        .build()
                    connectivityManager.requestNetwork(networkRequest, onNetworkCallback)
                } else {
                    STLogUtil.w(TAG, "android < 10 connect wifi")
                    if (securityType?.toUpperCase(Locale.getDefault())?.contains("WPS") == true) {
                        val builder: WifiConnectorBuilder.WifiUtilsBuilder = WifiUtils.withContext(application)
                        builder.connectWithWps(scanResult.BSSID, passwordString)
                            .setWpsTimeout(15000)
                            .onConnectionWpsResult { isSuccess ->
                                if (isSuccess) Toast.makeText(context, "WIFI ENABLED", Toast.LENGTH_SHORT).show() else Toast.makeText(context, "COULDN'T ENABLE WIFI", Toast.LENGTH_SHORT).show()
                            }
                            .start()
                        // builder.cancelAutoConnect(); // Canceling an ongoing connection
                        return@positiveButton
                    }
                    if (securityType?.toUpperCase(Locale.getDefault())?.contains("WPA") == true || securityType?.toUpperCase(Locale.getDefault())?.contains("WPA2") == true) {
                        val builder: WifiConnectorBuilder.WifiUtilsBuilder = WifiUtils.withContext(application)
                        builder
                            .connectWith(scanResult.SSID, scanResult.BSSID, passwordString)
                            .setTimeout(15000)
                            .onConnectionResult(object : ConnectionSuccessListener {
                                override fun success() {
                                    Toast.makeText(context, "SUCCESS!", Toast.LENGTH_SHORT).show()
                                }

                                override fun failed(errorCode: ConnectionErrorCode) {
                                    Toast.makeText(context, "EPIC FAIL!$errorCode", Toast.LENGTH_SHORT).show()
                                }
                            })
                            .start()
                        // builder.cancelAutoConnect(); // Canceling an ongoing connection
                    }
                }
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
        signalStrengthTV.text = getSignalStrengthType(WifiManager.calculateSignalLevel(scanResult.level, 3))
        ssidTV.text = scanResult.SSID
        securityTV.text = getSecurityType(scanResult.capabilities)
        val passwordInput: EditText = customView.findViewById(R.id.password)
        val showPasswordCheck: CheckBox = customView.findViewById(R.id.showPassword)
        showPasswordCheck.setOnCheckedChangeListener { _, isChecked ->
            passwordInput.inputType = if (!isChecked) InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT
            passwordInput.transformationMethod = if (!isChecked) PasswordTransformationMethod.getInstance() else null
        }
        STLogUtil.w(TAG, "scanResult=$scanResult")
    }

    // 0-2
    // https://github.com/paladinzh/decompile-hw/blob/4c3efd95f3e997b44dd4ceec506de6164192eca3/decompile/app/SystemUI/src/main/res/values-zh-rCN/strings.xml
    private fun getSignalStrengthType(level: Int): String? {
        return when (level) {
            2 -> "网络质量好"
            1 -> "网络质量一般"
            0 -> "网络质量不佳"
            else -> "网络质量未知"
        }
    }

    private fun getSecurityType(capabilities: String): String? {
        val canUseWPS: Boolean = capabilities.contains("WPS")
        return (if (capabilities.contains("WPA2") || capabilities.contains("wpa2")) {
            "WPA2"
        } else if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
            "WPA"
        } else {
            "NO PASSWORD"
        }) + (if (canUseWPS) "(可使用 WPS)" else "")
    }

    companion object {
        private const val TAG = "[wifi]"

        @JvmStatic
        fun goTo(context: Context?) {
            STActivity.startActivity(context, FinalWifiFragment::class.java)
        }
    }

}