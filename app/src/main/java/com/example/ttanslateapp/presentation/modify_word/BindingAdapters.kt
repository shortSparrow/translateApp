package com.example.ttanslateapp.presentation.modify_word

import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.ttanslateapp.R
import com.example.ttanslateapp.domain.model.modify_word_chip.HintItem
import com.example.ttanslateapp.domain.model.modify_word_chip.TranslateWordItem
import com.example.ttanslateapp.util.t
import com.google.android.material.textfield.TextInputLayout


// Translates
@BindingAdapter("addTranslateError")
fun bindAddTranslateError(textInputLayout: TextInputLayout, error: Boolean) {
    textInputLayout.error = if (error) {
        textInputLayout.t(R.string.validation_required_field)
    } else {
        null
    }
}

@BindingAdapter("addTranslateButton")
fun bindAddTranslateButton(button: Button, hintItem: TranslateWordItem?) {
    button.text = if (hintItem != null) {
        button.t(R.string.edit)
    } else {
        button.t(R.string.add)
    }
}

@BindingAdapter("cancelEditTranslate")
fun bindCancelEditTranslate(textView: TextView, translateWordItem: TranslateWordItem?) {
    textView.visibility = if (translateWordItem != null) {
        android.view.View.VISIBLE
    } else {
        android.view.View.INVISIBLE
    }
}


// Hints START
@BindingAdapter("englishWordError")
fun bindEnglishWordError(textInputLayout: TextInputLayout, error: Boolean) {
    textInputLayout.error = if (error) {
        textInputLayout.t(R.string.validation_required_field)
    } else {
        null
    }
}

@BindingAdapter("addHintButton")
fun bindAddTranslateButton(button: Button, hintItem: HintItem?) {
    button.text = if (hintItem != null) {
        button.t(R.string.edit)
    } else {
        button.t(R.string.add)
    }
}

@BindingAdapter("cancelEditHint")
fun bindCancelEditTranslate(textView: TextView, hintItem: HintItem?) {
    textView.visibility = if (hintItem != null) {
        android.view.View.VISIBLE
    } else {
        android.view.View.INVISIBLE
    }
}


// Additional fields
@BindingAdapter("additionalFieldVisibility")
fun bindAdditionalField(root: ConstraintLayout, isAdditionalFieldVisible: Boolean) {
    root.visibility = if (isAdditionalFieldVisible) {
        android.view.View.VISIBLE
    } else {
        android.view.View.GONE
    }
}
