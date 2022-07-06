package com.example.ttanslateapp.presentation.exam.adapter

import android.view.View
import com.example.ttanslateapp.domain.model.exam.ExamWord
import com.example.ttanslateapp.domain.model.exam.ExamWordStatus
import com.example.ttanslateapp.domain.model.modify_word_chip.Translate

sealed interface ExamKnowledgeUiState {
    object IsLoadingWords : ExamKnowledgeUiState
    object LoadedEmptyList : ExamKnowledgeUiState


    data class LoadedWordsSuccess(
        val examWordList: List<ExamWord>,
        val currentWord: ExamWord,
        val activeWordPosition: Int
    ) : ExamKnowledgeUiState

    data class HandleAnswerInput(val value: String, val userGaveAnswer: Boolean) :
        ExamKnowledgeUiState

    data class CheckedAnswer(
        val status: ExamWordStatus = ExamWordStatus.UNPROCESSED,
        val examWordList: List<ExamWord>,
        val currentWord: ExamWord,
        val isExamEnd: Boolean = false,
        val givenAnswer: String
    ) : ExamKnowledgeUiState

    data class QuestionNavigation(
        val examWordList: List<ExamWord>,
        val currentWord: ExamWord,
        val activeWordPosition: Int,
    ) : ExamKnowledgeUiState

    data class ToggleIsVariantsExpanded(val isExpanded: Boolean) : ExamKnowledgeUiState
    data class ToggleExpandedHint(val isExpanded: Boolean, val nextHintButtonVisibility: Int) :
        ExamKnowledgeUiState

    data class ShowNextHint(
        val allHintsIsShown: Boolean,
        val currentWord: ExamWord
    ) : ExamKnowledgeUiState

    data class ToggleVisibilityHiddenDescription(val visibility: Int) : ExamKnowledgeUiState

    data class ToggleCurrentWordTrasnalteExpanded(
        val isExpanded: Boolean,
        val translates: List<Translate>
    ) : ExamKnowledgeUiState

    data class UpdateHiddenTranslates(
        val translates: List<Translate>,
        val clearInputValue: Boolean = false
    ) : ExamKnowledgeUiState
}

data class ExamKnowledgeState(
    val isLoading: Boolean = false,
    var examWordList: List<ExamWord> = emptyList(),
    val examWordListEmpty: Boolean = false,
    val currentWord: ExamWord? = null,
    val isExamEnd: Boolean = false,
    val isInputWordInvalid: Boolean = false,
    val activeWordPosition: Int = 0,
    val hiddenTranslateDescriptionVisibility: Int = View.GONE,

    //    val isVariantsExpanded: Boolean = false, // FIXME replace to current word
//    val countOfVisibleHints: Int = 0, // FIXME replace to current word
//    val allHintsIsShown: Boolean = false, // FIXME replace to current word
//    val isHintsExpanded: Boolean = false, // FIXME replace to current word
)


