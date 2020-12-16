/*
 * Copyright (C) 2010 The Android Open Source Project
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smart.library.R;
import com.smart.library.util.wifi.accesspoint.AccessPoint;

import org.jetbrains.annotations.NotNull;

public class WifiDialog extends AlertDialog implements WifiConfigUiBase, DialogInterface.OnClickListener {

    public interface WifiDialogListener {
        void onForget(WifiDialog dialog);

        void onSubmit(WifiDialog dialog);
    }

    private static final int BUTTON_SUBMIT = DialogInterface.BUTTON_POSITIVE;
    private static final int BUTTON_FORGET = DialogInterface.BUTTON_NEUTRAL;

    private final int mMode;
    private final WifiDialogListener mListener;
    private final AccessPoint mAccessPoint;

    private WifiConfigController mController;
    private final boolean mHideSubmitButton;

    /**
     * Creates a WifiDialog with fullscreen style. It displays in fullscreen mode.
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    public static WifiDialog createFullscreen(Context context, WifiDialogListener listener, AccessPoint accessPoint, int mode) {
        return new WifiDialog(context, listener, accessPoint, mode, R.style.Theme_AppCompat_Dialog, false /* hideSubmitButton */);
    }

    /**
     * Creates a WifiDialog with no additional style. It displays as a dialog above the current
     * view.
     */
    public static WifiDialog createModal(Context context, WifiDialogListener listener, AccessPoint accessPoint, int mode) {
        return new WifiDialog(context, listener, accessPoint, mode, 0 /* style */, mode == WifiConfigUiBase.MODE_VIEW /* hideSubmitButton*/);
    }

    /* package */ WifiDialog(Context context, WifiDialogListener listener, AccessPoint accessPoint, int mode, int style, boolean hideSubmitButton) {
        super(context, style);
        mMode = mode;
        mListener = listener;
        mAccessPoint = accessPoint;
        mHideSubmitButton = hideSubmitButton;
    }

    @Override
    public WifiConfigController getController() {
        return mController;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.wifi_dialog, null);
        setView(view);
        setInverseBackgroundForced(true);
        mController = new WifiConfigController(this, view, mAccessPoint, mMode);
        super.onCreate(savedInstanceState);

        if (mHideSubmitButton) {
            mController.hideSubmitButton();
        } else {
            /* During creation, the submit button can be unavailable to determine
             * visibility. Right after creation, update button visibility */
            mController.enableSubmitIfAppropriate();
        }

        if (mAccessPoint == null) {
            mController.hideForgetButton();
        }
    }

    public void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mController.updatePassword();
    }

    @Override
    public void dispatchSubmit() {
        if (mListener != null) {
            mListener.onSubmit(this);
        }
        dismiss();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int id) {
        if (mListener != null) {
            switch (id) {
                case BUTTON_SUBMIT:
                    mListener.onSubmit(this);
                    break;
                case BUTTON_FORGET:
                    /*if (WifiUtils.isNetworkLockedDown(getContext(), mAccessPoint.getConfig())) {
                        RestrictedLockUtils.sendShowAdminSupportDetailsIntent(getContext(), RestrictedLockUtils.getDeviceOwner(getContext()));
                        return;
                    }*/
                    mListener.onForget(this);
                    break;
            }
        }
    }

    @Override
    public int getMode() {
        return mMode;
    }

    @Override
    public Button getSubmitButton() {
        return getButton(BUTTON_SUBMIT);
    }

    @Override
    public Button getForgetButton() {
        return getButton(BUTTON_FORGET);
    }

    @Override
    public Button getCancelButton() {
        return getButton(BUTTON_NEGATIVE);
    }

    @Override
    public void setSubmitButton(CharSequence text) {
        setButton(BUTTON_SUBMIT, text, this);
    }

    @Override
    public void setForgetButton(CharSequence text) {
        setButton(BUTTON_FORGET, text, this);
    }

    @Override
    public void setCancelButton(CharSequence text) {
        setButton(BUTTON_NEGATIVE, text, this);
    }
}
