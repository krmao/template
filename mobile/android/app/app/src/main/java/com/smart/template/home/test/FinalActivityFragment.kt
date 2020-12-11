package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STActivityDelegate
import com.smart.library.base.STBaseFragment
import com.smart.template.R
import kotlinx.android.synthetic.main.final_activity_fragment.*

@Suppress("unused", "DEPRECATION")
class FinalActivityFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.final_activity_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startActivity.setOnClickListener {
            STActivity.startActivity(
                context,
                fragmentClass = FinalActivityDetailFragment::class.java,
                fragmentArguments = Bundle(),
                requestCode = 0,
                activityTheme = if (checkedTransparentOrTranslucent.isChecked) STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id else STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSLUCENT.id,
                enableSwipeBack = enableSwipeBack.isChecked,
                enableImmersionStatusBar = enableImmersionStatusBar.isChecked,
                enableImmersionStatusBarWithDarkFont = enableImmersionStatusBarWithDarkFont.isChecked,
                enableExitWithDoubleBackPressed = enableExitWithDoubleBackPressed.isChecked,
                enableFinishIfIsNotTaskRoot = enableFinishIfIsNotTaskRoot.isChecked,
                enableActivityFullScreenAndExpandLayout = enableActivityFullScreenAndExpandLayout.isChecked,
                enableActivityFeatureNoTitle = enableActivityFeatureNoTitle.isChecked,
                activityDecorViewBackgroundResource = if (activityDecorViewBackgroundResource.isChecked) R.drawable.st_launch else -1,
                activityOpenEnterAnimation = if (checkedFadeOrLeft.isChecked) R.anim.st_anim_fade_enter else R.anim.st_anim_left_right_open_enter,
                activityOpenExitAnimation = if (checkedFadeOrLeft.isChecked) R.anim.st_anim_fade_exit else R.anim.st_anim_left_right_open_exit,
                activityCloseEnterAnimation = if (checkedFadeOrLeft.isChecked) R.anim.st_anim_fade_enter else R.anim.st_anim_left_right_close_enter,
                activityCloseExitAnimation = if (checkedFadeOrLeft.isChecked) R.anim.st_anim_fade_exit else R.anim.st_anim_left_right_close_exit,
            )
        }
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.startActivity(activity, FinalActivityFragment::class.java)
        }
    }
}