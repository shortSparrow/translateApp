package com.ovolk.dictionary.domain.use_case.modify_dictionary

import android.app.Application
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.Success
import javax.inject.Inject

class SetIsActiveDictionaryUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val application: Application
) {
    suspend fun setDictionaryActive(
        dictionaryId: Long,
        isActive: Boolean
    ): Either<Success, Failure> {
        val isSuccess =
            dictionaryRepository.setDictionaryActive(dictionaryId = dictionaryId, isActive)

        return if (isSuccess) Either.Success(Success)
        else Either.Failure(FailureMessage(application.getString(R.string.set_is_active_dictionary_use_case_cant_mark_dictionary_active)))
    }
}