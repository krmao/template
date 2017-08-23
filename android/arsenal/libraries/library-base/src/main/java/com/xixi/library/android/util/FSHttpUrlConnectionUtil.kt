package com.xixi.library.android.util

import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

@Suppress("unused")
object CXHttpUrlConnectionUtil {
    private var TAG = javaClass.name

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     * @param httpUrl 访问的服务器URL
     * @param params  普通参数
     * @param files   文件参数
     * @return responseMessage
     * @throws IOException
     */
    @Throws(IOException::class)
    fun post(httpUrl: String, params: Map<String, String>, files: Map<String, File>?): String {
        val BOUNDARY = UUID.randomUUID().toString()
        val PREFIX = "--"
        val LINE_END = "\r\n"
        val MULTIPART_FROM_DATA = "multipart/form-data"
        val CHARSET = "UTF-8"

        val uri = URL(httpUrl)
        val conn = uri.openConnection() as HttpURLConnection
        System.setProperty("sun.net.client.defaultConnectTimeout", (20 * 1000).toString())
        System.setProperty("sun.net.client.defaultReadTimeout", (20 * 1000).toString())
        conn.readTimeout = 20 * 1000 // 缓存的最长时间
        conn.connectTimeout = 20 * 1000 // 缓存的最长时间
        conn.doInput = true// 允许输入
        conn.doOutput = true// 允许输出
        conn.useCaches = false // 不允许使用缓存
        conn.requestMethod = "POST"
        conn.setRequestProperty("connection", "keep-alive")
        conn.setRequestProperty("Charsert", "UTF-8")
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY)

        // 首先组拼文本类型的参数
        val textStringBuilder = StringBuilder()
        for ((key, value) in params) {
            textStringBuilder.append(PREFIX)
            textStringBuilder.append(BOUNDARY)
            textStringBuilder.append(LINE_END)
            textStringBuilder.append("Content-Disposition: form-data; name=\"" + key + "\"" + LINE_END)
            textStringBuilder.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END)
            textStringBuilder.append("Content-Transfer-Encoding: 8bit" + LINE_END)
            textStringBuilder.append(LINE_END)
            textStringBuilder.append(value)
            textStringBuilder.append(LINE_END)
        }

        val outStream = DataOutputStream(conn.outputStream)
        outStream.write(textStringBuilder.toString().toByteArray())
        // 发送文件数据
        if (files != null) {
            for ((key, value) in files) {
                val fileStringBuilder = StringBuilder()
                fileStringBuilder.append(PREFIX)
                fileStringBuilder.append(BOUNDARY)
                fileStringBuilder.append(LINE_END)
                // name是post中传参的键 filename是文件的名称
                fileStringBuilder.append("Content-Disposition: form-data; name=\"file\"; filename=\"$key\"$LINE_END")
                fileStringBuilder.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END)
                fileStringBuilder.append(LINE_END)
                outStream.write(fileStringBuilder.toString().toByteArray())

                val inputStream = FileInputStream(value)
                val buffer = ByteArray(1024)

                var len = inputStream.read(buffer)
                while (len != -1) {
                    outStream.write(buffer, 0, len)
                    len = inputStream.read(buffer)
                }
                inputStream.close()
                outStream.write(LINE_END.toByteArray())
            }
        }
        // 请求结束标志
        val end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).toByteArray()
        outStream.write(end_data)
        outStream.flush()
        // 得到响应码
        var responseCode = 0
        try {
            responseCode = conn.responseCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val responseStringBuilder = StringBuilder()
        if (responseCode == 200) {
            val inputStream = conn.inputStream
            var ch: Int = inputStream.read()
            while (ch != -1) {
                responseStringBuilder.append(ch.toChar())
                ch = inputStream.read()
            }
        }
        CXLogUtil.d(TAG, "responseCode:" + responseCode)
        CXLogUtil.d(TAG, "responseMessage:" + conn.responseMessage)
        outStream.close()
        conn.disconnect()
        return responseStringBuilder.toString()
    }

    @Throws(IOException::class)
    operator fun get(httpUrl: String): String {
        val uri = URL(httpUrl)
        val conn = uri.openConnection() as HttpURLConnection
        System.setProperty("sun.net.client.defaultConnectTimeout", (10 * 1000).toString())
        System.setProperty("sun.net.client.defaultReadTimeout", (10 * 1000).toString())
        conn.readTimeout = 10 * 1000 // 缓存的最长时间
        conn.connectTimeout = 10 * 1000 // 缓存的最长时间
        conn.requestMethod = "GET"
        conn.setRequestProperty("Charsert", "UTF-8")
        conn.setRequestProperty("Content-Type", "application/json")
        // 得到响应码
        var responseCode = 0
        try {
            responseCode = conn.responseCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val responseStringBuilder = StringBuilder()
        if (responseCode == 200) {
            val inputStream = conn.inputStream
            var ch: Int = inputStream.read()
            while (ch != -1) {
                responseStringBuilder.append(ch.toChar())
                ch = inputStream.read()
            }
        }
        CXLogUtil.d(TAG, "responseCode:" + responseCode)
        CXLogUtil.d(TAG, "responseMessage:" + conn.responseMessage)
        try {
            val content = responseStringBuilder.substring(0, 200)
            CXLogUtil.d(TAG, "responseContent:" + content.toByteArray(charset("utf-8")))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        conn.disconnect()
        return responseStringBuilder.toString()
    }
}
