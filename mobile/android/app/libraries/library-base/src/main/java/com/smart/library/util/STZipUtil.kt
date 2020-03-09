package com.smart.library.util

import com.smart.library.bundle.STHybird
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

@Suppress("MemberVisibilityCanPrivate", "unused")
object STZipUtil {
    private const val BUFFER = 2048

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
        for (file in fileFolder.listFiles() ?: arrayOf()) {
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
     * Example: unzipInDir("android-rn-1.zip", ".../react-native/base/")
     *
     * @param unzipTopParentDir .../react-native/base/android-rn-1/
     */
    @Throws(ZipException::class, IOException::class, FileNotFoundException::class)
    fun unzipInDir(zipSrcFile: File, unzipTopParentDir: File?) = unzip(zipSrcFile, unzipTopParentDir, true)

    /**
     * Example: unzipToDir("android-rn-1.zip", ".../react-native/base/base-rn-1/")
     *
     * @param unzipTopDir .../react-native/base/base-rn-1/
     */
    @JvmStatic
    @Throws(ZipException::class, IOException::class, FileNotFoundException::class)
    fun unzipToDir(zipSrcFile: File?, unzipTopDir: File?) = unzip(zipSrcFile, unzipTopDir, false)

    @JvmStatic
    fun unzipToDirOrFalse(zipSrcFile: File?, unzipTopDir: File?): Boolean = unzipOrFalse(zipSrcFile, unzipTopDir, false)

    @JvmStatic
    fun unzipInDirOrFalse(zipSrcFile: File?, unzipTopParentDir: File?): Boolean = unzipOrFalse(zipSrcFile, unzipTopParentDir, true)

    /**
     * @param unzipInNotTo true:    unZipDir 为待解压文件夹的父文件夹, 解压后的文件夹名称为压缩时的文件夹名称, 不可更改
     *                     false:   unZipDir 为带解压文件夹, 解压的的内容直接放到该文件夹内, 过滤掉了压缩时的文件夹本身
     */
    @Throws(ZipException::class, IOException::class, FileNotFoundException::class)
    private fun unzip(zipSrcFile: File?, unZipDir: File?, unzipInNotTo: Boolean) {
        var zipFile: ZipFile? = null

        try {
            zipFile = ZipFile(zipSrcFile)
            val zipEntries = zipFile.entries()

            var originTopDirName: String? = null

            while (zipEntries.hasMoreElements()) {
                val entry: ZipEntry = zipEntries.nextElement()

                if (!unzipInNotTo && entry.isDirectory && originTopDirName == null) {
                    STLogUtil.e("unzip", "originName=${entry.name}, is top dir")
                    originTopDirName = entry.name   // 'android-rn-1/'
                } else {
                    val entryName = if (originTopDirName == null) entry.name else entry.name.replace(originTopDirName, "")

                    STLogUtil.v("unzip", "name=$entryName, isDirectory=${entry.isDirectory}")
                    val destFile = File(unZipDir, entryName)
                    destFile.parentFile?.mkdirs()
                    if (!entry.isDirectory) {
                        val bufferedInputStream = BufferedInputStream(zipFile.getInputStream(entry))
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
            }
        } finally {
            zipFile?.close()
        }
    }

    @JvmStatic
    @JvmOverloads
    fun unzipOrFalse(zipSrcFile: File?, unZipDir: File?, unzipInNotTo: Boolean = false): Boolean {
        if (zipSrcFile?.exists() != true || unZipDir == null)
            return false

        var success = false
        try {
            if (unzipInNotTo) {
                STFileUtil.deleteDirectory(File(unZipDir, STFileUtil.getFileName(zipSrcFile.absolutePath, true)))
                unzipInDir(zipSrcFile, unZipDir)
            } else {
                STFileUtil.deleteDirectory(unZipDir)
                unzipToDir(zipSrcFile, unZipDir)
            }
            success = true
        } catch (exception: FileNotFoundException) {
            STLogUtil.e(STHybird.TAG, "解压失败:文件不存在", exception)
        } catch (exception: IOException) {
            STLogUtil.e(STHybird.TAG, "解压失败:文件读写错误", exception)
        } catch (exception: ZipException) {
            STLogUtil.e(STHybird.TAG, "解压失败:压缩包错误", exception)
        }

        return success
    }
}
