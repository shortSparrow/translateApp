package com.example.ttanslateapp.data.database

import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.data.model.HintDb
import com.example.ttanslateapp.data.model.TranslateDb
import com.example.ttanslateapp.data.model.WordInfoDb
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.model.exam.ExamWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TranslatedWordRepositoryImpl @Inject constructor(
    private val translatedWordDao: TranslatedWordDao,
    private val mapper: WordMapper
) : TranslatedWordRepository {

    override suspend fun getExamWordList(count: Int): List<ExamWord> {
        return translatedWordDao.getExamWordList(count).map { mapper.wordFullDbToExamWord(it) }
    }

    override suspend fun searchWordList(query: String): Flow<List<WordRV>> {
        return translatedWordDao.searchWordList("%$query%").map { list ->
            mapper.wordListDbToWordList(list)
        }
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

    override suspend fun modifyWordTranslates(translates: List<TranslateDb>): List<Long> {
        return translatedWordDao.modifyTranslates(translates)
    }

    override suspend fun modifyWordHints(hints: List<HintDb>): List<Long> {
        return translatedWordDao.modifyHints(hints)
    }

    override suspend fun modifyWordInfo(wordInfoDb: WordInfoDb): Long {
        return translatedWordDao.modifyWordInfo(wordInfoDb)
    }

//    override suspend fun modifyWord(
//        word: ModifyWord
//    ): Long {
//        val wordId = translatedWordDao.modifyWord(
//            wordInfoDb = mapper.modifyWordToWordFullDb(word),
//            translates = word.translates.map {
//                mapper.translateLocalToDb(
//                    translate = it,
//                    wordId = word.id
//                )
//            },
//            hints = word.hints.map { mapper.hintLocalToDb(hint = it, wordId = word.id) }
//        )
//        return wordId
//    }


    companion object {
        private const val WORD_IS_NOT_FOUND = -1
    }
}