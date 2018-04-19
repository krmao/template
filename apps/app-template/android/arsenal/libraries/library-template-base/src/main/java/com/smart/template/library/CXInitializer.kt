package com.smart.template.library

import android.graphics.Color
import com.smart.template.repository.CXRepository
import com.smart.library.widget.titlebar.CXTitleBar

object CXInitializer {

    var init: Boolean = false
        private set

    fun init() {
        if (!init) {
            //add all code here
            CXTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
            CXTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
            CXTitleBar.DEFAULT_TEXT_SIZE = 16f

            CXRepository.init()
        }
        init = true
    }

}
