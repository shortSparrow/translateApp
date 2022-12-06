package com.ovolk.dictionary.presentation.modify_word

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.use_case.lists.AddNewListUseCase
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.*
import com.ovolk.dictionary.domain.use_case.settings_languages.GetSelectedLanguages
import com.ovolk.dictionary.presentation.modify_word.helpers.*
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
    private val addChipUseCase: AddChipUseCase,
    private val selectLanguageUseCase: SelectLanguageUseCase,
    application: Application,
    private val savedStateHandle: SavedStateHandle,
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
    val recordAudio = RecordAudioHandler(application = application)
    private var initialState by mutableStateOf(InitialState(recordAudio = recordAudio))

    private fun getTimestamp(): Long = System.currentTimeMillis()

    init {
        launchRightMode()
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

        recordAudio.listener = object : RecordAudioHandler.RecordAudioListener {
            override fun soundMarkAsExistButIsNoTrue() {
                updateAudio(null)
            }

            override fun saveAudio(savableFile: String?) {
                updateAudio(savableFile)
            }
        }
    }

    private fun launchRightMode() {
        val mode = checkNotNull(savedStateHandle.get<String>("mode"))

        if (ModifyWordModes.valueOf(mode) == ModifyWordModes.MODE_EDIT) {
            val wordId = checkNotNull(savedStateHandle.get<Long>("wordId"))
            launchEditMode(wordId)
        }

        if (ModifyWordModes.valueOf(mode) == ModifyWordModes.MODE_ADD) {
            val wordValue = savedStateHandle.get<String>("wordValue") ?: ""
            val listId = savedStateHandle.get<Long>("listId") ?: -1L
            launchAddMode(
                wordValue = wordValue,
                listId = listId // when open fragment from word list fragment
            )
        }
    }

    private fun resetModalError() {
        if (composeState.modalError.isError) {
            composeState = composeState.copy(modalError = SimpleError(isError = false))
        }
    }

    fun onRecordAction(action: RecordAudioAction) {
        when (action) {
            RecordAudioAction.DeleteRecord -> recordAudio.onPressDelete()
            RecordAudioAction.ListenRecord -> recordAudio.startPlaying()
            RecordAudioAction.SaveRecord -> recordAudio.prepareToSave()
            RecordAudioAction.StartRecording -> recordAudio.startRecording()
            RecordAudioAction.StopRecording -> recordAudio.stopRecording()
            RecordAudioAction.HideBottomSheet -> recordAudio.closeBottomSheet()
            RecordAudioAction.OpenBottomSheet -> recordAudio.openBottomSheet()
        }
    }

    fun onComposeAction(action: ModifyWordAction) {
        when (action) {
            ModifyWordAction.ResetModalError -> resetModalError()
            is ModifyWordAction.HandleAddNewListModal -> {
                composeState = composeState.copy(isOpenAddNewListModal = action.isOpen)
                if (!action.isOpen) resetModalError()
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
                    addNewLangModal = AddNewLangModal(isOpen = true, type = action.type)
                )
            }
            ModifyWordAction.CloseAddNewLanguageModal -> closeAddNewLanguageModal()
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
                composeState = composeState.copy(
                    englishWord = action.value,
                    englishWordError = ValidateResult()
                )
            }
            is ModifyWordAction.OnChangePriority -> {
                composeState = composeState.copy(priorityValue = action.value)
            }
            is ModifyWordAction.OnSelectList -> onSelectList(action.listId)
            ModifyWordAction.ToggleVisibleAdditionalPart -> {
                composeState =
                    composeState.copy(isAdditionalFieldVisible = !composeState.isAdditionalFieldVisible)
            }
            ModifyWordAction.OnPressSaveWord -> saveWord()
            ModifyWordAction.ToggleDeleteModalOpen -> {
                composeState =
                    composeState.copy(isOpenDeleteWordModal = !composeState.isOpenDeleteWordModal)
            }
            ModifyWordAction.DeleteWord -> {
                composeState.editableWordId?.let { wordId ->
                    viewModelScope.launch {
                        deleteWordUseCase(wordId)
                        composeState = composeState.copy(isOpenDeleteWordModal = false)
                        listener?.onDeleteWord()
                    }
                }
            }
            is ModifyWordAction.GoBack -> handlePressGoBack(action.withValidateUnsavedChanges)
            ModifyWordAction.ToggleUnsavedChanges -> {
                composeState =
                    composeState.copy(isOpenUnsavedChanges = !composeState.isOpenUnsavedChanges)
            }
            is ModifyWordAction.ToggleFieldDescribeModalOpen -> {
                composeState =
                    composeState.copy(
                        isFieldDescribeModalOpen = !composeState.isFieldDescribeModalOpen,
                        fieldDescribeModalQuestion = action.question
                    )
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
                    translateValue = translateState.translationWord.trim(),
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
                    hintValue = hintState.hintWord.trim(),
                    hintState = hintState
                )
            }
            ModifyWordHintsAction.CancelEditHint -> {
                hintState = hintState.copy(editableHint = null, hintWord = "")
            }
        }
    }

    private fun closeAddNewLanguageModal() {
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

    private fun onSelectList(listId: Long) {
        val newList = composeState.wordLists.map {
            if (it.id == listId) it.copy(isSelected = !it.isSelected) else it.copy(
                isSelected = false
            )
        }

        val pressableList =
            newList.find { it.id == listId }.takeIf { it?.isSelected == true }

        composeState = composeState.copy(
            wordLists = newList,
            wordListInfo = pressableList
        )
    }

    private fun handlePressGoBack(withValidateUnsavedChanges: Boolean) {
        if (withValidateUnsavedChanges) {

            // when user got to modifyWord screen from list screen we automatically apply wordListInfo.
            // In this case we should ignore it and go back without popup
            fun isWordListInfoTheSame(): Boolean {
                val passedListId = savedStateHandle.get<Long>("listId") ?: -1L
                if (passedListId != -1L) {
                    return true
                }
                return initialState.composeState.wordListInfo == composeState.wordListInfo
            }

            val isTheSame =
                initialState.composeState.descriptionWord == composeState.descriptionWord &&
                        initialState.composeState.englishWord == composeState.englishWord &&
                        initialState.composeState.priorityValue == composeState.priorityValue &&
                        initialState.composeState.soundFileName == composeState.soundFileName &&
                        initialState.composeState.transcriptionWord == composeState.transcriptionWord &&

                        isWordListInfoTheSame() &&

                        initialState.hintState.hints == hintState.hints &&
                        initialState.hintState.hintWord == hintState.hintWord &&

                        initialState.translateState.translates == translateState.translates &&
                        initialState.translateState.translationWord == translateState.translationWord &&

                        initialState.languageState.languageFromList.find { it.isChecked } == languageState.languageFromList.find { it.isChecked } &&
                        initialState.languageState.languageToList.find { it.isChecked } == languageState.languageToList.find { it.isChecked }

            if (isTheSame) {
                listener?.goBack()
            } else {
                composeState = composeState.copy(isOpenUnsavedChanges = true)
            }
        } else {
            composeState = composeState.copy(isOpenUnsavedChanges = false)
            listener?.goBack()
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

        val sound = composeState.soundFileName?.let { WordAudio(it) }

        val word = ModifyWord(
            id = composeState.editableWordId ?: 0L,
            priority = composeState.priorityValue.toInt(),
            value = composeState.englishWord.trim(),
            translates = translateState.translates,
            description = composeState.descriptionWord.trim(),
            sound = sound,
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
                listener?.onSaveWord()
            }
        }
    }

    // wordValue which selected and passed into app as intent
    private fun launchAddMode(wordValue: String, listId: Long) {
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
        setInitialState()
    }

    private fun launchEditMode(wordId: Long) {
        val id = if (wordId == -1L) error("wordId is null") else wordId
        getWordById(id)
    }

    private fun prefillListIdFromArgs(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = getListsUseCase.getListById(listId)

            withContext(Dispatchers.Main) {
                composeState = composeState.copy(wordListInfo = res)
            }
        }
    }

    private fun updateAudio(fileName: String?) {
        viewModelScope.launch {
            composeState.editableWordId?.let {
                val sound = if (fileName == null) {
                    null
                } else WordAudio(fileName = fileName)
                modifyWordUseCase.modifyOnlySound(it, sound = sound)
            }
            composeState = composeState.copy(soundFileName = fileName)
        }
    }

    private fun getWordById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val word = getWordItemUseCase(id)
            val langList = loadLanguages(langToCode = word.langTo, langFromCode = word.langFrom)
            val wordListInfo = word.wordListId?.let { getListsUseCase.getListById(word.wordListId) }

            word.sound?.let { sound -> recordAudio.prepareToOpen(sound.fileName) }
            withContext(Dispatchers.Main) {
                composeState = composeState.copy(
                    englishWord = word.value,
                    transcriptionWord = word.transcription,
                    descriptionWord = word.description,
                    soundFileName = word.sound?.fileName,
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

                setInitialState()
            }
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

    // state for track changes and show alert on goBack press if has unsaved changes
    private fun setInitialState() {
        initialState = InitialState(
            composeState = composeState,
            languageState = languageState,
            translateState = translateState,
            hintState = hintState,
            recordAudio = recordAudio
        )
    }

    interface Listener {
        fun onDeleteWord()
        fun onSaveWord()
        fun goBack()
    }
}