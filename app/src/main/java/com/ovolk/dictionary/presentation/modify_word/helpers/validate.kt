package com.ovolk.dictionary.presentation.modify_word.helpers

import android.text.TextUtils
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.DictionaryApp

fun validateOnAddChip(value: String): ValidateResult {
    return if (value.isBlank()) {
        ValidateResult(
            successful = false,
            errorMessage = DictionaryApp.applicationContext()
                .getString(R.string.field_cant_be_empty)
        )
    } else {
        ValidateResult(successful = true)
    }
}

fun validateWordValue(value: String): ValidateResult {
    return if (value.isBlank()) {
        ValidateResult(
            successful = false,
            errorMessage = DictionaryApp.applicationContext()
                .getString(R.string.field_is_required)
        )
    } else {
        ValidateResult(successful = true)
    }
}

fun validationPriority(value: String): ValidateResult {
    return if (value.isBlank()) {
        ValidateResult(
            successful = false,
            errorMessage = DictionaryApp.applicationContext()
                .getString(R.string.field_is_required)
        )
    } else if (!TextUtils.isDigitsOnly(value)) {
        ValidateResult(
            successful = false,
            errorMessage = DictionaryApp.applicationContext()
                .getString(R.string.field_is_required)
        )
    } else {
        ValidateResult(successful = true)
    }
}

fun validateTranslates(value: List<Translate>): ValidateResult {
    return if (value.isEmpty()) {
        ValidateResult(
            successful = false,
            errorMessage = DictionaryApp.applicationContext()
                .getString(R.string.modify_word_validate_add_empty_field)
        )
    } else {
        ValidateResult(successful = true)
    }
}

// TODO change on validate Dictionary
fun validateSelectLanguage(langCode: String?): ValidateResult {
    return if (langCode == null) {
        ValidateResult(
            successful = false,
            errorMessage = DictionaryApp.applicationContext()
                .getString(R.string.field_is_required)
        )
    } else {
        ValidateResult(successful = true)
    }
}