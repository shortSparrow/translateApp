package com.example.ttanslateapp.domain.use_case

import com.example.ttanslateapp.domain.TranslatedWordRepository
import javax.inject.Inject

class GetExamWordListUseCase @Inject constructor(
    private val repository: TranslatedWordRepository
) {
    suspend operator fun invoke(count: Int = EXAM_WORD_LIST_COUNT) =
        repository.getExamWordList(count)

    companion object {
        private const val EXAM_WORD_LIST_COUNT = 4
    }
}