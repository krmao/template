package com.smart.library.reactnative

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.facebook.react.bridge.Promise
import com.smart.library.util.*
// import com.smart.library.util.map.location.STLocationManager
import com.smart.template.library.user.STUserManager

/**
 * react native call native processors
 */
@Suppress("UNUSED_ANONYMOUS_PARAMETER", "UNUSED_VARIABLE")
class OnRNCallNativeHandler : Function4<Activity?, String?, String?, Promise?, Unit> {

    /**
     * @param functionName to native functions
     *
     * @param data
     *                  pageName    :String
     *                  requestCode :Int?   must be in [0, 65535]
     *                  params      :HashMap<String, String | Number>?
     *
     * @param promise
     *                  promise?.resolve(RNResult.successJson())
     *                  promise?.reject("0", "functionName not found !")
     */
    override fun invoke(currentActivity: Activity?, functionName: String?, data: String?, promise: Promise?) {
        if (!TextUtils.isEmpty(functionName) && currentActivity != null && STValueUtil.isValid(currentActivity)) {
            currentActivity.runOnUiThread(Runnable {
                val dataJsonObject = STJsonUtil.toJSONObjectOrNull(data)
                when (functionName) {
                    "finish" -> {
                        val intent = Intent()
                        intent.putExtra(ReactActivity.KEY_RESULT, dataJsonObject?.toString())
                        currentActivity.setResult(Activity.RESULT_OK, intent)
                        currentActivity.finish()
                        promise?.resolve(RNResult.successJson())
                        return@Runnable
                    }
                    "startForResult" -> {
                        val pageName: String? = dataJsonObject?.optString("pageName")
                        val requestCode: Int = dataJsonObject?.optInt("requestCode") ?: 0
                        val paramsMap: HashMap<String, Any> = STJsonUtil.toMapOrNull(dataJsonObject?.optString("params")) ?: hashMapOf() // json to hashMap

                        when (pageName) {
                            "pageLogin" -> {
                                STUserManager.goToLogin(currentActivity) {
                                    promise?.resolve(it)
                                }
                            }
                            "pageGoodsDetail" -> {
                                /*startActivityForResult(currentActivity, Intent(currentActivity, HomeTabActivity::class.java).apply {
                                    putExtra(ReactManager.KEY_RN_CALL_NATIVE_PARAMS_HASH_MAP, paramsMap)
                                }, requestCode, null) { _requestCode: Int, resultCode: Int, data: Intent? ->
                                    promise?.resolve(RNResult.resultJson(resultCode, data?.getSerializableExtra(ReactManager.KEY_RN_CALL_NATIVE_RESULT_HASH_MAP))) // hashMap to json
                                }*/
                            }
                            "pageGoodsList" -> {
                                /*ReactManager.instanceManager?.currentReactContext?.addActivityEventListener(object : BaseActivityEventListener() {
                                    override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
                                        super.onActivityResult(activity, requestCode, resultCode, data)
                                        // add code here
                                        promise?.resolve(RNResult.resultJson(resultCode, data?.getSerializableExtra(ReactManager.KEY_RN_CALL_NATIVE_RESULT_HASH_MAP))) // hashMap to json

                                        ReactManager.instanceManager?.currentReactContext?.removeActivityEventListener(this)
                                    }
                                })
                                currentActivity.startActivity(Intent(currentActivity, HomeTabActivity::class.java))*/
                            }
                            else -> {
                                promise?.resolve(null)
                            }
                        }
                        return@Runnable
                    }
                    "openPhone" -> { // 打点/dataCollection 返回值 Null
                        STIntentUtil.openPhone(currentActivity, dataJsonObject?.optString("text"))

                        promise?.resolve(RNResult.successJson())
                        return@Runnable
                    }
                    "toast" -> {
                        STToastUtil.show(dataJsonObject?.optString("text"))

                        promise?.resolve(RNResult.successJson())
                        return@Runnable
                    }
                    "getCacheLocation" -> {
                        // promise?.resolve(RNResult.successJson(STLocationManager.cacheLocation))
                        return@Runnable
                    }
                    "getUserInfo" -> {
                        promise?.resolve(RNResult.successJson(STUserManager.userModel))
                        return@Runnable
                    }
                    "isLogin" -> {
                        promise?.resolve(RNResult.resultJson(STUserManager.isLogin()))
                        return@Runnable
                    }
                    else -> {
                        STLogUtil.e(ReactManager.TAG, "code=0, functionName not found !")
                        promise?.reject("0", "functionName not found !")
                        return@Runnable
                    }
                }
            })
        } else {
            promise?.reject("0", "functionName:" + functionName + " is empty or activity:" + (currentActivity == null) + " is null or finishing:" + currentActivity?.isFinishing)
        }
    }

    /**
     *   {
     *      "resultCode": 0,
     *      "data"      : {}
     *   }
     */
    @Suppress("unused")
    private object RNResult {

        const val STATUS_SUCCESS = 0
        const val STATUS_FAILURE = -1

        @JvmStatic
        @JvmOverloads
        fun successJson(data: Any? = null) = STJsonUtil.toJson(hashMapOf("resultCode" to STATUS_SUCCESS, "data" to data))

        @JvmStatic
        @JvmOverloads
        fun failureJson(data: Any? = null) = STJsonUtil.toJson(hashMapOf("resultCode" to STATUS_FAILURE, "data" to data))

        @JvmStatic
        @JvmOverloads
        fun resultJson(success: Boolean, data: Any? = null) = STJsonUtil.toJson(hashMapOf("resultCode" to (if (success) STATUS_SUCCESS else STATUS_FAILURE), "data" to data))

        @JvmStatic
        @JvmOverloads
        fun resultJson(activityResultCode: Int, data: Any? = null) = STJsonUtil.toJson(hashMapOf("resultCode" to parseStatusFromActivityResultCode(activityResultCode), "data" to data))

        /**
         * @return STATUS_SUCCESS or STATUS_FAILURE
         */
        @JvmStatic
        private fun parseStatusFromActivityResultCode(activityResultCode: Int): Int = if (activityResultCode == Activity.RESULT_OK) STATUS_SUCCESS else STATUS_FAILURE
    }
}