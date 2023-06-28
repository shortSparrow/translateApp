package com.ovolk.dictionary.domain.response


sealed class Either<out A, out B> {
    class Success<A>(val value: A) : Either<A, Nothing>()
    class Failure<B>(val value: B) : Either<Nothing, B>()
}
