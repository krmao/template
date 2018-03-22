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

@RestController
@RequestMapping("/developer")
class DeveloperController() {
    private val log: Logger = LogManager.getLogger(DeveloperController::class.java.name)

    @ApiOperation("获取UI切图", notes = "返回UI切图")
    @GetMapping("/getAllFiles")
    fun getAllFiles(): HKResponse<FileNode> {
        val rootPath = CXConfig.DEFAULT_FILES_DIR
        val rootDir = File(rootPath, "developer")

        println("filesDir=${rootDir.absolutePath}")
        println("filesDir.exists=${rootDir.exists()}")

        val fileNode = getFileNode(rootDir)

        log.error(fileNode)
        return HKResponse(fileNode)
    }

    data class FileNode(
        var path: String,
        var children: MutableList<FileNode> = arrayListOf()
    ) {
        override fun toString(): String {
            return "\n$path"
        }
    }


    fun getFileNode(file: File): FileNode {
        if (file.exists()) {
            val rootPath = CXConfig.DEFAULT_FILES_DIR //用在tomcat部署的正式正产环境
            val fileNode: FileNode = FileNode(file.path.replace(rootPath.absolutePath, "/template-files"))
            log.warn("${if (file.isDirectory) "dire" else "file"}:" + file.path)
            if (file.isDirectory) file.listFiles().filter { it.exists() /*&& it.name != "prd"*/ }.forEach { fileNode.children.add(getFileNode(it)) }
            return fileNode
        } else {
            return FileNode("")
        }
    }
}
