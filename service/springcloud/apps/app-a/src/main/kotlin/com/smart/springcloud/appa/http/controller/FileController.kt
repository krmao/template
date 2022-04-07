package com.smart.springcloud.appa.http.controller

import com.smart.springcloud.appa.base.config.config.CXConfig
import com.smart.springcloud.appa.base.util.CXChecksumUtil
import com.smart.springcloud.appa.base.util.CXFileUtil
import com.smart.springcloud.appa.http.model.HKResponse
import io.swagger.annotations.ApiOperation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
@RequestMapping("/file")
class FileController() {
    private val log: Logger = LogManager.getLogger(FileController::class.java.name)

    @ApiOperation("上传文件", notes = "返回相对路径")
    @PostMapping("/upload")
    fun upload(@RequestPart("files") files: Array<MultipartFile>): HKResponse<Any> {
        if (files.count() == 0) return HKResponse.ok()

        println("filesDir=${CXConfig.DEFAULT_FILES_DIR.absolutePath}")
        println("filesDir.exists=${CXConfig.DEFAULT_FILES_DIR.exists()}")

        val filePathMap = mutableMapOf<String, String>()

        for (file in files) {
            val oldName = file.originalFilename ?: ""
            val newName = CXChecksumUtil.genMD5Checksum(oldName + "-" + System.currentTimeMillis())
            CXFileUtil.copy(file.inputStream, File(CXConfig.DEFAULT_FILES_DIR, newName))
            filePathMap[oldName] = newName
        }

        log.warn("filePathMap:$filePathMap")
        return HKResponse(filePathMap)
    }
}
