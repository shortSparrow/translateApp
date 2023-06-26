package com.ovolk.dictionary.domain.use_case.exam

import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import javax.inject.Inject

class GetWordsForDelayedUpdatePriorityUseCase @Inject constructor(
    val repository: TranslatedWordRepository,
    val mapper: WordMapper
) {
    suspend operator fun invoke() =
        repository.getWordsForDelayedUpdatePriority()
}