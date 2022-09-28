package com.ovolk.dictionary.domain.use_case.exam

import com.ovolk.dictionary.domain.TranslatedWordRepository
import javax.inject.Inject

class UpdateWordPriorityUseCase @Inject constructor(val repository: TranslatedWordRepository) {
    suspend operator fun invoke(priority: Int, id: Long) =
        repository.updatePriorityById(priority, id)
}