package com.smart.template.http.controller

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
        val rootFile = File("./src/")

        val fileNode = getFileNode(rootFile)

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
        val fileNode: FileNode = FileNode(file.path)
        log.warn("${if (file.isDirectory) "dire" else "file"}:" + file.path)
        fileNode.path = file.path
        if (file.isDirectory) {
            file.listFiles().filter { it.exists() }.forEach {
                fileNode.children.add(getFileNode(it))
            }
        }
        return fileNode
    }

}


/*
var data = {
    path: '水果',
    children: [
    {path: 'http://oznsh6z3y.bkt.clouddn.com/banner_10.jpg'},
    {path: 'http://oznsh6z3y.bkt.clouddn.com/banner_1.jpg'},
    {
        path: '苹果',
        children: [
        {
            path: '红富士',
            children: [
            {path: 'http://oznsh6z3y.bkt.clouddn.com/banner_14.jpg'},
            {path: 'http://oznsh6z3y.bkt.clouddn.com/banner_11.jpg'}
            ]
        },
        {path: 'http://oznsh6z3y.bkt.clouddn.com/banner_4.jpg'},
        {path: 'http://oznsh6z3y.bkt.clouddn.com/banner_7.jpg'},
        {
            path: '金苹果',
            children: [
            {path: 'http://oznsh6z3y.bkt.clouddn.com/banner_8.jpg'},
            {path: 'http://oznsh6z3y.bkt.clouddn.com/banner_9.jpg'}
            ]
        }
        ]
    }
    ]
};*/
