package com.smart.library.reactnative

import android.app.Application
import android.content.Context
import com.facebook.soloader.SoLoader
import com.smart.library.STInitializer
import com.smart.library.reactnative.dev.RNDevSettingsView
import com.smart.library.util.STLogUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.image.impl.STImageFrescoHandler
import com.smart.library.util.okhttp.STOkHttpManager
import com.smart.library.widget.debug.STDebugFragment
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

@Suppress("unused", "PrivatePropertyName")
class RNBusHandler : STBusManager.IBusHandler {

    override fun onInitOnce(application: Application?, callback: ((success: Boolean) -> Unit)?) {
        SoLoader.init(application, false)
        STDebugFragment.childViewList.add(RNDevSettingsView::class.java)

        Flowable.fromCallable {
            val frescoConfig = STImageFrescoHandler.getConfigBuilder(application, STInitializer.debug(), STOkHttpManager.client).build()
            RNDeployManager.init(application, frescoConfig, callback)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun onUpgradeOnce(application: Application?) {

    }

    @Suppress("UNCHECKED_CAST")
    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "reactnative/open" -> {
                STLogUtil.w(RNInstanceManager.TAG, "reactnative/open busFunctionName:$busFunctionName, params:$params")

                val component: String = (params.getOrNull(0) as? String) ?: ""
                val page: String = (params.getOrNull(1) as? String) ?: ""
                val paramJsonObjectString: String = (params.getOrNull(2) as? String) ?: "{}"
                RNJumper.goTo(context, pageName = page, paramJsonObjectString = paramJsonObjectString, component = component)
            }
        }
    }

    override fun onAsyncCall(callback: ((key: Any?, value: Any?) -> Unit)?, context: Context?, busFunctionName: String, vararg params: Any) {

    }
}
