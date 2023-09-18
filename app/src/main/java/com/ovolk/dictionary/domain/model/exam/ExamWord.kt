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
    val langFrom: String,
    var answerVariants: MutableList<ExamAnswerVariant>,
    val isInverseWord: Boolean = false,
    val inverseValueList: List<String> = emptyList(),
    initialTranslates: List<Translate>,
    initialPriority: Int,
    initialStatus: ExamWordStatus = ExamWordStatus.UNPROCESSED,
) {
    var status by mutableStateOf(initialStatus)
    var givenAnswer by mutableStateOf("")
    var priority by mutableStateOf(initialPriority)
    var translates by mutableStateOf<List<Translate>>(initialTranslates)

    fun copy(
        id: Long = this.id,
        value: String = this.value,
        hints: List<HintItem> = this.hints,
        langTo: String = this.langTo,
        langFrom: String = this.langFrom,
        answerVariants: MutableList<ExamAnswerVariant> = this.answerVariants,
        initialTranslates: List<Translate> = this.translates,
        initialPriority: Int = this.priority,
        initialStatus: ExamWordStatus = this.status,
        isInverseWord: Boolean = this.isInverseWord,
        inverseValueList: List<String> = this.inverseValueList,
    ) = ExamWord(
        id = id,
        value = value,
        hints = hints,
        langTo = langTo,
        answerVariants = answerVariants,
        initialPriority = initialPriority,
        initialTranslates = initialTranslates,
        initialStatus = initialStatus,
        langFrom = langFrom,
        isInverseWord = isInverseWord,
        inverseValueList = inverseValueList
    )
}

