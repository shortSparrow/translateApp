package com.ovolk.dictionary.presentation.exam

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.ovolk.dictionary.data.in_memory_storage.ExamLocalCache
import com.ovolk.dictionary.data.in_memory_storage.ExamStatus
import com.ovolk.dictionary.data.model.UpdatePriority
import com.ovolk.dictionary.data.workers.UpdateWordsPriorityWorker
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.use_case.exam.GetExamWordListUseCase
import com.ovolk.dictionary.domain.use_case.exam.UpdateWordPriorityUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import com.ovolk.dictionary.presentation.exam.NavigateButtons.NEXT
import com.ovolk.dictionary.presentation.exam.NavigateButtons.PREVIOUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExamKnowledgeWordsViewModel @Inject constructor(
    private val getExamWordListUseCase: GetExamWordListUseCase,
    private val updateWordPriorityUseCase: UpdateWordPriorityUseCase,
    private val modifyWordUseCase: ModifyWordUseCase,
    private val application: Application,
) : ViewModel() {
    var listener: Listener? = null
    private val examLocalCache = ExamLocalCache.getInstance()
    var composeState by mutableStateOf(ExamKnowledgeState())
        private set

    init {
//        GenerateFakeWords(modifyWordUseCase).generateFakeWords()
        examLocalCache.setExamStatus(ExamStatus.IN_PROGRESS)
    }

    private fun getTimestamp(): Long = System.currentTimeMillis()

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
                            getExamWordListUseCase.loadNextPage(
                                composeState.listId,
                                mode = composeState.mode
                            )
                                ?: return@launch
                        val newList = composeState.examWordList.plus(response.examWordList)

                        composeState = composeState.copy(
                            examWordList = newList,
                            isAllExamWordsLoaded = response.totalCount == newList.size,
                            examListTotalCount = response.totalCount
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

            is ExamAction.OnLongPressHiddenTranslate -> toggleIsHiddenTranslate(action.translateId)

            is ExamAction.CloseTheEndExamModal -> {
                composeState = composeState.copy(isExamEnd = false)
                if (action.behavior == CompleteAlertBehavior.GO_HOME) {
                    listener?.onNavigateToHome()
                }
            }

            ExamAction.OnNavigateToCreateFirstWord -> {
                listener?.onNavigateToCreateFirstWord()

                // temporary solution for updating exam list after create first word
                viewModelScope.launch {
                    delay(100L)
                    composeState = composeState.copy(
                        shouldLoadWordListAgain = true,
                        isAllExamWordsLoaded = false
                    )
                }
            }

            is ExamAction.LoadExamList -> {
                val listId = if (action.listId == -1L) null else action.listId
                loadWordsList(listId = listId, listName = action.listName)
            }
        }
    }

    private fun loadWordsList(listId: Long?, listName: String? = null) {
        composeState = composeState.copy(
            isLoading = true,
            listId = listId,
            listName = listName ?: "",
            shouldLoadWordListAgain = false
        )
        viewModelScope.launch {
            val response = getExamWordListUseCase(
                isInitialLoad = true,
                listId = listId,
                mode = composeState.mode
            )
            val list: List<ExamWord> = response.examWordList
            if (list.isNotEmpty()) {
                list[0].status = ExamWordStatus.IN_PROCESS
            }

            composeState = composeState.copy(
                examWordList = list,
                isAllExamWordsLoaded = response.totalCount == list.size,
                examListTotalCount = response.totalCount,
                isLoading = false,
            )
        }
    }

    private fun addHiddenTranslate() {
        val value = composeState.answerValue
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
            modifyWordUseCase.modifyTranslates(
                wordId = currentWord.id,
                translates = listOf(hiddenTranslate),
            ).apply {
                currentWord.translates =
                    currentWord.translates.plus(hiddenTranslate.copy(id = this.first()))
                currentWord.status = ExamWordStatus.SUCCESS
                currentWord.givenAnswer = value
                composeState = composeState.copy(answerValue = "")
            }

            updateWordPriorityUseCase.addWordForUpdatePriority(
                UpdatePriority(
                    priority = currentWord.priority.minus(1),
                    wordId = getCurrentWord().id
                )
            )
        }

    }

    private fun toggleIsHiddenTranslate(translateId: Long) {
        val currentWord = getCurrentWord()

        currentWord.translates = currentWord.translates.map {
            if (it.id == translateId) it.copy(isHidden = !it.isHidden)
            else it
        }

        viewModelScope.launch {
            modifyWordUseCase.modifyTranslates(
                wordId = currentWord.id,
                translates = currentWord.translates
            )
        }
    }

    private fun checkAnswer(answer: String) {
        val answerQuery = answer.lowercase().trim()

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
            updateWordPriorityUseCase.addWordForUpdatePriority(
                UpdatePriority(
                    priority = newPriority,
                    wordId = getCurrentWord().id
                )
            )
        }

        val isExamEnd =
            composeState.examWordList.none { word -> word.status == ExamWordStatus.UNPROCESSED || word.status == ExamWordStatus.IN_PROCESS }
        composeState = composeState.copy(isExamEnd = isExamEnd)
        if (isExamEnd) {
            examLocalCache.setExamStatus(ExamStatus.INACTIVE)
        }
    }

    private fun handleNavigation(newActiveWordPosition: Int) {
        val oldActivePosition = composeState.activeWordPosition
        if (oldActivePosition == newActiveWordPosition) return

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

    /**
     * We can't update priority during exam, because update priority change word position in DB, and we load words by bunch (10 words
     * per bunch for example). If we make update during the exam some words appears twice, and some never. For avoid this we add words to
     * temporary table during the exam and after end/cancel exam worker go to the temporary table, get data and update main table, and clear
     * temporary table. If crash happens, or user kill app, worker also runs every times on app launch, so data will be updated anyway
     * **/
    fun updateAnsweredWords() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            UpdateWordsPriorityWorker.DELAY_UPDATE_WORDS_PRIORITY_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE, // What should we do, when worker with this name already exist
            UpdateWordsPriorityWorker.getWorker()
        )
    }

    interface Listener {
        fun onNavigateToCreateFirstWord()
        fun onNavigateToHome()
    }
}
