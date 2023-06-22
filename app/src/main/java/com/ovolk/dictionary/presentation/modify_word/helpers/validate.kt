package com.ovolk.dictionary.presentation.modify_word.helpers

import android.text.TextUtils
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.DictionaryApp
import kotlinx.coroutines.flow.MutableStateFlow

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

fun validateDictionary(dictionary: Dictionary?): ValidateResult {
    return if (dictionary == null) {
        ValidateResult(
            successful = false,
            errorMessage = "dictionary not selected"
        )
    } else if (dictionary.langFromCode.isEmpty() || dictionary.langToCode.isEmpty()) {
        ValidateResult(
            successful = false,
            errorMessage = "dictionary dose not contain languages"
        )
    } else {
        ValidateResult(successful = true)
    }
}
