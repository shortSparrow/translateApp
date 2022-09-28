package com.ovolk.dictionary.domain.use_case.lists

import javax.inject.Inject


class RenameListUseCase @Inject constructor(
    private val repository: ListsRepository,
) {
    suspend fun addNewList(title: String, id: Long) {
        if (title.isEmpty()) {
            return
        }
        repository.renameList(
            title = title,
            id = id
        )
    }
}