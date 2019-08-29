package com.smart.library.flutter

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.taobao.idlefish.flutterboost.containers.BoostFlutterFragment
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.GeneratedPluginRegistrant

@Suppress("unused")
class FlutterFragment : BoostFlutterFragment() {

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
        fun startForResult(activity: Activity?, containerName: String, containerParams: HashMap<String, String> = hashMapOf(), requestCode: Int = 0) {
            FlutterActivity.startForResult(activity, requestCode, FlutterFragment::class.java, createArguments(containerName, containerParams))
        }

        @JvmStatic
        @JvmOverloads
        fun startForResult(fragment: Fragment?, containerName: String, containerParams: HashMap<String, String> = hashMapOf(), requestCode: Int = 0) {
            FlutterActivity.startForResult(fragment, requestCode, FlutterFragment::class.java, createArguments(containerName, containerParams))
        }

        @JvmStatic
        @JvmOverloads
        fun createFragment(containerName: String, containerParams: HashMap<String, String> = hashMapOf()): FlutterFragment {
            val fragment = FlutterFragment()
            fragment.arguments = createArguments(containerName, containerParams)
            return fragment
        }

        @JvmStatic
        @JvmOverloads
        fun createArguments(containerName: String, containerParams: HashMap<String, String> = hashMapOf()): Bundle {
            val arguments = Bundle()
            arguments.putString(KEY_CONTAINER_NAME, containerName)
            arguments.putSerializable(KEY_CONTAINER_PARAMS, containerParams)
            return arguments
        }
    }

    private val name: String? by lazy { arguments?.getString(KEY_CONTAINER_NAME) }
    private val params: Map<*, *>? by lazy { arguments?.get(KEY_CONTAINER_PARAMS) as? Map<*, *> }

    override fun getContainerName(): String? = name
    override fun getContainerParams(): Map<*, *>? = params
    override fun onRegisterPlugins(pluginRegistry: PluginRegistry) = GeneratedPluginRegistrant.registerWith(pluginRegistry)
    override fun destroyContainer() {}

}
