package com.smart.library.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.smart.library.base.CXActivity
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused", "MemberVisibilityCanPrivate")
object CXRouteManager {
    private const val KEY_ID_CALLBACK = "id_callback"

    private val callbackMap: MutableMap<String, ((bundle: Bundle?) -> Unit?)?> = ConcurrentHashMap()

    private val interceptors: MutableSet<(activity: Activity?, uri: Uri?, bundle: Bundle?) -> Boolean> = Collections.synchronizedSet(HashSet())

    fun addInterceptor(interceptor: (activity: Activity?, uri: Uri?, bundle: Bundle?) -> Boolean) {
        interceptors.add(interceptor)
    }

    @JvmOverloads
    @Synchronized
    fun goToWeb(activity: Activity?, uri: Uri?, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        if (activity == null || activity.isFinishing || uri == null) {
            CXLogUtil.e("goToActivity failed, activity or activityName is invalid !")
            if (callback != null)
                callback(null)
            return
        }
        //val paramBundle = Bundle()
        //uri?.query?.split("&")?.map { it.split("=") }?.filter { it.size == 2 }?.forEach { paramBundle.putString(it[0], it[1]) }
        //return interceptors.any { it.invoke(activity, uri, paramBundle) }
    }

    @JvmOverloads
    @Synchronized
    fun goToActivity(activity: Activity?, activityName: String?, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        if (activity == null || activity.isFinishing || TextUtils.isEmpty(activityName)) {
            CXLogUtil.e("goToActivity failed, activity or activityName is invalid !")
            if (callback != null)
                callback(null)
            return
        }
        val intent = Intent().setClassName(activity, activityName?:"").putExtras(bundle ?: Bundle())
        if (callback != null) {
            val id = activityName + ":" + System.currentTimeMillis()
            callbackMap[id] = callback
            intent.putExtra(KEY_ID_CALLBACK, id)
        }
        CXLogUtil.v("callback size:" + callbackMap.size + " : " + callbackMap.keys)
        activity.startActivity(intent)
    }

    @JvmOverloads
    @Synchronized
    fun goToFragment(activity: Activity?, fragmentName: String?, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        if (activity == null || activity.isFinishing || fragmentName == null || TextUtils.isEmpty(fragmentName)) {
            CXLogUtil.e("goToFragment failed, activity or fragmentName is invalid !")
            if (callback != null)
                callback(null)
            return
        }

        //该类在atlas组件中
        /*if (AtlasBundleInfoManager.instance().bundleInfo.bundles.keys.any { fragmentName.startsWith(it) }) {
            val filterList = AtlasBundleInfoManager.instance().getUninstallBundles().filter { fragmentName.startsWith(it) }
            if (filterList.isNotEmpty()) {
                //该组件尚未被安装
                val location = filterList[0]
                val isInternalBundle = AtlasBundleInfoManager.instance().isInternalBundle(location)

                CXLogUtil.w("$location:isInternalBundle=$isInternalBundle")
                if (isInternalBundle) {
                    //该组件是内部组件
                    //val activity = ActivityTaskMgr.getInstance().peekTopActivity()
                    val dialog = RuntimeVariables.alertDialogUntilBundleProcessed(activity, location) ?: throw RuntimeException("alertDialogUntilBundleProcessed can not return null")

                    val activityActivitySize = ActivityTaskMgr.getInstance().sizeOfActivityStack()
                    val successTask = BundleUtil.CancelableTask(Runnable {
                        CXToastUtil.show(filterList.toString() + " 安装成功")
                        if (activity === ActivityTaskMgr.getInstance().peekTopActivity() || activityActivitySize == ActivityTaskMgr.getInstance().sizeOfActivityStack() + 1) {
                            goToFragmentInternal(activity, fragmentName, bundle, callback)
                        }
                        if (!activity.isFinishing && dialog.isShowing) {
                            dialog.dismiss()
                        }
                    })
                    val failedTask = BundleUtil.CancelableTask(Runnable {
                        CXToastUtil.show(filterList.toString() + " 安装失败")
                        if (!activity.isFinishing && dialog.isShowing) {
                            dialog.dismiss()
                        }
                    })
                    dialog.setOnDismissListener {
                        successTask.cancel()
                        failedTask.cancel()
                    }
                    if (Atlas.getInstance().getBundle(location) == null || Build.VERSION.SDK_INT < 22) {
                        if (!activity.isFinishing && dialog.isShowing) {
                            dialog.show()
                        }
                    }
                    BundleUtil.checkBundleStateAsync(location, successTask, failedTask)
                } else {
                    //该组件是远程组件
                    CXLogUtil.w("检测到远程组件:$location")
                    CXToastUtil.show("检测到远程组件:$location")

                    val remoteBundleFile = File(activity.externalCacheDir, "lib" + location.replace(".", "_") + ".so")
                    if (remoteBundleFile.exists()) {
                        val path = remoteBundleFile.absolutePath
                        val info = activity.packageManager.getPackageArchiveInfo(path, 0)
                        try {
                            @Suppress("DEPRECATION")
                            Atlas.getInstance().installBundle(info.packageName, File(path))
                            CXLogUtil.e("远程组件安装成功:$location")

                            val tmpBundle = Atlas.getInstance().getBundle(location) as BundleImpl?
                            if (tmpBundle == null || !tmpBundle.checkValidate()) {
                                CXLogUtil.e("远程组件校验失败:$location")
                            } else {
                                tmpBundle.startBundle()
                                goToFragmentInternal(activity, fragmentName, bundle, callback)
                            }
                        } catch (e: BundleException) {
                            CXLogUtil.e("远程组件安装失败::$location:" + e.message, e)
                            CXToastUtil.show("远程组件安装失败::$location:" + e.message)
                        }
                    } else {
                        CXToastUtil.show("远程组件不存在,请确定:" + remoteBundleFile.absolutePath)
                    }
                }
            } else if (Atlas.getInstance().bundles.any { fragmentName.startsWith(it.location) }) {
                //该组件已经被安装
                goToFragmentInternal(activity, fragmentName, bundle, callback)
            }
        } else {*/
        goToFragmentInternal(activity, fragmentName, bundle, callback)
//        }
    }

    private fun goToFragmentInternal(activity: Activity?, fragmentName: String?, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        val tmpBundle = bundle ?: Bundle()
        if (callback != null) {
            val id = fragmentName + ":" + System.currentTimeMillis()
            tmpBundle.putString(KEY_ID_CALLBACK, id)
            callbackMap[id] = callback
        }
        CXLogUtil.v("callback size:" + callbackMap.size + " : " + callbackMap.keys)
        CXActivity.start(activity, fragmentName, tmpBundle)
    }

    /*fun AtlasBundleInfoManager.getUninstallBundles(): List<String> {
        val installedBundles: List<String> = Atlas.getInstance().bundles.flatMap { listOf(it.location) }
        val allBundles: List<String> = AtlasBundleInfoManager.instance().bundleInfo.bundles.keys.toList()
        allBundles.forEach { CXLogUtil.d("allBundles: $it : isInternalBundle=" + AtlasBundleInfoManager.instance().isInternalBundle(it)) }
        CXLogUtil.v("installedBundles:" + installedBundles)
        val uninstallBundles = allBundles.minus(installedBundles)
        CXLogUtil.v("uninstallBundles:" + uninstallBundles)
        CXLogUtil.v("installedBundles:" + installedBundles)
        allBundles.forEach { CXLogUtil.d("allBundles: $it :" + AtlasBundleInfoManager.instance().isInternalBundle(it)) }
        return uninstallBundles
    }*/

    @Synchronized
    fun getCallback(fragment: Fragment?): ((bundle: Bundle?) -> Unit?)? = callbackMap[fragment?.arguments?.getString(KEY_ID_CALLBACK)]

    @Synchronized
    fun getCallback(activity: Activity?): ((bundle: Bundle?) -> Unit?)? = callbackMap[activity?.intent?.getStringExtra(KEY_ID_CALLBACK)]

    @Synchronized
    fun removeCallback(fragment: Fragment?) {
        val key = fragment?.arguments?.getString(KEY_ID_CALLBACK)
        if (!TextUtils.isEmpty(key))
            callbackMap.remove(key)
        CXLogUtil.v("callback size:" + callbackMap.size + " : " + callbackMap.keys)
    }

    @Synchronized
    fun removeCallback(activity: Activity?) {
        val key = activity?.intent?.getStringExtra(KEY_ID_CALLBACK)
        if (!TextUtils.isEmpty(key))
            callbackMap.remove(key)
        CXLogUtil.v("callback size:" + callbackMap.size + " : " + callbackMap.keys)
    }

    fun testGoToFragment(activity: Activity, fragmentName: String, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {

        goToFragment(activity, fragmentName, bundle)
        goToFragment(activity, fragmentName, bundle, callback)
        goToFragment(activity, fragmentName, bundle) {

        }
    }

    fun testGoToActivity(activity: Activity, activityName: String, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        goToActivity(activity, activityName, bundle)
        goToActivity(activity, activityName, bundle, callback)
        goToActivity(activity, activityName, bundle) {

        }
    }
}
