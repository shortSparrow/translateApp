package com.ovolk.dictionary.domain.use_case.exam

import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.data.model.UpdatePriority
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import javax.inject.Inject

class UpdateWordPriorityUseCase @Inject constructor(
    val repository: TranslatedWordRepository,
    val mapper: WordMapper
) {
    suspend operator fun invoke(priority: Int, id: Long) =
        repository.updatePriorityById(priority, id)

    suspend fun updateList(list: List<UpdatePriority>) {
        repository.updateWordsPriority(
            list.map { mapper.updatePriorityToUpdatePriorityDb(it) }
        )
    }

    suspend fun addWordForUpdatePriority(updatePriority: UpdatePriority): Boolean {
        return repository.addWordForDelayedUpdatePriority(
            mapper.updatePriorityToUpdatePriorityDb(updatePriority)
        )
    }

}