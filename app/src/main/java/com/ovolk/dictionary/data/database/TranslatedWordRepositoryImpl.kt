package com.ovolk.dictionary.data.database

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.data.in_memory_storage.InMemoryStorage
import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.data.model.HintDb
import com.ovolk.dictionary.data.model.TranslateDb
import com.ovolk.dictionary.data.model.WordInfoDb
import com.ovolk.dictionary.domain.TranslatedWordRepository
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.util.SHOW_VARIANTS_EXAM_AVAILABLE_LANGUAGES
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type
import javax.inject.Inject


// InMemoryStorage by inMemoryStorage need for avoid override here InMemoryStorage methods.
// We copy them from inMemoryStorage implementation (Local Cache)
class TranslatedWordRepositoryImpl @Inject constructor(
    private val translatedWordDao: TranslatedWordDao,
    private val mapper: WordMapper,
    private val inMemoryStorage: InMemoryStorage,
    private val application: Application
) : TranslatedWordRepository, InMemoryStorage by inMemoryStorage {

    // show variants feature for now available only for Ukrainian language
    private fun getAvailableLangList(): List<String> {
        val sharedPref: SharedPreferences =
            application.getSharedPreferences(
                USER_STATE_PREFERENCES, // TODO change on new pref
                AppCompatActivity.MODE_PRIVATE
            )
        val availableLangListString =
            sharedPref.getString(SHOW_VARIANTS_EXAM_AVAILABLE_LANGUAGES, "")
        val languageListType: Type = object : TypeToken<List<String>>() {}.type

        return Gson().fromJson(availableLangListString, languageListType)
    }

    override suspend fun getExamWordList(count: Int, skip: Int): List<ExamWord> {
        return translatedWordDao.getExamWordList(count = count, skip = skip)
            .map { word ->
                mapper.wordFullDbToExamWord(
                    word,
                    isShowVariantsAvailable = getAvailableLangList().contains(
                        word.wordInfo.langTo.uppercase()
                    )
                )
            }
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
            .map { word ->
                mapper.wordFullDbToExamWord(
                    word, isShowVariantsAvailable = getAvailableLangList().contains(
                        word.wordInfo.langTo
                    )
                )
            }
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