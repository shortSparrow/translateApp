package com.ovolk.dictionary.domain.use_case.modify_word

import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import javax.inject.Inject

class GetWordItemUseCase @Inject constructor(
    private val repository: TranslatedWordRepository
){
    suspend operator fun invoke(wordId: Long) = repository.getWordById(wordId)
}