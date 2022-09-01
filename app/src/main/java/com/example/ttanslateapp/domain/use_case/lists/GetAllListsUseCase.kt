package com.example.ttanslateapp.domain.use_case.lists

import com.example.ttanslateapp.domain.model.lists.ListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllListsUseCase @Inject constructor(
    private val repository: ListsRepository,
) {
    suspend fun getAllLists():Flow<List<ListItem>> = repository.getAllLists()
}