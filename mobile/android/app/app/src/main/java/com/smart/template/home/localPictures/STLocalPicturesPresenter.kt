package com.smart.template.home.localPictures

import com.smart.library.util.STLogUtil
import com.smart.library.util.STStorageUtil
import com.smart.library.util.rx.permission.RxPermissions
import java.util.*

class STLocalPicturesPresenter(var activity: STLocalPicturesActivity) : STLocalPicturesContract.Presenter<STLocalPicturesContract.View?>() {
    private var scanLocalDataResult: STStorageUtil.LocalPictureTimeSlotResult? = null

    fun getLocalPictureDataList(): List<STStorageUtil.LocalPictureTimeSlotInfo> = scanLocalDataResult?.localPictureInfoList ?: ArrayList()

    /**
     * load local pictures
     */
    override fun loadLocalPicture() { // you can use rxJava and rxAndroid here
        val loadLocalPictureTask = {
            Thread(Runnable {
                scanLocalDataResult = STStorageUtil.loadAllLocalPictures(activity)
                activity.runOnUiThread { activity.refreshLocalPictures(scanLocalDataResult) }
            }).start()
        }

        if (RxPermissions.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            loadLocalPictureTask()
        } else {
            RxPermissions.ensurePermissions(activity, {
                STLogUtil.e(RxPermissions.TAG, "request permissions callback -> $it")
                if (it) {
                    loadLocalPictureTask()
                }
            }, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    /**
     * use the binary search method find the closest title position before the current position
     *
     * @param coordinatePosition
     * @return
     */
    override fun findBeforeTitlePosition(coordinatePosition: Int): Int {
        return if (scanLocalDataResult != null) STLocalPicturesUtil.findRecentMinValue(scanLocalDataResult?.titlePositionList, coordinatePosition) else Int.MIN_VALUE
    }

    /**
     * use the binary search method finds the closest title position after the current position
     *
     * @param coordinatePosition
     * @return
     */
    override fun findAfterTitlePosition(coordinatePosition: Int): Int {
        return if (scanLocalDataResult != null) STLocalPicturesUtil.findRecentMaxValue(scanLocalDataResult?.titlePositionList, coordinatePosition) else Int.MIN_VALUE
    }
}