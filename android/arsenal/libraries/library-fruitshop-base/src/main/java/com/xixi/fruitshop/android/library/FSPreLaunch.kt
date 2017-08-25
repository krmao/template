package com.xixi.fruitshop.android.library

import android.content.Context
import android.taobao.atlas.runtime.AtlasPreLauncher
import android.util.Log

class FSPreLaunch : AtlasPreLauncher {
    override fun initBeforeAtlas(context: Context?) {
        Log.w("FSPreLaunch", "FSPreLaunch:initBeforeAtlas")
    }
}
