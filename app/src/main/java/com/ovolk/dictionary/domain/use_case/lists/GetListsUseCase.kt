package com.ovolk.dictionary.domain.use_case.lists

import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.model.lists.ModifyWordListItem
import com.ovolk.dictionary.domain.repositories.ListsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListsUseCase @Inject constructor(
    private val repository: ListsRepository,
) {
    suspend fun getAllLists(dictionaryId: Long): Flow<List<ListItem>> =
        repository.getAllLists(dictionaryId)

    suspend fun getAllListsForModifyWord(dictionaryId: Long): Flow<List<ModifyWordListItem>> =
        repository.getAllListsForModifyWord(dictionaryId)

    suspend fun getListById(id: Long): ModifyWordListItem? = repository.getListById(id)

    suspend fun searchWordListByListId(listId: Long, query: String) =
        repository.searchWordListByListId(listId = listId, query = query)
}