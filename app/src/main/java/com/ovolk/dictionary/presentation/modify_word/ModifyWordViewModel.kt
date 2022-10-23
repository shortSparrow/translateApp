package com.ovolk.dictionary.presentation.modify_word

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType.LANG_FROM
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType.LANG_TO
import com.ovolk.dictionary.domain.use_case.lists.AddNewListUseCase
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.DeleteWordUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.GetWordItemUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import com.ovolk.dictionary.domain.use_case.settings_languages.GetSelectedLanguages
import com.ovolk.dictionary.presentation.modify_word.helpers.validateSelectLanguage
import com.ovolk.dictionary.presentation.modify_word.helpers.validateTranslates
import com.ovolk.dictionary.presentation.modify_word.helpers.validateWordValue
import com.ovolk.dictionary.presentation.modify_word.helpers.validationPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ModifyWordViewModel @Inject constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
    private val getWordItemUseCase: GetWordItemUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val getListsUseCase: GetListsUseCase,
    private val addNewListUseCase: AddNewListUseCase,
    private val getSelectedLanguages: GetSelectedLanguages,
) : ViewModel() {

    private val _uiState = MutableLiveData<ModifyWordUiState>()
    val uiState: LiveData<ModifyWordUiState> = _uiState

    var composeState by mutableStateOf(ComposeState())
    private var state = ModifyWordState()

    private fun getTimestamp(): Long = System.currentTimeMillis()
    fun getAudioFileName(): String? = state.soundFileName

    init {
        viewModelScope.launch {
            getListsUseCase.getAllListsForModifyWord().collectLatest { list ->
                composeState = composeState.copy(
                    wordLists = list.map {
                        if (it.id == composeState.wordListInfo?.id) {
                            it.copy(isSelected = true)
                        } else it
                    },
                )
            }
        }
    }

    private fun resetModalError() {
        if (composeState.modalError.isError) {
            composeState = composeState.copy(modalError = SimpleError(isError = false))
        }
    }

    fun onComposeAction(action: ModifyWordAction) {
        when (action) {
            ModifyWordAction.ResetModalError -> {
                resetModalError()
            }
            is ModifyWordAction.HandleAddNewListModal -> {
                composeState = composeState.copy(isOpenAddNewListModal = action.isOpen)
                if (!action.isOpen) {
                    resetModalError()
                }
            }
            is ModifyWordAction.HandleSelectModal -> {
                composeState = composeState.copy(isOpenSelectModal = action.isOpen)
            }
            is ModifyWordAction.OnSelectLanguage -> {
                when (action.type) {
                    LANG_TO -> {
                        composeState =
                            composeState.copy(
                                languageToList = composeState.languageToList.map {
                                    if (it.langCode == action.language.langCode) {
                                        return@map it.copy(isChecked = true)
                                    }
                                    return@map it.copy(isChecked = false)
                                },
                                selectLanguageToError = ValidateResult(true),
                            )
                        val selectedLang = composeState.languageToList.find { it.isChecked }
                        selectedLang?.let {
                            state = state.copy(langTo = it.langCode)
                        }
                    }
                    LANG_FROM -> {
                        composeState =
                            composeState.copy(
                                languageFromList = composeState.languageFromList.map {
                                    if (it.langCode == action.language.langCode) {
                                        return@map it.copy(isChecked = true)
                                    }
                                    return@map it.copy(isChecked = false)
                                },
                                selectLanguageFromError = ValidateResult(true),
                            )
                        val selectedLang = composeState.languageFromList.find { it.isChecked }
                        selectedLang?.let {
                            state = state.copy(langFrom = it.langCode)
                        }
                    }
                }
            }
            is ModifyWordAction.PressAddNewLanguage -> {
                composeState = composeState.copy(
                    addNewLangModal = AddNewLangModal(
                        isOpen = true,
                        type = action.type
                    )
                )
            }
            ModifyWordAction.CloseAddNewLanguageModal -> {



                val langList = when(state.modifyMode) {
                    ModifyWordModes.MODE_ADD -> loadLanguages(langFromCode = null, langToCode = null)
                    ModifyWordModes.MODE_EDIT -> loadLanguages(langFromCode = state.langFrom, langToCode = state.langTo)
                }

                composeState = composeState.copy(
                    addNewLangModal = AddNewLangModal(
                        isOpen = false,
                        type = null
                    ),
                    languageFromList = langList["languageFrom"] ?: emptyList(),
                    languageToList = langList["languageTo"] ?: emptyList(),
                )
            }
        }
    }

    fun addNewList(title: String) {
        viewModelScope.launch {
            val validationResult = addNewListUseCase.addNewList(title)
            composeState = composeState.copy(
                isOpenAddNewListModal = validationResult.isError,
                modalError = validationResult
            )
        }
    }

    fun resetWordValueError() {
        state = state.copy(wordValueError = null)
        _uiState.value = ModifyWordUiState.EditFieldError(
            wordValueError = null,
            translatesError = state.translatesError
        )
    }

    fun resetTranslatesError() {
        state = state.copy(translatesError = null)
        _uiState.value = ModifyWordUiState.EditFieldError(
            translatesError = null,
            wordValueError = state.wordValueError
        )
    }

    fun deleteWord(wordId: Long?) {
        if (wordId != null) {
            state = state.copy(isDeleteModalOpen = false)
            viewModelScope.launch { deleteWordUseCase(wordId) }
        }
    }

    fun setIsOpenedDeleteModal(isOpened: Boolean) {
        state = state.copy(isDeleteModalOpen = isOpened)
        _uiState.value = ModifyWordUiState.ToggleOpenedDeleteModel(isOpened)
    }

    fun saveWord(
        value: String = "",
        description: String = "",
        transcription: String = "",
        priority: String,
    ) {
        val wordValidation = validateWordValue(value)
        val priorityValidation = validationPriority(priority)
        val translatesValidation = validateTranslates(state.translates)
        val selectLanguageToValidation = validateSelectLanguage(state.langTo)
        val selectLanguageFromValidation = validateSelectLanguage(state.langFrom)

        val hasError =
            listOf(
                wordValidation,
                translatesValidation,
                priorityValidation,
                selectLanguageToValidation,
                selectLanguageFromValidation
            ).any { !it.successful }

        // FIXME replace to usecase
        if (hasError) {
            state = state.copy(
                wordValueError = wordValidation.errorMessage,
                translatesError = translatesValidation.errorMessage,
                priorityValidation = priorityValidation.errorMessage,
            )

            _uiState.value = ModifyWordUiState.EditFieldError(
                wordValueError = wordValidation.errorMessage,
                translatesError = translatesValidation.errorMessage,
                priorityValidation = priorityValidation.errorMessage,
            )
            composeState = composeState.copy(
                selectLanguageFromError = selectLanguageFromValidation,
                selectLanguageToError = selectLanguageToValidation
            )
            return
        }

        val sound = state.soundFileName?.let { WordAudio(it) }

        val word = ModifyWord(
            id = state.editableWordId ?: 0L,
            priority = priority.toInt(),
            value = value,
            translates = state.translates,
            description = description,
            sound = sound,
            langFrom = state.langFrom!!,
            langTo = state.langTo!!,
            hints = state.hints,
            transcription = transcription,
            createdAt = state.createdAt ?: getTimestamp(),
            updatedAt = getTimestamp(),
            wordListId = composeState.wordListInfo?.id,
        )

        viewModelScope.launch {
            modifyWordUseCase(word).apply {
                val isSuccess = this != -1L
                _uiState.value = ModifyWordUiState.ShowResultModify(isSuccess)
            }
        }
    }

    // wordValue which selected and passed into app as intent
    fun launchAddMode(wordValue: String, listId: Long) {
        state = state.copy(wordValue = wordValue, modifyMode = ModifyWordModes.MODE_ADD)
        _uiState.postValue(state.toUiState())

        val langList = loadLanguages(langFromCode = null, langToCode = null)
        composeState = composeState.copy(
            languageFromList = langList["languageFrom"] ?: emptyList(),
            languageToList = langList["languageTo"] ?: emptyList(),
        )

        if (listId != -1L) {
            prefillListIdFromArgs(listId)
        }
    }

    fun launchEditMode(wordId: Long) {
        val id = if (wordId == -1L) {
            error("wordId is null")
        } else {
            wordId
        }
        state = state.copy(modifyMode = ModifyWordModes.MODE_EDIT)
        getWordById(id)
    }

    private fun prefillListIdFromArgs(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = getListsUseCase.getListById(listId)

            composeState = composeState.copy(
                wordListInfo = res,
            )
        }
    }

    fun onSelectList(listId: Long) {
        val newList = composeState.wordLists.map {
            if (it.id == listId) it.copy(isSelected = !it.isSelected) else it.copy(isSelected = false)
        }

        val pressableList = newList.find { it.id == listId }.takeIf { it?.isSelected == true }

        composeState = composeState.copy(
            wordLists = newList,
            wordListInfo = pressableList
        )
    }

    fun restoreRightMode() {
        _uiState.value = state.toUiState(screenIsRestored = true)
    }

    fun addTranslate(translateValue: String) {
        if (translateValue.isBlank()) return

        val translateList = state.translates
        val editableTranslate = state.editableTranslate

        val newTranslateItem =
            editableTranslate?.copy(value = translateValue, updatedAt = getTimestamp())
                ?: Translate(
                    localId = getTimestamp(),
                    value = translateValue,
                    createdAt = getTimestamp(),
                    updatedAt = getTimestamp(),
                    isHidden = false
                )

        val hintAlreadyExist =
            translateList.find { it.localId == newTranslateItem.localId }

        val newList = if (hintAlreadyExist == null) {
            translateList.plus(newTranslateItem)
        } else {
            translateList.map {
                if (it.localId == newTranslateItem.localId) {
                    return@map newTranslateItem
                }
                return@map it
            }
        }

        state = state.copy(translates = newList, editableTranslate = null)
        _uiState.postValue(ModifyWordUiState.CompleteModifyTranslate(newList))
    }

    fun addHint(hintValue: String) {
        if (hintValue.isBlank()) return

        val hintList = state.hints
        val editableHint = state.editableHint

        val newHintItem =
            editableHint?.copy(value = hintValue, updatedAt = getTimestamp())
                ?: HintItem(
                    localId = getTimestamp(),
                    value = hintValue,
                    createdAt = getTimestamp(),
                    updatedAt = getTimestamp(),
                )

        val hintAlreadyExist = hintList.find { it.localId == newHintItem.localId }

        val newList = if (hintAlreadyExist == null) {
            hintList.plus(newHintItem)
        } else {
            hintList.map {
                if (it.localId == newHintItem.localId) {
                    return@map newHintItem
                }
                return@map it
            }
        }

        state = state.copy(hints = newList, editableHint = null)
        _uiState.postValue(ModifyWordUiState.CompleteModifyHint(newList))
    }

    fun deleteTranslate(translateLocalId: Long) {
        val updatedTranslates = state.translates.filter { it.localId != translateLocalId }

        state = state.copy(editableTranslate = null, translates = updatedTranslates)
        _uiState.value = ModifyWordUiState.DeleteTranslates(translates = updatedTranslates)
    }

    fun deleteHint(hintLocalId: Long) {
        val updatedHints = state.hints.filter { it.localId != hintLocalId }

        state = state.copy(editableHint = null, hints = updatedHints)
        _uiState.value = ModifyWordUiState.DeleteHints(hints = updatedHints)
    }

    fun setEditableHint(editableHint: HintItem) {
        state = state.copy(editableHint = editableHint)
        _uiState.value = ModifyWordUiState.StartModifyHint(value = editableHint.value)
    }

    fun cancelEditableHint() {
        state = state.copy(editableHint = null)
        _uiState.value = ModifyWordUiState.CompleteModifyHint(state.hints)
    }

    fun cancelEditableTranslate() {
        state = state.copy(editableTranslate = null)
        _uiState.value = ModifyWordUiState.CompleteModifyTranslate(state.translates)
    }

    fun setEditableTranslate(editableTranslate: Translate) {
        state = state.copy(editableTranslate = editableTranslate)
        _uiState.value =
            ModifyWordUiState.StartModifyTranslate(value = editableTranslate.value)
    }

    fun toggleIsHiddenTranslate(item: Translate) {
        val newTranslateList = state.translates.map {
            if (it.localId == item.localId) return@map it.copy(isHidden = !item.isHidden)
            return@map it
        }
        state = state.copy(translates = newTranslateList)
        _uiState.value = ModifyWordUiState.CompleteModifyTranslate(translates = newTranslateList)
    }

    fun toggleVisibleAdditionalField() {
        val newIsAdditionalFieldVisible =
            if (state.isAdditionalFieldVisible == View.VISIBLE) View.GONE else View.VISIBLE
        state = state.copy(isAdditionalFieldVisible = newIsAdditionalFieldVisible)
        _uiState.value = ModifyWordUiState.ShowAdditionalFields(newIsAdditionalFieldVisible)
    }

    fun updateAudio(fileName: String?) {
        viewModelScope.launch {
            state = state.copy(soundFileName = fileName)
            _uiState.value = ModifyWordUiState.UpdateSoundFile(fileName)

            state.editableWordId?.let {
                val sound = if (fileName == null) null else WordAudio(fileName = fileName)
                modifyWordUseCase.modifyOnlySound(it, sound = sound)
            }
        }
    }

    private fun getWordById(id: Long) {
        _uiState.value = ModifyWordUiState.IsWordLoading(true)
        viewModelScope.launch(Dispatchers.IO) {

            val word = getWordItemUseCase(id)
            val langList = loadLanguages(langToCode = word.langTo, langFromCode = word.langFrom)

            state = state.copy(
                wordValue = word.value,
                translates = word.translates,
                transcription = word.transcription,
                description = word.description,
                selectableLanguage = word.langTo,
                hints = word.hints,
                soundFileName = word.sound?.fileName,
                editableWordId = word.id,
                langFrom = word.langFrom,
                langTo = word.langTo,
                createdAt = word.createdAt,
                priority = word.priority,
            )

            val res = word.wordListId?.let {
                getListsUseCase.getListById(word.wordListId)
            }
            composeState = composeState.copy(
                wordListInfo = res,
                languageToList = langList["languageTo"] ?: emptyList(),
                languageFromList = langList["languageFrom"] ?: emptyList(),
            )

            // or you could avoid `withContext` and just use `uiState.postValue()`
            withContext(Dispatchers.Main) {
                _uiState.value = ModifyWordUiState.IsWordLoading(false)
                _uiState.value = state.toUiState()
            }
        }
    }


    private fun loadLanguages(
        langToCode: String? = null,
        langFromCode: String? = null
    ): Map<String, List<SelectLanguage>> {

        val langList = getSelectedLanguages.getLanguagesForSelect(
            savedLangToCode = langToCode,
            savedLangFromCode = langFromCode
        )

        val languageFromList = langList["languageFrom"] ?: emptyList()
        val languageToList = langList["languageTo"] ?: emptyList()

        val selectedLangFrom = languageFromList.find { it.isChecked }
        val selectedLangTo = languageToList.find { it.isChecked }

        state = state.copy(
            langFrom = selectedLangFrom?.langCode,
            langTo = selectedLangTo?.langCode
        )

        return langList
    }

    private fun ModifyWordState.toUiState(screenIsRestored: Boolean = false): ModifyWordUiState {
        return ModifyWordUiState.PreScreen(
            wordValue = wordValue,
            wordValueError = wordValueError,
            priority = priority,
            transcription = transcription,
            description = description,
            translates = translates,
            translatesError = translatesError,
            editableTranslate = state.editableTranslate,
            hints = hints,
            editableHint = state.editableHint,
            langFrom = langFrom,
            soundFileName = soundFileName,
            isAdditionalFieldVisible = isAdditionalFieldVisible,
            screenIsRestored = screenIsRestored,
            isDeleteModalOpen = isDeleteModalOpen,
        )
    }
}