package com.example.ttanslateapp.domain

abstract class ValidateTranslatedWord {

    fun validateInput(value: String): SimpleValidateResults {
        if (value.trim().isEmpty()) {
            return SimpleValidateResults.Success
        }

        return SimpleValidateResults.Error
    }
}

sealed class SimpleValidateResults {
    object Success : SimpleValidateResults()

    object Error : SimpleValidateResults()
}