package com.ovolk.dictionary.domain.use_case.modify_dictionary

import android.app.Application
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.ExamReminder
import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.Success
import javax.inject.Inject

class SetIsActiveDictionaryUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val application: Application,
    private val wordRepository: TranslatedWordRepository,
    private val examReminder: ExamReminder,
) {
    suspend fun setDictionaryActive(
        dictionaryId: Long,
        isActive: Boolean
    ): Either<Success, Failure> {
        val isSuccess =
            dictionaryRepository.setDictionaryActive(dictionaryId = dictionaryId, isActive)

        return if (isSuccess) {
            val listSize = wordRepository.getExamWordListSize(dictionaryId)
            if (listSize > 0) {
                examReminder.setInitialReminder()
            }
            Either.Success(Success)
        } else Either.Failure(FailureMessage(application.getString(R.string.set_is_active_dictionary_use_case_cant_mark_dictionary_active)))
    }
}