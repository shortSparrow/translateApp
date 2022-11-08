package com.ovolk.dictionary.presentation.modify_word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType.LANG_FROM
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType.LANG_TO
import com.ovolk.dictionary.domain.use_case.lists.AddNewListUseCase
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.*
import com.ovolk.dictionary.domain.use_case.settings_languages.GetSelectedLanguages
import com.ovolk.dictionary.presentation.modify_word.helpers.validateSelectLanguage
import com.ovolk.dictionary.presentation.modify_word.helpers.validateTranslates
import com.ovolk.dictionary.presentation.modify_word.helpers.validateWordValue
import com.ovolk.dictionary.presentation.modify_word.helpers.validationPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyWordViewModel @Inject constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
    private val getWordItemUseCase: GetWordItemUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val getListsUseCase: GetListsUseCase,
    private val addNewListUseCase: AddNewListUseCase,
    private val getSelectedLanguages: GetSelectedLanguages,
    private val addChipUseCase: AddChipUseCase,
    private val selectLanguageUseCase: SelectLanguageUseCase
) : ViewModel() {
    var listener: Listener? = null

    var composeState by mutableStateOf(ComposeState())
        private set

    var languageState by mutableStateOf(Languages())
        private set

    var translateState by mutableStateOf(Translates())
        private set

    var hintState by mutableStateOf(Hints())
        private set


    private fun getTimestamp(): Long = System.currentTimeMillis()
//    fun getAudioFileName(): String? = state.soundFileName

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
            ModifyWordAction.ResetModalError -> resetModalError()
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
                languageState = selectLanguageUseCase.selectLanguage(
                    languageState,
                    action.language.langCode,
                    action.type
                )
            }
            is ModifyWordAction.PressAddNewLanguage -> {
                languageState = languageState.copy(
                    addNewLangModal = AddNewLangModal(
                        isOpen = true,
                        type = action.type
                    )
                )
            }
            ModifyWordAction.CloseAddNewLanguageModal -> {
                val langList = when (composeState.modifyMode) {
                    ModifyWordModes.MODE_ADD -> loadLanguages(
                        langFromCode = null,
                        langToCode = null
                    )
                    ModifyWordModes.MODE_EDIT -> loadLanguages(
                        langFromCode = languageState.languageFromList.find { it.isChecked }?.langCode,
                        langToCode = languageState.languageToList.find { it.isChecked }?.langCode,
                    )
                }

                languageState = languageState.copy(
                    addNewLangModal = AddNewLangModal(isOpen = false, type = null),
                    languageFromList = langList["languageFrom"] ?: emptyList(),
                    languageToList = langList["languageTo"] ?: emptyList(),
                )
            }
            is ModifyWordAction.AddNewList -> {
                viewModelScope.launch {
                    val validationResult = addNewListUseCase.addNewList(action.title)
                    composeState = composeState.copy(
                        isOpenAddNewListModal = validationResult.isError,
                        modalError = validationResult
                    )
                }
            }
            is ModifyWordAction.OnChangeDescription -> {
                composeState = composeState.copy(descriptionWord = action.value)
            }
            is ModifyWordAction.OnChangeEnglishTranscription -> {
                composeState = composeState.copy(transcriptionWord = action.value)
            }
            is ModifyWordAction.OnChangeEnglishWord -> {
                composeState = composeState.copy(englishWord = action.value)
            }
            is ModifyWordAction.OnChangePriority -> {
                composeState = composeState.copy(priorityValue = action.value)
            }
            is ModifyWordAction.OnSelectList -> {
                val newList = composeState.wordLists.map {
                    if (it.id == action.listId) it.copy(isSelected = !it.isSelected) else it.copy(
                        isSelected = false
                    )
                }

                val pressableList =
                    newList.find { it.id == action.listId }.takeIf { it?.isSelected == true }

                composeState = composeState.copy(
                    wordLists = newList,
                    wordListInfo = pressableList
                )
            }
            ModifyWordAction.ToggleVisibleAdditionalPart -> {
                composeState =
                    composeState.copy(isAdditionalFieldVisible = !composeState.isAdditionalFieldVisible)
            }
            ModifyWordAction.OnPressSaveWord -> saveWord()
            ModifyWordAction.ToggleDeleteModalOpen -> {
                composeState =
                    composeState.copy(isDeleteModalOpen = !composeState.isOpenDeleteWordModal)
            }
            ModifyWordAction.DeleteWord -> {
                composeState.editableWordId?.let { wordId ->
                    viewModelScope.launch { deleteWordUseCase(wordId) }
                }
            }
        }
    }

    fun onTranslateAction(action: ModifyWordTranslatesAction) {
        when (action) {
            is ModifyWordTranslatesAction.OnChangeTranslate -> {
                translateState =
                    translateState.copy(translationWord = action.value, error = ValidateResult())
            }
            is ModifyWordTranslatesAction.OnLongPressTranslate -> {
                val newTranslateList = translateState.translates.map {
                    if (it.localId == action.translateLocalId) return@map it.copy(isHidden = !it.isHidden)
                    return@map it
                }
                translateState = translateState.copy(translates = newTranslateList)

            }
            is ModifyWordTranslatesAction.OnPressDeleteTranslate -> {
                translateState =
                    translateState.copy(translates = translateState.translates.filter { it.localId != action.translateLocalId })
            }
            is ModifyWordTranslatesAction.OnPressEditTranslate -> {
                translateState = translateState.copy(
                    editableTranslate = action.translate,
                    translationWord = action.translate.value,
                    error = ValidateResult()
                )
            }
            ModifyWordTranslatesAction.OnPressAddTranslate -> {
                translateState = addChipUseCase.addTranslate(
                    translateValue = translateState.translationWord,
                    translateState = translateState
                )
            }
            ModifyWordTranslatesAction.CancelEditTranslate -> {
                translateState = translateState.copy(editableTranslate = null, translationWord = "")
            }
        }
    }

    fun onHintAction(action: ModifyWordHintsAction) {
        when (action) {
            is ModifyWordHintsAction.OnChangeHint -> {
                hintState = hintState.copy(hintWord = action.value, error = ValidateResult())
            }
            is ModifyWordHintsAction.OnDeleteHint -> {
                hintState =
                    hintState.copy(hints = hintState.hints.filter { it.localId != action.hintLocalId })
            }
            is ModifyWordHintsAction.OnPressEditHint -> {
                hintState = hintState.copy(
                    editableHint = action.hint,
                    hintWord = action.hint.value,
                    error = ValidateResult()
                )
            }
            ModifyWordHintsAction.OnPressAddHint -> {
                hintState = addChipUseCase.addHint(
                    hintValue = hintState.hintWord,
                    hintState = hintState
                )
            }
            ModifyWordHintsAction.CancelEditHint -> {
                hintState = hintState.copy(editableHint = null, hintWord = "")
            }
        }
    }

    private fun saveWord() {
        val wordValidation = validateWordValue(composeState.englishWord)
        val priorityValidation = validationPriority(composeState.priorityValue)
        val translatesValidation = validateTranslates(translateState.translates)
        val selectLanguageToValidation =
            validateSelectLanguage(languageState.languageToList.find { it.isChecked }?.langCode)
        val selectLanguageFromValidation =
            validateSelectLanguage(languageState.languageFromList.find { it.isChecked }?.langCode)

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
            composeState = composeState.copy(
                englishWordError = wordValidation,
                priorityError = priorityValidation
            )
            translateState = translateState.copy(error = translatesValidation)
            languageState = languageState.copy(
                selectLanguageFromError = selectLanguageFromValidation,
                selectLanguageToError = selectLanguageToValidation
            )
            return
        }

//        val sound = state.soundFileName?.let { WordAudio(it) }

        val word = ModifyWord(
            id = composeState.editableWordId ?: 0L,
            priority = composeState.priorityValue.toInt(),
            value = composeState.englishWord.trim(),
            translates = translateState.translates,
            description = composeState.descriptionWord.trim(),
//            sound = sound,
            sound = null,
            langFrom = languageState.languageFromList.find { it.isChecked }!!.langCode,
            langTo = languageState.languageToList.find { it.isChecked }!!.langCode,
            hints = hintState.hints,
            transcription = composeState.transcriptionWord.trim(),
            createdAt = composeState.createdAt ?: getTimestamp(),
            updatedAt = getTimestamp(),
            wordListId = composeState.wordListInfo?.id,
        )

        viewModelScope.launch {
            modifyWordUseCase(word).apply {
//                val isSuccess = this != -1L
//                _uiState.value = ModifyWordUiState.ShowResultModify(isSuccess)
            }
        }
    }


    // wordValue which selected and passed into app as intent
    fun launchAddMode(wordValue: String, listId: Long) {

        val langList = loadLanguages(langFromCode = null, langToCode = null)
        languageState = languageState.copy(
            languageFromList = langList["languageFrom"]
                ?: emptyList(),
            languageToList = langList["languageTo"] ?: emptyList(),
        )
        composeState =
            composeState.copy(modifyMode = ModifyWordModes.MODE_ADD, englishWord = wordValue)


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
        getWordById(id)
    }


    private fun prefillListIdFromArgs(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = getListsUseCase.getListById(listId)
            composeState = composeState.copy(wordListInfo = res)
        }
    }


//    fun updateAudio(fileName: String?) {
//        viewModelScope.launch {
//            state = state.copy(soundFileName = fileName)
//            _uiState.value = ModifyWordUiState.UpdateSoundFile(fileName)
//
//            state.editableWordId?.let {
//                val sound = if (fileName == null) null else WordAudio(fileName = fileName)
//                modifyWordUseCase.modifyOnlySound(it, sound = sound)
//            }
//        }
//    }

    private fun getWordById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val word = getWordItemUseCase(id)
            val langList = loadLanguages(langToCode = word.langTo, langFromCode = word.langFrom)
            val wordListInfo = word.wordListId?.let {
                getListsUseCase.getListById(word.wordListId)
            }

            composeState = composeState.copy(
                englishWord = word.value,
                transcriptionWord = word.transcription,
                descriptionWord = word.description,
//                soundFileName = word.sound?.fileName,
                editableWordId = word.id,
                createdAt = word.createdAt,
                priorityValue = word.priority.toString(),
                wordListInfo = wordListInfo,
                modifyMode = ModifyWordModes.MODE_EDIT
            )

            translateState = translateState.copy(translates = word.translates)
            hintState = hintState.copy(hints = word.hints)

            languageState = languageState.copy(
                languageToList = langList["languageTo"] ?: emptyList(),
                languageFromList = langList["languageFrom"] ?: emptyList(),
            )
        }
    }

    private fun loadLanguages(
        langToCode: String? = null,
        langFromCode: String? = null
    ): Map<String, List<SelectLanguage>> {
        return getSelectedLanguages.getLanguagesForSelect(
            savedLangToCode = langToCode,
            savedLangFromCode = langFromCode
        )
    }

    interface Listener {
        fun onDeleteWord()
        fun onSaveWord()
    }
}