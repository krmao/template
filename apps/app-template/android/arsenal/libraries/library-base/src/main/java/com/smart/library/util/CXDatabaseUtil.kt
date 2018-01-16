package com.smart.library.util

import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.smart.library.base.CXBaseApplication
import java.io.File
import java.io.FileOutputStream

@Suppress("unused")
object CXDatabaseUtil {

    fun getDatabase(dbName: String): File = CXBaseApplication.INSTANCE.getDatabasePath(dbName)

    fun copyDBFile(dbName: String, dbResId: Int, destDBDir: File?) {
        if (destDBDir == null || dbResId <= 0 || TextUtils.isEmpty(dbName))
            return

        if (!destDBDir.exists())
            destDBDir.mkdirs()
        val destDBFile = File(destDBDir, dbName)
        try {
            if (destDBFile.exists())
                destDBFile.delete()
            SQLiteDatabase.openOrCreateDatabase(destDBFile, null)

            val inputStream = CXBaseApplication.INSTANCE.resources.openRawResource(dbResId)
            val outputStream = FileOutputStream(destDBFile)

            val buffer = ByteArray(1024)
            var length: Int = inputStream.read(buffer)
            while (length > 0) {
                outputStream.write(buffer, 0, length)
                length = inputStream.read(buffer)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
