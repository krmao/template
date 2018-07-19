package com.smart.library.util

import com.smart.library.bundle.CXHybird
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

@Suppress("MemberVisibilityCanPrivate", "unused")
object CXZipUtil {
    private val BUFFER = 2048

    /**
     * Zips a file at a location and places the resulting zip file at the toLocation
     * Example: zip("downloads/images", "downloads/images.zip");
     */
    @Throws(NullPointerException::class, FileNotFoundException::class, IOException::class)
    fun zip(sourcePath: String, targetFilePath: String) = zip(File(sourcePath), targetFilePath)

    @Throws(NullPointerException::class, FileNotFoundException::class, IOException::class)
    fun zip(sourceFile: File, targetFilePath: String) {
        var bufferedInputStream: BufferedInputStream? = null
        var zipOutputStream: ZipOutputStream? = null
        try {
            zipOutputStream = ZipOutputStream(BufferedOutputStream(FileOutputStream(targetFilePath)))
            if (sourceFile.isDirectory) {
                zipSubFolder(zipOutputStream, sourceFile, sourceFile.path.length)
            } else {
                val data = ByteArray(BUFFER)
                bufferedInputStream = BufferedInputStream(FileInputStream(sourceFile), BUFFER)
                zipOutputStream.putNextEntry(ZipEntry(getLastPathComponent(sourceFile.path)))
                var count: Int = bufferedInputStream.read(data, 0, BUFFER)
                while (count != -1) {
                    zipOutputStream.write(data, 0, count)
                    count = bufferedInputStream.read(data, 0, BUFFER)
                }
            }
        } finally {
            bufferedInputStream?.close()
            zipOutputStream?.close()
        }
    }

    /**
     * Zips a subfolder
     */
    @Throws(IOException::class)
    private fun zipSubFolder(zipOutputStream: ZipOutputStream, fileFolder: File, basePathLength: Int) {
        for (file in fileFolder.listFiles()) {
            if (file.isDirectory) {
                zipSubFolder(zipOutputStream, file, basePathLength)
            } else {
                var bufferedInputStream: BufferedInputStream? = null
                try {
                    val data = ByteArray(BUFFER)
                    bufferedInputStream = BufferedInputStream(FileInputStream(file.path), BUFFER)
                    val entryName = file.path.substring(basePathLength)
                    //println("entryName:" + entryName + " , " + file.path)
                    zipOutputStream.putNextEntry(ZipEntry(entryName))
                    var count: Int = bufferedInputStream.read(data, 0, BUFFER)
                    while (count != -1) {
                        zipOutputStream.write(data, 0, count)
                        count = bufferedInputStream.read(data, 0, BUFFER)
                    }
                } finally {
                    bufferedInputStream?.close()
                }
            }
        }
    }

    /**
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     *
     */
    private fun getLastPathComponent(filePath: String): String {
        val segments = filePath.split(File.separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (segments.isEmpty())
            return ""
        return segments[segments.size - 1]
    }

    /**
     * Example: unzip("downloads/images.zip", "downloads/");
     */
    @Throws(ZipException::class, IOException::class)
    fun unzip(zipFilePath: String, inNotToDirPath: String) = unzip(File(zipFilePath), File(inNotToDirPath))

    /**
     * Example: unzip("downloads/images.zip", "downloads/");
     */
    @Throws(ZipException::class, IOException::class, FileNotFoundException::class)
    fun unzip(zipFile: File, inNotToDir: File?) {
        var zip: ZipFile? = null

        try {
            val targetFinalFile = File(inNotToDir?.absolutePath)
            targetFinalFile.mkdirs()
            zip = ZipFile(zipFile)
            val zipFileEntries = zip.entries()
            while (zipFileEntries.hasMoreElements()) {
                val entry = zipFileEntries.nextElement()
                CXLogUtil.e("unzip", "name=${entry.name}, isDirectory=${entry.isDirectory}")
                val destFile = File(targetFinalFile, entry.name)
                destFile.parentFile.mkdirs()
                if (!entry.isDirectory) {
                    val bufferedInputStream = BufferedInputStream(zip.getInputStream(entry))
                    val dataBytes = ByteArray(BUFFER)
                    val bufferedOutputStream = BufferedOutputStream(FileOutputStream(destFile), BUFFER)
                    var currentByte: Int = bufferedInputStream.read(dataBytes, 0, BUFFER)
                    while (currentByte != -1) {
                        bufferedOutputStream.write(dataBytes, 0, currentByte)
                        currentByte = bufferedInputStream.read(dataBytes, 0, BUFFER)
                    }
                    bufferedOutputStream.flush()
                    bufferedOutputStream.close()
                    bufferedInputStream.close()
                }
            }
        } finally {
            zip?.close()
        }
    }

    @JvmStatic
    fun unzipOrFalse(zipFile: File, unZipDir: File?, unzipInNotTo: Boolean = false): Boolean {
        if (!zipFile.exists() || unZipDir == null)
            return false

        var success = false
        val inNotToDir: File

        if (unzipInNotTo) {
            CXFileUtil.deleteDirectory(File(unZipDir, CXFileUtil.getFileName(zipFile.absolutePath, true)))
            inNotToDir = unZipDir
        } else {
            CXFileUtil.deleteDirectory(unZipDir)
            inNotToDir = unZipDir.parentFile
        }

        try {
            CXZipUtil.unzip(zipFile, inNotToDir)
            success = true
        } catch (exception: FileNotFoundException) {
            CXLogUtil.e(CXHybird.TAG, "解压失败:文件不存在", exception)
        } catch (exception: IOException) {
            CXLogUtil.e(CXHybird.TAG, "解压失败:文件读写错误", exception)
        } catch (exception: ZipException) {
            CXLogUtil.e(CXHybird.TAG, "解压失败:压缩包错误", exception)
        }
        return success
    }
}
