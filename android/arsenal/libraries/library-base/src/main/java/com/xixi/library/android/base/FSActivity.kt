package com.xixi.library.android.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.taobao.atlas.framework.Atlas
//import android.taobao.atlas.framework.Atlas
import android.util.Log
import android.widget.FrameLayout
import com.xixi.library.android.util.FSLogUtil

open class FSActivity : FSBaseActivity() {

    companion object {
        val KEY_THEME = "KEY_THEME"
        val KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS"
        val KEY_FRAGMENT_ARGS = "KEY_FRAGMENT_ARGS"

        fun start(from: Context, fragmentClass: Class<*>) {
            start(from, fragmentClass, null)
        }

        fun start(from: Context, fragmentClass: Class<*>, args: Bundle?) {
            start(from, fragmentClass, args, 0)
        }

        fun startNewTask(fragmentClass: Class<*>, args: Bundle?) {
            FSBaseApplication.INSTANCE.startActivity(getNewTaskIntent(FSBaseApplication.INSTANCE, 0, fragmentClass, args))
        }

        fun startSingleTask(from: Context, fragmentClass: Class<*>, args: Bundle) {
            FSBaseApplication.INSTANCE.startActivity(getSingleTaskIntent(from, 0, fragmentClass, args))
        }

        fun start(activity: Context, fragmentClass: Class<*>, args: Bundle?, themResId: Int) {
            activity.startActivity(getIntent(activity, themResId, fragmentClass, args))
        }

        fun start(activity: Context, intent: Intent?) {
            activity.startActivity(intent)
        }

        fun start(activity: Activity, fragmentClassName: String?, args: Bundle? = null) {
            activity.startActivity(getIntent(activity, 0, fragmentClassName, args))
        }

        fun startByCustomAnimation(activity: Activity, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            activity.startActivity(getIntent(activity, fragmentClass, args))
            activity.overridePendingTransition(enterAnim, exitAnim)
        }


        fun startForResultByCustomAnimation(activity: Activity, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResultByCustomAnimation(activity, 0, requestCode, fragmentClass, args, enterAnim, exitAnim)
        }

        fun startForResultByCustomAnimation(activity: Activity, themResId: Int, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResult(activity, themResId, requestCode, fragmentClass, args)
            activity.overridePendingTransition(enterAnim, exitAnim)
        }

        fun startForResultByCustomAnimation(fragment: Fragment, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResultByCustomAnimation(fragment, 0, requestCode, fragmentClass, args, enterAnim, exitAnim)
        }

        fun startForResultByCustomAnimation(fragment: Fragment, themResId: Int, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResult(fragment, themResId, requestCode, fragmentClass, args)
            fragment.activity.overridePendingTransition(enterAnim, exitAnim)
        }

        //================================//================================//================================
        //base
        /* Activity页面发起的，再由Activity来接收结果 如果由Fragment来接收结果，需要使用 {@link #startForResult(Fragment, int, Class, Bundle)} */
        fun startForResult(activity: Activity, reqCode: Int, fragmentClass: Class<*>, args: Bundle) {
            startForResult(activity, 0, reqCode, fragmentClass, args)
        }

        fun startForResult(activity: Activity, themResId: Int, reqCode: Int, fragmentClass: Class<*>, args: Bundle) {
            activity.startActivityForResult(getIntent(activity, themResId, fragmentClass, args), reqCode)
        }

        /* 由Fragment页面发起的，再由Fragment接收结果 */
        fun startForResult(fragment: Fragment, reqCode: Int, fragmentClass: Class<*>, args: Bundle) {
            startForResult(fragment, 0, reqCode, fragmentClass, args)
        }

        /* 由Fragment页面发起的，再由Fragment接收结果 */
        fun startForResult(fragment: Fragment, themResId: Int, reqCode: Int, fragmentClass: Class<*>, args: Bundle) {
            fragment.startActivityForResult(getIntent(fragment.activity, themResId, fragmentClass, args), reqCode)
        }

        fun getIntent(context: Context, fragmentClass: Class<*>, args: Bundle): Intent = getIntent(context, 0, fragmentClass, args)

        fun getIntent(context: Context, themResId: Int, fragmentClass: Class<*>, args: Bundle?): Intent = getIntent(context, themResId, fragmentClass.canonicalName, args)

        fun getIntent(context: Context, themResId: Int, fragmentClassName: String?, args: Bundle?): Intent {
            val intent = Intent(context, FSActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClassName)
            if (args != null)
                intent.putExtra(KEY_FRAGMENT_ARGS, args)
            if (themResId > 0)
                intent.putExtra(KEY_THEME, themResId)
            return intent
        }

        fun getNewTaskIntent(context: Context, themResId: Int, fragmentClass: Class<*>, args: Bundle?): Intent {
            val intent = Intent(context, FSActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
            if (args != null)
                intent.putExtra(KEY_FRAGMENT_ARGS, args)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (themResId > 0)
                intent.putExtra(KEY_THEME, themResId)
            return intent
        }

        fun getSingleTaskIntent(context: Context, themResId: Int, fragmentClass: Class<*>, args: Bundle?): Intent {
            val intent = Intent(context, FSActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
            if (args != null)
                intent.putExtra(KEY_FRAGMENT_ARGS, args)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            if (themResId > 0)
                intent.putExtra(KEY_THEME, themResId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            val args = intent.extras
            val themResId = args.getInt(KEY_THEME, 0)
            if (themResId > 0)
                setTheme(themResId)
            super.onCreate(savedInstanceState)

            setContentView(FrameLayout(this))

            var fragment: Fragment? = null
            val fragmentClassName: String
            val fragmentObject = args.get(KEY_FRAGMENT_CLASS)

            if (fragmentObject is Class<*>) {
                fragmentClassName = fragmentObject.name
                fragment = fragmentObject.newInstance() as Fragment
            } else {
                fragmentClassName = fragmentObject as String
                try {
                    fragment = Class.forName(fragmentObject).newInstance() as Fragment
                } catch (_: Exception) {
                    try {
                        @Suppress("DEPRECATION")
                        fragment = Atlas.getInstance().delegateClassLoader.loadClass(fragmentObject).newInstance() as Fragment
                    } catch (e: Exception) {
                        FSLogUtil.e("ClassNotFoundException:$fragmentClassName", e)
                    }
                }
            }
            if (fragment != null) {
                fragment.arguments = args.getBundle(KEY_FRAGMENT_ARGS)
                supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            Log.e(FSActivity::javaClass.name, "Has error in new instance of fragment", e)
        }
    }
}