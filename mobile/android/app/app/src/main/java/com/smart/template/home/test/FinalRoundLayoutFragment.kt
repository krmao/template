package com.smart.template.home.test

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STToastUtil
import com.smart.library.widget.shapeable.edgedrawable.STEdgeDrawableHelper
import com.smart.template.R
import kotlinx.android.synthetic.main.final_round_fragment.*


@Suppress("unused", "DEPRECATION")
class FinalRoundLayoutFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.final_round_fragment, container, false)
    }

    fun onClick(v: View) {
        button.setOnClickListener {
            STToastUtil.show("hehe")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region margin
        val marginString = "[  ]"
        val spannableString = SpannableString("01${marginString}234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950")
        val startInclusiveIndex = 2
        val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
        transparentDrawable.setBounds(0, 0, 10.toPxFromDp(), 0)
        spannableString.setSpan(ImageSpan(transparentDrawable), startInclusiveIndex, startInclusiveIndex + marginString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        //endregion

        textViewTest.text = spannableString

        textViewTest.setOnClickListener {
            Thread(Runnable {
                textViewTest.text = "haha${Thread.currentThread().name}"
            }).start()
        }

        textViewTest.setOnLongClickListener {
            var a :String? = null
            textViewTest.text = "haha${Thread.currentThread().name}" + a!!.length
            true
        }


        edit1.setOnClickListener {
            STToastUtil.show("clicked")
        }
        layout_1.setOnClickListener {
            STToastUtil.show("clicked")
        }
        txt3.setOnClickListener {
            STToastUtil.show("clicked")
        }
        click_btn.setOnClickListener {
            STToastUtil.show("clicked")
        }
        button.setOnClickListener {
            STToastUtil.show("button1")
        }
        textTest.setOnClickListener {
            STToastUtil.show("textTest")
        }
        button2.setOnClickListener {
            STToastUtil.show("button2")
        }
        textView.setOnClickListener {
            STToastUtil.show("you clicked me!")
        }
        setupAutoCompleteView()


        textView.setOnEdgeDrawableClickListener {
            when (it) {
                STEdgeDrawableHelper.Position.RIGHT -> {
                    setTextViewRightArrow(!isChecked && popupWindow?.isShowing != true)
                }
                else -> STToastUtil.show("bottom")
            }
        }
        // setTextViewRightArrow(false, force = true)
    }

    private var isChecked = false
    private var popupWindow: PopupWindow? = null
    private fun setTextViewRightArrow(checked: Boolean, force: Boolean = false) {
        if (!force && checked == isChecked) return
        textView.setEdgeDrawable(STEdgeDrawableHelper.Position.RIGHT, if (checked) R.drawable.st_icon_arrow_up else R.drawable.st_icon_arrow_down)
        if (checked) {
            if (popupWindow == null) {
                val innerTextView = TextView(context)
                innerTextView.text = "测试文本"
                innerTextView.setBackgroundColor(Color.BLUE)
                popupWindow = PopupWindow(innerTextView, 400, 600) //参数为1.View 2.宽度 3.高度
                popupWindow?.isOutsideTouchable = true //设置点击外部区域可以取消popupWindow
                popupWindow?.setOnDismissListener {
                    STToastUtil.show("onDismiss")
                    textView.postDelayed({
                        setTextViewRightArrow(false)
                    }, 50)
                }
            }
            popupWindow?.showAsDropDown(textView, 0, 0, Gravity.BOTTOM or Gravity.END)
        }
        isChecked = checked
    }

    /**
     * https://material.io/components/menus/android#exposed-dropdown-menus
     */
    private fun setupAutoCompleteView() {
        val context = context
        context ?: return

        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        (autoCompleteInput.editText as? AutoCompleteTextView)?.also {
            it.setAdapter(adapter)
            it.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                STToastUtil.show("you clicked ${items[position]}")
            }
        }
    }

    private fun getAnimalList(): ArrayList<String> {
        val animalList = ArrayList<String>()
        animalList.add("dog")
        animalList.add("cat")
        animalList.add("cow")
        animalList.add("elephant")
        animalList.add("snake")
        return animalList
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.startActivity(activity, FinalRoundLayoutFragment::class.java)
        }
    }
}