package com.smart.library.util.wifi.settingslib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.smart.library.R;

public class Utils {

    static final int[] WIFI_PIE = {
            R.drawable.ic_wifi_signal_0,
            R.drawable.ic_wifi_signal_1,
            R.drawable.ic_wifi_signal_2,
            R.drawable.ic_wifi_signal_3,
            R.drawable.ic_wifi_signal_4
    };

    /**
     * Returns the Wifi icon resource for a given RSSI level.
     *
     * @param level The number of bars to show (0-4)
     * @throws IllegalArgumentException if an invalid RSSI level is given.
     */
    public static int getWifiIconResource(int level) {
        if (level < 0 || level >= WIFI_PIE.length) {
            throw new IllegalArgumentException("No Wifi icon found for level: " + level);
        }
        return WIFI_PIE[level];
    }

    /**
     * 是否仅支持 WIFI, 不支持 4G?
     * @param context
     * @return
     */
    public static boolean isWifiOnly(Context context) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return !(((ConnectivityManager) context.getSystemService(ConnectivityManager.class)).isNetworkSupported(ConnectivityManager.TYPE_MOBILE));
        } else {
            return false;
        }*/
        return false;
    }

    public boolean hasSystemFeature(PackageManager pm, String name) {
        return pm.hasSystemFeature(name);
    }
}
