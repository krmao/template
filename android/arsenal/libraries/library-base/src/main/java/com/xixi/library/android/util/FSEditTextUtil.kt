package com.xixi.library.android.util

import android.text.Selection
import android.text.TextUtils
import android.widget.EditText

@Suppress("unused")
object FSEditTextUtil {

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
