package com.smart.library.deploy.model

@Suppress("MemberVisibilityCanBePrivate", "unused")
data class CXPatchInfo(
        val baseVersion: Int,
        val toVersion: Int,
        val indexName: String = "index.android.bundle",
        val patchChecksum: String = "",
        val bundleChecksum: String = ""
)