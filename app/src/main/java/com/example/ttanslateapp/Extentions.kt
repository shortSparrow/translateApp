package com.example.ttanslateapp

import android.content.res.Resources.getSystem
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

internal inline fun TextInputEditText.setOnTextChange(crossinline block: () -> Unit) =
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = block()
    })

