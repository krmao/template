package com.smart.library.pictureviewer.localpictures

import com.smart.library.util.STStorageUtil

interface STLocalPicturesContract {
    interface View {
        fun refreshLocalPictures(scanLocalDataResult: STStorageUtil.LocalPictureTimeSlotResult?)
    }

    abstract class Presenter<V> {
        abstract fun loadLocalPicture()
        abstract fun findBeforeTitlePosition(coordinatePosition: Int): Int
        abstract fun findAfterTitlePosition(coordinatePosition: Int): Int
    }
}