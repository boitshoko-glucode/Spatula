package com.boitshoko.spatula.extensions

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.setOnSearchListener(handler: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        (actionId == EditorInfo.IME_ACTION_SEARCH).also {
            if (it) handler()
        }
    }
}