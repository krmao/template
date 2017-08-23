package com.xixi.library.android.util

import android.graphics.Bitmap
import android.text.TextUtils
import java.io.*
import java.nio.channels.FileChannel
import java.util.*

@Suppress("unused")
object CXFileUtil {

    val ENCODING_UTF8 = "UTF-8"

    fun fileChannelCopy(sourceFile: File, destFile: File) {
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        var fileChannelIn: FileChannel? = null
        var fileChannelOut: FileChannel? = null
        try {
            fileInputStream = FileInputStream(sourceFile)
            fileOutputStream = FileOutputStream(destFile)
            fileChannelIn = fileInputStream.channel// 得到对应的文件通道
            fileChannelOut = fileOutputStream.channel// 得到对应的文件通道
            fileChannelIn!!.transferTo(0, fileChannelIn.size(), fileChannelOut)// 连接两个通道，并且从in通道读取，然后写入out通道
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close()
                if (fileChannelIn != null)
                    fileChannelIn.close()
                if (fileOutputStream != null)
                    fileOutputStream.close()
                if (fileChannelOut != null)
                    fileChannelOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun deleteDirectory(filePath: String?) {
        if (!TextUtils.isEmpty(filePath))
            deleteDirectory(File(filePath))
    }

    fun deleteDirectory(file: File?) {
        try {
            if (file != null) {
                val childFiles = file.listFiles()
                if (file.isDirectory && childFiles != null && childFiles.isNotEmpty()) {
                    for (childFile in childFiles)
                        deleteDirectory(childFile)
                }
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //返回文件夹的大小(bytes)
    fun getDirSize(dir: File): Long {
        var result: Long = 0
        if (dir.exists()) {
            for (tmpFile in dir.listFiles()) {
                if (tmpFile.isDirectory)
                    result += getDirSize(tmpFile)
                else
                    result += tmpFile.length()
            }
        }
        return result
    }

    fun readTextFromFile(file: File): String {
        var content = ""
        try {
            val bufferedReader = BufferedReader(InputStreamReader(FileInputStream(file), ENCODING_UTF8))
            var oneLine: String? = bufferedReader.readLine()
            while (oneLine != null) {
                content += oneLine + "\r\n"
                oneLine = bufferedReader.readLine()
            }
            bufferedReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return content
    }

    fun readLinesFromFile(file: File): List<String> {
        val contentList = ArrayList<String>()
        try {
            val bufferedReader = BufferedReader(InputStreamReader(FileInputStream(file), ENCODING_UTF8))
            var oneLine: String? = bufferedReader.readLine()
            while (oneLine != null) {
                contentList.add(oneLine)
                oneLine = bufferedReader.readLine()
            }
            bufferedReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return contentList
    }

    fun writeLinesToFile(contentList: List<String>, file: File): Boolean {
        var isWriteSuccess = false
        try {
            val outputStreamWriter = OutputStreamWriter(FileOutputStream(file, true), ENCODING_UTF8)
            val printWriter = PrintWriter(outputStreamWriter, true)
            for (oneLine in contentList) {
                printWriter.println(oneLine)
            }
            printWriter.flush()
            printWriter.close()
            isWriteSuccess = true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return isWriteSuccess
    }

    fun writeTextToFile(content: String, file: File): Boolean {
        return writeTextToFile(content, null, file)
    }

    fun writeTextToFile(content: String, throwable: Throwable?, file: File): Boolean {
        var isAppendSuccess = false
        try {
            val outputStreamWriter = OutputStreamWriter(FileOutputStream(file, false), ENCODING_UTF8)
            val printWriter = PrintWriter(outputStreamWriter, true)
            printWriter.print(content)
            throwable?.printStackTrace(printWriter)
            printWriter.flush()
            printWriter.close()
            isAppendSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return isAppendSuccess
    }

    fun saveBitmapToFile(bitmap: Bitmap, file: File): Boolean {
        var isAppendSuccess = false
        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            isAppendSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return isAppendSuccess
    }

    fun updateBitmapToFile(bitmap: Bitmap, file: File): Boolean {
        var isAppendSuccess = false
        if (file.exists()) {
            file.delete()
        }
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            isAppendSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return isAppendSuccess
    }

    fun appendLine(filePath: String, oneLine: String): Boolean {
        var isAppendSuccess = false
        var outputStreamWriter: OutputStreamWriter? = null
        var printWriter: PrintWriter? = null
        try {
            outputStreamWriter = OutputStreamWriter(FileOutputStream(filePath, true), ENCODING_UTF8)
            printWriter = PrintWriter(outputStreamWriter, true)
            printWriter.print(oneLine)
            printWriter.flush()
            printWriter.close()
            isAppendSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (outputStreamWriter != null)
                    outputStreamWriter.close()
                if (printWriter != null)
                    printWriter.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return isAppendSuccess
    }
}
