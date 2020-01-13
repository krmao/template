package com.smart.library.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.R

@Suppress("unused")
object STDialogManager {

    private val dpTranslation by lazy { STSystemUtil.getPxFromDp(6f) }
    private val dpAnimationViewSize by lazy { STSystemUtil.getPxFromDp(64f).toInt() }
    private val dpAnimationViewLargeSize by lazy { STSystemUtil.getPxFromDp(76f).toInt() }

    @JvmStatic
    @JvmOverloads
    @SuppressLint("InflateParams")
    fun createLoadingDialog(context: Context?, text: String? = null, cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false, dimAmount: Float = 0f): Dialog? {
        return createDialog(context, cancelable = cancelable, canceledOnTouchOutside = canceledOnTouchOutside, dimAmount = dimAmount, contentView = {
            LayoutInflater.from(context).inflate(R.layout.st_dialog_progress_loading, null, false).apply {
                val textView: TextView = findViewById(R.id.text)
                val animationView: View = findViewById(R.id.animation_view)

                textView.text = text
                if (text.isNullOrBlank()) {
                    textView.visibility = View.GONE
                    animationView.translationY = 0f
                    animationView.layoutParams = animationView.layoutParams.apply {
                        width = dpAnimationViewLargeSize // FrameLayout.LayoutParams.MATCH_PARENT
                        height = dpAnimationViewLargeSize //FrameLayout.LayoutParams.MATCH_PARENT
                    }
                } else {
                    textView.visibility = View.VISIBLE
                    animationView.translationY = -dpTranslation
                    animationView.layoutParams = animationView.layoutParams.apply {
                        width = dpAnimationViewSize
                        height = dpAnimationViewSize
                    }
                }
            }
        })
    }

    /**
     * @param themeResId    dialog 弹出样式, R.style.STDialog_App_FromFadeInOut, R.style.STDialog_App_FromBottom
     * @param cancelable    点按 返回键 是否可以取消 dialog
     * @param canceledOnTouchOutside     点按 dialog 外部是否可以取消 dialog
     * @param dimAmount     背景模糊程度
     * @param width         dialog 宽度
     * @param height        dialog 高度
     */
    @JvmStatic
    @JvmOverloads
    fun createDialog(context: Context?, themeResId: Int = R.style.STDialog_App_FromFadeInOut, cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false, dimAmount: Float = 0f, onShowListener: DialogInterface.OnShowListener? = null, onDismissListener: DialogInterface.OnDismissListener? = null, onCancelListener: DialogInterface.OnCancelListener? = null, width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT, contentView: (Context?) -> View): Dialog? {
        context ?: return null

        val dialog = Dialog(context, themeResId)
        dialog.setContentView(contentView.invoke(context), ViewGroup.LayoutParams(width, height))
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        dialog.setOnDismissListener(onDismissListener)
        dialog.setOnShowListener(onShowListener)
        dialog.setOnCancelListener(onCancelListener)

        dialog.window?.attributes?.dimAmount = dimAmount
        dialog.window?.attributes?.width = width
        dialog.window?.attributes?.height = height
        return dialog
    }
}