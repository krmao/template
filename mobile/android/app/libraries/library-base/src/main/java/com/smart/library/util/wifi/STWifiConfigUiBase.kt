package com.smart.library.util.wifi

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import androidx.annotation.Keep

/**
 * Foundation interface glues between Activities and UIs like [STWifiDialog].
 */
//@Keep
interface STWifiConfigUiBase {
    fun getContext(): Context
    fun getController(): STWifiConfigController
    fun getLayoutInflater(): LayoutInflater
    fun getMode(): Int
    fun dispatchSubmit()
    fun setTitle(id: Int)
    fun setTitle(title: CharSequence?)
    fun setSubmitButton(text: CharSequence)
    fun setForgetButton(text: CharSequence)
    fun setCancelButton(text: CharSequence)
    fun getSubmitButton(): Button?
    fun getForgetButton(): Button?
    fun getCancelButton(): Button?

    companion object {
        /**
         * Viewing mode for a Wi-Fi access point. Data is displayed in non-editable mode.
         */
        const val MODE_VIEW = 0

        /**
         * Connect mode. Data is displayed in editable mode, and a connect button will be shown.
         */
        const val MODE_CONNECT = 1

        /**
         * Modify mode. All data is displayed in editable fields, and a "save" button is shown instead
         * of "connect". Clients are expected to only save but not connect to the access point in this
         * mode.
         */
        const val MODE_MODIFY = 2
    }
}