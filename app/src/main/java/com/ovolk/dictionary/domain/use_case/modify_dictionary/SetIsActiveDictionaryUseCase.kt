package com.ovolk.dictionary.domain.use_case.modify_dictionary

import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.Success
import javax.inject.Inject

class SetIsActiveDictionaryUseCase @Inject constructor(private val dictionaryRepository: DictionaryRepository) {
    suspend fun setDictionaryActive(
        dictionaryId: Long,
        isActive: Boolean
    ): Either<Success, Failure> {
        val isSuccess =
            dictionaryRepository.setDictionaryActive(dictionaryId = dictionaryId, isActive)

        return if (isSuccess) Either.Success(Success)
        else Either.Failure(FailureMessage("Something went wrong, dictionary wasn't set as active, try again"))
    }
}