package com.ovolk.dictionary.domain.repositories

import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.model.modify_word.ModifyWordListItem
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    suspend fun searchWordListByListId(query: String, listId:Long): Flow<List<WordRV>>
    suspend fun getAllLists(dictionaryId:Long): Flow<List<ListItem>>
    suspend fun getListById(id: Long): ModifyWordListItem?
    suspend fun getAllListsForModifyWord(dictionaryId:Long): Flow<List<ModifyWordListItem>>
    suspend fun getAllListsForDictionary(dictionaryId:Long): List<ListItem>
    suspend fun addNewList(newList: ListItem): Long
    suspend fun renameList(title: String, id: Long): Boolean
    suspend fun deleteList(idList: List<Long>)
}