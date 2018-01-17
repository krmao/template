package com.smart.library.bundle.model

import com.smart.library.util.CXTimeUtil

@Suppress("MemberVisibilityCanPrivate")
class CXHybirdStatisticalUrlModel {
    var timeConsumingList: MutableList<CXHybirdStatisticalUrlTimeConsumingModel> = mutableListOf()
    private var startTime: Long = 0L
        set(value) {
            field = value
            if (field != 0L) {
                finishTime = 0L
            }
        }

    private var finishTime: Long = 0L
        set(value) {
            field = value
            if (field != 0L) {
                synchronized(this.startTime) {
                    val timeConsuming = System.currentTimeMillis() - startTime
                    val analysisModel = CXHybirdStatisticalUrlTimeConsumingModel(CXTimeUtil.yMdHmsS(startTime), CXTimeUtil.yMdHmsS(field), timeConsuming)
                    timeConsumingList.add(analysisModel)
                    startTime = 0L
                }
            }
        }

    fun onPageStarted() {
        startTime = System.currentTimeMillis()
    }

    fun onPageFinished() {
        finishTime = System.currentTimeMillis()
    }

}

