package com.ovolk.dictionary.data.workers

import com.ovolk.dictionary.data.model.UpdatePriority
import com.ovolk.dictionary.domain.repositories.TranslatedWordRepository
import com.ovolk.dictionary.domain.use_case.exam.UpdateWordPriorityUseCase
import java.util.*
import javax.inject.Inject

class HandleOldWordsPriority @Inject constructor(
    private val updateWordPriorityUseCase: UpdateWordPriorityUseCase,
    val repository: TranslatedWordRepository,
) {

    suspend fun updatePriorityForBunchOldWords() {
        val list = getWordsForSilentUpdatePriority()
        updateWordPriorityUseCase.updateList(list)
    }

    // get words which user learn along time ago
    private suspend fun getWordsForSilentUpdatePriority(): List<UpdatePriority> {
        val tenDays = 1000 * 60 * 60 * 24 * 10
        val beforeUpdatedAt = Calendar.getInstance().timeInMillis - tenDays

        // list items which didn't update a lot of time and have priority less than default
        return repository.getWordsForSilentUpdatePriority(
            beforeUpdatedAt = beforeUpdatedAt,
            count = DEFAULT_WORDS_COUNT_FOR_UPDATE_PRIORITY
        )
            .map {
                it.copy(priority = it.priority + 1)
            }
    }

    companion object {
        const val DEFAULT_WORDS_COUNT_FOR_UPDATE_PRIORITY = 10
    }
}