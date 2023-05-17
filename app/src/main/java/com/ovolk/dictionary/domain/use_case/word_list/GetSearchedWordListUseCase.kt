package com.ovolk.dictionary.domain.use_case.word_list

import com.ovolk.dictionary.domain.TranslatedWordRepository
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class SearchWordListResponse(
    val list: List<WordRV>,
    val total: Int
)

class GetSearchedWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository
) {

    suspend operator fun invoke(searchValue: String) = coroutineScope {
        val list = repository.searchWordList(query = searchValue)
        val totalCont = repository.searchWordListSize()

        return@coroutineScope list.combine(totalCont) { list, count ->
            SearchWordListResponse(list = list, total = count)
        }
    }

    suspend fun getExactSearchedWord(searchValue: String) =
        repository.searchExactWord(query = searchValue.trim().lowercase())
}