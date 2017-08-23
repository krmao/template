package com.xixi.library.android.util

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * <pre>
 * author : maokangren
 * e-mail : maokangren@chexiang.com
 * desc   :
 * </pre> *
 */
object CXZipUtil {
    private val BUFFER = 2048

    /**
     * Zips a file at a location and places the resulting zip file at the toLocation
     * Example: zip("downloads/images", "downloads/images.zip");
     */
    @Throws(NullPointerException::class, FileNotFoundException::class, IOException::class)
    fun zip(sourcePath: String, targetFilePath: String) {
        zip(File(sourcePath), targetFilePath)
    }

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
    fun unzip(zipFilePath: String, targetDirPath: String) {
        unzip(File(zipFilePath), File(targetDirPath))
    }

    /**
     * Example: unzip("downloads/images.zip", "downloads/");
     */
    @Throws(ZipException::class, IOException::class)
    fun unzip(zipFile: File, targetDirFile: File) {
        val targetFinalFile = File(targetDirFile.absolutePath + File.separator + zipFile.nameWithoutExtension)
        targetFinalFile.mkdirs()
        val zip = ZipFile(zipFile)
        val zipFileEntries = zip.entries()
        while (zipFileEntries.hasMoreElements()) {
            val entry = zipFileEntries.nextElement()
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
    }
}
