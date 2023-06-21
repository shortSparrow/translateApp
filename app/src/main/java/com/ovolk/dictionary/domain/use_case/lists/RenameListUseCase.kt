package com.ovolk.dictionary.domain.use_case.lists

import android.app.Application
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.repositories.ListsRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.FailureWithCode
import com.ovolk.dictionary.domain.use_case.modify_dictionary.UNKNOWN_ERROR
import javax.inject.Inject


class RenameListUseCase @Inject constructor(
    private val repository: ListsRepository,
    private val application: Application,
) {
    suspend fun addNewList(text: String, dictionaryId: Long?): Either<Long, Failure> {
        val title = text.trim()
        if (title.isNotEmpty() && dictionaryId != null) {
            val isSuccess = repository.renameList(
                title = title,
                id = dictionaryId
            )

            return if (isSuccess) {
                Either.Success(dictionaryId)
            } else {
                Either.Failure(
                    FailureWithCode(
                        message = "Something went wrong",
                        code = UNKNOWN_ERROR
                    )
                )
            }
        }
        if (title.isNotEmpty() && dictionaryId == null) {
            return Either.Failure(
                FailureWithCode(
                    message = "Something went wrong",
                    code = UNKNOWN_ERROR
                )
            )
        }

        return Either.Failure(FailureMessage(application.getString(R.string.lists_screen_modal_error)))

    }
}