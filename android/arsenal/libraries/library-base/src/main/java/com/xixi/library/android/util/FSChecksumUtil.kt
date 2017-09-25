package com.xixi.library.android.util

import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import java.io.File
import java.io.IOException
import java.security.MessageDigest

object FSChecksumUtil {
    @Deprecated("use @see FSChecksumUtil#genMD5Checksum(charSequence: CharSequence)")
    fun genMD5Checksum(buffer: ByteArray): String {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        try {
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
            return String(str)
        } catch (e: Exception) {
            return ""
        }
    }

    fun genMD5Checksum(charSequence: CharSequence): String {
        @Suppress("DEPRECATION")
        return getChecksum(charSequence, Hashing.md5())
    }

    @Throws(IOException::class)
    fun genMD5Checksum(file: File): String {
        @Suppress("DEPRECATION")
        return getChecksum(file, Hashing.md5())
    }

    /**
     * md5()		murmur3_128()	murmur3_32()	sha1()
     * sha256()		sha512()		goodFastHash(int bits)
     */
    @Throws(IOException::class)
    fun getChecksum(charSequence: CharSequence, hashFunction: HashFunction): String {
        return hashFunction.newHasher().putString(charSequence, Charsets.UTF_8).hash().toString()
    }

    @Throws(IOException::class)
    fun getChecksum(file: File, hashFunction: HashFunction): String {
        return com.google.common.io.Files.asByteSource(file).hash(hashFunction).toString()
    }
}