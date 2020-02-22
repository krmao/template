package com.smart.template.home.localPictures

import java.util.*

object STLocalPicturesUtil {

    fun findRecentMaxValue(arrayTitlePos: ArrayList<Int>?, coordinatePosition: Int): Int {
        var resultPos = Int.MIN_VALUE
        if (arrayTitlePos == null) {
            return resultPos
        }
        var low = 0
        var high = arrayTitlePos.size - 1
        while (high - low >= 0) {
            val halfPosition = low + (high - low shr 1)
            if (arrayTitlePos[halfPosition] < coordinatePosition) {
                low = halfPosition + 1
                if (low < arrayTitlePos.size && arrayTitlePos[low] > coordinatePosition) {
                    resultPos = low
                    break
                }
            } else if (arrayTitlePos[halfPosition] > coordinatePosition) {
                if (halfPosition - 1 < 0) {
                    resultPos = halfPosition
                    break
                } else {
                    if (arrayTitlePos[halfPosition - 1] < coordinatePosition) {
                        resultPos = halfPosition
                        break
                    } else {
                        high = halfPosition - 1
                    }
                }
            } else {
                resultPos = halfPosition
                break
            }
        }
        return if (resultPos >= 0 && resultPos < arrayTitlePos.size) arrayTitlePos[resultPos] else resultPos
    }


    fun findRecentMinValue(arrayTitlePos: ArrayList<Int>?, coordinatePosition: Int): Int {
        var resultPos = Int.MIN_VALUE
        if (arrayTitlePos == null) {
            return resultPos
        }
        var low = 0
        var high = arrayTitlePos.size - 1
        while (high - low >= 0) {
            val halfPosition = low + (high - low shr 1)
            if (arrayTitlePos[halfPosition] < coordinatePosition) {
                if (halfPosition + 1 < arrayTitlePos.size) {
                    if (arrayTitlePos[halfPosition + 1] > coordinatePosition) {
                        resultPos = halfPosition
                        break
                    } else {
                        low = halfPosition + 1
                    }
                } else {
                    resultPos = halfPosition
                    break
                }
            } else if (arrayTitlePos[halfPosition] > coordinatePosition) {
                if (halfPosition - 1 < 0) {
                    break
                } else {
                    if (arrayTitlePos[halfPosition - 1] < coordinatePosition) {
                        resultPos = halfPosition - 1
                        break
                    } else {
                        high = halfPosition - 1
                    }
                }
            } else {
                resultPos = halfPosition
                break
            }
        }
        return if (resultPos >= 0 && resultPos < arrayTitlePos.size) arrayTitlePos[resultPos] else resultPos
    }
}