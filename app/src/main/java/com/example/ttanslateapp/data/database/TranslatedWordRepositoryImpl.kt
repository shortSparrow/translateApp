package com.example.ttanslateapp.data.database

import com.example.ttanslateapp.data.in_memory_storage.InMemoryStorage
import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.data.model.HintDb
import com.example.ttanslateapp.data.model.TranslateDb
import com.example.ttanslateapp.data.model.WordInfoDb
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.modify_word.ModifyWord
import com.example.ttanslateapp.domain.model.modify_word.WordRV
import com.example.ttanslateapp.domain.model.exam.ExamWord
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

    override suspend fun getExamWordList(count: Int, skip: Int): List<ExamWord> {
        return translatedWordDao.getExamWordList(count = count, skip = skip)
            .map { mapper.wordFullDbToExamWord(it) }
    }

    override suspend fun getExamWordListSize(): Int {
        return translatedWordDao.getExamWordListSize()
    }

    override suspend fun searchWordList(query: String): Flow<List<WordRV>> {
        return translatedWordDao.searchWordList(query = "%$query%")
            .map { list ->
                mapper.wordListDbToWordList(list)
            }
    }

    override suspend fun searchWordListSize(): Flow<Int> {
        return translatedWordDao.searchWordListSize()
    }

    override suspend fun getWordById(id: Long): ModifyWord {
        return mapper.wordFullDbToModifyWord(translatedWordDao.getWordById(id))
    }

    override suspend fun deleteWord(id: Long): Boolean {
        return translatedWordDao.deleteWord(id) != WORD_IS_NOT_FOUND
    }

    override suspend fun updatePriorityById(priority: Int, id: Long): Boolean {
        return translatedWordDao.updatePriorityById(priority, id) != WORD_IS_NOT_FOUND
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
        private const val WORD_IS_NOT_FOUND = -1
    }
}