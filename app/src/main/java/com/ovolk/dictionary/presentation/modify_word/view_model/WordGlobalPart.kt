package com.ovolk.dictionary.presentation.modify_word.view_model

import androidx.compose.material.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.FailureWithCode
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.lists.AddNewListUseCase
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.UNKNOWN_ERROR
import com.ovolk.dictionary.domain.use_case.modify_word.AddedWordResult
import com.ovolk.dictionary.domain.use_case.modify_word.DeleteWordUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarError
import com.ovolk.dictionary.presentation.modify_word.ComposeState
import com.ovolk.dictionary.presentation.modify_word.LocalState
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.modify_word.WordAlreadyExistActions
import com.ovolk.dictionary.presentation.modify_word.helpers.validateDictionary
import com.ovolk.dictionary.presentation.modify_word.helpers.validateTranslates
import com.ovolk.dictionary.presentation.modify_word.helpers.validateWordValue
import com.ovolk.dictionary.presentation.modify_word.helpers.validationPriority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class ViewModelWordGlobalSlice {
    lateinit var modifyWordViewModel: ModifyWordViewModel

    operator fun invoke(
        modifyWordViewModel: ModifyWordViewModel
    ) {
        this.modifyWordViewModel = modifyWordViewModel
        afterInit()
    }

    open fun afterInit() {}
}


open class WordGlobalPart @Inject constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val getListsUseCase: GetListsUseCase,
    private val addNewListUseCase: AddNewListUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val crudDictionaryUseCase: CrudDictionaryUseCase,
) : ViewModelWordGlobalSlice() {

    // must be lazy because this class create before modifyWordViewModel.viewModelScope available. viewModelScope set in init
    val viewModelScope by lazy { modifyWordViewModel.viewModelScope }

    val globalState = MutableStateFlow(ComposeState())
    private var _localState = MutableStateFlow(LocalState())

    private fun getTimestamp(): Long = System.currentTimeMillis()

    override fun afterInit() {
        viewModelScope.launch {
            var initialDictionaryListSize: Int? = null
            crudDictionaryUseCase.getDictionaryList().collectLatest { dictionaryList ->
                globalState.update {
                    it.copy(dictionaryList = dictionaryList)
                }

                if (initialDictionaryListSize == 0 && dictionaryList.size == 1 && globalState.value.dictionary.value == null) {
                    globalState.value.dictionary.value = dictionaryList.first()
                }
                initialDictionaryListSize = dictionaryList.size
            }
        }

        viewModelScope.launch {
            globalState.value.dictionary.collectLatest { dictionary ->
                dictionary?.let {
                    getListsUseCase.getAllListsForModifyWord(it.id).collectLatest { list ->
                        withContext(Dispatchers.Main) {
                            globalState.update { state -> state.copy(wordLists = list) }
                        }
                    }
                }
            }
        }
    }

    private fun resetModalError() {
        if (globalState.value.modalError.isError) {
            globalState.update { it.copy(modalError = SimpleError(isError = false)) }
        }
    }

    fun onAction(action: ModifyWordAction) {
        when (action) {
            ModifyWordAction.ResetModalError -> resetModalError()
            is ModifyWordAction.HandleAddNewListModal -> {
                globalState.update { it.copy(isOpenAddNewListModal = action.isOpen) }
                if (!action.isOpen) resetModalError()
            }

            is ModifyWordAction.HandleSelectModal -> {
                globalState.update { it.copy(isOpenSelectModal = action.isOpen) }
            }

            is ModifyWordAction.OnSelectDictionary -> {
                val selectedWordList =
                    if (action.dictionaryId == globalState.value.selectedWordList?.dictionaryId) globalState.value.selectedWordList else null

                globalState.update {
                    it.copy(
                        selectedWordList = selectedWordList,
                        dictionaryError = ValidateResult()
                    )
                }
                globalState.value.dictionary.value =
                    globalState.value.dictionaryList.find { it.id == action.dictionaryId }
            }

            is ModifyWordAction.PressAddNewDictionary -> {
                modifyWordViewModel.listener?.toAddNewDictionary()
            }

            is ModifyWordAction.AddNewList -> {
                viewModelScope.launch {
                    val validationResult = addNewListUseCase.addNewList(
                        action.title,
                        dictionaryId = globalState.value.dictionary.value?.id
                    )

                    when (validationResult) {
                        is Either.Failure -> {
                            if (validationResult.value is FailureMessage) {
                                globalState.update {
                                    it.copy(
                                        isOpenAddNewListModal = true,
                                        modalError = SimpleError(
                                            isError = true,
                                            text = validationResult.value.message
                                        )
                                    )
                                }
                            }
                            if (validationResult.value is FailureWithCode) {
                                if (validationResult.value.code == UNKNOWN_ERROR) {
                                    GlobalSnackbarManger.showGlobalSnackbar(
                                        duration = SnackbarDuration.Short,
                                        data = SnackBarError(message = validationResult.value.message),
                                    )
                                }
                            }
                        }

                        is Either.Success -> {
                            globalState.update {
                                it.copy(
                                    isOpenAddNewListModal = false,
                                    modalError = SimpleError(isError = false, text = "")
                                )
                            }
                        }
                    }
                }
            }

            is ModifyWordAction.OnChangeDescription -> {
                globalState.update { it.copy(descriptionWord = action.value) }
            }

            is ModifyWordAction.OnChangeEnglishTranscription -> {
                globalState.update { it.copy(transcriptionWord = action.value) }
            }

            is ModifyWordAction.OnChangeWord -> {
                globalState.update {
                    it.copy(
                        word = action.value,
                        englishWordError = ValidateResult()
                    )
                }
            }

            is ModifyWordAction.OnChangePriority -> {
                globalState.update { it.copy(priorityValue = action.value) }
            }

            is ModifyWordAction.OnSelectList -> onSelectList(action.listId)
            ModifyWordAction.ToggleVisibleAdditionalPart -> {
                globalState.update { it.copy(isAdditionalFieldVisible = !it.isAdditionalFieldVisible) }

            }

            ModifyWordAction.OnPressSaveWord -> saveWord(overrideWordWithSameValue = false)
            ModifyWordAction.ToggleDeleteModalOpen -> {
                globalState.update { it.copy(isOpenDeleteWordModal = !it.isOpenDeleteWordModal) }

            }

            ModifyWordAction.DeleteWord -> {
                globalState.value.editableWordId?.let { wordId ->
                    viewModelScope.launch {
                        deleteWordUseCase(wordId)
                        globalState.update { it.copy(isOpenDeleteWordModal = false) }
                        modifyWordViewModel.listener?.onDeleteWord()
                    }
                }
            }

            is ModifyWordAction.GoBack -> handlePressGoBack(action.withValidateUnsavedChanges)
            ModifyWordAction.ToggleUnsavedChanges -> {
                globalState.update { it.copy(isOpenUnsavedChanges = !it.isOpenUnsavedChanges) }
            }

            is ModifyWordAction.ToggleFieldDescribeModalOpen -> {
                globalState.update {
                    it.copy(
                        isFieldDescribeModalOpen = !it.isFieldDescribeModalOpen,
                        fieldDescribeModalQuestion = action.question
                    )
                }

            }

            is ModifyWordAction.HandleWordAlreadyExistModal -> {
                globalState.update { it.copy(isOpenModalWordAlreadyExist = false) }

                when (action.action) {
                    WordAlreadyExistActions.REPLACE -> {
                        saveWord(overrideWordWithSameValue = true)
                        _localState.update { it.copy(alreadyExistWordId = null) }
                    }

                    WordAlreadyExistActions.CLOSE -> {
                        _localState.update { it.copy(alreadyExistWordId = null) }
                    }

                    WordAlreadyExistActions.GO_TO_WORD -> {
                        _localState.value.alreadyExistWordId?.let { id ->
                            modifyWordViewModel.launchEditMode(id)
                        }
                    }
                }

            }
        }
    }

    private fun saveWordAndOverrideIfExist(word: ModifyWord) {
        viewModelScope.launch {
            modifyWordUseCase(
                word = word.copy(
                    id = _localState.value.alreadyExistWordId ?: word.id
                )
            ).apply {
                modifyWordViewModel.listener?.onSaveWord()
            }
        }
    }

    private fun saveWordOrGetModal(word: ModifyWord) {
        viewModelScope.launch {
            val result = modifyWordUseCase.addWordIfNotExist(word)
            when (result.status) {
                AddedWordResult.SUCCESS -> modifyWordViewModel.listener?.onSaveWord()
                AddedWordResult.WORD_ALREADY_EXIST -> {
                    globalState.update { it.copy(isOpenModalWordAlreadyExist = true) }
                    _localState.update { it.copy(alreadyExistWordId = result.wordId) }
                }
            }
        }
    }

    private fun onSelectList(listId: Long) {
        val selectedList = globalState.value.wordLists.find { it.id == listId }
        val selectedWordList =
            if (selectedList?.id == globalState.value.selectedWordList?.id) null else selectedList
        globalState.update {
            it.copy(selectedWordList = selectedWordList)
        }
    }

    private fun handlePressGoBack(withValidateUnsavedChanges: Boolean) {
        val initialState = modifyWordViewModel.initialState
        if (withValidateUnsavedChanges) {
            // when user got to modifyWord screen from list screen we automatically apply wordListInfo.
            // In this case we should ignore it and go back without popup
            fun isWordListInfoTheSame(): Boolean {
                val passedListId = savedStateHandle.get<Long>("listId") ?: -1L
                if (passedListId != -1L) {
                    return true
                }
                return initialState.composeState.selectedWordList == globalState.value.selectedWordList
            }

            val isTheSame =
                initialState.composeState.descriptionWord == globalState.value.descriptionWord &&
                        initialState.composeState.word == globalState.value.word &&
                        initialState.composeState.priorityValue == globalState.value.priorityValue &&
                        initialState.composeState.soundFileName == globalState.value.soundFileName &&
                        initialState.composeState.transcriptionWord == globalState.value.transcriptionWord &&

                        isWordListInfoTheSame() &&

                        initialState.hintState.hints == modifyWordViewModel.hintState.value.hints &&
                        initialState.hintState.hintWord == modifyWordViewModel.hintState.value.hintWord &&

                        initialState.translateState.translates == modifyWordViewModel.translateState.value.translates &&
                        initialState.translateState.translationWord == modifyWordViewModel.translateState.value.translationWord

//                        initialState.languageState.languageToList.find { it.isChecked } == languageState.languageToList.find { it.isChecked }

            if (isTheSame) {
                modifyWordViewModel.listener?.goBack()
            } else {
                globalState.update { it.copy(isOpenUnsavedChanges = true) }
            }
        } else {
            globalState.update { it.copy(isOpenUnsavedChanges = false) }
            modifyWordViewModel.listener?.goBack()
        }
    }

    private fun saveWord(overrideWordWithSameValue: Boolean) {
        val wordValidation = validateWordValue(globalState.value.word)
        val priorityValidation = validationPriority(globalState.value.priorityValue)
        val translatesValidation =
            validateTranslates(modifyWordViewModel.wordTranslatesPart.translateState.value.translates)
        val dictionaryValidation = validateDictionary(globalState.value.dictionary.value)

        val hasError =
            listOf(
                wordValidation,
                translatesValidation,
                priorityValidation,
                dictionaryValidation,
            ).any { !it.successful }

        if (hasError) {
            globalState.update {
                it.copy(
                    englishWordError = wordValidation,
                    priorityError = priorityValidation,
                    dictionaryError = dictionaryValidation
                )
            }

            modifyWordViewModel.wordTranslatesPart.translateState.value =
                modifyWordViewModel.wordTranslatesPart.translateState.value.copy(error = translatesValidation)

            return
        }

        val sound = globalState.value.soundFileName?.let { WordAudio(it) }

        val word = ModifyWord(
            id = globalState.value.editableWordId ?: 0L,
            priority = globalState.value.priorityValue.toInt(),
            value = globalState.value.word.trim(),
            translates = modifyWordViewModel.translateState.value.translates,
            description = globalState.value.descriptionWord.trim(),
            sound = sound,
            hints = modifyWordViewModel.hintState.value.hints,
            transcription = globalState.value.transcriptionWord.trim(),
            createdAt = globalState.value.createdAt ?: getTimestamp(),
            updatedAt = getTimestamp(),
            wordListId = globalState.value.selectedWordList?.id,
            dictionary = globalState.value.dictionary.value!!,
        )

        if (!overrideWordWithSameValue && globalState.value.modifyMode == ModifyWordModes.MODE_ADD) {
            saveWordOrGetModal(word = word)
        } else {
            saveWordAndOverrideIfExist(word = word)
        }
    }
}
