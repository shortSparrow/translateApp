package com.example.ttanslateapp.data.database

import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.data.model.WordFullDb
import com.example.ttanslateapp.domain.model.lists.ListItem
import com.example.ttanslateapp.domain.model.modify_word.ModifyWordListItem
import com.example.ttanslateapp.domain.model.modify_word.WordRV
import com.example.ttanslateapp.domain.use_case.lists.ListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class ListsRepositoryImpl @Inject constructor(
    private val listsDao: ListsDao,
    private val mapper: WordMapper,
) : ListsRepository {
    override suspend fun searchWordListByListId(
        query: String,
        listId: Long
    ): Flow<List<WordRV>> {
        return listsDao.searchWordListByListId(query = "%${query}%", listId = listId)
            .map { mapper.wordListDbToWordList(it) }
    }

    override suspend fun getAllLists(): Flow<List<ListItem>> {
        return listsDao.getAllLists().map { list ->
            mapper.fullListToLocal(list)
        }
    }

    override suspend fun getListById(id: Long): ModifyWordListItem? {
        val res = listsDao.getListById(id = id)
        return if (res == null) {
            null
        } else {
            mapper.fullListItemToModifyWordListItem(res)
        }
    }

    override suspend fun getAllListsForModifyWord(): Flow<List<ModifyWordListItem>> {
        return listsDao.getAllLists().map { list ->
            mapper.fullListToModifyWordListItem(list)
        }
    }

    override suspend fun addNewList(newList: ListItem): Boolean {
        val id = listsDao.addNewList(mapper.listItemLocalToDb(newList))
        return id != UNDEFINED_ID
    }

    override suspend fun renameList(title: String, id: Long): Boolean {
        val id = listsDao.renameList(title = title, id = id).toLong()
        return id != UNDEFINED_ID
    }

    override suspend fun deleteList(idList: List<Long>) {
        listsDao.deleteList(idList)
    }

    companion object {
        const val UNDEFINED_ID = 0L
    }
}