package com.ovolk.dictionary.domain.response

interface Failure
data class FailureWithCode(val message: String, val code: Int) : Failure
data class FailureMessage(val message: String) : Failure

