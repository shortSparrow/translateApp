package com.example.ttanslateapp.domain.use_case.lists

import com.example.ttanslateapp.domain.model.lists.ListItem
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    suspend fun getAllLists(): Flow<List<ListItem>>
    suspend fun addNewList(newList: ListItem): Boolean
    suspend fun renameList(title: String, id: Long): Boolean
    suspend fun deleteList(id: Long): Boolean
}