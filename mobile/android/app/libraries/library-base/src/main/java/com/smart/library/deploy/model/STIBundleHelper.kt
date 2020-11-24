package com.smart.library.deploy.model

import com.smart.library.util.STLogUtil
import java.io.File

abstract class STIBundleHelper(val info: STBundleInfo, val rootDir: File?, val TAG: String) {

    fun checkUnzipDirValid(): Boolean {
        val indexFile = getIndexFile()
        val indexFileExists = indexFile.exists()
        STLogUtil.d(TAG, "checkUnzipDirValid=$indexFileExists, path=${indexFile.absolutePath}, indexFileExists=$indexFileExists")
        return indexFileExists
    }

    fun checkZipFileValid(zipFile: File?): Boolean {
        val valid = zipFile?.exists() == true
        STLogUtil.d(TAG, "checkZipFileValid=$valid, path=${zipFile?.absolutePath}")
        return valid
    }

    abstract fun getIndexFile(): File

}