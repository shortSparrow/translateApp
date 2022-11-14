package com.ovolk.dictionary.domain.model.exam

import androidx.compose.runtime.*
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate

@Stable
class ExamWord(
    val id: Long,
    val value: String,
    var translates: List<Translate>, // TODO make mutable
    val hints: List<HintItem>,
    initialPriority: Int,
    initialStatus: ExamWordStatus = ExamWordStatus.UNPROCESSED,
    var answerVariants: MutableList<ExamAnswerVariant>,
    val isShowVariantsAvailable: Boolean = false, // needed fro disabling "show variants" feature for unavailable languages (for now except UA)
) {
    var status by mutableStateOf(initialStatus)
    var givenAnswer by mutableStateOf("")
    var priority by mutableStateOf(initialPriority)
}

