package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STToastUtil
import com.smart.template.R
import kotlinx.android.synthetic.main.final_round_fragment.*

@Suppress("unused", "DEPRECATION")
class FinalRoundLayoutFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.final_round_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        click_btn.setOnClickListener {
            STToastUtil.show("clicked")
        }
        button.setOnClickListener {
            STToastUtil.show("button1")
        }
        button2.setOnClickListener {
            STToastUtil.show("button2")
        }
        textView.setOnClickListener {
            STToastUtil.show("you clicked me!")
        }
        textView.addOnLeftDrawableTouchUpListener {
            STToastUtil.show("left")
        }
        textView.addOnTopDrawableTouchUpListener {
            STToastUtil.show("top")
        }
        textView.addOnRightDrawableTouchUpListener {
            STToastUtil.show("right")
        }
        textView.addOnBottomDrawableTouchUpListener {
            STToastUtil.show("bottom")
        }

        setupAutoCompleteView()
        setupAutoCompleteView2()
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

    private fun setupAutoCompleteView2() {
        val context = context
        context ?: return

        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        (autoCompleteInput2.editText as? AutoCompleteTextView)?.also {
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