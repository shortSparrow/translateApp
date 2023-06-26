package com.ovolk.dictionary.presentation.exam

import android.widget.EditText
import androidx.compose.runtime.Stable
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.domain.model.exam.ExamWord


enum class NavigateButtons { NEXT, PREVIOUS }
enum class ExamMode { DAILY_MODE, INFINITY_MODE }
enum class CompleteAlertBehavior { STAY_HERE, GO_HOME }


sealed interface ExamAction {
    data class OnInputTranslate(val value: String) : ExamAction
    data class OnPressNavigate(val navigateButton: NavigateButtons) : ExamAction
    object OnCheckAnswer : ExamAction
    object ToggleShowVariants : ExamAction
    data class OnSelectVariant(val variant: ExamAnswerVariant) : ExamAction
    object ToggleHints : ExamAction
    object ToggleHiddenTranslateDescription : ExamAction
    object ToggleTranslates : ExamAction
    data class ToggleSelectModeModal(val isVisible: Boolean) : ExamAction
    data class OnSelectMode(val mode: ExamMode) : ExamAction
    object OnPressAddHiddenTranslate : ExamAction
    object OnLoadNextPageWords : ExamAction
    data class OnSelectActiveWord(val wordIndex: Int) : ExamAction
    data class OnLongPressHiddenTranslate(val translateId: Long) : ExamAction
    data class CloseTheEndExamModal(val behavior: CompleteAlertBehavior) : ExamAction
    object OnNavigateToCreateFirstWord : ExamAction
    data class LoadExamList(val listId: Long, val listName: String? = null) : ExamAction
    data class SetEditText(val editText: EditText?) : ExamAction
}


@Stable
data class ExamKnowledgeState(
    val isLoading: Boolean = true,
    var examWordList: List<ExamWord> = emptyList(),
    val isAllExamWordsLoaded: Boolean = false,
    val examListTotalCount: Int = 0,
    val answerValue: String = "",
    val activeWordPosition: Int = 0,
    val mode: ExamMode = ExamMode.DAILY_MODE,

    val isTranslateExpanded: Boolean = false,
    val isHiddenTranslateDescriptionExpanded: Boolean = false,
    val isVariantsExpanded: Boolean = false,
    val isHintsExpanded: Boolean = false,

    val listId: Long? = null, // when show words only from this list
    val listName: String = "",

    val isDoubleLanguageExamEnable: Boolean = false,

    // modal
    val isExamEnd: Boolean = false,
    val isModeDialogOpen: Boolean = false,
    val isExamEndDialogOpen: Boolean = false,

    val shouldLoadWordListAgain: Boolean = false, // needed for update list when list is empty and user press "create first word", create one and go back
)

