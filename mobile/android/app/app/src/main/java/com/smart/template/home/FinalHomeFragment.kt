package com.smart.template.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STDialogManager
import com.smart.library.util.STFragmentManager
import com.smart.library.util.STLogUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.image.STImageUtil
import com.smart.library.widget.colorpicker.STColorPickerUtil
import com.smart.module.crash.STCrashActivity
import com.smart.template.R
import com.smart.template.home.tab.FinalHomeTabActivity
import com.smart.template.home.test.*
import kotlinx.android.synthetic.main.final_home_fragment.*

class FinalHomeFragment : STBaseFragment(), FragmentManager.OnBackStackChangedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.final_home_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.fitsSystemWindows = false
        /*label1TV.ensureOnGlobalLayoutListener {
            val text = label1TV.text.toString().trim()
            STLogUtil.w("[SYS] LABEL 1 realHeight=${it.height}, realWidth=${it.width}, measuredHeight=${it.measuredHeight}, measuredWidth=${it.measuredWidth}")
            STLogUtil.w("[SYS] LABEL 1 calculateHeight=${STSystemUtil.measuringMultiLineTextHeight(text, 16f.toPxFromDp(), STSystemUtil.screenWidth.toFloat())}, calculateWidth=${STSystemUtil.measuringTextWidth(text, 16f.toPxFromDp(), typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD))}")
        }
        label2TV.ensureOnGlobalLayoutListener {
            val text = label2TV.text.toString().trim()
            STLogUtil.w("[SYS] LABEL 2 realHeight=${it.height}, realWidth=${it.width}, measuredHeight=${it.measuredHeight}, measuredWidth=${it.measuredWidth}")
            STLogUtil.w("[SYS] LABEL 2 calculateHeight=${STSystemUtil.measuringMultiLineTextHeight(text, 16f.toPxFromDp(), STSystemUtil.screenWidth.toFloat())}")
        }
        label3TV.ensureOnGlobalLayoutListener {
            val text = label3TV.text.toString().trim()
            STLogUtil.w("[SYS] LABEL 3 realHeight=${it.height}, realWidth=${it.width}, measuredHeight=${it.measuredHeight}, measuredWidth=${it.measuredWidth}")
            STLogUtil.w("[SYS] LABEL 3 calculateHeight=${STSystemUtil.measuringMultiLineTextHeight(text, 16f.toPxFromDp(), STSystemUtil.screenWidth.toFloat())}")
        }
        label4TV.ensureOnGlobalLayoutListener {
            val text = label4TV.text.toString().trim()
            STLogUtil.w("[SYS] LABEL 4 realHeight=${it.height}, realWidth=${it.width}, measuredHeight=${it.measuredHeight}, measuredWidth=${it.measuredWidth}")
            STLogUtil.w("[SYS] LABEL 4 calculateHeight=${STSystemUtil.measuringMultiLineTextHeight(text, 16f.toPxFromDp(), STSystemUtil.screenWidth.toFloat())}")
        }
        label5TV.ensureOnGlobalLayoutListener {
            val text = label5TV.text.toString().trim()
            STLogUtil.w("[SYS] LABEL 5 realHeight=${it.height}, realWidth=${it.width}, measuredHeight=${it.measuredHeight}, measuredWidth=${it.measuredWidth}")
            STLogUtil.w("[SYS] LABEL 5 calculateHeight=${STSystemUtil.measuringMultiLineTextHeight(text, 16f.toPxFromDp(), STSystemUtil.screenWidth.toFloat())}")
        }*/

        val mergedBitmap: Bitmap? = STImageUtil.mergeBitmap(STImageUtil.getBitmapFromResourceVector(R.drawable.st_launch_background, context), STImageUtil.getBitmapFromResource(R.drawable.st_image, context?.resources))
        STLogUtil.w("mergedBitmap=$mergedBitmap")
        imageView.setImageBitmap(mergedBitmap)

        roundLayout.setOnClickListener {
            FinalRoundLayoutFragment.goTo(context)
        }
        btnSwipe.setOnClickListener {
            FinalSwipeMenuFragment.goTo(context)
        }
        wifiBtn.setOnClickListener {
            FinalWifiFragment.goTo(context)
        }
        crashTestBtn.setOnClickListener {
            STCrashActivity.goTo(context)
        }
        var lastSelectedAlpha = 1.0
        var lastSelectedColor: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(R.color.red_500, null)
        } else {
            @Suppress("DEPRECATION")
            resources.getColor(R.color.red_500)
        }
        btnColorAlpha.setOnClickListener {
            STColorPickerUtil.showColorAlphaDialog(activity = activity as FragmentActivity, initAlpha = lastSelectedAlpha, initColor = lastSelectedColor) { alpha: Double, colorWithAlpha: Int ->
                lastSelectedAlpha = alpha
                lastSelectedColor = colorWithAlpha
                STLogUtil.w("alpha=$alpha, colorWithAlpha=$colorWithAlpha")
            }
        }
        btnColorPicker.setOnClickListener {
            STColorPickerUtil.showColorPickerDialogByColorIntArray(activity = activity as FragmentActivity, selectedColor = lastSelectedColor, colorIntArrayResId = R.array.colorIntArray) {
                lastSelectedColor = it
                STLogUtil.w("lastSelectedColor=$lastSelectedColor")
            }
        }
        btnMagic.setOnClickListener {
            FinalMagicFragment.goTo(context)
        }

        activityBtn.setOnClickListener {
            FinalActivityFragment.goTo(context)
        }

        val fragmentTag = "fragmentTag"
        fragments.setOnClickListener {
            STFragmentManager.addFragment(
                activity = activity,
                targetFragment = FinalReactNativeFragment(),
                tag = fragmentTag,
                addToBackStack = true,
                enableCustomAnimations = true,
                executePendingTransactions = true
            )

            fragments.postDelayed({
                STFragmentManager.addFragment(
                    activity = activity,
                    targetFragment = FinalFlutterFragment(),
                    tag = fragmentTag,
                    addToBackStack = true,
                    enableCustomAnimations = true,
                    executePendingTransactions = true
                )
            }, 2000)

            fragments.postDelayed({
                STFragmentManager.addFragmentWithFadeAnimation(
                    activity = activity,
                    targetFragment = FinalHybirdFragment(),
                    tag = fragmentTag,
                    addToBackStack = true,
                    enableCustomAnimations = true,
                    executePendingTransactions = true
                )
            }, 4000)
        }
        btnRipple.setOnClickListener {
            FinalRippleFragment.goTo(context)
        }
        videoPager.setOnClickListener {
            // startActivity(Intent(context, STVideoPagerActivity::class.java))
        }
        bottomSheet.setOnClickListener {
            startActivity(Intent(context, FinalBehaviorBottomSheetActivity::class.java))
        }
        bottomSheetV2.setOnClickListener {
            startActivity(Intent(context, FinalBehaviorBottomSheetV2Activity::class.java))
        }
        baiduMap.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("useBaidu", true)
            STActivity.startActivity(activity, "com.smart.library.map.STMapFragment", bundle)
        }
        videoPlayerExamples.setOnClickListener {
            STActivity.startActivity(activity, "com.example.gsyvideoplayer.VideoPlayerExamplesActivity")
        }
        dialogText.setOnClickListener {
            val loadingDialog: Dialog? = STDialogManager.createLoadingDialog(context, "规划中...", canceledOnTouchOutside = true)
            loadingDialog?.show()
        }
        dialogLoading.setOnClickListener {
            val loadingDialog: Dialog? = STDialogManager.createLoadingDialog(context)
            loadingDialog?.show()
        }
        recyclersTransferItem.setOnClickListener {
            FinalRecyclerViewDragAndTransferFragment.goTo(context)
        }
        recyclerItemSnapTop.setOnClickListener {
            FinalRecyclerViewSnapTopFragment.goTo(context)
        }

        ijkPullRtmp.requestFocus()
        ijkPullRtmp.setOnClickListener {
            val url = rtmpURLET.text.toString()
            STBusManager.call(context, "livestreaming/play", url, url.substringAfterLast("/"))
        }
        ijkSettings.setOnClickListener {
            STBusManager.call(context, "livestreaming/opensettings")
        }
        yeseaPushRtmp.setOnClickListener {
            STBusManager.call(context, "livestreamingpush/push", "rtmp://10.32.33.20:5388/rtmplive/room-mobile")
        }
        screenPushRtmp.setOnClickListener {
            STBusManager.call(context, "livestreamingpush/push-screen", "rtmp://10.32.33.20:5388/rtmplive/room-mobile")
        }
        playVideo.setOnClickListener {
            STBusManager.call(context, "livestreaming/playvideo", "https://vodlnr6niz5.vod.126.net/vodlnr6niz5/c6bc3543-429a-4342-a555-702596131fe4.mp4")
        }
        playVideoNormal.setOnClickListener {
            FinalVideoPlayerFragment.goTo(context)
        }

    }

    override fun onStart() {
        super.onStart()
        Log.w(FinalHomeTabActivity.TAG, "HomeFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w(FinalHomeTabActivity.TAG, "HomeFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w(FinalHomeTabActivity.TAG, "HomeFragment:onDestroy");
    }

    override fun onBackStackChanged() {
        STLogUtil.w(FinalHomeTabActivity.TAG, "onBackStackChanged")
    }
}
