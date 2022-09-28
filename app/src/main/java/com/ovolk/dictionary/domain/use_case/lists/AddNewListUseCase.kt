package com.ovolk.dictionary.domain.use_case.lists

import com.ovolk.dictionary.domain.model.lists.ListItem
import javax.inject.Inject

class AddNewListUseCase @Inject constructor(
    private val repository: ListsRepository,
) {
    suspend fun addNewList(title: String) {
        if (title.isEmpty()) {
            return
        }
        repository.addNewList(
            ListItem(
                id=0,
                title = title,
                count = 0,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}