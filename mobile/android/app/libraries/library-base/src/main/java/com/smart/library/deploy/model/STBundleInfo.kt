package com.smart.library.deploy.model

import androidx.annotation.Keep

@Keep
data class STBundleInfo(
        val version: Int,
        val checksum: String = "",
        val indexName: String = "index.android.bundle"
)