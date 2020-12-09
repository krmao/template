package com.smart.library.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil

@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
open class STActivity : STBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            val bundle: Bundle? = intent.extras
            val themResId: Int = bundle?.getInt(KEY_THEME, 0) ?: 0
            if (themResId > 0) setTheme(themResId)
            super.onCreate(savedInstanceState)

            setContentView(FrameLayout(this))

            var fragment: Fragment? = null
            val fragmentClassName: String
            val fragmentObject = bundle?.get(KEY_FRAGMENT_CLASS)

            if (fragmentObject is Class<*>) {
                fragmentClassName = fragmentObject.name
                fragment = fragmentObject.newInstance() as Fragment
            } else {
                fragmentClassName = fragmentObject as String
                try {
                    fragment = Class.forName(fragmentObject).newInstance() as Fragment
                } catch (e: Exception) {
                    STLogUtil.e(TAG, "ClassNotFoundException:$fragmentClassName", e)
                }
            }
            if (fragment != null) {
                fragment.arguments = bundle.getBundle(KEY_FRAGMENT_ARGS)
                supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            STLogUtil.e(STActivity::javaClass.name, "Has error in new instance of fragment", e)
        }
    }

    override fun onStart() {
        STLogUtil.w(TAG, "onStart:$taskId")
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        STLogUtil.w(TAG, "onRestart:$taskId")
    }

    override fun onResume() {
        super.onResume()
        STLogUtil.w(TAG, "onResume:$taskId")
    }

    override fun onPause() {
        super.onPause()
        STLogUtil.w(TAG, "onPause:$taskId")
    }

    override fun onStop() {
        super.onStop()
        STLogUtil.w(TAG, "onStop:$taskId")
    }

    override fun onDestroy() {
        super.onDestroy()
        STLogUtil.w(TAG, "onDestroy:$taskId")
    }

    enum class Theme(@StyleRes val id: Int) {
        APP_THEME(R.style.STAppTheme),
        APP_THEME_LAUNCH(R.style.STAppTheme_Launch),
        APP_THEME_HOME(R.style.STAppTheme_Home),
        APP_THEME_LOGIN(R.style.STAppTheme_Login),
        APP_THEME_NORMAL(R.style.STAppTheme_Normal),
        APP_THEME_NORMAL_FADE(R.style.STAppTheme_Normal_Fade),
        APP_THEME_NORMAL_FULLSCREEN(R.style.STAppTheme_Normal_FullScreen),
        APP_THEME_NORMAL_ACTIONBAR(R.style.STAppTheme_Normal_ActionBar),
        APP_THEME_NORMAL_ACTIONBAR_TRANSLUCENT(R.style.STAppTheme_Normal_ActionBar_Translucent),
        APP_THEME_NORMAL_ACTIONBAR_TRANSLUCENT_FADE(R.style.STAppTheme_Normal_ActionBar_Translucent_Fade),
        APP_THEME_NORMAL_ACTIONBAR_TRANSPARENT(R.style.STAppTheme_Normal_ActionBar_Transparent),
        APP_THEME_NORMAL_ACTIONBAR_TRANSPARENT_FADE(R.style.STAppTheme_Normal_ActionBar_Transparent_Fade),
        APP_THEME_NORMAL_TRANSLUCENT(R.style.STAppTheme_Normal_Translucent),
        APP_THEME_NORMAL_TRANSPARENT(R.style.STAppTheme_Normal_Transparent),
        APP_THEME_NORMAL_TRANSPARENT_FADE(R.style.STAppTheme_Normal_Transparent_Fade),
        APP_THEME_NORMAL_TRANSLUCENT_FADE(R.style.STAppTheme_Normal_Translucent_Fade),
    }

    companion object {

        private const val TAG = "[STActivity]"

        @JvmStatic
        val KEY_THEME = "KEY_THEME"

        @JvmStatic
        val KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS"

        @JvmStatic
        val KEY_FRAGMENT_ARGS = "KEY_FRAGMENT_ARGS"

        @JvmOverloads
        @JvmStatic
        fun start(from: Context?, fragmentClass: Class<*>, args: Bundle = Bundle()) {
            start(from, fragmentClass, args, 0)
        }

        @JvmOverloads
        @JvmStatic
        fun startNewTask(fragmentClass: Class<*>, args: Bundle = Bundle()) {
            STInitializer.application()?.startActivity(getNewTaskIntent(STInitializer.application(), fragmentClass, args))
        }

        @JvmOverloads
        @JvmStatic
        fun startSingleTask(from: Context?, fragmentClass: Class<*>, args: Bundle = Bundle()) {
            STInitializer.application()?.startActivity(getSingleTaskIntent(from, 0, fragmentClass, args))
        }

        @JvmOverloads
        @JvmStatic
        fun start(activity: Context?, fragmentClass: Class<*>, args: Bundle = Bundle(), themResId: Int) {
            activity?.startActivity(getIntent(activity, themResId, fragmentClass, args))
        }

        @JvmStatic
        fun start(activity: Context?, intent: Intent?) {
            activity?.startActivity(intent)
        }

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
        fun startForResult(activity: Activity?, reqCode: Int, fragmentClass: Class<*>, args: Bundle = Bundle()) {
            startForResult(activity, 0, reqCode, fragmentClass, args)
        }


        @JvmStatic
        fun startForResult(activity: Activity?, themResId: Int, reqCode: Int, fragmentClass: Class<*>, args: Bundle = Bundle()) {
            activity?.startActivityForResult(getIntent(activity, themResId, fragmentClass, args), reqCode)
        }


        /* 由Fragment页面发起的，再由Fragment接收结果 */
        @JvmStatic
        fun startForResult(fragment: Fragment?, reqCode: Int, fragmentClass: Class<*>, args: Bundle = Bundle()) {
            startForResult(fragment, 0, reqCode, fragmentClass, args)
        }


        /* 由Fragment页面发起的，再由Fragment接收结果 */
        @JvmStatic
        fun startForResult(fragment: Fragment?, themResId: Int, reqCode: Int, fragmentClass: Class<*>, args: Bundle = Bundle()) {
            fragment?.startActivityForResult(getIntent(fragment.activity, themResId, fragmentClass, args), reqCode)
        }


        @JvmStatic
        fun getIntent(context: Context?, fragmentClass: Class<*>, args: Bundle): Intent {
            return getIntent(context, 0, fragmentClass, args)
        }

        @JvmStatic
        fun getIntent(context: Context?, themResId: Int, fragmentClass: Class<*>, args: Bundle = Bundle()): Intent {
            return getIntent(context, themResId, fragmentClass.canonicalName, args)
        }

        @JvmOverloads
        @JvmStatic
        fun getIntent(context: Context?, themResId: Int, fragmentClassName: String?, args: Bundle = Bundle()): Intent {
            val intent = Intent(context, STActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClassName)
            intent.putExtra(KEY_FRAGMENT_ARGS, args)
            if (themResId > 0) intent.putExtra(KEY_THEME, themResId)
            return intent
        }

        @JvmOverloads
        @JvmStatic
        fun getNewTaskIntent(context: Context?, fragmentClass: Class<*>, args: Bundle = Bundle(), themResId: Int = 0): Intent {
            val intent = Intent(context, STActivity::class.java)
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
            val intent = Intent(context, STActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
            intent.putExtra(KEY_FRAGMENT_ARGS, args)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            if (themResId > 0) intent.putExtra(KEY_THEME, themResId)
            return intent
        }

        @UiThread
        fun overrideWindowAnim(intent: Intent?, context: Context?) {
            //region home class 无需重写动画
            try {
                if (intent != null && intent.component != null) {
                    val className = intent.component?.className
                    if (!className.isNullOrBlank() && className == STInitializer.configClass?.homeClass?.javaClass?.name) {
                        return
                    }
                }
            } catch (_: java.lang.Exception) {
            }
            //endregion
            if (context != null && context is Activity) {
                context.overridePendingTransition(
                    R.anim.st_anim_left_right_open_enter,
                    R.anim.st_anim_left_right_close_exit
                )
            }
        }
    }

}

