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
package com.smart.module.crash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.smart.module.test.R
import xcrash.XCrash

@Suppress("FunctionName", "UNUSED_PARAMETER")
class STCrashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_crash_activity)
    }

    fun testJavaCrashNullPointerInMainThread_onClick(view: View?) {
        val text:String? = null
        text!!.toString()
    }
    fun testNativeCrashInMainThread_onClick(view: View?) {
        XCrash.testNativeCrash(false)
    }

    fun testNativeCrashInAnotherJavaThread_onClick(view: View?) {
        Thread({ XCrash.testNativeCrash(false) }, "java_thread_with_a_very_long_name").start()
    }

    fun testNativeCrashInAnotherNativeThread_onClick(view: View?) {
        XCrash.testNativeCrash(true)
    }

    fun testNativeCrashInAnotherActivity_onClick(view: View?) {
        startActivity(Intent(this, STCrashTestActivity::class.java).putExtra("type", "native"))
    }

    fun testNativeCrashInAnotherProcess_onClick(view: View?) {
        startService(Intent(this, STCrashTestService::class.java).putExtra("type", "native"))
    }

    fun testJavaCrashInMainThread_onClick(view: View?) {
        XCrash.testJavaCrash(false)
    }

    fun testJavaCrashInAnotherThread_onClick(view: View?) {
        XCrash.testJavaCrash(true)
    }

    fun testJavaCrashInAnotherActivity_onClick(view: View?) {
        startActivity(Intent(this, STCrashTestActivity::class.java).putExtra("type", "java"))
    }

    fun testJavaCrashInAnotherProcess_onClick(view: View?) {
        startService(Intent(this, STCrashTestService::class.java).putExtra("type", "java"))
    }

    fun testAnrInput_onClick(view: View?) {
        while (true) {
            try {
                Thread.sleep(1000)
            } catch (ignored: Exception) {
            }
        }
    }

    companion object {
        fun goTo(context: Context?) {
            context?.startActivity(Intent(context, STCrashActivity::class.java))
        }
    }
}