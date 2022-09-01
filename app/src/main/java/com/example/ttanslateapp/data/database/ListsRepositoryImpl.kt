package com.example.ttanslateapp.data.database

import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.domain.model.lists.ListItem
import com.example.ttanslateapp.domain.use_case.lists.ListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListsRepositoryImpl @Inject constructor(
    private val listsDao: ListsDao,
    private val mapper: WordMapper,
) : ListsRepository {
    override fun getAllLists(): Flow<List<ListItem>> {
        return listsDao.getAllLists().map { list -> mapper.listDbToLocal(list) }
    }

    override suspend fun addNewList(newList: ListItem): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun renameList(title: String, id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteList(id: Long): Boolean {
        TODO("Not yet implemented")
    }
}