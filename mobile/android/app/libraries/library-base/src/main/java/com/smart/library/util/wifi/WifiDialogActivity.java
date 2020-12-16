/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smart.library.util.wifi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.smart.library.STInitializer;
import com.smart.library.util.STLogUtil;
import com.smart.library.util.STWifiUtil;
import com.smart.library.util.wifi.accesspoint.AccessPoint;

public class WifiDialogActivity extends Activity implements WifiDialog.WifiDialogListener, DialogInterface.OnDismissListener {

    private static final String TAG = "WifiDialogActivity";

    private static final int RESULT_CONNECTED = RESULT_FIRST_USER;
    private static final int RESULT_FORGET = RESULT_FIRST_USER + 1;

    private static final String KEY_ACCESS_POINT_STATE = "access_point_state";
    private static final String KEY_WIFI_CONFIGURATION = "wifi_configuration";

    /**
     * Boolean extra indicating whether this activity should connect to an access point on the
     * caller's behalf. If this is set to false, the caller should check
     * {@link #KEY_WIFI_CONFIGURATION} in the result data and save that using
     */
    @VisibleForTesting
    static final String KEY_CONNECT_FOR_CALLER = "connect_for_caller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Intent intent = getIntent();

        super.onCreate(savedInstanceState);

        final Bundle accessPointState = intent.getBundleExtra(KEY_ACCESS_POINT_STATE);
        AccessPoint accessPoint = null;
        if (accessPointState != null) {
            accessPoint = new AccessPoint(this, accessPointState);
        }

        WifiDialog dialog = WifiDialog.createModal(this, this, accessPoint, WifiConfigUiBase.MODE_CONNECT);
        dialog.show();
        dialog.setOnDismissListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onForget(WifiDialog dialog) {
        final AccessPoint accessPoint = dialog.getController().getAccessPoint();
        final WifiManager wifiManager = STWifiUtil.getWifiManager(STInitializer.application());
        if (accessPoint != null) {
            if (!accessPoint.isSaved()) {
                if (accessPoint.getNetworkInfo() != null && accessPoint.getNetworkInfo().getState() != NetworkInfo.State.DISCONNECTED) {
                    // Network is active but has no network ID - must be ephemeral.
                    // wifiManager.disableEphemeralNetwork(AccessPoint.convertToQuotedString(accessPoint.getSsidStr())); // todo
                } else {
                    // Should not happen, but a monkey seems to trigger it
                    Log.e(TAG, "Failed to forget invalid network " + accessPoint.getConfig());
                }
            } else {
                // wifiManager.forget(accessPoint.getConfig().networkId, null /* listener */); // todo
            }
        }

        Intent resultData = new Intent();
        if (accessPoint != null) {
            Bundle accessPointState = new Bundle();
            accessPoint.saveWifiState(accessPointState);
            resultData.putExtra(KEY_ACCESS_POINT_STATE, accessPointState);
        }
        setResult(RESULT_FORGET);
        finish();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSubmit(WifiDialog dialog) {
        final WifiConfiguration config = dialog.getController().getConfig();
        final AccessPoint accessPoint = dialog.getController().getAccessPoint();
        final WifiManager wifiManager = STWifiUtil.getWifiManager(STInitializer.application());

        // if (getIntent().getBooleanExtra(KEY_CONNECT_FOR_CALLER, true)) { // todo
        if (config == null) {
            if (accessPoint != null/* && accessPoint.isSaved()*/) {
                // wifiManager.connect(accessPoint.getConfig(), null *//* listener *//*);
                STLogUtil.w("[wifi]", "connectWifi accessPoint.getConfig()=" + accessPoint.getConfig());
                STWifiUtil.connectWifi(STInitializer.application(), wifiManager, accessPoint.getConfig());
            } else {
                STLogUtil.w("[wifi]", "config==null && accessPoint==null");
            }
        } else {
            // wifiManager.save(config, null /* listener */);
            if (accessPoint != null) {
                // accessPoint is null for "Add network"
                NetworkInfo networkInfo = accessPoint.getNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    // wifiManager.connect(config, null /* listener */);
                    STLogUtil.w("[wifi]", "connectWifi config=" + config);
                    STWifiUtil.connectWifi(STInitializer.application(), wifiManager, config);
                } else {
                    STLogUtil.w("[wifi]", "accessPoint!=null && networkInfo==null");
                }
            } else {
                STLogUtil.w("[wifi]", "accessPoint==null config=" + config);
                STWifiUtil.connectWifi(STInitializer.application(), wifiManager, config);
            }
        }
        //}

        Intent resultData = new Intent();
        if (accessPoint != null) {
            Bundle accessPointState = new Bundle();
            accessPoint.saveWifiState(accessPointState);
            resultData.putExtra(KEY_ACCESS_POINT_STATE, accessPointState);
        }
        if (config != null) {
            resultData.putExtra(KEY_WIFI_CONFIGURATION, config);
        }
        setResult(RESULT_CONNECTED, resultData);
        finish();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        finish();
    }
}
