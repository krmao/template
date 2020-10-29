package com.smart.library.flutter

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.idlefish.flutterboost.containers.BoostFlutterActivity

@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
open class FlutterActivity : BoostFlutterActivity() {


    companion object {

        private const val KEY_CONTAINER_NAME = "KEY_CONTAINER_NAME"
        private const val KEY_CONTAINER_PARAMS = "KEY_CONTAINER_PARAMS"

        /**
         * @param containerName       当前Activity在Flutter层对应的name，
         *                            混合栈将会在flutter层根据这个名字，在注册的Route表中查找对应的Widget
         *                            在flutter层有注册函数：
         *                            <p>
         *                            FlutterBoost.singleton.registerPageBuilders({
         *                            'first': (pageName, params, _) => FirstRouteWidget(),
         *                            'second': (pageName, params, _) => SecondRouteWidget(),
         *                            ...
         *                            });
         *                            <p>
         *                            该方法中返回的就是注册的key：first , second
         * @param containerParams     该方法返回的参数将会传递给上层的flutter对应的Widget
         *                            <p>
         *                            在flutter层有注册函数：
         *                            FlutterBoost.singleton.registerPageBuilders({
         *                            'first': (pageName, params, _) => FirstRouteWidget(),
         *                            'second': (pageName, params, _) => SecondRouteWidget(),
         *                            ...
         *                            });
         *                            <p>
         *                            该方法返回的参数就会封装成上面的params
         * @return
         */
        @JvmStatic
        @JvmOverloads
        fun startForResult(activity: Activity?, containerName: String, containerParams: MutableMap<String, Any>? = hashMapOf(), requestCode: Int = 0) {
            activity?.startActivityForResult(createIntent(activity, containerName, containerParams), requestCode)
        }

        @JvmStatic
        @JvmOverloads
        fun createIntent(activity: Activity?, containerName: String, containerParams: MutableMap<String, Any>? = hashMapOf()): Intent {
            val intent = Intent(activity, FlutterActivity::class.java)
            intent.putExtra(KEY_CONTAINER_NAME, containerName)
            if (containerParams != null) {
                intent.putExtra(KEY_CONTAINER_PARAMS, HashMap<String, Any>(containerParams))
            }
            return intent
        }
    }

    private val name: String? by lazy { intent?.getStringExtra(KEY_CONTAINER_NAME) }
    @Suppress("UNCHECKED_CAST")
    private val params: HashMap<String, Any>? by lazy { intent?.getSerializableExtra(KEY_CONTAINER_PARAMS) as? HashMap<String, Any> }

//    override fun getContainerName(): String? = name
//    override fun getContainerParams(): Map<*, *>? = params
//    override fun onRegisterPlugins(pluginRegistry: PluginRegistry) = GeneratedPluginRegistrant.registerWith(pluginRegistry)
//    override fun destroyContainer() {}

    override fun getContainerUrl(): String? = name

    override fun getContainerUrlParams(): MutableMap<String, Any>? = params

    override fun onStart() {
        Log.w("FlutterActivity", "onStart:$taskId")
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        Log.w("FlutterActivity", "onRestart:$taskId")
    }

    override fun onResume() {
        super.onResume()
        Log.w("FlutterActivity", "onResume:$taskId")
    }

    override fun onPause() {
        super.onPause()
        Log.w("FlutterActivity", "onPause:$taskId")
    }

    override fun onStop() {
        super.onStop()
        Log.w("FlutterActivity", "onStop:$taskId")
    }
}
