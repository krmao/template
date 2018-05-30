package com.smart.template.http.controller

import com.smart.template.base.config.config.CXConfig
import com.smart.template.http.model.HKResponse
import io.swagger.annotations.ApiOperation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
@RestController
@RequestMapping("/developer")
class DeveloperController() {
    private val log: Logger = LogManager.getLogger(DeveloperController::class.java.name)

    private var rootDirName = "apps"

    private var rootPath = File("")

    @ApiOperation("获取UI切图", notes = "返回UI切图")
    @GetMapping("/getAllFiles")
    fun getAllFiles(): HKResponse<FileNode> {
        val rootDir = File(CXConfig.PROJECT_DIR.parent, rootDirName)

        println("rootDir.path=${rootDir.path}")
        println("rootDir.absolutePath=${rootDir.absolutePath}")
        println("rootDir.canonicalPath=${rootDir.canonicalPath}")
        println("rootDir.exists=${rootDir.exists()}")

        rootPath = rootDir.canonicalFile

        println("rootPath.path=${rootPath.path}")
        println("rootPath.absolutePath=${rootPath.absolutePath}")
        println("rootPath.canonicalPath=${rootPath.canonicalPath}")
        println("rootPath.exists=${rootPath.exists()}")

        val fileNode = getFileNode(rootDir.canonicalFile)

        log.error(fileNode)
        return HKResponse(fileNode)
    }

    data class FileNode(
        var path: String,
        var directory: Boolean = false,
        var children: MutableList<FileNode> = arrayListOf()
    ) {
        override fun toString(): String {
            return "\n$path"
        }
    }


    fun getFileNode(file: File): FileNode {
        if (file.exists()) {
            val fileNode = FileNode(file.path.replace(rootPath.absolutePath, "/$rootDirName"), file.isDirectory)
            log.warn("${if (file.isDirectory) "dire" else "file"}:" + file.path)
            if (file.isDirectory) file.listFiles().filter { it.exists() /*&& it.name != "prd"*/ }.forEach { fileNode.children.add(getFileNode(it)) }
            return fileNode
        } else {
            return FileNode("")
        }
    }
}
