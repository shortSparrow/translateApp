package com.example.ttanslateapp.domain.use_case.lists

import com.example.ttanslateapp.domain.model.lists.ListItem
import com.example.ttanslateapp.domain.model.modify_word.ModifyWordListItem
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    suspend fun getAllLists(): Flow<List<ListItem>>
    suspend fun getListById(id: Long): ModifyWordListItem?
    suspend fun getAllListsForModifyWord(): Flow<List<ModifyWordListItem>>
    suspend fun addNewList(newList: ListItem): Boolean
    suspend fun renameList(title: String, id: Long): Boolean
    suspend fun deleteList(idList: List<Long>)
}