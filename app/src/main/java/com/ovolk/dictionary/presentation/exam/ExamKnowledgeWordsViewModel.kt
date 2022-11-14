package com.ovolk.dictionary.presentation.exam

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.TranslatedWordRepository
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.use_case.exam.GetExamWordListUseCase
import com.ovolk.dictionary.domain.use_case.exam.UpdateWordPriorityUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import com.ovolk.dictionary.presentation.exam.NavigateButtons.NEXT
import com.ovolk.dictionary.presentation.exam.NavigateButtons.PREVIOUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExamKnowledgeWordsViewModel @Inject constructor(
    val getExamWordListUseCase: GetExamWordListUseCase,
    val updateWordPriorityUseCase: UpdateWordPriorityUseCase,
    private val modifyWordUseCase: ModifyWordUseCase,
    val repository: TranslatedWordRepository,
    val application: Application,
) : ViewModel() {
    var composeState by mutableStateOf(ExamKnowledgeState())
        private set

    private fun getTimestamp(): Long = System.currentTimeMillis()

    init {
        loadWordsList(null, null)
//        viewModelScope.async {
//            var wordCount = 1
//            while (wordCount < 10_000) {
//                val word = ModifyWord(
//                    value = wordCount.toString(),
//                    translates = listOf(
//                        Translate(
//                            localId = 1,
//                            updatedAt = getTimestamp(),
//                            createdAt = getTimestamp(),
//                            value = "translate_$wordCount",
//                            isHidden = false,
//                        )
//                    ),
//                    description = "",
//                    sound = null,
//                    langFrom = "EN",
//                    langTo = "UA",
//                    hints = listOf(
//                        HintItem(
//                            localId = 1,
//                            updatedAt = getTimestamp(),
//                            createdAt = getTimestamp(),
//                            value = "translate_$wordCount",
//                        )
//                    ),
//                    createdAt = getTimestamp(),
//                    updatedAt = getTimestamp(),
//                    transcription = ""
//                )
//                val d = viewModelScope.async {
//                    modifyWordUseCase(word = word)
//                }
//                d.await()
//                delay(1)
//                wordCount++
//            }
//        }
    }

    private fun getCurrentWord() = composeState.examWordList[composeState.activeWordPosition]

    fun onAction(action: ExamAction) {
        when (action) {
            ExamAction.OnCheckAnswer -> checkAnswer(composeState.answerValue)
            is ExamAction.OnInputTranslate -> {
                composeState =
                    composeState.copy(answerValue = action.value)
                getCurrentWord().answerVariants.forEach {
                    if (it.value != action.value.trim()) {
                        it.isSelected = false
                    }
                }
            }
            is ExamAction.OnPressNavigate -> {
                when (action.navigateButton) {
                    NEXT -> {
                        if (composeState.activeWordPosition + 1 >= composeState.examWordList.size) {
                            return
                        }
                        val newActiveWordPosition = composeState.activeWordPosition + 1
                        handleNavigation(newActiveWordPosition)
                    }
                    PREVIOUS -> {
                        if (composeState.activeWordPosition - 1 < 0) {
                            return
                        }

                        val newActiveWordPosition = composeState.activeWordPosition - 1
                        handleNavigation(newActiveWordPosition)
                    }
                }
            }
            is ExamAction.OnSelectVariant -> {
                composeState = composeState.copy(answerValue = action.variant.value)
                // TODO mayve do it locally in compose
                getCurrentWord().answerVariants.forEach {
                    it.isSelected = it.id == action.variant.id
                }
            }
            ExamAction.ToggleHints -> {
                composeState = composeState.copy(isHintsExpanded = !composeState.isHintsExpanded)
            }
            ExamAction.ToggleShowVariants -> {
                composeState =
                    composeState.copy(isVariantsExpanded = !composeState.isVariantsExpanded)
            }
            is ExamAction.ToggleSelectModeModal -> {
                composeState = composeState.copy(isModeDialogOpen = !composeState.isModeDialogOpen)
            }
            is ExamAction.OnSelectMode -> {
                composeState = ExamKnowledgeState(
                    mode = action.mode,
                    listId = composeState.listId,
                    listName = composeState.listName
                )
                loadWordsList(composeState.listId, listName = composeState.listName)
            }
            ExamAction.OnLoadNextPageWords -> {
                if (!composeState.isAllExamWordsLoaded) {
                    viewModelScope.launch {
                        val response =
                            getExamWordListUseCase.loadNextPage(composeState.listId)
                                ?: return@launch
                        val newList = composeState.examWordList.plus(response.examWordList)
                        composeState = composeState.copy(
                            examWordList = newList,
                            isAllExamWordsLoaded = response.isReachEnd
                        )
                    }
                }
            }
            is ExamAction.OnSelectActiveWord -> handleNavigation(action.wordIndex)
            ExamAction.ToggleHiddenTranslateDescription -> {
                composeState =
                    composeState.copy(isHiddenTranslateDescriptionExpanded = !composeState.isHiddenTranslateDescriptionExpanded)
            }
            ExamAction.ToggleTranslates -> {
                composeState =
                    composeState.copy(isTranslateExpanded = !composeState.isTranslateExpanded)
            }
            ExamAction.OnPressAddHiddenTranslate -> addHiddenTranslate()
        }
    }

    private fun loadWordsList(listId: Long?, listName: String? = null) {
        composeState = composeState.copy(isLoading = true, listId = listId, listName = listName)
        viewModelScope.launch {
            val response = getExamWordListUseCase(
                isInitialLoad = true,
                listId = listId
            )
            val list: List<ExamWord> = response.examWordList
            list[0].status = ExamWordStatus.IN_PROCESS

            composeState = ExamKnowledgeState(
                examWordList = list,
                isAllExamWordsLoaded = response.isReachEnd,
                isLoading = false
            )
        }
    }

    private fun addHiddenTranslate() {
        val value = composeState.answerValue
        // TODO add UI validation
        if (value.isEmpty()) return

        val hiddenTranslate = Translate(
            id = 0L,
            localId = getTimestamp(),
            createdAt = getTimestamp(),
            updatedAt = getTimestamp(),
            value = value,
            isHidden = true
        )


        val currentWord = getCurrentWord()
        viewModelScope.launch {
            // add translate and return priority to old value, and mark answer as success
            modifyWordUseCase.modifyTranslateWithUpdatePriority(
                wordId = currentWord.id,
                translates = listOf(hiddenTranslate),
                priority = currentWord.priority.minus(1),
            ).apply {
                currentWord.translates = currentWord.translates.plus(hiddenTranslate.copy(id = this.first()))
                currentWord.status = ExamWordStatus.SUCCESS
            }
        }

    }


//    fun toggleIsHiddenTranslate(item: Translate) {
//        state.currentWord?.let { currentWord ->
//            if (!currentWord.isFreeze) return // forbidden add/modify word translates if user don't answer yet
//
//            val updatedTranslate = item.copy(isHidden = !item.isHidden)
//            val translates =
//                currentWord.translates.map { if (it.id == item.id) return@map updatedTranslate else return@map it }
//
//            val updatedList = state.examWordList.map {
//                if (it.id == currentWord.id) return@map it.copy(translates = translates) else return@map it
//            }
//
//            state = state.copy(
//                currentWord = currentWord.copy(translates = translates),
//                examWordList = updatedList
//            )
//            _uiState.value =
//                ExamKnowledgeUiState.UpdateHiddenTranslates(translates = translates)
//
//            viewModelScope.launch {
//                modifyWordUseCase.modifyTranslates(
//                    wordId = currentWord.id,
//                    translates = listOf(updatedTranslate)
//                )
//            }
//        }
//    }

    private fun checkAnswer(answer: String) {
        val answerQuery = answer.lowercase()

        val matchedAnswer = getCurrentWord().translates.find { translate ->
            answerQuery == translate.value.lowercase().trim()
        }

        val isAnswerCorrect = matchedAnswer != null

        var newPriority =
            if (isAnswerCorrect) getCurrentWord().priority.minus(1)
            else getCurrentWord().priority.plus(1)
        if (newPriority < 0) newPriority = 0
        val newStatus =
            if (isAnswerCorrect) ExamWordStatus.SUCCESS else ExamWordStatus.FAIL


        getCurrentWord().priority = newPriority
        getCurrentWord().status = newStatus
        getCurrentWord().givenAnswer = answer

        viewModelScope.launch {
            updateWordPriorityUseCase(priority = newPriority, id = getCurrentWord().id)
        }

        val isExamEnd =
            composeState.examWordList.none { word -> word.status == ExamWordStatus.UNPROCESSED || word.status == ExamWordStatus.IN_PROCESS }

        composeState = composeState.copy(
            isExamEnd = isExamEnd,
        )
    }

    private fun handleNavigation(newActiveWordPosition: Int) {
        val oldActivePosition = composeState.activeWordPosition
        composeState.examWordList[newActiveWordPosition].let {
            if (it.status != ExamWordStatus.SUCCESS && it.status !== ExamWordStatus.FAIL) {
                it.status = ExamWordStatus.IN_PROCESS
            }
        }

        composeState.examWordList[oldActivePosition].let {
            if (it.status != ExamWordStatus.SUCCESS && it.status !== ExamWordStatus.FAIL) {
                it.status = ExamWordStatus.UNPROCESSED
            }
            it.answerVariants.forEach { it.isSelected = false }
        }

        composeState = composeState.copy(
            activeWordPosition = newActiveWordPosition,
            answerValue = "",
            isTranslateExpanded = false,
            isHiddenTranslateDescriptionExpanded = false,
            isVariantsExpanded = false,
            isHintsExpanded = false
        )
    }
}
