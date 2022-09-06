package com.example.ttanslateapp.domain.use_case.lists

import androidx.room.Query
import com.example.ttanslateapp.data.model.FullListItem
import com.example.ttanslateapp.data.model.WordFullDb
import com.example.ttanslateapp.domain.model.lists.ListItem
import com.example.ttanslateapp.domain.model.modify_word.ModifyWordListItem
import com.example.ttanslateapp.domain.model.modify_word.WordRV
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