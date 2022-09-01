package com.example.ttanslateapp.util

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment

import com.example.ttanslateapp.presentation.TranslateApp
import com.google.android.material.textfield.TextInputEditText

/**
 * Do exactly the same as a lazy, but without synchronization. Designed mostly
 * to use in activity, fragments, view models etc, where more then 1 thread doesn't
 * expected.
 */
fun <T> lazySimple(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

internal inline fun TextInputEditText.setOnTextChange(crossinline block: (p0: CharSequence?) -> Unit) =
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (rootView.hasFocus()) {
                block(p0)
            }
        }
    })

fun View.t(resourceId: Int) = this.context.getString(resourceId)

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()