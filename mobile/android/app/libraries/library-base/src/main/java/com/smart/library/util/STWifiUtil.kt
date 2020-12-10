package com.smart.library.util

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.provider.Settings
import androidx.annotation.RequiresPermission
import com.smart.library.STInitializer

/**
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 */
@Suppress("unused")
object STWifiUtil {
    const val TAG = "[wifi]"

    @JvmStatic
    fun openSystemWifiSettings(context: Context?) {
        context?.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    @JvmStatic
    fun getWifiManager(application: Application? = STInitializer.application()): WifiManager? {
        application ?: return null
        return application.getSystemService(WIFI_SERVICE) as? WifiManager
    }

    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    fun getWifiList(application: Application? = STInitializer.application()): List<ScanResult>? {
        val wifiManager = getWifiManager(application)
        wifiManager ?: return null
        return wifiManager.scanResults
    }

    @JvmStatic
    fun isWifiEnabled(application: Application? = STInitializer.application()): Boolean? {
        val wifiManager = getWifiManager(application)
        wifiManager ?: return null
        return wifiManager.isWifiEnabled
    }

    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    fun setWifiEnabled(application: Application? = STInitializer.application(), enable: Boolean) {
        val wifiManager = getWifiManager(application)
        wifiManager ?: return
        wifiManager.isWifiEnabled = enable
    }

    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    fun disconnectWifi(application: Application? = STInitializer.application(), netId: Int) {
        val wifiManager = getWifiManager(application)
        wifiManager ?: return
        wifiManager.disconnect()
        wifiManager.removeNetwork(netId)
    }

    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    fun connectWifi(application: Application? = STInitializer.application(), config: WifiConfiguration, netId: Int) {
        application ?: return
        val wifiManager = getWifiManager(application)
        wifiManager ?: return
        wifiManager.disconnect()
        wifiManager.addNetwork(config)
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()
    }


    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    fun addNetworkSuggestions(application: Application? = STInitializer.application(), networkSuggestions: List<WifiNetworkSuggestion?>): Int {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val wifiManager = getWifiManager(application)
            wifiManager ?: return -1
            return wifiManager.addNetworkSuggestions(networkSuggestions)
        } else {
            return -1
        }
    }

    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    fun getNetworkRequest(networkSpecifier: String?): NetworkRequest {
        return NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(networkSpecifier)
            .build()
    }

    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    fun requestNetwork(application: Application? = STInitializer.application(), networkRequest: NetworkRequest, networkCallback: ConnectivityManager.NetworkCallback) {
        getConnectivityManager(application)?.requestNetwork(networkRequest, networkCallback)
    }

    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
    fun getConnectivityManager(application: Application? = STInitializer.application()): ConnectivityManager? {
        return application?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

}