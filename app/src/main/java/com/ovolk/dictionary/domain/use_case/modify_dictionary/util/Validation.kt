package com.ovolk.dictionary.domain.use_case.modify_dictionary.util

import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.Success


data class ValidationFailure(
    val langFrom: ValidateResult,
    val langTo: ValidateResult,
    val dictionaryName: ValidateResult
) : Failure

fun validateNewDictionary(
    title: String,
    langFromCode: String?,
    langToCode: String?
): Either<Success, ValidationFailure> {

    var titleValidation = ValidateResult()
    var langFromValidation = ValidateResult()
    var langToValidation = ValidateResult()

    if (title.trim().isEmpty()) {
        titleValidation =
            ValidateResult(successful = false, errorMessage = "title must be not empty")
    }

    if (langFromCode == null) {
        langFromValidation =
            ValidateResult(successful = false, errorMessage = "lang from code must be not null")
    }

    if (langToCode == null) {
        langToValidation =
            ValidateResult(successful = false, errorMessage = "lang to code must be not null")
    }

    return if (!titleValidation.successful || !langFromValidation.successful || !langToValidation.successful) {
        Either.Failure(
            ValidationFailure(
                dictionaryName = titleValidation,
                langFrom = langFromValidation,
                langTo = langToValidation
            )
        )
    } else Either.Success(Success)
}