package com.ovolk.dictionary.domain.use_case.modify_dictionary.util

import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.Success


data class Validation(
    val title: MutableList<FailureMessage> = emptyList<FailureMessage>().toMutableList(),
    val langFromCode: MutableList<FailureMessage> = emptyList<FailureMessage>().toMutableList(),
    val langToCode: MutableList<FailureMessage> = emptyList<FailureMessage>().toMutableList(),
) {
    fun isErrorExist(): Boolean =
        title.isNotEmpty() || langFromCode.isNotEmpty() || langToCode.isNotEmpty()

    private fun combineAllErrorsMessage(): String {
        val title = this.title.joinToString { "${it.message}\n" }
        val langFromCode = this.langFromCode.joinToString { "${it.message}\n" }
        val langToCode = this.langToCode.joinToString { "${it.message}\n" }
        return "${title}${langFromCode}${langToCode}"
    }

    fun returnValidationFailure(): Either<Success, Failure> {
        return Either.Failure(
            ValidationFailure(
                langFromError = langFromCode.isNotEmpty(),
                langToError = langToCode.isNotEmpty(),
                dictionaryNameError = title.isNotEmpty(),
                langErrorMessage = combineAllErrorsMessage()
            )
        )
    }
}

data class ValidationFailure(
    val langFromError: Boolean,
    val langToError: Boolean,
    val langErrorMessage: String?,
    val dictionaryNameError: Boolean
) : Failure

fun validateNewDictionary(
    title: String,
    langFromCode: String?,
    langToCode: String?
): Validation {

    val response = Validation();

    if (title.trim().isEmpty()) {
        response.title.add(
            FailureMessage(
                "title must be not empty",
            )
        )
    }

    if (langFromCode == null) {
        response.langFromCode.add(
            FailureMessage(
                "lang from code must be not null",
            )
        )
    }

    if (langToCode == null) {
        response.langToCode.add(
            FailureMessage(
                "lang to code must be not null",
            )
        )
    }

    return response
}