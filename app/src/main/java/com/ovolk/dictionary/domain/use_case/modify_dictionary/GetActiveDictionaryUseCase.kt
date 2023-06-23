package com.ovolk.dictionary.domain.use_case.modify_dictionary

import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import javax.inject.Inject

class GetActiveDictionaryUseCase @Inject constructor(private val dictionaryRepository: DictionaryRepository) {
    suspend fun getDictionaryActive(): Either<Dictionary, Failure> {
        val dictionary =
            dictionaryRepository.getCurrentActiveDictionary()

        return if (dictionary != null) Either.Success(dictionary)
        else Either.Failure(FailureMessage("there is no active dictionary"))
    }
}