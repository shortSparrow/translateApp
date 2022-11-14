package com.ovolk.dictionary.domain.model.exam

import androidx.compose.runtime.*
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate

@Stable
class ExamWord(
    val id: Long,
    val value: String,
    val hints: List<HintItem>,
    val langTo: String,
    var answerVariants: MutableList<ExamAnswerVariant>,
    initialTranslates: List<Translate>,
    initialPriority: Int,
    initialStatus: ExamWordStatus = ExamWordStatus.UNPROCESSED,
) {
    var status by mutableStateOf(initialStatus)
    var givenAnswer by mutableStateOf("")
    var priority by mutableStateOf(initialPriority)
    var translates by mutableStateOf<List<Translate>>(initialTranslates)
}

