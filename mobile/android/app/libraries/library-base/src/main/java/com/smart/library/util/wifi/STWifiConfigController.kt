@file:Suppress("DEPRECATION")

package com.smart.library.util.wifi

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.net.NetworkInfo.DetailedState
import android.net.ProxyInfo
import android.net.Uri
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiConfiguration.AuthAlgorithm
import android.net.wifi.WifiConfiguration.KeyMgmt
import android.net.wifi.WifiEnterpriseConfig
import android.net.wifi.WifiEnterpriseConfig.Eap
import android.net.wifi.WifiEnterpriseConfig.Phase2
import android.net.wifi.WifiInfo
import android.os.Build
import android.text.*
import android.text.style.TtsSpan.TelephoneBuilder
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView.OnEditorActionListener
import com.smart.library.R
import com.smart.library.util.STThreadUtils.post
import com.smart.library.util.STWifiUtil
import com.smart.library.util.STWifiUtil.convertToQuotedString
import com.smart.library.util.STWifiUtil.getSecurityString
import com.smart.library.util.STWifiUtil.getSummary
import java.security.KeyStore
import java.util.*
import java.util.regex.Pattern

@Suppress("MemberVisibilityCanBePrivate", "DEPRECATION", "SpellCheckingInspection", "unused")
class STWifiConfigController(private val configUi: STWifiConfigUiBase, private val contentView: View, val scanResult: ScanResult?, val mode: Int) : TextWatcher, OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, OnEditorActionListener, View.OnKeyListener {

    /**
     * For applications targeting {@link android.os.Build.VERSION_CODES#Q} or above, this API will return null
     */
    val oldWifiConfiguration: WifiConfiguration? by lazy { STWifiUtil.getWifiConfiguration(scanResult = scanResult) }
    private val isPasspointNetwork: Boolean by lazy { STWifiUtil.isPasspointNetwork(oldWifiConfiguration) }

    private val providerFriendlyName: String by lazy { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scanResult?.operatorFriendlyName?.toString() ?: "" else "" }
    private val isCarrierAp: Boolean by lazy { false } // 运营商网络
    private val ssidStr: String by lazy { STWifiUtil.removeDoubleQuotes(scanResult?.SSID ?: "") }
    private val hiddenSSID: Boolean by lazy { oldWifiConfiguration?.hiddenSSID ?: false }
    private val isSaved: Boolean by lazy { false } // 该网络是否已连接(已保存)
    private val security: Int by lazy { STWifiUtil.getSecurity(scanResult) }

    /* Phase2 methods supported by PEAP are limited */
    private val phase2PeapAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(context, android.R.layout.simple_spinner_item, context.resources.getStringArray(R.array.wifi_peap_phase2_entries_with_sim_auth)).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    /* Full list of phase2 methods */
    private val phase2FullAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.wifi_phase2_entries)).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    // e.g. AccessPoint.SECURITY_NONE
    private var accessPointSecurity: Int = STWifiUtils.SECURITY_NONE
    private val passwordView: TextView by lazy { contentView.findViewById(R.id.password) }
    private val unspecifiedCertString: String by lazy { context.getString(R.string.wifi_unspecified) }
    private val multipleCertSetString: String by lazy { context.getString(R.string.wifi_multiple_cert_added) }
    private val useSystemCertsString: String by lazy { context.getString(R.string.wifi_use_system_certs) }
    private val doNotProvideEapUserCertString: String by lazy { context.getString(R.string.wifi_do_not_provide_eap_user_cert) }
    private val doNotValidateEapServerString: String by lazy { context.getString(R.string.wifi_do_not_validate_eap_server) }
    private val dialogContainer: ScrollView by lazy { contentView.findViewById(R.id.dialog_scrollview) }
    private val securitySpinner: Spinner by lazy { contentView.findViewById(R.id.security) }
    private val eapMethodSpinner: Spinner by lazy {
        contentView.findViewById<Spinner>(R.id.method).apply {
            onItemSelectedListener = this@STWifiConfigController
        }
    }
    private val eapCaCertSpinner: Spinner by lazy {
        contentView.findViewById<Spinner>(R.id.ca_cert).apply {
            onItemSelectedListener = this@STWifiConfigController
        }
    }
    private val eapDomainView: TextView by lazy {
        contentView.findViewById<TextView>(R.id.domain).apply {
            addTextChangedListener(this@STWifiConfigController)
        }
    }
    private val phase2Spinner: Spinner by lazy {
        contentView.findViewById<Spinner>(R.id.phase2).apply {
            onItemSelectedListener = this@STWifiConfigController
        }
    }

    // Associated with mPhase2Spinner, one of mPhase2FullAdapter or mPhase2PeapAdapter
    private var phase2Adapter: ArrayAdapter<String>? = null
    private val eapUserCertSpinner: Spinner by lazy {
        contentView.findViewById<Spinner>(R.id.user_cert).apply {
            onItemSelectedListener = this@STWifiConfigController
        }
    }
    private val eapIdentityView: TextView by lazy { contentView.findViewById(R.id.identity) }
    private val eapAnonymousView: TextView by lazy { contentView.findViewById(R.id.anonymous) }
    private val ipSettingsSpinner: Spinner by lazy {
        contentView.findViewById<Spinner>(R.id.ip_settings).apply {
            onItemSelectedListener = this@STWifiConfigController
        }
    }
    private val ipAddressView: TextView by lazy { contentView.findViewById(R.id.ipaddress) }
    private val gatewayView: TextView by lazy { contentView.findViewById(R.id.gateway) }
    private val networkPrefixLengthView: TextView by lazy { contentView.findViewById(R.id.network_prefix_length) }
    private val dns1View: TextView by lazy { contentView.findViewById(R.id.dns1) }
    private val dns2View: TextView by lazy { contentView.findViewById(R.id.dns2) }
    private val proxySettingsSpinner: Spinner by lazy {
        contentView.findViewById<Spinner>(R.id.proxy_settings).apply {
            onItemSelectedListener = this@STWifiConfigController
        }
    }
    private val meteredSettingsSpinner: Spinner by lazy { contentView.findViewById(R.id.metered_settings) }
    private val hiddenSettingsSpinner: Spinner by lazy {
        contentView.findViewById<Spinner>(R.id.hidden_settings).apply {
            onItemSelectedListener = this@STWifiConfigController
        }
    }
    private val hiddenWarningView: TextView by lazy {
        contentView.findViewById<TextView>(R.id.hidden_settings_warning).apply {
            visibility = if (hiddenSettingsSpinner.selectedItemPosition == NOT_HIDDEN_NETWORK) View.GONE else View.VISIBLE
        }
    }
    private val proxyHostView: TextView by lazy { contentView.findViewById(R.id.proxy_hostname) }
    private val proxyPortView: TextView by lazy { contentView.findViewById(R.id.proxy_port) }
    private val proxyExclusionListView: TextView by lazy { contentView.findViewById(R.id.proxy_exclusionlist) }
    private val proxyPacView: TextView by lazy { contentView.findViewById(R.id.proxy_pac) }
    private val sharedCheckBox: CheckBox by lazy {
        contentView.findViewById<CheckBox>(R.id.shared).apply {
            visibility = View.GONE
        }
    }
    private val levels: Array<String> by lazy { resources.getStringArray(R.array.wifi_signal) }

    private var ipAssignment = IpAssignment.UNASSIGNED
    private var proxySettings = ProxySettings.UNASSIGNED
    private var httpProxy: ProxyInfo? = null

    private val ssidView: TextView by lazy { contentView.findViewById(R.id.ssid) }
    private val context: Context by lazy { configUi.getContext() }
    private val resources: Resources by lazy { context.resources }

    private fun addRow(group: ViewGroup, name: Int, value: String?) {
        val row = configUi.getLayoutInflater().inflate(R.layout.wifi_dialog_row, group, false)
        (row.findViewById<View>(R.id.name) as TextView).setText(name)
        (row.findViewById<View>(R.id.value) as TextView).text = value
        group.addView(row)
    }

    fun hideForgetButton() {
        configUi.getForgetButton()?.visibility = View.GONE
    }

    fun hideSubmitButton() {
        configUi.getSubmitButton()?.visibility = View.GONE
    }

    /* show submit button if password, ip and proxy settings are valid */
    fun enableSubmitIfAppropriate() {
        configUi.getSubmitButton()?.isEnabled = isSubmittable()
    }

    fun isValidPsk(password: String): Boolean {
        if (password.length == 64 && password.matches(Regex("[0-9A-Fa-f]{64}"))) {
            return true
        } else if (password.length in 8..63) {
            return true
        }
        return false
    }

    fun isSubmittable(): Boolean {
        var enabled: Boolean
        var passwordInvalid = false

        if ((accessPointSecurity == STWifiUtils.SECURITY_WEP && passwordView.length() == 0) || (accessPointSecurity == STWifiUtils.SECURITY_PSK && !isValidPsk(passwordView.text.toString()))) {
            passwordInvalid = true
        }
        enabled = if (scanResult == null && ssidView.length() == 0 ||
            ((scanResult == null || !isSaved) && passwordInvalid)
            || (scanResult != null && isSaved && passwordInvalid && passwordView.length() > 0)
        ) {
            false
        } else {
            ipAndProxyFieldsAreValid()
        }
        if (contentView.findViewById<View>(R.id.l_ca_cert).visibility != View.GONE) {
            val caCertSelection = eapCaCertSpinner.selectedItem as String
            if (caCertSelection == unspecifiedCertString) {
                // Disallow submit if the user has not selected a CA certificate for an EAP network
                // configuration.
                enabled = false
            }
            if (caCertSelection == useSystemCertsString && contentView.findViewById<View>(R.id.l_domain).visibility != View.GONE && TextUtils.isEmpty(eapDomainView.text.toString())) {
                // Disallow submit if the user chooses to use system certificates for EAP server
                // validation, but does not provide a domain.
                enabled = false
            }
        }
        if (contentView.findViewById<View>(R.id.l_user_cert).visibility != View.GONE && (eapUserCertSpinner.selectedItem as String == unspecifiedCertString)) {
            // Disallow submit if the user has not selected a user certificate for an EAP network
            // configuration.
            enabled = false
        }
        return enabled
    }

    //endregion
    private fun ipAndProxyFieldsAreValid(): Boolean {
        ipAssignment = if (ipSettingsSpinner.selectedItemPosition == STATIC_IP) IpAssignment.STATIC else IpAssignment.DHCP
        val selectedPosition = proxySettingsSpinner.selectedItemPosition
        proxySettings = ProxySettings.NONE
        httpProxy = null
        if (selectedPosition == PROXY_STATIC) {
            proxySettings = ProxySettings.STATIC
            val host = proxyHostView.text.toString()
            val portStr = proxyPortView.text.toString()
            val exclusionList = proxyExclusionListView.text.toString()
            var port = 0
            var result: Int
            try {
                port = portStr.toInt()
                result = proxySelectorValidate(host, portStr, exclusionList)
            } catch (e: NumberFormatException) {
                result = R.string.proxy_error_invalid_port
            }
            httpProxy = if (result == 0) {
                ProxyInfo.buildDirectProxy(host, port, listOf(*parseExclusionList(exclusionList)))
            } else {
                return false
            }
        } else if (selectedPosition == PROXY_PAC) {
            proxySettings = ProxySettings.PAC
            val uriSequence = proxyPacView.text
            if (TextUtils.isEmpty(uriSequence)) {
                return false
            }
            val uri = Uri.parse(uriSequence.toString()) ?: return false
            httpProxy = ProxyInfo.buildPacProxy(uri)
        }
        return true
    }

    private fun isSSIDTooLong(ssid: String): Boolean {
        return if (TextUtils.isEmpty(ssid)) {
            false
        } else ssid.length > SSID_ASCII_MAX_LENGTH
    }

    fun showWarningMessagesIfAppropriate() {
        contentView.findViewById<View>(R.id.no_ca_cert_warning).visibility = View.GONE
        contentView.findViewById<View>(R.id.no_domain_warning).visibility = View.GONE
        contentView.findViewById<View>(R.id.ssid_too_long_warning).visibility = View.GONE
        val ssid = ssidView.text.toString()
        if (isSSIDTooLong(ssid)) {
            contentView.findViewById<View>(R.id.ssid_too_long_warning).visibility = View.VISIBLE
        }
        if (contentView.findViewById<View>(R.id.l_ca_cert).visibility != View.GONE) {
            val caCertSelection = eapCaCertSpinner.selectedItem as String
            if (caCertSelection == doNotValidateEapServerString) {
                // Display warning if user chooses not to validate the EAP server with a
                // user-supplied CA certificate in an EAP network configuration.
                contentView.findViewById<View>(R.id.no_ca_cert_warning).visibility = View.VISIBLE
            }
            if (caCertSelection == useSystemCertsString && contentView.findViewById<View>(R.id.l_domain).visibility != View.GONE && TextUtils.isEmpty(eapDomainView.text.toString())) {
                // Display warning if user chooses to use pre-installed public CA certificates
                // without restricting the server domain that these certificates can be used to
                // validate.
                contentView.findViewById<View>(R.id.no_domain_warning).visibility = View.VISIBLE
            }
        }
    }// clear password// For security reasons, a previous password is not displayed to user.

    // do not support ca
    /*String caCert = (String) mEapCaCertSpinner.getSelectedItem();

     //region reflect
     Class<?>[] parameterTypes = new Class[1];
     Object[] params = new Object[1];
     parameterTypes[0] = String[].class;
     params[0] = null;
     STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypes, params);
     // config.enterpriseConfig.setCaCertificateAliases(null);
     //endregion

     //region reflect
     Class<?>[] parameterTypesForCaPath = new Class[1];
     Object[] paramsForCaPath = new Object[1];
     parameterTypesForCaPath[0] = String.class;
     paramsForCaPath[0] = null;
     STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypesForCaPath, paramsForCaPath);
     // config.enterpriseConfig.setCaPath(null);
     //endregion
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         config.enterpriseConfig.setDomainSuffixMatch(mEapDomainView.getText().toString());
     }

     if (caCert.equals(mUnspecifiedCertString) || caCert.equals(mDoNotValidateEapServerString)) {
         // ca_cert already set to null, so do nothing.
     } else if (caCert.equals(mUseSystemCertsString)) {
         Class<?>[] parameterTypesForCaPath2 = new Class[1];
         Object[] paramsForCaPath2 = new Object[1];
         parameterTypesForCaPath2[0] = String.class;
         paramsForCaPath2[0] = SYSTEM_CA_STORE_PATH;
         STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypesForCaPath2, paramsForCaPath2);
         // config.enterpriseConfig.setCaPath(SYSTEM_CA_STORE_PATH);
     } else if (caCert.equals(mMultipleCertSetString)) {
         if (mAccessPoint != null) {
             if (!mAccessPoint.isSaved()) {
                 Log.e(TAG, "Multiple certs can only be set " + "when editing saved network");
             }
             Class<?>[] parameterTypesAliases = new Class[1];
             Object[] paramsAliases = new Object[1];
             parameterTypesAliases[0] = String[].class;
             paramsAliases[0] = STReflectUtil.invokeDeclaredMethod(mAccessPoint.getWifiConfiguration().enterpriseConfig, "getCaCertificateAliases");
             STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypesAliases, paramsAliases);
             // config.enterpriseConfig.setCaCertificateAliases(mAccessPoint.getConfig().enterpriseConfig.getCaCertificateAliases());
         }
     } else {
         Class<?>[] parameterTypesAliases = new Class[1];
         Object[] paramsAliases = new Object[1];
         parameterTypesAliases[0] = String[].class;
         paramsAliases[0] = new String[]{caCert};
         STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypesAliases, paramsAliases);
         // config.enterpriseConfig.setCaCertificateAliases(new String[]{caCert});
     }

     // ca_cert or ca_path should not both be non-null, since we only intend to let
     // the use either their own certificate, or the system certificates, not both.
     // The variable that is not used must explicitly be set to null, so that a
     // previously-set value on a saved configuration will be erased on an update.
     */
    /*if (config.enterpriseConfig.getCaCertificateAliases() != null && config.enterpriseConfig.getCaPath() != null) {
                        Log.e(TAG, "ca_cert ("
                                + config.enterpriseConfig.getCaCertificateAliases()
                                + ") and ca_path ("
                                + config.enterpriseConfig.getCaPath()
                                + ") should not both be non-null");
                    }*/
    /*

                    String clientCert = (String) mEapUserCertSpinner.getSelectedItem();
                    if (clientCert.equals(mUnspecifiedCertString) || clientCert.equals(mDoNotProvideEapUserCertString)) {
                        // Note: |clientCert| should not be able to take the value |unspecifiedCert|,
                        // since we prevent such configurations from being saved.
                        clientCert = "";
                    }
                    Class<?>[] parameterTypesClientAliases = new Class[1];
                    Object[] paramsClientAliases = new Object[1];
                    parameterTypesClientAliases[0] = String.class;
                    paramsClientAliases[0] = clientCert;
                    STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setClientCertificateAlias", parameterTypesClientAliases, paramsClientAliases);
                    // config.enterpriseConfig.setClientCertificateAlias(clientCert);*/

    // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
    // If the user adds a network manually, assume that it is hidden.
    fun getConfig(): WifiConfiguration? {
        if (mode == STWifiConfigUiBase.MODE_VIEW) {
            return null
        }
        val config = WifiConfiguration()
        if (scanResult == null) {
            config.SSID = convertToQuotedString(ssidView.text.toString())
            // If the user adds a network manually, assume that it is hidden.
            config.hiddenSSID = hiddenSettingsSpinner.selectedItemPosition == HIDDEN_NETWORK
        } else if (!isSaved) {
            config.SSID = convertToQuotedString(ssidStr)
        } else {
            val networkId = oldWifiConfiguration?.networkId
            if (networkId != null) {
                config.networkId = networkId
            }
            config.hiddenSSID = hiddenSSID
        }
        when (accessPointSecurity) {
            STWifiUtils.SECURITY_NONE -> config.allowedKeyManagement.set(KeyMgmt.NONE)
            STWifiUtils.SECURITY_WEP -> {
                config.allowedKeyManagement.set(KeyMgmt.NONE)
                config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN)
                config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED)
                if (passwordView.length() != 0) {
                    val length = passwordView.length()
                    val password = passwordView.text.toString()
                    // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
                    if ((length == 10 || length == 26 || length == 58) && password.matches(Regex("[0-9A-Fa-f]*"))) {
                        config.wepKeys[0] = password
                    } else {
                        config.wepKeys[0] = '"'.toString() + password + '"'
                    }
                }
            }
            STWifiUtils.SECURITY_PSK -> {
                config.allowedKeyManagement.set(KeyMgmt.WPA_PSK)
                if (passwordView.length() != 0) {
                    val password = passwordView.text.toString()
                    if (password.matches(Regex("[0-9A-Fa-f]{64}"))) {
                        config.preSharedKey = password
                    } else {
                        config.preSharedKey = '"'.toString() + password + '"'
                    }
                }
            }
            STWifiUtils.SECURITY_EAP -> {
                config.allowedKeyManagement.set(KeyMgmt.WPA_EAP)
                config.allowedKeyManagement.set(KeyMgmt.IEEE8021X)
                config.enterpriseConfig = WifiEnterpriseConfig()
                val eapMethod = eapMethodSpinner.selectedItemPosition
                val phase2Method = phase2Spinner.selectedItemPosition
                config.enterpriseConfig.eapMethod = eapMethod
                when (eapMethod) {
                    Eap.PEAP -> when (phase2Method) {
                        WIFI_PEAP_PHASE2_NONE -> config.enterpriseConfig.phase2Method = Phase2.NONE
                        WIFI_PEAP_PHASE2_MSCHAPV2 -> config.enterpriseConfig.phase2Method = Phase2.MSCHAPV2
                        WIFI_PEAP_PHASE2_GTC -> config.enterpriseConfig.phase2Method = Phase2.GTC
                        WIFI_PEAP_PHASE2_SIM -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            config.enterpriseConfig.phase2Method = Phase2.SIM
                        }
                        WIFI_PEAP_PHASE2_AKA -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            config.enterpriseConfig.phase2Method = Phase2.AKA
                        }
                        WIFI_PEAP_PHASE2_AKA_PRIME -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            config.enterpriseConfig.phase2Method = Phase2.AKA_PRIME
                        }
                        else -> Log.e(TAG, "Unknown phase2 method$phase2Method")
                    }
                    else -> config.enterpriseConfig.phase2Method = phase2Method
                }

                // do not support ca
                /*String caCert = (String) mEapCaCertSpinner.getSelectedItem();

                 //region reflect
                 Class<?>[] parameterTypes = new Class[1];
                 Object[] params = new Object[1];
                 parameterTypes[0] = String[].class;
                 params[0] = null;
                 STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypes, params);
                 // config.enterpriseConfig.setCaCertificateAliases(null);
                 //endregion

                 //region reflect
                 Class<?>[] parameterTypesForCaPath = new Class[1];
                 Object[] paramsForCaPath = new Object[1];
                 parameterTypesForCaPath[0] = String.class;
                 paramsForCaPath[0] = null;
                 STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypesForCaPath, paramsForCaPath);
                 // config.enterpriseConfig.setCaPath(null);
                 //endregion
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                     config.enterpriseConfig.setDomainSuffixMatch(mEapDomainView.getText().toString());
                 }

                 if (caCert.equals(mUnspecifiedCertString) || caCert.equals(mDoNotValidateEapServerString)) {
                     // ca_cert already set to null, so do nothing.
                 } else if (caCert.equals(mUseSystemCertsString)) {
                     Class<?>[] parameterTypesForCaPath2 = new Class[1];
                     Object[] paramsForCaPath2 = new Object[1];
                     parameterTypesForCaPath2[0] = String.class;
                     paramsForCaPath2[0] = SYSTEM_CA_STORE_PATH;
                     STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypesForCaPath2, paramsForCaPath2);
                     // config.enterpriseConfig.setCaPath(SYSTEM_CA_STORE_PATH);
                 } else if (caCert.equals(mMultipleCertSetString)) {
                     if (mAccessPoint != null) {
                         if (!mAccessPoint.isSaved()) {
                             Log.e(TAG, "Multiple certs can only be set " + "when editing saved network");
                         }
                         Class<?>[] parameterTypesAliases = new Class[1];
                         Object[] paramsAliases = new Object[1];
                         parameterTypesAliases[0] = String[].class;
                         paramsAliases[0] = STReflectUtil.invokeDeclaredMethod(mAccessPoint.getWifiConfiguration().enterpriseConfig, "getCaCertificateAliases");
                         STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypesAliases, paramsAliases);
                         // config.enterpriseConfig.setCaCertificateAliases(mAccessPoint.getConfig().enterpriseConfig.getCaCertificateAliases());
                     }
                 } else {
                     Class<?>[] parameterTypesAliases = new Class[1];
                     Object[] paramsAliases = new Object[1];
                     parameterTypesAliases[0] = String[].class;
                     paramsAliases[0] = new String[]{caCert};
                     STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setCaCertificateAliases", parameterTypesAliases, paramsAliases);
                     // config.enterpriseConfig.setCaCertificateAliases(new String[]{caCert});
                 }

                 // ca_cert or ca_path should not both be non-null, since we only intend to let
                 // the use either their own certificate, or the system certificates, not both.
                 // The variable that is not used must explicitly be set to null, so that a
                 // previously-set value on a saved configuration will be erased on an update.
                 */
                /*if (config.enterpriseConfig.getCaCertificateAliases() != null && config.enterpriseConfig.getCaPath() != null) {
                                    Log.e(TAG, "ca_cert ("
                                            + config.enterpriseConfig.getCaCertificateAliases()
                                            + ") and ca_path ("
                                            + config.enterpriseConfig.getCaPath()
                                            + ") should not both be non-null");
                                }*/
                /*

                                String clientCert = (String) mEapUserCertSpinner.getSelectedItem();
                                if (clientCert.equals(mUnspecifiedCertString) || clientCert.equals(mDoNotProvideEapUserCertString)) {
                                    // Note: |clientCert| should not be able to take the value |unspecifiedCert|,
                                    // since we prevent such configurations from being saved.
                                    clientCert = "";
                                }
                                Class<?>[] parameterTypesClientAliases = new Class[1];
                                Object[] paramsClientAliases = new Object[1];
                                parameterTypesClientAliases[0] = String.class;
                                paramsClientAliases[0] = clientCert;
                                STReflectUtil.invokeDeclaredMethod(config.enterpriseConfig, "setClientCertificateAlias", parameterTypesClientAliases, paramsClientAliases);
                                // config.enterpriseConfig.setClientCertificateAlias(clientCert);*/
                if (eapMethod == Eap.SIM || eapMethod == Eap.AKA || eapMethod == Eap.AKA_PRIME) {
                    config.enterpriseConfig.identity = ""
                    config.enterpriseConfig.anonymousIdentity = ""
                } else if (eapMethod == Eap.PWD) {
                    config.enterpriseConfig.identity = eapIdentityView.text.toString()
                    config.enterpriseConfig.anonymousIdentity = ""
                } else {
                    config.enterpriseConfig.identity = eapIdentityView.text.toString()
                    config.enterpriseConfig.anonymousIdentity = eapAnonymousView.text.toString()
                }
                if (passwordView.isShown) {
                    // For security reasons, a previous password is not displayed to user.
                    // Update only if it has been changed.
                    if (passwordView.length() > 0) {
                        config.enterpriseConfig.password = passwordView.text.toString()
                    }
                } else {
                    // clear password
                    config.enterpriseConfig.password = passwordView.text.toString()
                }
            }
            else -> return null
        }
        return config
    }

    private fun showSecurityFields() {
        if (accessPointSecurity == STWifiUtils.SECURITY_NONE) {
            contentView.findViewById<View>(R.id.security_fields).visibility = View.GONE
            return
        }
        contentView.findViewById<View>(R.id.security_fields).visibility = View.VISIBLE
        passwordView.addTextChangedListener(this)
        passwordView.setOnEditorActionListener(this)
        passwordView.setOnKeyListener(this)
        (contentView.findViewById<View>(R.id.show_password) as CheckBox).setOnCheckedChangeListener(this)
        if (scanResult != null && isSaved) {
            passwordView.setHint(R.string.wifi_unchanged)
        }
        if (accessPointSecurity != STWifiUtils.SECURITY_EAP) {
            contentView.findViewById<View>(R.id.eap).visibility = View.GONE
            return
        }
        contentView.findViewById<View>(R.id.eap).visibility = View.VISIBLE
        // http://androidxref.com/9.0.0_r3/xref/frameworks/base/core/res/res/values/config.xml
        /*if (Utils.isWifiOnly(mContext)|| !mContext.getResources().getBoolean(R.bool.config_eap_sim_based_auth_supported)) {
            String[] eapMethods = mContext.getResources().getStringArray(R.array.eap_method_without_sim_auth);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, eapMethods);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEapMethodSpinner.setAdapter(spinnerAdapter);
        }*/
        if (scanResult != null && isCarrierAp) {
            eapMethodSpinner.setSelection(0)
        }
        loadCertificates(
            eapCaCertSpinner,
            CA_CERTIFICATE,
            doNotValidateEapServerString,
            false,
            showUsePreinstalledCertOption = true
        )
        loadCertificates(
            eapUserCertSpinner,
            USER_PRIVATE_KEY,
            doNotProvideEapUserCertString,
            false,
            showUsePreinstalledCertOption = false
        )

        // Modifying an existing network
        if (scanResult != null && isSaved) {
            eapMethodSpinner.setSelection(Eap.PEAP)
            showEapFieldsByMethod(WIFI_EAP_METHOD_PEAP)
            phase2Spinner.setSelection(WIFI_PEAP_PHASE2_NONE)
            setSelection(eapCaCertSpinner, doNotValidateEapServerString) // default not validate
            /*if (!TextUtils.isEmpty((CharSequence) STReflectUtil.invokeDeclaredMethod(enterpriseConfig, "getCaPath"))) {
                setSelection(mEapCaCertSpinner, mUseSystemCertsString);
            } else {
                String[] caCerts = (String[]) STReflectUtil.invokeDeclaredMethod(enterpriseConfig, "getCaCertificateAliases");
                if (caCerts == null) {
                    setSelection(mEapCaCertSpinner, mDoNotValidateEapServerString);
                } else if (caCerts.length == 1) {
                    setSelection(mEapCaCertSpinner, caCerts[0]);
                } else {
                    // Reload the cert spinner with an extra "multiple certificates added" item.
                    loadCertificates(
                            mEapCaCertSpinner,
                            Credentials.CA_CERTIFICATE,
                            mDoNotValidateEapServerString,
                            true,
                            true);
                    setSelection(mEapCaCertSpinner, mMultipleCertSetString);
                }
            }*/

            // mEapDomainView.setText((Integer) STReflectUtil.invokeDeclaredMethod(enterpriseConfig, "getDomainSuffixMatch"));
            val userCert: String? = null // enterpriseConfig.getClientCertificateAlias();
            if (TextUtils.isEmpty(userCert)) {
                setSelection(eapUserCertSpinner, doNotProvideEapUserCertString)
            } else {
                setSelection(eapUserCertSpinner, userCert)
            }
            // mEapIdentityView.setText(enterpriseConfig.getIdentity());
            // mEapAnonymousView.setText(enterpriseConfig.getAnonymousIdentity());
        } else {
            showEapFieldsByMethod(eapMethodSpinner.selectedItemPosition)
        }
    }

    /**
     * EAP-PWD valid fields include
     * identity
     * password
     * EAP-PEAP valid fields include
     * phase2: MSCHAPV2, GTC, SIM, AKA, AKA'
     * ca_cert
     * identity
     * anonymous_identity
     * password (not required for SIM, AKA, AKA')
     * EAP-TLS valid fields include
     * user_cert
     * ca_cert
     * domain
     * identity
     * EAP-TTLS valid fields include
     * phase2: PAP, MSCHAP, MSCHAPV2, GTC
     * ca_cert
     * identity
     * anonymous_identity
     * password
     */
    private fun showEapFieldsByMethod(eapMethod: Int) {
        // Common defaults
        contentView.findViewById<View>(R.id.l_method).visibility = View.VISIBLE
        contentView.findViewById<View>(R.id.l_identity).visibility = View.VISIBLE
        contentView.findViewById<View>(R.id.l_domain).visibility = View.VISIBLE

        // Defaults for most of the EAP methods and over-riden by
        // by certain EAP methods
        contentView.findViewById<View>(R.id.l_ca_cert).visibility = View.VISIBLE
        contentView.findViewById<View>(R.id.password_layout).visibility = View.VISIBLE
        contentView.findViewById<View>(R.id.show_password_layout).visibility = View.VISIBLE
        when (eapMethod) {
            WIFI_EAP_METHOD_PWD -> {
                setPhase2Invisible()
                setCaCertInvisible()
                setDomainInvisible()
                setAnonymousIdentInvisible()
                setUserCertInvisible()
            }
            WIFI_EAP_METHOD_TLS -> {
                contentView.findViewById<View>(R.id.l_user_cert).visibility = View.VISIBLE
                setPhase2Invisible()
                setAnonymousIdentInvisible()
                setPasswordInvisible()
            }
            WIFI_EAP_METHOD_PEAP -> {
                // Reset adapter if needed
                if (phase2Adapter !== phase2PeapAdapter) {
                    phase2Adapter = phase2PeapAdapter
                    phase2Spinner.adapter = phase2Adapter
                }
                contentView.findViewById<View>(R.id.l_phase2).visibility = View.VISIBLE
                contentView.findViewById<View>(R.id.l_anonymous).visibility = View.VISIBLE
                showPeapFields()
                setUserCertInvisible()
            }
            WIFI_EAP_METHOD_TTLS -> {
                // Reset adapter if needed
                if (phase2Adapter !== phase2FullAdapter) {
                    phase2Adapter = phase2FullAdapter
                    phase2Spinner.adapter = phase2Adapter
                }
                contentView.findViewById<View>(R.id.l_phase2).visibility = View.VISIBLE
                contentView.findViewById<View>(R.id.l_anonymous).visibility = View.VISIBLE
                setUserCertInvisible()
            }
            WIFI_EAP_METHOD_SIM, WIFI_EAP_METHOD_AKA, WIFI_EAP_METHOD_AKA_PRIME -> {
                setPhase2Invisible()
                setAnonymousIdentInvisible()
                setCaCertInvisible()
                setDomainInvisible()
                setUserCertInvisible()
                setPasswordInvisible()
                setIdentityInvisible()
                if (scanResult != null && isCarrierAp) {
                    setEapMethodInvisible()
                }
            }
        }
        if (contentView.findViewById<View>(R.id.l_ca_cert).visibility != View.GONE) {
            val eapCertSelection = eapCaCertSpinner.selectedItem as String
            if (eapCertSelection == doNotValidateEapServerString || eapCertSelection == unspecifiedCertString) {
                // Domain suffix matching is not relevant if the user hasn't chosen a CA
                // certificate yet, or chooses not to validate the EAP server.
                setDomainInvisible()
            }
        }
    }

    private fun showPeapFields() {
        val phase2Method = phase2Spinner.selectedItemPosition
        if (phase2Method == WIFI_PEAP_PHASE2_SIM || phase2Method == WIFI_PEAP_PHASE2_AKA || phase2Method == WIFI_PEAP_PHASE2_AKA_PRIME) {
            eapIdentityView.text = ""
            contentView.findViewById<View>(R.id.l_identity).visibility = View.GONE
            setPasswordInvisible()
        } else {
            contentView.findViewById<View>(R.id.l_identity).visibility = View.VISIBLE
            contentView.findViewById<View>(R.id.l_anonymous).visibility = View.VISIBLE
            contentView.findViewById<View>(R.id.password_layout).visibility = View.VISIBLE
            contentView.findViewById<View>(R.id.show_password_layout).visibility = View.VISIBLE
        }
    }

    private fun setIdentityInvisible() {
        contentView.findViewById<View>(R.id.l_identity).visibility = View.GONE
        phase2Spinner.setSelection(Phase2.NONE)
    }

    private fun setPhase2Invisible() {
        contentView.findViewById<View>(R.id.l_phase2).visibility = View.GONE
        phase2Spinner.setSelection(Phase2.NONE)
    }

    private fun setCaCertInvisible() {
        contentView.findViewById<View>(R.id.l_ca_cert).visibility = View.GONE
        setSelection(eapCaCertSpinner, unspecifiedCertString)
    }

    private fun setDomainInvisible() {
        contentView.findViewById<View>(R.id.l_domain).visibility = View.GONE
        eapDomainView.text = ""
    }

    private fun setUserCertInvisible() {
        contentView.findViewById<View>(R.id.l_user_cert).visibility = View.GONE
        setSelection(eapUserCertSpinner, unspecifiedCertString)
    }

    private fun setAnonymousIdentInvisible() {
        contentView.findViewById<View>(R.id.l_anonymous).visibility = View.GONE
        eapAnonymousView.text = ""
    }

    private fun setPasswordInvisible() {
        passwordView.text = ""
        contentView.findViewById<View>(R.id.password_layout).visibility = View.GONE
        contentView.findViewById<View>(R.id.show_password_layout).visibility = View.GONE
    }

    private fun setEapMethodInvisible() {
        contentView.findViewById<View>(R.id.eap).visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun showIpConfigFields() {
        contentView.findViewById<View>(R.id.ip_fields).visibility = View.VISIBLE
        if (ipSettingsSpinner.selectedItemPosition == STATIC_IP) {
            contentView.findViewById<View>(R.id.staticip).visibility = View.VISIBLE
            ipAddressView.addTextChangedListener(this)
            gatewayView.addTextChangedListener(this)
            networkPrefixLengthView.addTextChangedListener(this)
            dns1View.addTextChangedListener(this)
            dns2View.addTextChangedListener(this)
        } else {
            contentView.findViewById<View>(R.id.staticip).visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showProxyFields() {
        contentView.findViewById<View>(R.id.proxy_settings_fields).visibility = View.VISIBLE
        when (proxySettingsSpinner.selectedItemPosition) {
            PROXY_STATIC -> {
                setVisibility(R.id.proxy_warning_limited_support, View.VISIBLE)
                setVisibility(R.id.proxy_fields, View.VISIBLE)
                setVisibility(R.id.proxy_pac_field, View.GONE)
                proxyHostView.addTextChangedListener(this)
                proxyPortView.addTextChangedListener(this)
                proxyExclusionListView.addTextChangedListener(this)
            }
            PROXY_PAC -> {
                setVisibility(R.id.proxy_warning_limited_support, View.GONE)
                setVisibility(R.id.proxy_fields, View.GONE)
                setVisibility(R.id.proxy_pac_field, View.VISIBLE)
                proxyPacView.addTextChangedListener(this)
            }
            else -> {
                setVisibility(R.id.proxy_warning_limited_support, View.GONE)
                setVisibility(R.id.proxy_fields, View.GONE)
                setVisibility(R.id.proxy_pac_field, View.GONE)
            }
        }
    }

    private fun setVisibility(id: Int, visibility: Int) {
        val v = contentView.findViewById<View>(id)
        if (v != null) {
            v.visibility = visibility
        }
    }

    @Suppress("SameParameterValue", "ControlFlowWithEmptyBody", "UNUSED_PARAMETER")
    private fun loadCertificates(
        spinner: Spinner,
        prefix: String,
        noCertificateString: String,
        showMultipleCerts: Boolean,
        showUsePreinstalledCertOption: Boolean
    ) {
        val context = configUi.getContext()
        val certs = ArrayList<String>()
        certs.add(unspecifiedCertString)
        if (showMultipleCerts) {
            certs.add(multipleCertSetString)
        }
        if (showUsePreinstalledCertOption) {
            certs.add(useSystemCertsString)
        }
        try {
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            if (keyStore != null) {
                // certs.addAll(Arrays.asList(keyStore.list(prefix, WIFI_UID)));
            }
        } catch (e: Exception) {
            Log.e(TAG, "can't get the certificate list from KeyStore")
        }
        certs.add(noCertificateString)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, certs.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setSelection(spinner: Spinner, value: String?) {
        if (value != null) {
            val adapter = spinner.adapter as? ArrayAdapter<*>
            if (adapter != null) {
                for (i in adapter.count - 1 downTo 0) {
                    if (value == adapter.getItem(i)) {
                        spinner.setSelection(i)
                        break
                    }
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable) {
        post {
            showWarningMessagesIfAppropriate()
            enableSubmitIfAppropriate()
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // work done in afterTextChanged
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // work done in afterTextChanged
    }

    override fun onEditorAction(textView: TextView?, id: Int, keyEvent: KeyEvent?): Boolean {
        if (textView === passwordView) {
            if (id == EditorInfo.IME_ACTION_DONE && isSubmittable()) {
                configUi.dispatchSubmit()
                return true
            }
        }
        return false
    }

    override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
        if (view === passwordView) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && isSubmittable()) {
                configUi.dispatchSubmit()
                return true
            }
        }
        return false
    }

    override fun onCheckedChanged(view: CompoundButton, isChecked: Boolean) {
        if (view.id == R.id.show_password) {
            val pos = passwordView.selectionEnd
            passwordView.inputType = (InputType.TYPE_CLASS_TEXT or if (isChecked) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD)
            if (pos >= 0) (passwordView as EditText?)?.setSelection(pos)
        } else if (view.id == R.id.wifi_advanced_togglebox) {
            val advancedToggle = contentView.findViewById<View>(R.id.wifi_advanced_toggle)
            val toggleVisibility: Int
            val stringID: Int
            if (isChecked) {
                toggleVisibility = View.VISIBLE
                stringID = R.string.wifi_advanced_toggle_description_expanded
            } else {
                toggleVisibility = View.GONE
                stringID = R.string.wifi_advanced_toggle_description_collapsed
            }
            contentView.findViewById<View>(R.id.wifi_advanced_fields).visibility = toggleVisibility
            advancedToggle.contentDescription = context.getString(stringID)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent === securitySpinner) {
            accessPointSecurity = position
            showSecurityFields()
        } else if (parent === eapMethodSpinner || parent === eapCaCertSpinner) {
            showSecurityFields()
        } else if (parent === phase2Spinner
            && eapMethodSpinner.selectedItemPosition == WIFI_EAP_METHOD_PEAP
        ) {
            showPeapFields()
        } else if (parent === proxySettingsSpinner) {
            showProxyFields()
        } else if (parent === hiddenSettingsSpinner) {
            hiddenWarningView.visibility = if (position == NOT_HIDDEN_NETWORK) View.GONE else View.VISIBLE
            if (position == HIDDEN_NETWORK) {
                dialogContainer.post { dialogContainer.fullScroll(View.FOCUS_DOWN) }
            }
        } else {
            showIpConfigFields()
        }
        showWarningMessagesIfAppropriate()
        enableSubmitIfAppropriate()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //
    }

    /**
     * Make the characters of the password visible if show_password is checked.
     */
    fun updatePassword() {
        val passwordView = contentView.findViewById<View>(R.id.password) as TextView
        passwordView.inputType = (InputType.TYPE_CLASS_TEXT or if ((contentView.findViewById<View>(R.id.show_password) as CheckBox).isChecked) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD)
    }

    init {
        accessPointSecurity = security

        if (scanResult == null) { // new network
            configUi.setTitle(R.string.wifi_add_network)
            ssidView.addTextChangedListener(this)
            securitySpinner.onItemSelectedListener = this
            contentView.findViewById<View>(R.id.type).visibility = View.VISIBLE
            showIpConfigFields()
            showProxyFields()
            contentView.findViewById<View>(R.id.wifi_advanced_toggle).visibility = View.VISIBLE
            // Hidden option can be changed only when the user adds a network manually.
            contentView.findViewById<View>(R.id.hidden_settings_field).visibility = View.VISIBLE
            (contentView.findViewById<View>(R.id.wifi_advanced_togglebox) as CheckBox).setOnCheckedChangeListener(this)
            configUi.setSubmitButton(resources.getString(R.string.wifi_save))
        } else {
            if (!isPasspointNetwork) {
                val str = SpannableString(ssidStr)
                str.setSpan(TelephoneBuilder(ssidStr).build(), 0, ssidStr.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                configUi.setTitle(str)
            } else {
                var configName = ssidStr
                if (isPasspointNetwork) configName = providerFriendlyName
                configUi.setTitle(configName)
            }
            val group = contentView.findViewById<View>(R.id.info) as ViewGroup
            var showAdvancedFields: Boolean
            if (isSaved) {
                meteredSettingsSpinner.setSelection(0)
                hiddenSettingsSpinner.setSelection(NOT_HIDDEN_NETWORK)
                ipSettingsSpinner.setSelection(DHCP)
                sharedCheckBox.isEnabled = false
                @Suppress("UNUSED_VALUE")
                showAdvancedFields = true
                proxySettingsSpinner.setSelection(PROXY_NONE)
                if (isPasspointNetwork) addRow(group, R.string.passpoint_label, String.format(context.getString(R.string.passpoint_content), providerFriendlyName))
            }
            showAdvancedFields = false // force hide advanced fields default
            if (!isSaved && !isPasspointNetwork || this.mode != STWifiConfigUiBase.MODE_VIEW) {
                showSecurityFields()
                showIpConfigFields()
                showProxyFields()
                val advancedTogglebox = contentView.findViewById<View>(R.id.wifi_advanced_togglebox) as CheckBox
                contentView.findViewById<View>(R.id.wifi_advanced_toggle).visibility = if (isCarrierAp) View.GONE else View.VISIBLE
                advancedTogglebox.setOnCheckedChangeListener(this)
                advancedTogglebox.isChecked = showAdvancedFields
                contentView.findViewById<View>(R.id.wifi_advanced_fields).visibility = if (showAdvancedFields) View.VISIBLE else View.GONE
                if (isCarrierAp) addRow(group, R.string.wifi_carrier_connect, String.format(context.getString(R.string.wifi_carrier_content), "carrierName"))
            }
            if (this.mode == STWifiConfigUiBase.MODE_MODIFY) {
                configUi.setSubmitButton(resources.getString(R.string.wifi_save))
            } else if (this.mode == STWifiConfigUiBase.MODE_CONNECT) {
                configUi.setSubmitButton(resources.getString(R.string.wifi_connect))
            } else {
                val state: DetailedState? = null
                val signalStrength = STWifiUtil.getSignalStrength(scanResult)
                val signalLevel = if (signalStrength > -1 && signalStrength < levels.size) levels[signalStrength] else null
                if ((state == null || state == DetailedState.DISCONNECTED) && signalLevel != null) {
                    configUi.setSubmitButton(resources.getString(R.string.wifi_connect))
                } else {
                    if (state != null) {
                        var finalProviderFriendlyName: String? = null
                        if (isPasspointNetwork) finalProviderFriendlyName = this.providerFriendlyName
                        addRow(group, R.string.wifi_status, getSummary(configUi.getContext(), state, false, finalProviderFriendlyName))
                    }
                    if (signalLevel != null) {
                        addRow(group, R.string.wifi_signal, signalLevel)
                    }
                    val info: WifiInfo? = null
                    if (info != null && info.linkSpeed != -1) {
                        addRow(group, R.string.wifi_speed, String.format(resources.getString(R.string.link_speed), info.linkSpeed))
                    }
                    if (info != null && info.frequency != -1) {
                        val frequency = info.frequency
                        var band: String? = null
                        if (frequency >= STWifiUtil.LOWER_FREQ_24GHZ && frequency < STWifiUtil.HIGHER_FREQ_24GHZ) {
                            band = resources.getString(R.string.wifi_band_24ghz)
                        } else if (frequency >= STWifiUtil.LOWER_FREQ_5GHZ && frequency < STWifiUtil.HIGHER_FREQ_5GHZ) {
                            band = resources.getString(R.string.wifi_band_5ghz)
                        } else {
                            Log.e(TAG, "Unexpected frequency $frequency")
                        }
                        if (band != null) {
                            addRow(group, R.string.wifi_frequency, band)
                        }
                    }
                    addRow(group, R.string.wifi_security, getSecurityString(scanResult))
                    contentView.findViewById<View>(R.id.ip_fields).visibility = View.GONE
                }
                if (isSaved || isPasspointNetwork) {
                    configUi.setForgetButton(resources.getString(R.string.wifi_forget))
                }
            }
        }

        // custom force hide advanced layout, not support!!!
        contentView.findViewById<View>(R.id.wifi_advanced_toggle).visibility = View.GONE

        configUi.setCancelButton(resources.getString(R.string.wifi_cancel))

        if (configUi.getSubmitButton() != null) {
            enableSubmitIfAppropriate()
        }

        // After done view show and hide, request focus from parent view
        contentView.findViewById<View>(R.id.l_wifidialog).requestFocus()
    }


    companion object {
        private const val TAG = "WifiConfigController"
        private const val SYSTEM_CA_STORE_PATH = "/system/etc/security/cacerts"


        private const val SSID_ASCII_MAX_LENGTH = 32

        /**
         * Key prefix for CA certificates.
         */
        private const val CA_CERTIFICATE = "CACERT_"

        /**
         * Key prefix for user private and secret keys.
         */
        private const val USER_PRIVATE_KEY = "USRPKEY_"

        /* This value comes from "wifi_ip_settings" resource array */
        private const val DHCP = 0
        private const val STATIC_IP = 1

        /* Constants used for referring to the hidden state of a network. */
        const val HIDDEN_NETWORK = 1
        const val NOT_HIDDEN_NETWORK = 0

        /* These values come from "wifi_proxy_settings" resource array */
        const val PROXY_NONE = 0
        const val PROXY_STATIC = 1
        const val PROXY_PAC = 2

        /* These values come from "wifi_eap_method" resource array */
        const val WIFI_EAP_METHOD_PEAP = 0
        const val WIFI_EAP_METHOD_TLS = 1
        const val WIFI_EAP_METHOD_TTLS = 2
        const val WIFI_EAP_METHOD_PWD = 3
        const val WIFI_EAP_METHOD_SIM = 4
        const val WIFI_EAP_METHOD_AKA = 5
        const val WIFI_EAP_METHOD_AKA_PRIME = 6

        /* These values come from "wifi_peap_phase2_entries" resource array */
        const val WIFI_PEAP_PHASE2_NONE = 0
        const val WIFI_PEAP_PHASE2_MSCHAPV2 = 1
        const val WIFI_PEAP_PHASE2_GTC = 2
        const val WIFI_PEAP_PHASE2_SIM = 3
        const val WIFI_PEAP_PHASE2_AKA = 4
        const val WIFI_PEAP_PHASE2_AKA_PRIME = 5
        private fun parseExclusionList(exclusionList: String?): Array<String?> {
            return exclusionList?.toLowerCase(Locale.ROOT)?.split(",".toRegex())?.toTypedArray() ?: arrayOfNulls(0)
        }

        //region http://androidxref.com/9.0.0_r3/xref/frameworks/base/core/java/android/net/Proxy.java
        const val PROXY_VALID = 0
        const val PROXY_HOSTNAME_EMPTY = 1
        const val PROXY_HOSTNAME_INVALID = 2
        const val PROXY_PORT_EMPTY = 3
        const val PROXY_PORT_INVALID = 4
        const val PROXY_EXCL_LIST_INVALID = 5

        // Hostname / IP REGEX validation
        // Matches blank input, ips, and domain names
        private const val NAME_IP_REGEX = "[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*"
        private const val HOSTNAME_REGEXP = "^$|^$NAME_IP_REGEX$"
        private val HOSTNAME_PATTERN: Pattern by lazy { Pattern.compile(HOSTNAME_REGEXP) }
        private const val EXCL_REGEX = "[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*(\\.[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*)*"
        private const val EXCL_LIST_REGEXP = "^$|^$EXCL_REGEX(,$EXCL_REGEX)*$"
        private val EXCL_LIST_PATTERN: Pattern by lazy { Pattern.compile(EXCL_LIST_REGEXP) }

        /**
         * Validate syntax of hostname, port and exclusion list entries
         */
        fun proxyValidate(hostname: String, port: String, exclList: String): Int {
            val match = HOSTNAME_PATTERN.matcher(hostname)
            val listMatch = EXCL_LIST_PATTERN.matcher(exclList)
            if (!match.matches()) return PROXY_HOSTNAME_INVALID
            if (!listMatch.matches()) return PROXY_EXCL_LIST_INVALID
            if (hostname.isNotEmpty() && port.isEmpty()) return PROXY_PORT_EMPTY
            if (port.isNotEmpty()) {
                if (hostname.isEmpty()) return PROXY_HOSTNAME_EMPTY
                val portVal = try {
                    port.toInt()
                } catch (ex: NumberFormatException) {
                    return PROXY_PORT_INVALID
                }
                if (portVal <= 0 || portVal > 0xFFFF) return PROXY_PORT_INVALID
            }
            return PROXY_VALID
        }

        /**
         * validate syntax of hostname and port entries
         *
         * @return 0 on success, string resource ID on failure
         */
        fun proxySelectorValidate(hostname: String, port: String, exclList: String): Int {
            return when (proxyValidate(hostname, port, exclList)) {
                PROXY_VALID -> 0
                PROXY_HOSTNAME_EMPTY -> R.string.proxy_error_empty_host_set_port
                PROXY_HOSTNAME_INVALID -> R.string.proxy_error_invalid_host
                PROXY_PORT_EMPTY -> R.string.proxy_error_empty_port
                PROXY_PORT_INVALID -> R.string.proxy_error_invalid_port
                PROXY_EXCL_LIST_INVALID -> R.string.proxy_error_invalid_exclusion_list
                else -> {
                    // should neven happen
                    Log.e(TAG, "Unknown proxy settings error")
                    -1
                }
            }
        }

        enum class IpAssignment {
            /* Use statically configured IP settings. Configuration can be accessed with staticIpConfiguration */
            STATIC,

            /* Use dynamically configured IP settings */
            DHCP,

            /* no IP details are assigned, this is used to indicate that any existing IP settings should be retained */
            UNASSIGNED
        }

        enum class ProxySettings {
            /* No proxy is to be used. Any existing proxy settings should be cleared. */
            NONE,

            /* Use statically configured proxy. Configuration can be accessed with httpProxy. */
            STATIC,

            /* no proxy details are assigned, this is used to indicate that any existing proxy settings should be retained */
            UNASSIGNED,

            /* Use a Pac based proxy. */
            PAC
        }
    }

}