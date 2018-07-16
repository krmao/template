package com.smart.library.util.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.smart.library.util.CXLogUtil
import com.smart.library.util.rx.RxBus


class CXNetworkChangedReceiver : BroadcastReceiver() {

    companion object {

        @Throws(Exception::class)
        fun register(context: Context): CXNetworkChangedReceiver {
            val filter = IntentFilter()
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            val networkChangedReceiver = CXNetworkChangedReceiver()
            context.registerReceiver(networkChangedReceiver, filter)
            return networkChangedReceiver
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
        CXLogUtil.v("[network] action=${intent.action}")
        RxBus.post(CXNetworkChangedEvent(context, intent))
    }

}