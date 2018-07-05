package com.smart.template.handlers

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.Promise
import com.smart.library.base.startActivityForResult
import com.smart.library.util.*
import com.smart.library.util.map.location.CXLocationManager
import com.smart.template.library.user.CXUserManager
import com.smart.template.module.rn.ReactActivity
import com.smart.template.module.rn.ReactManager
import com.smart.template.tab.HomeTabActivity

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
     *                  promise?.resolve(result or null)    :null is failure, otherwise is success , result:HashMap<String, String | Number>
     *                  promise?.reject("0", "functionName not found !")
     */
    override fun invoke(currentActivity: Activity?, functionName: String?, data: String?, promise: Promise?) {
        if (!TextUtils.isEmpty(functionName) && currentActivity != null && CXValueUtil.isValid(currentActivity)) {
            currentActivity.runOnUiThread(Runnable {
                val dataJsonObject = CXJsonUtil.toJSONObjectOrNull(data)
                when (functionName) {
                    "finish" -> {
                        val intent = Intent()
                        intent.putExtra(ReactActivity.KEY_RESULT, dataJsonObject?.toString())
                        currentActivity.setResult(Activity.RESULT_OK, intent)
                        currentActivity.finish()
                        promise?.resolve(null)
                        return@Runnable
                    }
                    "startForResult" -> {
                        val pageName: String? = dataJsonObject?.optString("pageName")
                        val requestCode: Int = dataJsonObject?.optInt("requestCode") ?: 0
                        val paramsMap: HashMap<String, Any> = CXJsonUtil.toMapOrNull(dataJsonObject?.optString("params")) ?: hashMapOf() // json to hashMap

                        when (pageName) {
                            "pageLogin" -> {
                                CXUserManager.goToLogin(currentActivity) {
                                    promise?.resolve(it)
                                }
                            }
                            "pageGoodsDetail" -> {
                                startActivityForResult(currentActivity, Intent(currentActivity, HomeTabActivity::class.java).apply {
                                    putExtra(ReactManager.KEY_RN_CALL_NATIVE_PARAMS_HASH_MAP, paramsMap)
                                }, requestCode, null) { _requestCode: Int, resultCode: Int, data: Intent? ->
                                    promise?.resolve(CXJsonUtil.toJson(data?.getSerializableExtra(ReactManager.KEY_RN_CALL_NATIVE_RESULT_HASH_MAP))) // hashMap to json
                                }
                            }
                            "pageGoodsList" -> {
                                ReactManager.instanceManager?.currentReactContext?.addActivityEventListener(object : BaseActivityEventListener() {
                                    override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
                                        super.onActivityResult(activity, requestCode, resultCode, data)
                                        // add code here

                                        ReactManager.instanceManager?.currentReactContext?.removeActivityEventListener(this)
                                    }
                                })
                                currentActivity.startActivity(Intent(currentActivity, HomeTabActivity::class.java))
                            }
                            else -> {
                                promise?.resolve(null)
                            }
                        }
                        return@Runnable
                    }
                    "mapNavigate" -> { // 打开/mapNavigate 返回值 Null
                        //TODO
                    }
                    "trace" -> { // 打点/dataCollection 返回值 Null
                        //TODO
                    }
                    "pay" -> { // 打点/dataCollection 返回值 Null
                        //TODO
                    }
                    "openPhone" -> { // 打点/dataCollection 返回值 Null
                        CXIntentUtil.openPhone(currentActivity, dataJsonObject?.optString("text"))

                        promise?.resolve(null)
                        return@Runnable
                    }
                    "toast" -> {
                        CXToastUtil.show(dataJsonObject?.optString("text"))

                        promise?.resolve(null)
                        return@Runnable
                    }
                    "getCacheLocation" -> {
                        promise?.resolve(CXJsonUtil.toJson(CXLocationManager.cacheLocation))
                        return@Runnable
                    }
                    "getUserInfo" -> {
                        promise?.resolve(CXJsonUtil.toJson(CXUserManager.userModel))
                        return@Runnable
                    }
                    "isLogin" -> {
                        promise?.resolve(CXUserManager.isLogin())
                        return@Runnable
                    }
                    else -> {
                        CXLogUtil.e(ReactManager.TAG, "code=0, functionName not found !")
                        promise?.reject("0", "functionName not found !")
                        return@Runnable
                    }
                }
                promise?.reject("0", "some params parse error!")
            })
        } else {
            promise?.reject("0", "functionName:" + functionName + " is empty or activity:" + (currentActivity == null) + " is null or finishing:" + currentActivity?.isFinishing)
        }
    }
}