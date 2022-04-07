package com.smart.library.widget.debug

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.R
import com.smart.library.base.STActivityDelegate
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STCrashManager
import com.smart.library.util.STLogUtil

//@Keep
class STDebugCrashPanelFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.st_fragment_debug_crash_panel, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: com.yuyh.jsonviewer.library.JsonRecyclerView = view.findViewById(R.id.textView)

        val crashLogPath = arguments?.getString(KEY_XCRASH_LOG_PATH)
        val jsonObjectString = STCrashManager.getJsonStringFromLogPath(crashLogPath)

        if (jsonObjectString != null && jsonObjectString.isNotBlank()) {
            // Bind
            textView.bindJson(jsonObjectString)

            // Color
            textView.setKeyColor(resources.getColor(R.color.pink_800))
            textView.setBracesColor(resources.getColor(R.color.pink_800))
            textView.setValueTextColor(resources.getColor(R.color.grey_400))
            textView.setValueNumberColor(resources.getColor(R.color.grey_400))
            textView.setValueUrlColor(resources.getColor(R.color.grey_400))
            textView.setValueNullColor(resources.getColor(R.color.grey_400))

            // TextSize
            // textView.setTextSize(6f)
            textView.updateAll(1f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        STCrashManager.clearLastCrashLogFlag()
    }

    companion object {
        const val TAG = "[crash]"
        const val KEY_XCRASH_LOG_PATH = "KEY_XCRASH_LOG_PATH"

        @JvmStatic
        fun startActivity(
            from: Context?,
            crashLogPath: String
        ) {

            if (from != null) {
                val intent: Intent = STDebugCrashActivity.createIntent(
                    context = from,
                    activityThem = STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT_FADE.id,
                    fragmentClass = STDebugCrashPanelFragment::class.java,
                    fragmentArguments = Bundle().apply {
                        putString(KEY_XCRASH_LOG_PATH, crashLogPath)
                    },
                    enableSwipeBack = false,
                    enableSwipeBackShadow = false,
                    activityCloseEnterAnimation = R.anim.st_anim_fade_enter,
                    activityCloseExitAnimation = R.anim.st_anim_fade_exit
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED

                // https://github.com/Ereza/CustomActivityOnCrash/blob/master/library/src/main/java/cat/ereza/customactivityoncrash/CustomActivityOnCrash.java
                // if (intent.component != null) {
                //If the class name has been set, we force it to simulate a Launcher launch.
                //If we don't do this, if you restart from the error activity, then press home,
                //and then launch the activity from the launcher, the main activity appears twice on the back stack.
                //This will most likely not have any detrimental effect because if you set the Intent component,
                //if will always be launched regardless of the actions specified here.
                // intent.action = Intent.ACTION_MAIN
                // intent.addCategory(Intent.CATEGORY_LAUNCHER)
                // }
                from.startActivity(intent)

                // https://stackoverflow.com/a/34356589/4348530
                // android.os.Process.killProcess(android.os.Process.myPid())
                // exitProcess(10)
            } else {
                STLogUtil.e(TAG, "context is null !")
            }
        }
    }
}