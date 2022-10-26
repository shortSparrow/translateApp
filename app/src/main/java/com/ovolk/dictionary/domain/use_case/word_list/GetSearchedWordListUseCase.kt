package com.ovolk.dictionary.domain.use_case.word_list

import com.ovolk.dictionary.domain.TranslatedWordRepository
import javax.inject.Inject

class GetSearchedWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository
) {
    suspend operator fun invoke(searchValue: String) =
        repository.searchWordList(query = searchValue)

    suspend fun getExactSearchedWord(searchValue: String) =
        repository.searchExactWordList(query = searchValue.trim().lowercase())
}