package com.example.ttanslateapp.domain.use_case.lists

import javax.inject.Inject

class DeleteListsUseCase @Inject constructor(
    private val repository: ListsRepository,
) {
    suspend fun deleteLists(IdList: List<Long>) {
        return repository.deleteList(IdList)
    }
}