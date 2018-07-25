package com.smart.library.deploy.model.bundle

import com.smart.library.util.CXFileUtil

@Suppress("MemberVisibilityCanBePrivate", "unused")
data class CXBundleInfo(
        val version: Int,
        val checksum: String = "",
        val indexName: String = "index.android.bundle"
) {
//    fun getZipFileName(): String? = if (fullName == null || fullName.isNullOrBlank()) null else CXFileUtil.getFileName(fullName, false)
//    fun getUnzipDirName(): String? = if (fullName == null || fullName.isNullOrBlank()) null else CXFileUtil.getFileName(fullName, true)
}