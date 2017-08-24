package com.xixi.fruitshop.android.library

import android.graphics.Color
import com.xixi.library.android.widget.titlebar.FSTitleBar

object FSInitializer {

    var init: Boolean = false
        private set

    fun init() {
        if (!init) {
            //add all code here
            FSTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
            FSTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
            FSTitleBar.DEFAULT_TEXT_SIZE = 16f
        }
        init = true
    }

}