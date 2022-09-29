package com.ovolk.dictionary.domain.use_case.lists

import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.model.modify_word.ModifyWordListItem
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    suspend fun searchWordListByListId(query: String, listId:Long): Flow<List<WordRV>>
    suspend fun getAllLists(): Flow<List<ListItem>>
    suspend fun getListById(id: Long): ModifyWordListItem?
    suspend fun getAllListsForModifyWord(): Flow<List<ModifyWordListItem>>
    suspend fun addNewList(newList: ListItem): Boolean
    suspend fun renameList(title: String, id: Long): Boolean
    suspend fun deleteList(idList: List<Long>)
}