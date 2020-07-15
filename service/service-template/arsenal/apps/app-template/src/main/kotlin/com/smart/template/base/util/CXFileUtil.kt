package com.smart.template.base.util

import java.io.*
import java.nio.channels.FileChannel
import java.util.*

@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate", "ControlFlowWithEmptyBody")
object CXFileUtil {

    private val ENCODING_UTF8 = "UTF-8"
    private val TAG = CXFileUtil::class.java.simpleName

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

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @Throws(FileNotFoundException::class, IOException::class)
    fun copy(inputStream: InputStream?, destFile: File?, onProgress: ((current: Long, total: Long) -> Unit?)? = null) {
        var outputStream: OutputStream? = null
        try {
            if (destFile != null && !destFile.exists()) {
                destFile.parentFile.mkdirs()
                destFile.createNewFile()
            }
            outputStream = FileOutputStream(destFile)
            copy(inputStream, outputStream, onProgress)
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun copy(inputStream: InputStream?, toFilePath: String?, onProgress: ((current: Long, total: Long) -> Unit?)? = null) = copy(inputStream, if (toFilePath == null) null else File(toFilePath), onProgress)

    /**
     * copy from
     * @see com.google.common.io.ByteStreams.copy
     *
     * Copies all bytes from the input stream to the output stream. Does not close or flush either
     * stream.
     *
     * @param from the input stream to read from
     * @param to the output stream to write to
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun copy(from: InputStream?, to: OutputStream?, onProgress: ((current: Long, total: Long) -> Unit?)? = null): Long {
        checkNotNull(from)
        checkNotNull(to)
        val total = from.available().toLong()
        val buf = ByteArray(8192)
        var current: Long = 0
        while (true) {
            val r = from.read(buf)
            if (r == -1) {
                break
            }
            to.write(buf, 0, r)
            current += r.toLong()
            onProgress?.invoke(current, total)
        }
        return current
    }

    fun deleteDirectory(filePath: String?) {
        if (!filePath.isNullOrBlank())
            deleteDirectory(File(filePath))
    }

    fun deleteDirectory(file: File?) {
        if (file == null) {
        } else {
            var childFiles: Array<File>? = null
            var isDirectory = false
            try {
                childFiles = file.listFiles()
                isDirectory = file.isDirectory
            } catch (exception: SecurityException) {
                exception.printStackTrace()
            }
            if (isDirectory && childFiles?.isNotEmpty() == true) {
                for (childFile: File? in childFiles)
                    deleteDirectory(childFile)
            }
            deleteFile(file)
        }
    }

    fun deleteFile(filePath: String?) {
        if (!filePath.isNullOrEmpty())
            deleteFile(File(filePath))
    }

    fun deleteFile(file: File?) {
        file?.delete()
    }

    //返回文件夹的大小(bytes)
    fun getDirSize(dir: File): Long {
        var result: Long = 0
        if (dir.exists()) {
            for (tmpFile in dir.listFiles() ?: arrayOf()) {
                result += if (tmpFile.isDirectory) getDirSize(tmpFile) else tmpFile.length()
            }
        }
        return result
    }

    fun readTextFromFile(inputStream: InputStream): String {
        var content = ""
        try {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, ENCODING_UTF8))
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

    fun writeTextToFile(content: String, file: File): Boolean = writeTextToFile(content, null, file)

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

    fun getFileList(dirPath: String): List<File> =
        getFileList(File(dirPath))

    fun getFileList(dir: File): List<File> =
        dir.walkTopDown().filter { it.isFile }.toList()
}
