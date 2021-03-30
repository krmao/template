package com.smart.library.widget.debug

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.R
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STLogUtil

class STDebugCrashPanelFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.st_fragment_debug_crash_panel, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: com.yuyh.jsonviewer.library.JsonRecyclerView = view.findViewById(R.id.textView)

        val jsonObjectString = arguments?.getString(KEY_JSON_OBJECT_STRING)
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

    companion object {
        const val TAG = "[crash]"
        const val KEY_JSON_OBJECT_STRING = "KEY_JSON_OBJECT_STRING"

        @JvmStatic
        fun startActivity(
            from: Context?,
            jsonObjectString: String
        ) {
            if (from != null) {
                val intent: Intent = STActivity.createIntent(
                    context = from,
                    fragmentClass = STDebugCrashPanelFragment::class.java,
                    fragmentArguments = Bundle().apply {
                        putString(KEY_JSON_OBJECT_STRING, jsonObjectString)
                    },
                    enableSwipeBack = false,
                    enableSwipeBackShadow = false,
                    activityCloseEnterAnimation = R.anim.st_anim_fade_enter,
                    activityCloseExitAnimation = R.anim.st_anim_fade_exit
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                from.startActivity(intent)
            } else {
                STLogUtil.e(TAG, "context is null !")
            }
        }
    }
}