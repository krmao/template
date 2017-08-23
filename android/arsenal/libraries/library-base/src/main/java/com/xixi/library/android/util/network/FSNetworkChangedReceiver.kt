package com.xixi.library.android.util.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.xixi.library.android.util.CXLogUtil

@Suppress("DEPRECATION")
class CXNetworkChangedReceiver : BroadcastReceiver() {
    private val TAG = javaClass.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        if (WifiManager.WIFI_STATE_CHANGED_ACTION == intent.action) {
            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)
            //val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //val gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            //val wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            when (wifiState) {
                WifiManager.WIFI_STATE_DISABLED -> if (mLastNetworkType == null)
                    CXLogUtil.w(TAG, "首次启动，多多关照....")
                else
                    CXLogUtil.e(TAG, ">>>>--------start-------->>>>")
                WifiManager.WIFI_STATE_DISABLING -> {
                }
                WifiManager.WIFI_STATE_ENABLED -> if (mLastNetworkType == null)
                    CXLogUtil.w(TAG, "首次启动，多多关照....")
                else
                    CXLogUtil.e(TAG, ">>>>--------start-------->>>>")
                WifiManager.WIFI_STATE_ENABLING -> {
                }
                WifiManager.WIFI_STATE_UNKNOWN -> if (mLastNetworkType == null)
                    CXLogUtil.w(TAG, "首次启动，多多关照....")
                else
                    CXLogUtil.e(TAG, ">>>>--------start-------->>>>")
            }
        }
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        /*if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                CXLogUtil.w(TAG, "网络状态变化广播  WIFI: " + wifi.isConnected() + "- GPRS: " + gprs.isConnected() + "- 当前状态: " + MNetworkUtil.getNetType(manager));
            }
        }*/
        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //val gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            //val wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val currentNetworkType = CXNetworkUtil.getNetType(manager)
            if (mLastNetworkType != null) {//第一次
                if (mLastNetworkType != currentNetworkType) {
                    // todo others
                    CXLogUtil.w(TAG, "呼叫下载处理逻辑")
                    //
                    CXLogUtil.e(TAG, "<<<<--------end--------<<<<")
                    mLastNetworkType = currentNetworkType
                }
            } else
                mLastNetworkType = currentNetworkType
        }
    }

    //"UnKnow" WIFI 2G 3G 4G ""
    var mLastNetworkType: CXNetworkUtil.MNetworkType? = null

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
}
