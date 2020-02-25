package com.smart.library.util

object STCppTestUtil {

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("native-lib")
    }

}