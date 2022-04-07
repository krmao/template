package com.smart.library.deploy.model

import androidx.annotation.Keep

@Suppress("MemberVisibilityCanBePrivate", "unused")
//@Keep
data class STPatchInfo(
        val baseVersion: Int,
        val toVersion: Int,
        val indexName: String = "index.android.bundle",
        val patchChecksum: String = "",
        val bundleChecksum: String = ""
)