package com.ovolk.dictionary.domain.use_case.lists

import android.app.Application
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.repositories.ListsRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.FailureWithCode
import com.ovolk.dictionary.domain.response.Success
import com.ovolk.dictionary.domain.use_case.modify_dictionary.UNKNOWN_ERROR
import com.ovolk.dictionary.presentation.DictionaryApp
import javax.inject.Inject

class AddNewListUseCase @Inject constructor(
    private val repository: ListsRepository,
    private val application: Application
) {
    suspend fun addNewList(text: String, dictionaryId: Long?): Either<Long, Failure> {
        val title = text.trim()
        if (title.isNotEmpty() && dictionaryId != null) {
            val newListId = repository.addNewList(
                ListItem(
                    id = 0,
                    title = title,
                    count = 0,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    dictionaryId = dictionaryId
                )
            )
            return Either.Success(newListId)
        }

        if (title.isNotEmpty() && dictionaryId == null) {
            return Either.Failure(
                FailureWithCode(
                    message = DictionaryApp.applicationContext()
                        .getString(R.string.something_wrong),
                    code = UNKNOWN_ERROR
                )
            )
        }

        return Either.Failure(FailureMessage(application.getString(R.string.lists_screen_modal_error)))

    }
}