package com.ovolk.dictionary.domain.use_case.word_list

import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class SearchWordListResponse(
    val list: List<WordRV>,
    val total: Int
)

class GetSearchedWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository,
) {

    suspend fun getWords(searchValue: String, dictionaryId: Long? = null) = coroutineScope {
        val list =
            if (dictionaryId == null) {
                repository.searchWordList(query = searchValue)
            } else {
                repository.searchWordListByDictionary(
                    query = searchValue,
                    dictionaryId = dictionaryId
                )
            }

        val totalCont = repository.searchWordListSize()

        return@coroutineScope list.combine(totalCont) { list, count ->
            SearchWordListResponse(
                list = list.map { it.copy(translates = it.translates.filter { !it.isHidden }) },
                total = count
            )
        }
    }

    suspend fun getExactSearchedWord(searchValue: String) =
        repository.searchExactWord(query = searchValue.trim().lowercase())
}