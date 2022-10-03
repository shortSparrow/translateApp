package com.ovolk.dictionary.domain

data class SimpleError(
    val isError: Boolean = false,
    val text: String = ""
)