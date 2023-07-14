package com.ovolk.dictionary.domain.model.modify_word

data class ValidateResult(
    val successful: Boolean = true,
    val errorMessage: String? = null
)