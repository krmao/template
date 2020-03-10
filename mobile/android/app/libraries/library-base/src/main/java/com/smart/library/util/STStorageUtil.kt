package com.smart.library.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresPermission
import com.smart.library.util.STStorageUtil.LocalPictureTimeSlotInfo.Companion.DATA_TYPE_TITLE
import java.util.*

@Suppress("unused")
object STStorageUtil {
    const val TAG = "STStorageUtil"

    /**
     * query only jpeg and png image type files
     * default search path is MediaStore.Images.Media.EXTERNAL_CONTENT_URI
     */
    @Suppress("DEPRECATION")
    @RequiresPermission(value = android.Manifest.permission.READ_EXTERNAL_STORAGE)
    fun loadAllLocalPictures(mContext: Context, contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI): LocalPictureTimeSlotResult? {
        val result = LocalPictureTimeSlotResult(mContext)
        try {
            val mContentResolver: ContentResolver = mContext.contentResolver
            Log.i(TAG, contentUri.path ?: "")
            val cursor: Cursor? = mContentResolver.query(contentUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", arrayOf("image/jpeg", "image/png"), MediaStore.Images.Media.DATE_MODIFIED + " DESC")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val path: String? = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    val width: Int = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                    val height: Int = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                    val modifiedData: Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                    val addedData: Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
                    if (path == null || "" == path || width == 0 || height == 0 || modifiedData == 0L) {
                        continue
                    }
                    val lpi = LocalPictureInfo(path, width, height, addedData * 1000, modifiedData * 1000)
                    result.add(lpi)
                }
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    @Suppress("MemberVisibilityCanBePrivate")
    class LocalPictureTimeSlotResult(private val context: Context) {
        val titlePositionList: ArrayList<Int> = ArrayList()
        val titleSet: HashSet<String?> = HashSet()
        val localPictureInfoList: ArrayList<LocalPictureTimeSlotInfo> = ArrayList()

        fun add(pictureInfo: LocalPictureInfo) {
            val timeSlotStr: String? = STTimeUtil.getTimeSlotDescription(pictureInfo.modifiedDate)
            if (!titleSet.contains(timeSlotStr)) {
                titleSet.add(timeSlotStr)
                // save title position
                titlePositionList.add(this.localPictureInfoList.size)
                // add title object
                this.localPictureInfoList.add(LocalPictureTimeSlotInfo(dataTitle = timeSlotStr, dataType = DATA_TYPE_TITLE))
                // add content object
                val contentInfo = LocalPictureTimeSlotInfo(pictureInfo = pictureInfo)
                this.localPictureInfoList.add(contentInfo)
            } else {
                this.localPictureInfoList.add(LocalPictureTimeSlotInfo(pictureInfo = pictureInfo))
            }
        }
    }

    class LocalPictureInfo(val path: String, val srcWidth: Int, val srcHeight: Int, val addedDate: Long, val modifiedDate: Long)

    @Suppress("MemberVisibilityCanBePrivate")
    class LocalPictureTimeSlotInfo @JvmOverloads constructor(var pictureInfo: LocalPictureInfo? = null, var dataTitle: String? = "", var dataType: Int = DATA_TYPE_CONTENT) {
        companion object {
            const val DATA_TYPE_TITLE = 1
            const val DATA_TYPE_CONTENT = 2
        }
    }
}