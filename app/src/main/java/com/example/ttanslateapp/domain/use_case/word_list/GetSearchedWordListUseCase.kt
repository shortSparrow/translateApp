package com.example.ttanslateapp.domain.use_case.word_list

import com.example.ttanslateapp.domain.TranslatedWordRepository
import javax.inject.Inject

class GetSearchedWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository
) {
    suspend operator fun invoke(searchValue: String) =  repository.searchWordList(query = searchValue)
}