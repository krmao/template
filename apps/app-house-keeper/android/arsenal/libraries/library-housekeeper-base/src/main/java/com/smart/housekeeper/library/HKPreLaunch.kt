package com.smart.housekeeper.library

import android.content.Context
import android.taobao.atlas.runtime.AtlasPreLauncher
import android.util.Log

class HKPreLaunch : AtlasPreLauncher {
    override fun initBeforeAtlas(context: Context?) {
        Log.w("HKPreLaunch", "HKPreLaunch:initBeforeAtlas")
    }
}
