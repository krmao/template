package com.smart.library.util.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.smart.library.util.STLogUtil
import com.smart.library.util.rx.RxBus


@Suppress("DEPRECATION", "unused")
open class STNetworkChangedReceiver : BroadcastReceiver() {

    companion object {

        @Throws(Exception::class)
        fun register(context: Context, networkChangedReceiver: STNetworkChangedReceiver? = null): STNetworkChangedReceiver {
            val filter = IntentFilter()
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            val finalNetworkChangedReceiver = networkChangedReceiver ?: STNetworkChangedReceiver()
            context.registerReceiver(finalNetworkChangedReceiver, filter)
            return finalNetworkChangedReceiver
        }

        @Throws(Exception::class)
        fun unregister(context: Context, broadcastReceiver: BroadcastReceiver) {
            val filter = IntentFilter()
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            context.unregisterReceiver(broadcastReceiver)
        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        STLogUtil.v("[network] action=${intent.action}")
        RxBus.post(STNetworkChangedEvent(context, intent))
    }

}