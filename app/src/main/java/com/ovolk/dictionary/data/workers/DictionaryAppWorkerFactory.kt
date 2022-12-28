package com.ovolk.dictionary.data.workers

import androidx.work.DelegatingWorkerFactory
import com.ovolk.dictionary.domain.use_case.exam.GetWordsForDelayedUpdatePriorityUseCase
import com.ovolk.dictionary.domain.use_case.exam.UpdateWordPriorityUseCase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DictionaryAppWorkerFactory @Inject constructor(
    updateWordPriorityUseCase: UpdateWordPriorityUseCase,
    getWordsForDelayedUpdatePriorityUseCase: GetWordsForDelayedUpdatePriorityUseCase,
) : DelegatingWorkerFactory() {
    init {
        addFactory(
            UpdateWordsPriorityWorkerFactory(
                updateWordPriorityUseCase = updateWordPriorityUseCase,
                getWordsForDelayedUpdatePriorityUseCase = getWordsForDelayedUpdatePriorityUseCase
            )
        )
    }
}

