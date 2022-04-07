package com.smart.library.util

import android.text.Selection
import android.text.TextUtils
import android.widget.EditText
import androidx.annotation.Keep

@Suppress("unused")
//@Keep
object STEditTextUtil {

    fun setTextWithLastCursor(editText: EditText?, text: String) {
        if (editText == null || TextUtils.isEmpty(text))
            return
        editText.setText(text)
        val charSequence = editText.text
        if (charSequence != null) {
            try {
                Selection.setSelection(charSequence, text.length)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
