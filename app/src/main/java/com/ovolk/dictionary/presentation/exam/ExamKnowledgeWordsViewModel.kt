package com.ovolk.dictionary.presentation.exam

import android.app.Application
import android.os.LocaleList
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.exam.GetExamWordListUseCase
import com.ovolk.dictionary.domain.use_case.exam.UpdateWordPriorityUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.GetActiveDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarError
import com.ovolk.dictionary.presentation.exam.NavigateButtons.NEXT
import com.ovolk.dictionary.presentation.exam.NavigateButtons.PREVIOUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

fun parseDefaultLongProps(value: Long?): Long? {
    if (value == -1L) return null
    return value
}

@HiltViewModel
class ExamKnowledgeWordsViewModel @Inject constructor(
    private val getExamWordListUseCase: GetExamWordListUseCase,
    private val updateWordPriorityUseCase: UpdateWordPriorityUseCase,
    private val modifyWordUseCase: ModifyWordUseCase,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val getActiveDictionaryUseCase: GetActiveDictionaryUseCase,
    appSettingsRepository: AppSettingsRepository,
) : ViewModel() {
    private val listId = parseDefaultLongProps(savedStateHandle.get<Long>("listId"))
    private val listName = savedStateHandle.get<String>("listName")
    private val appSettings = appSettingsRepository.getAppSettings()
    val isDoubleLanguageExamEnable = appSettings.isDoubleLanguageExamEnable
    private val isAutoSuggestEnable = appSettings.isExamAutoSuggestEnable

    var listener: Listener? = null
    private val examLocalCache = ExamLocalCache.getInstance()
    var composeState by mutableStateOf(ExamKnowledgeState())
        private set

    private var editText: EditText? by mutableStateOf(null)

    private fun getTimestamp(): Long = System.currentTimeMillis()

    private fun getCurrentWord() = composeState.examWordList[composeState.activeWordPosition]

    private fun setupKeyboardLanguage() {
        if (!isDoubleLanguageExamEnable) return

        editText?.imeHintLocales = LocaleList(Locale(getCurrentWord().langTo))
        val inputMethodManager = DictionaryApp.applicationContext()
            .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.restartInput(editText)

    }

    init {
        initialLaunch()
    }

    private fun initialLaunch() {
        viewModelScope.launch {
            composeState = composeState.copy(
                dictionaryId = parseDefaultLongProps(savedStateHandle.get<Long>("dictionaryId"))
                    ?: kotlin.run {
                        when (val activeDictionary =
                            getActiveDictionaryUseCase.getDictionaryActive()) {
                            is Either.Failure -> null
                            is Either.Success -> activeDictionary.value.id
                        }
                    }
            )
            loadWordsList()
        }
    }

    fun onAction(action: ExamAction) {
        when (action) {
            ExamAction.OnCheckAnswer -> checkAnswer(composeState.answerValue)
            is ExamAction.SetEditText -> {
                editText = action.editText
            }

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
                    listName = composeState.listName,
                    dictionaryId = composeState.dictionaryId,
                )
                loadWordsList()
            }

            ExamAction.OnLoadNextPageWords -> {
                if (!composeState.isAllExamWordsLoaded) {
                    viewModelScope.launch {
                        val response =
                            getExamWordListUseCase.loadNextPage(
                                listId = listId,
                                mode = composeState.mode,
                                dictionaryId = composeState.dictionaryId
                            )
                                ?: return@launch

                        when (response) {
                            is Either.Failure -> {
                                GlobalSnackbarManger.showGlobalSnackbar(
                                    duration = SnackbarDuration.Short,
                                    data = SnackBarError(message = response.value.message),
                                )
                            }

                            is Either.Success -> {
                                val newList =
                                    composeState.examWordList.plus(response.value.examWordList)

                                composeState = composeState.copy(
                                    examWordList = newList,
                                    isAllExamWordsLoaded = response.value.totalCount == newList.size,
                                    examListTotalCount = response.value.totalCount
                                )
                            }
                        }
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
                listener?.onNavigateToCreateFirstWord(listId = listId)

                // temporary solution for updating exam list after create first word
                viewModelScope.launch {
                    delay(100L)
                    composeState = composeState.copy(
                        shouldLoadWordListAgain = true,
                        isAllExamWordsLoaded = false
                    )
                }
            }

            is ExamAction.ReloadLoadExamList -> {
                initialLaunch()
            }

            ExamAction.OnPressAddDictionary -> {
                listener?.goToDictionaryList()
                composeState = composeState.copy(shouldLoadWordListAgain = true)
            }
        }
    }

    private fun loadWordsList() {
        composeState = composeState.copy(
            isLoading = true,
            listName = listName ?: "",
            shouldLoadWordListAgain = false
        )
        viewModelScope.launch {
            val response = getExamWordListUseCase(
                isInitialLoad = true,
                listId = listId,
                mode = composeState.mode,
                dictionaryId = composeState.dictionaryId
            )
            when (response) {
                is Either.Failure -> {
                    GlobalSnackbarManger.showGlobalSnackbar(
                        duration = SnackbarDuration.Short,
                        data = SnackBarError(message = response.value.message),
                    )
                    composeState = composeState.copy(isLoading = false)
                }

                is Either.Success -> {
                    val list: List<ExamWord> = response.value.examWordList
                    if (list.isNotEmpty()) {
                        list[0].status = ExamWordStatus.IN_PROCESS
                        examLocalCache.setExamStatus(ExamStatus.IN_PROGRESS)
                    }

                    composeState = composeState.copy(
                        examWordList = list,
                        isAllExamWordsLoaded = response.value.totalCount == list.size,
                        examListTotalCount = response.value.totalCount,
                        isLoading = false,
                        isDoubleLanguageExamEnable = isDoubleLanguageExamEnable,
                        isAutoSuggestEnable = isAutoSuggestEnable
                    )

                    delay(100) // delay for apply keyboard locale
                    setupKeyboardLanguage()
                }
            }
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
        setupKeyboardLanguage()
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
        fun onNavigateToCreateFirstWord(listId: Long?)
        fun onNavigateToHome()
        fun goToDictionaryList()
    }
}
