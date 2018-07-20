package com.smart.library.deploy.model.bundle

@Suppress("MemberVisibilityCanBePrivate", "unused")
data class CXPatchInfo(
        val patchName: String? = "bundle.patch",
        val baseVersion: Int,
        val toVersion: Int,
        val bundleFullName: String? = "bundle.zip",
        val indexName: String? = "index.android.bundle",
        val patchChecksum: String = "",
        val bundleChecksum: String = ""
)