package com.ovolk.dictionary.data.database.word_lists

import com.ovolk.dictionary.data.mapper.ListMapper
import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.model.lists.ModifyWordListItem
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.repositories.ListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListsRepositoryImpl @Inject constructor(
    private val listsDao: ListsDao,
    private val wordMapper: WordMapper,
    private val listMapper: ListMapper
) : ListsRepository {
    override suspend fun searchWordListByListId(
        query: String,
        listId: Long
    ): Flow<List<WordRV>> {
        return listsDao.searchWordListByListId(query = "%${query}%", listId = listId)
            .map { wordMapper.wordListDbToWordList(it) }
    }

    override suspend fun getAllLists(dictionaryId: Long): Flow<List<ListItem>> {
        return listsDao.getAllLists(dictionaryId).map { list ->
            listMapper.fullListToLocal(list)
        }
    }

    override suspend fun getListById(id: Long): ModifyWordListItem? {
        val res = listsDao.getListById(id = id)
        return if (res == null) {
            null
        } else {
            listMapper.fullListItemToModifyWordListItem(res)
        }
    }

    override suspend fun getAllListsForModifyWord(dictionaryId: Long): Flow<List<ModifyWordListItem>> {
        return listsDao.getAllLists(dictionaryId).map { list ->
            listMapper.fullListToModifyWordListItem(list)
        }
    }

    override suspend fun getAllListsForDictionary(dictionaryId: Long): List<ListItem> {
        return listMapper.fullListToLocal(listsDao.getAllListsForDictionary(dictionaryId))
    }

    override suspend fun addNewList(newList: ListItem): Long {
        return listsDao.addNewList(listMapper.listItemLocalToDb(newList))
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