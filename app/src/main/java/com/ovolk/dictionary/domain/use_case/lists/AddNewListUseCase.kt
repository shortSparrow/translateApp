package com.ovolk.dictionary.domain.use_case.lists

import android.app.Application
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.lists.ListItem
import javax.inject.Inject

class AddNewListUseCase @Inject constructor(
    private val repository: ListsRepository,
    private val application: Application
) {
    suspend fun addNewList(text: String): SimpleError {
        val title = text.trim()
        if (title.isNotEmpty()) {
            repository.addNewList(
                ListItem(
                    id = 0,
                    title = title,
                    count = 0,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
            return SimpleError(isError = false)
        }

        return SimpleError(
            isError = true,
            text = application.getString(R.string.lists_screen_modal_error)
        )
    }
}