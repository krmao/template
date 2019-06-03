package com.smart.library.util

import android.graphics.Bitmap
import android.text.TextUtils
import com.smart.library.base.STBaseApplication
import com.smart.library.util.cache.STCacheManager
import java.io.*
import java.nio.channels.FileChannel
import java.util.*


@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
object STFileUtil {

    const val ENCODING_UTF8 = "UTF-8"

    @JvmStatic
    fun fileChannelCopy(sourceFile: File, destFile: File): Boolean {
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        var fileChannelIn: FileChannel? = null
        var fileChannelOut: FileChannel? = null
        try {
            fileInputStream = FileInputStream(sourceFile)
            fileOutputStream = FileOutputStream(destFile)
            fileChannelIn = fileInputStream.channel// 得到对应的文件通道
            fileChannelOut = fileOutputStream.channel// 得到对应的文件通道
            fileChannelIn?.transferTo(0, fileChannelIn.size(), fileChannelOut)// 连接两个通道，并且从in通道读取，然后写入out通道
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            STLogUtil.e("fileChannelCopy", "copy failure", e)
        } finally {
            try {
                fileInputStream?.close()
                fileChannelIn?.close()
                fileOutputStream?.close()
                fileChannelOut?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return false
    }

    @Throws(FileNotFoundException::class, IOException::class)
    @JvmStatic
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

    /**
     * 根据文件路径获取文件名称
     */
    fun getFileName(path: String, removeSuffix: Boolean = true): String {
        val nameWithSuffix = path.substringAfterLast(File.separatorChar, path)
        return (if (removeSuffix) path.substringBefore('.', path) else nameWithSuffix)
    }

    @Throws(FileNotFoundException::class, IOException::class)
    @JvmStatic
    fun copyFromAssets(fromPathInAssetsDir: String?, toFile: File?): Boolean {
        STLogUtil.d("copyFromAssets, fromPathInAssetsDir=$fromPathInAssetsDir, toFile=${toFile?.absolutePath}")
        var success = false
        if (!fromPathInAssetsDir.isNullOrBlank() && toFile != null) {
            try {
                STFileUtil.deleteFile(toFile)
                STFileUtil.copy(STBaseApplication.INSTANCE.assets.open(fromPathInAssetsDir), toFile)
                success = true
            } catch (exception: FileNotFoundException) {
                STLogUtil.e("copyFromAssets, FileNotFoundException", exception)
            } catch (exception: IOException) {
                STLogUtil.e("copyFromAssets, IOException", exception)
            }
        } else {
            STLogUtil.e("copyFromAssets, arguments invalid")
        }
        return success
    }

    @Throws(FileNotFoundException::class, IOException::class)
    @JvmStatic
    fun copy(inputStream: InputStream?, toFilePath: String?, onProgress: ((current: Long, total: Long) -> Unit?)? = null) = copy(inputStream, File(toFilePath), onProgress)

    /**
     * Copies all bytes from the input stream to the output stream. Does not close or flush either
     * stream.
     *
     * @param from the input stream to read from
     * @param to the output stream to write to
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    @JvmStatic
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

    /**
     * If toLocation does not exist, it will be created.
     */
    @JvmStatic
    fun copyDirectory(fromLocation: File, toLocation: File): Boolean {
        var copySuccess = true
        if (fromLocation.isDirectory) {
            if (!toLocation.exists() && !toLocation.mkdirs()) {
                STLogUtil.e("copyDirectory", "Cannot create dir ${fromLocation.absolutePath}")
                copySuccess = false
            } else {
                fromLocation.list().filter { it?.isNotBlank() == true }.forEach {
                    if (!copyDirectory(File(fromLocation, it), File(toLocation, it))) {
                        copySuccess = false
                    }
                }
            }
        } else {
            // make sure the directory we plan to store the recording in exists
            val directory = toLocation.parentFile
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                STLogUtil.e("copyDirectory", "Cannot create dir ${directory.absolutePath}")
                return false
            }
            return fileChannelCopy(fromLocation, toLocation)
        }
        return copySuccess
    }

    @JvmStatic
    fun deleteDirectory(filePath: String?) {
        if (!TextUtils.isEmpty(filePath))
            deleteDirectory(File(filePath))
    }

    @JvmStatic
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

    @JvmStatic
    fun deleteFile(filePath: String?) {
        if (!TextUtils.isEmpty(filePath))
            deleteFile(File(filePath))
    }

    @JvmStatic
    fun deleteFile(file: File?) {
        file?.delete()
    }

    //返回文件夹的大小(bytes)
    @JvmStatic
    fun getDirSize(dir: File): Long {
        var result: Long = 0
        if (dir.exists()) {
            for (tmpFile in dir.listFiles()) {
                result += if (tmpFile.isDirectory) getDirSize(tmpFile) else tmpFile.length()
            }
        }
        return result
    }

    @JvmStatic
    fun getFileSize(file: File): Long {
        var rslt: Long = 0
        if (file.exists()) {
            rslt = file.length()
        }
        return rslt
    }

    @JvmStatic
    fun existsInAssets(pathInAssetsDir: String?): Boolean {
        if (!pathInAssetsDir.isNullOrBlank()) {
            val assetManager = STBaseApplication.INSTANCE.resources.assets
            var inputStream: InputStream? = null
            try {
                inputStream = assetManager.open(pathInAssetsDir.replace("assets://", ""))
                if (null != inputStream) {
                    return true
                }
            } catch (_: IOException) {
            } finally {
                try {
                    inputStream?.close()
                } catch (_: IOException) {
                }
            }
        }
        return false
    }

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
    fun writeTextToFile(content: String, file: File): Boolean {
        return writeTextToFile(content, null, file)
    }


    @JvmStatic
    fun saveUncaughtException(thread: Thread?, throwable: Throwable?) {
        STLogUtil.e("crash", "app crash, thread=${thread?.name}\n", throwable)
        STFileUtil.writeTextToFile("app crash, thread=${thread?.name}\n", throwable, File(STCacheManager.getCacheCrashDir(), STTimeUtil.yMdHmsSWithoutSeparator() + ".txt"))
    }

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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
                outputStreamWriter?.close()
                printWriter?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return isAppendSuccess
    }

    @JvmStatic
    fun getFileList(dirPath: String): List<File> = getFileList(File(dirPath))

    @JvmStatic
    fun getFileList(dir: File): List<File> = dir.walkTopDown().filter { it.isFile }.toList()
}