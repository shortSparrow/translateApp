package com.ovolk.dictionary.domain.use_case.lists

import com.ovolk.dictionary.domain.repositories.ListsRepository
import javax.inject.Inject

class DeleteListsUseCase @Inject constructor(
    private val repository: ListsRepository,
) {
    suspend fun deleteLists(IdList: List<Long>) {
        return repository.deleteList(IdList)
    }
}