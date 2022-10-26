package com.ovolk.dictionary.presentation.modify_word.helpers

import android.text.TextUtils
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate


fun validateWordValue(value: String): ValidateResult {
    return if (value.isBlank()) {
        ValidateResult(successful = false, errorMessage = "this field is required")
    } else {
        ValidateResult(successful = true)
    }
}

fun validationPriority(value: String): ValidateResult {
    return if (value.isBlank()) {
        ValidateResult(successful = false, errorMessage = "this field is required")
    } else if (!TextUtils.isDigitsOnly(value)) {
        ValidateResult(successful = false, errorMessage = "must contain only digits")
    } else {
        ValidateResult(successful = true)
    }
}

fun validateTranslates(value: List<Translate>): ValidateResult {
    return if (value.isEmpty()) {
        ValidateResult(successful = false, errorMessage = "this field is required")
    } else {
        ValidateResult(successful = true)
    }
}

fun validateSelectLanguage(langCode: String?): ValidateResult {
    return if (langCode == null) {
        ValidateResult(successful = false, errorMessage = "this field is required")
    } else {
        ValidateResult(successful = true)
    }
}