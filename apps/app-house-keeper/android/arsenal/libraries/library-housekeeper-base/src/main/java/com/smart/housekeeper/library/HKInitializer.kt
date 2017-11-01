package com.smart.housekeeper.library

import android.graphics.Color
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import com.smart.housekeeper.repository.HKRepository
import com.smart.library.util.HKLogUtil
import com.smart.library.widget.titlebar.HKTitleBar

object HKInitializer {

    var init: Boolean = false
        private set

    fun init() {
        if (!init) {
            //add all code here
            HKTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
            HKTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
            HKTitleBar.DEFAULT_TEXT_SIZE = 16f

            HKRepository.init()
        }
        init = true
    }

}
