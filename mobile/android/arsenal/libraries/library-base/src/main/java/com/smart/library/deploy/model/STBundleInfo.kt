package com.smart.library.deploy.model

data class STBundleInfo(
        val version: Int,
        val checksum: String = "",
        val indexName: String = "index.android.bundle"
)