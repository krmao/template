package com.smart.library.util

import androidx.annotation.Keep
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest

@Keep
@Suppress("MemberVisibilityCanPrivate", "unused", "MemberVisibilityCanBePrivate")
object STChecksumUtil {
    fun genMD5ForByteArray(buffer: ByteArray): String {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        return try {
            val mdTemp = MessageDigest.getInstance("MD5")
            mdTemp.update(buffer)
            val md = mdTemp.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (byte0 in md) {
                str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            String(str)
        } catch (e: Exception) {
            ""
        }
    }

    fun genMD5ForCharSequence(charSequence: CharSequence): String {
        return genMD5ForString(charSequence.toString())
    }

    fun genMD5ForString(string: String): String {
        return genMD5ForByteArray(string.toByteArray(Charsets.UTF_8))
    }

    fun genMD5ForFile(file: File?): String? {
        if (file == null || !file.isFile) {
            return null
        }
        val digest: MessageDigest?
        val fileInputStream: FileInputStream?
        val buffer = ByteArray(1024)
        var len: Int
        try {
            digest = MessageDigest.getInstance("MD5")
            fileInputStream = FileInputStream(file)
            while (fileInputStream.read(buffer, 0, 1024).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            fileInputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }
        val bigInt = BigInteger(1, digest.digest())
        return bigInt.toString(16)
    }

    fun genMD5ForDir(file: File?, listChild: Boolean): Map<String, String>? {
        if (file == null || !file.isDirectory) {
            return null
        }
        val map: MutableMap<String, String> = HashMap()
        var md5: String?
        val files = file.listFiles()
        if (files != null) {
            for (index in files.indices) {
                val itemFile = files[index]
                if (itemFile.isDirectory && listChild) {
                    genMD5ForDir(itemFile, listChild)?.let {
                        map.putAll(it)
                    }
                } else {
                    md5 = genMD5ForFile(itemFile)
                    if (md5 != null) {
                        map[itemFile.path] = md5
                    }
                }
            }
        }
        return map
    }
}
