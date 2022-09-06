package com.example.ttanslateapp.domain.use_case.lists

import com.example.ttanslateapp.domain.model.lists.ListItem
import com.example.ttanslateapp.domain.model.modify_word.ModifyWordListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListsUseCase @Inject constructor(
    private val repository: ListsRepository,
) {
    suspend fun getAllLists(): Flow<List<ListItem>> = repository.getAllLists()
    suspend fun getAllListsForModifyWord(): Flow<List<ModifyWordListItem>> =
        repository.getAllListsForModifyWord()

    suspend fun getListById(id: Long): ModifyWordListItem? = repository.getListById(id)
    suspend fun searchWordListByListId(listId: Long, query: String) =
        repository.searchWordListByListId(listId = listId, query = query)
}