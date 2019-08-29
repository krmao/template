package com.smart.library.flutter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout

@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
open class FlutterActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        val KEY_THEME = "KEY_THEME"
        @JvmStatic
        val KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS"
        @JvmStatic
        val KEY_FRAGMENT_ARGS = "KEY_FRAGMENT_ARGS"

        @JvmOverloads
        @JvmStatic
        fun start(from: Context?, fragmentClass: Class<*>, args: Bundle = Bundle()) =
                start(from, fragmentClass, args, 0)

        @JvmOverloads
        @JvmStatic
        fun startNewTask(fragmentClass: Class<*>, args: Bundle = Bundle()) =
                FlutterInitializer.application?.startActivity(getNewTaskIntent(FlutterInitializer.application, fragmentClass, args))

        @JvmOverloads
        @JvmStatic
        fun startSingleTask(from: Context?, fragmentClass: Class<*>, args: Bundle = Bundle()) =
                FlutterInitializer.application?.startActivity(getSingleTaskIntent(from, 0, fragmentClass, args))

        @JvmOverloads
        @JvmStatic
        fun start(activity: Context?, fragmentClass: Class<*>, args: Bundle = Bundle(), themResId: Int) =
                activity?.startActivity(getIntent(activity, themResId, fragmentClass, args))

        @JvmStatic
        fun start(activity: Context?, intent: Intent?) = activity?.startActivity(intent)

        @JvmOverloads
        @JvmStatic
        fun start(activity: Activity?, fragmentClassName: String?, args: Bundle = Bundle()) {
            activity?.startActivity(getIntent(activity, 0, fragmentClassName, args))
        }

        @JvmStatic
        fun startByCustomAnimation(activity: Activity?, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            activity?.startActivity(getIntent(activity, fragmentClass, args))
            activity?.overridePendingTransition(enterAnim, exitAnim)
        }


        @JvmStatic
        fun startForResultByCustomAnimation(activity: Activity?, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) =
                startForResultByCustomAnimation(activity, 0, requestCode, fragmentClass, args, enterAnim, exitAnim)

        @JvmStatic
        fun startForResultByCustomAnimation(activity: Activity?, themResId: Int, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResult(activity, themResId, requestCode, fragmentClass, args)
            activity?.overridePendingTransition(enterAnim, exitAnim)
        }

        @JvmStatic
        fun startForResultByCustomAnimation(fragment: Fragment, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) =
                startForResultByCustomAnimation(fragment, 0, requestCode, fragmentClass, args, enterAnim, exitAnim)

        @JvmStatic
        fun startForResultByCustomAnimation(fragment: Fragment, themResId: Int, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResult(fragment, themResId, requestCode, fragmentClass, args)
            fragment.activity?.overridePendingTransition(enterAnim, exitAnim)
        }

        //================================//================================//================================
        //base
        /* Activity页面发起的，再由Activity来接收结果 如果由Fragment来接收结果，需要使用 {@link #startForResult(Fragment, int, Class, Bundle)} */
        @JvmStatic
        fun startForResult(activity: Activity?, reqCode: Int, fragmentClass: Class<*>, args: Bundle = Bundle()) =
                startForResult(activity, 0, reqCode, fragmentClass, args)

        @JvmStatic
        fun startForResult(activity: Activity?, themResId: Int, reqCode: Int, fragmentClass: Class<*>, args: Bundle = Bundle()) =
                activity?.startActivityForResult(getIntent(activity, themResId, fragmentClass, args), reqCode)

        /* 由Fragment页面发起的，再由Fragment接收结果 */
        @JvmStatic
        fun startForResult(fragment: Fragment?, reqCode: Int, fragmentClass: Class<*>, args: Bundle = Bundle()) =
                startForResult(fragment, 0, reqCode, fragmentClass, args)

        /* 由Fragment页面发起的，再由Fragment接收结果 */
        @JvmStatic
        fun startForResult(fragment: Fragment?, themResId: Int, reqCode: Int, fragmentClass: Class<*>, args: Bundle = Bundle()) =
                fragment?.startActivityForResult(getIntent(fragment.activity, themResId, fragmentClass, args), reqCode)

        @JvmStatic
        fun getIntent(context: Context?, fragmentClass: Class<*>, args: Bundle): Intent? = getIntent(context, 0, fragmentClass, args)

        @JvmStatic
        fun getIntent(context: Context?, themResId: Int, fragmentClass: Class<*>, args: Bundle = Bundle()): Intent? = getIntent(context, themResId, fragmentClass.canonicalName, args)

        @JvmOverloads
        @JvmStatic
        fun getIntent(context: Context?, themResId: Int, fragmentClassName: String?, args: Bundle = Bundle()): Intent? {
            context ?: return null

            val intent = Intent(context, FlutterActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClassName)
            intent.putExtra(KEY_FRAGMENT_ARGS, args)
            if (themResId > 0) intent.putExtra(KEY_THEME, themResId)
            return intent
        }

        @JvmOverloads
        @JvmStatic
        fun getNewTaskIntent(context: Context?, fragmentClass: Class<*>, args: Bundle = Bundle(), themResId: Int = 0): Intent? {
            context ?: return null

            val intent = Intent(context, FlutterActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
            intent.putExtra(KEY_FRAGMENT_ARGS, args)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            if (themResId > 0) intent.putExtra(KEY_THEME, themResId)
            return intent
        }

        @JvmOverloads
        @JvmStatic
        fun getSingleTaskIntent(context: Context?, themResId: Int, fragmentClass: Class<*>, args: Bundle = Bundle()): Intent {
            val intent = Intent(context, FlutterActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
            intent.putExtra(KEY_FRAGMENT_ARGS, args)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            if (themResId > 0) intent.putExtra(KEY_THEME, themResId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            val args = intent.extras
            val themResId = args?.getInt(KEY_THEME, 0) ?: 0
            if (themResId > 0) setTheme(themResId)

            super.onCreate(savedInstanceState)

            setContentView(FrameLayout(this))

            var fragment: Fragment? = null
            val fragmentClassName: String
            val fragmentObject = args?.get(KEY_FRAGMENT_CLASS)

            if (fragmentObject is Class<*>) {
                fragmentClassName = fragmentObject.name
                fragment = fragmentObject.newInstance() as Fragment
            } else {
                fragmentClassName = fragmentObject as String
                try {
                    fragment = Class.forName(fragmentObject).newInstance() as Fragment
                } catch (_: Exception) {
                }
            }
            if (fragment != null) {
                fragment.arguments = args.getBundle(KEY_FRAGMENT_ARGS)
                supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            Log.e(FlutterActivity::javaClass.name, "Has error in new instance of fragment", e)
        }
    }

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
