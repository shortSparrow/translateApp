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
    override suspend fun getAllLists(): Flow<List<ListItem>> {
        return listsDao.getAllLists().map { list ->
            mapper.listDbToLocal(list)
        }
    }

    override suspend fun addNewList(newList: ListItem): Boolean {
        val id = listsDao.addNewList(mapper.listItemLocalToDb(newList))
        return id != UNDEFINED_ID
    }

    override suspend fun renameList(title: String, id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteList(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    companion object {
        const val UNDEFINED_ID = 0L
    }
}