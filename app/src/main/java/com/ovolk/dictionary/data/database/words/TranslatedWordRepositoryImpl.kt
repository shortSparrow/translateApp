package com.ovolk.dictionary.data.database.words

import com.ovolk.dictionary.data.in_memory_storage.InMemoryStorage
import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.data.model.HintDb
import com.ovolk.dictionary.data.model.TranslateDb
import com.ovolk.dictionary.data.model.UpdatePriority
import com.ovolk.dictionary.data.model.UpdatePriorityDb
import com.ovolk.dictionary.data.model.WordInfoDb
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


// InMemoryStorage by inMemoryStorage need for avoid override here InMemoryStorage methods.
// We copy them from inMemoryStorage implementation (Local Cache)
class TranslatedWordRepositoryImpl @Inject constructor(
    private val translatedWordDao: TranslatedWordDao,
    private val mapper: WordMapper,
    private val inMemoryStorage: InMemoryStorage,
) : TranslatedWordRepository, InMemoryStorage by inMemoryStorage {
    override suspend fun getWordsForSilentUpdatePriority(
        beforeUpdatedAt: Long,
        count: Int
    ): List<UpdatePriority> {
        return translatedWordDao.getWordsForSilentUpdatePriority(
            beforeUpdatedAt = beforeUpdatedAt,
            count = count
        )
            .map { word -> mapper.wordFullDbToUpdatePriority(word) }
    }

    override suspend fun getExamWordList(count: Int, skip: Int): List<ExamWord> {
        return translatedWordDao.getExamWordList(count = count, skip = skip)
            .map { word -> mapper.wordFullDbToExamWord(word) }
    }

    override suspend fun getExamWordListFromOneList(
        count: Int,
        skip: Int,
        listId: Long
    ): List<ExamWord> {

        return translatedWordDao.getExamWordListFromOneList(
            count = count,
            skip = skip,
            listId = listId
        )
            .map { word -> mapper.wordFullDbToExamWord(word) }
    }

    override suspend fun getExamWordListSize(): Int {
        return translatedWordDao.getExamWordListSize()
    }

    override suspend fun getExamWordListSizeForOneList(listId: Long): Int {
        return translatedWordDao.getExamWordListSizeForOneList(listId = listId)
    }

    override suspend fun searchWordList(query: String): Flow<List<WordRV>> {
        return translatedWordDao.searchWordList(query = "%$query%")
            .map { list ->
                mapper.wordListDbToWordList(list)
            }
    }

    override suspend fun searchWordListByDictionary(
        query: String,
        dictionaryId: Long
    ): Flow<List<WordRV>> {
        return translatedWordDao.searchWordListByDictionary(query = "%$query%", dictionaryId=dictionaryId)
            .map { list ->
                mapper.wordListDbToWordList(list)
            }
    }

    override suspend fun searchExactWord(query: String): WordRV? {
        val res = translatedWordDao.searchExactWord(query = query)
        return if (res != null) {
            mapper.wordFullDbToWordRv(res)
        } else {
            res
        }
    }

    override suspend fun searchWordListSize(): Flow<Int> {
        return translatedWordDao.searchWordListSize()
    }

    override suspend fun getWordById(id: Long): ModifyWord {
        return mapper.wordFullDbToModifyWord(translatedWordDao.getWordById(id))
    }

    override suspend fun getWordByValue(value: String, dictionaryId: Long): Long {
        return try {
            translatedWordDao.getWordByValue(
                value = value,
                dictionaryId = dictionaryId,
            )
        } catch (e: Exception) {
            return WORD_IS_NOT_FOUND.toLong()
        }
    }

    override suspend fun deleteWord(id: Long): Boolean {
        return translatedWordDao.deleteWord(id) != WORD_IS_NOT_FOUND
    }

    override suspend fun getWordsForDelayedUpdatePriority(): List<UpdatePriority> {
        return translatedWordDao.getWordsForDelayedUpdatePriority()
            .map { mapper.updatePriorityDbToUpdatePriority(it) }
    }

    override suspend fun addWordForDelayedUpdatePriority(word: UpdatePriorityDb): Boolean {
        return translatedWordDao.addWordForDelayedUpdatePriority(word) != WORD_IS_NOT_FOUND.toLong()
    }

    override suspend fun updatePriorityById(priority: Int, id: Long): Boolean {
        return translatedWordDao.updatePriorityById(priority, id) != WORD_IS_NOT_FOUND
    }

    override suspend fun updateWordsPriority(words: List<UpdatePriorityDb>) {
        translatedWordDao.updateWordsForDelayedUpdatePriority(words)
    }

    override suspend fun addHiddenTranslateWithUpdatePriority(
        translates: List<TranslateDb>,
        priority: Int,
        wordId: Long
    ): List<Long> {
        return translatedWordDao.addHiddenTranslateWithUpdatePriority(
            translates = translates,
            priority = priority,
            wordId = wordId
        )
    }

    override suspend fun modifyWordTranslates(translates: List<TranslateDb>): List<Long> {
        return translatedWordDao.modifyTranslates(translates)
    }

    override suspend fun modifyWordHints(hints: List<HintDb>): List<Long> {
        return translatedWordDao.modifyHints(hints)
    }

    override suspend fun modifyWordInfo(wordInfoDb: WordInfoDb): Long {
        return translatedWordDao.modifyWordInfo(wordInfoDb)
    }

    override suspend fun modifyWord(word: ModifyWord, mapper: WordMapper): Long {
        return translatedWordDao.modifyWord(word, mapper)
    }

    companion object {
        const val WORD_IS_NOT_FOUND = -1
    }
}