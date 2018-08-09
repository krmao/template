package com.smart.library.deploy.model

import com.smart.library.util.CXLogUtil
import java.io.File

abstract class CXIBundleHelper(val info: CXBundleInfo, val rootDir: File, val TAG: String) {

    fun checkUnzipDirValid(): Boolean {
        val indexFile = getIndexFile()
        val indexFileExists = indexFile.exists()
        CXLogUtil.d(TAG, "checkUnzipDirValid=$indexFileExists, path=${indexFile.absolutePath}, indexFileExists=$indexFileExists")
        return indexFileExists
    }

    fun checkZipFileValid(zipFile: File?): Boolean {
        val valid = zipFile?.exists() == true
        CXLogUtil.d(TAG, "checkZipFileValid=$valid, path=${zipFile?.absolutePath}")
        return valid
    }

    abstract fun getIndexFile(): File

}