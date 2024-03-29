package com.ovolk.dictionary.domain.use_case.modify_dictionary

import android.app.Application
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.FailureMessage
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetActiveDictionaryUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val application: Application
) {
    suspend fun getDictionaryActive(): Either<Dictionary, FailureMessage> {
        val dictionary = dictionaryRepository.getCurrentActiveDictionary()

        return if (dictionary != null) Either.Success(dictionary)
        else Either.Failure(FailureMessage(application.getString(R.string.get_active_dictionary_use_case_no_active_dictionary)))
    }

    fun getDictionaryActiveFlow() = channelFlow<Dictionary?> {
        var dictionary: Dictionary? = null
        dictionaryRepository.getCurrentActiveDictionaryFlow().collectLatest {
            if (dictionary?.id != it?.id) { // emit data only if dictionary changed
                dictionary = it
                send(dictionary)
            }
        }
    }
}