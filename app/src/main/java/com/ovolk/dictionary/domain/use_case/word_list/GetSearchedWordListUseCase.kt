package com.ovolk.dictionary.domain.use_case.word_list

import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
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

//        return@coroutineScope list.map { l -> l.map { it.copy(translates = it.translates.filter { !it.isHidden }) }}

        return@coroutineScope list.map { l ->
            SearchWordListResponse(
                list = l.map { it.copy(translates = it.translates.filter { !it.isHidden }) },
                total = l.size
            )
        }
    }

    suspend fun getExactSearchedWord(searchValue: String) =
        repository.searchExactWord(query = searchValue.trim().lowercase())
}