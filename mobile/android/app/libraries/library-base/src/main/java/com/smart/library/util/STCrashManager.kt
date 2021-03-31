// Copyright (c) 2019-present, iQIYI, Inc. All rights reserved.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// Created by caikelun on 2019-03-07.
package com.smart.library.util

import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import com.smart.library.BuildConfig
import com.smart.library.STInitializer
import com.smart.library.widget.debug.STDebugCrashPanelFragment
import org.json.JSONObject
import xcrash.ICrashCallback
import xcrash.TombstoneManager
import xcrash.TombstoneParser
import xcrash.XCrash
import xcrash.XCrash.InitParameters
import java.io.File
import java.io.FileWriter

@Keep
object STCrashManager {
    private const val TAG = "[xcrash]"

    @JvmStatic
    fun attachBaseContext(base: Context?) {

        // callback for java crash, native crash and ANR
        val callback = ICrashCallback { logPath, emergency ->
            Log.d(TAG, "log path: " + (logPath ?: "(null)") + ", emergency: " + (emergency ?: "(null)"))
            if (emergency != null) {
                debug(base, logPath, emergency)

                // Disk is exhausted, send crash report immediately.
                sendThenDeleteCrashLog(logPath, emergency)
            } else {
                // Add some expanded sections. Send crash report at the next time APP startup.

                // OK
                TombstoneManager.appendSection(logPath, "from", "---- codesdancing.com ----")
                // TombstoneManager.appendSection(logPath, "expanded_key_2", "expanded_content_row_1\nexpanded_content_row_2")

                // Invalid. (Do NOT include multiple consecutive newline characters ("\n\n") in the content string.)
                // TombstoneManager.appendSection(logPath, "expanded_key_3", "expanded_content_row_1\n\nexpanded_content_row_2");
                debug(base, logPath, null)
            }
        }
        Log.d(TAG, "xCrash SDK init: start")

        // Initialize xCrash.
        XCrash.init(
            base, InitParameters()
                .setAppVersion("${STSystemUtil.getAppVersionName(STInitializer.application())}_${STSystemUtil.getAppVersionCode(STInitializer.application())}")
                .setJavaRethrow(true)
                .setJavaLogCountMax(10)
                .setJavaDumpAllThreadsWhiteList(arrayOf("^main$", "^Binder:.*", ".*Finalizer.*"))
                .setJavaDumpAllThreadsCountMax(10)
                .setJavaCallback(callback)
                .setNativeRethrow(true)
                .setNativeLogCountMax(10)
                .setNativeDumpAllThreadsWhiteList(arrayOf("^xcrash\\.sample$", "^Signal Catcher$", "^Jit thread pool$", ".*(R|r)ender.*", ".*Chrome.*"))
                .setNativeDumpAllThreadsCountMax(10)
                .setNativeCallback(callback)
                .setAnrRethrow(true)
                .setAnrLogCountMax(10)
                .setAnrCallback(callback)
                .setPlaceholderCountMax(3)
                .setPlaceholderSizeKb(512)
                .setLogDir(base?.getExternalFilesDir("xcrash").toString())
                .setLogFileMaintainDelayMs(1000)
        )
        Log.d(TAG, "xCrash SDK init: end")

        // Send all pending crash log files.
        Thread {
            for (file in TombstoneManager.getAllTombstones()) {
                sendThenDeleteCrashLog(file.absolutePath, null)
            }
        }.start()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun sendThenDeleteCrashLog(logPath: String, emergency: String?) {
        // Parse
        //Map<String, String> map = TombstoneParser.parse(logPath, emergency);
        //String crashReport = new JSONObject(map).toString();

        // Send the crash report to server-side.
        // ......

        // If the server-side receives successfully, delete the log file.
        //
        // Note: When you use the placeholder file feature,
        //       please always use this method to delete tombstone files.
        //
        //TombstoneManager.deleteTombstone(logPath);
    }

    private fun debug(base: Context?, logPath: String, emergency: String?) {
        // Parse and save the crash info to a JSON file for debugging.
        var writer: FileWriter? = null
        try {
            val debug = File(XCrash.getLogDir() + "/debug.json")
            debug.createNewFile()
            writer = FileWriter(debug, false)
            val desc = JSONObject(TombstoneParser.parse(logPath, emergency) as Map<String, String>).toString()
            Log.e(TAG, desc)

            // 仅在调试模式下启动
            if (BuildConfig.DEBUG) {
                STDebugCrashPanelFragment.startActivity(base, desc)
            }

            writer.write(desc)
        } catch (e: Exception) {
            Log.d(TAG, "debug failed", e)
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (ignored: Exception) {
                }
            }
        }
    }
}