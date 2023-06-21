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
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.use_case.lists.AddNewListUseCase
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.GetActiveDictionary
import com.ovolk.dictionary.domain.use_case.modify_word.AddChipUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.AddedWordResult.SUCCESS
import com.ovolk.dictionary.domain.use_case.modify_word.AddedWordResult.WORD_ALREADY_EXIST
import com.ovolk.dictionary.domain.use_case.modify_word.DeleteWordUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.GetWordItemUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import com.ovolk.dictionary.presentation.list_full.LoadingState
import com.ovolk.dictionary.presentation.modify_word.helpers.RecordAudioHandler
import com.ovolk.dictionary.presentation.modify_word.helpers.validateTranslates
import com.ovolk.dictionary.presentation.modify_word.helpers.validateWordValue
import com.ovolk.dictionary.presentation.modify_word.helpers.validationPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val addChipUseCase: AddChipUseCase,
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val crudDictionaryUseCase: CrudDictionaryUseCase,
    private val getActiveDictionary: GetActiveDictionary,
) : ViewModel() {
    var listener: Listener? = null

    var composeState by mutableStateOf(ComposeState())
        private set

    var translateState by mutableStateOf(Translates())
        private set
    var hintState by mutableStateOf(Hints())
        private set
    private var _localState by mutableStateOf(LocalState())
    val recordAudio = RecordAudioHandler(application = application)
    private var initialState by mutableStateOf(InitialState(recordAudio = recordAudio))

    private fun getTimestamp(): Long = System.currentTimeMillis()


    init {
        launchRightMode()

        viewModelScope.launch {
            crudDictionaryUseCase.getDictionaryList().collectLatest { dictionaryList ->
                composeState = composeState.copy(dictionaryList = dictionaryList)
            }
        }

        viewModelScope.launch {
            composeState.dictionary.collectLatest { dictionary ->
                dictionary?.let {
                    getListsUseCase.getAllListsForModifyWord(it.id).collectLatest { list ->
                        withContext(Dispatchers.Main) {
                            composeState = composeState.copy(wordLists = list)
                        }
                    }
                }
            }
        }

        recordAudio.listener =
            object : RecordAudioHandler.RecordAudioListener {
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
            val dictionaryId = checkNotNull(savedStateHandle.get<Long>("dictionaryId"))
            launchAddMode(
                wordValue = wordValue,
                listId = listId, // when open fragment from word list fragment
                dictionaryId = dictionaryId,
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

            is ModifyWordAction.OnSelectDictionary -> {
                val selectedWordList =
                    if (action.dictionaryId == composeState.selectedWordList?.dictionaryId) composeState.selectedWordList else null

                composeState =
                    composeState.copy(
//                        dictionary = composeState.dictionaryList.find { it.id == action.dictionaryId },
                        selectedWordList = selectedWordList,
                    )
                composeState.dictionary.value =
                    composeState.dictionaryList.find { it.id == action.dictionaryId }

//                // TODO do as the same in ListViewModel (observe currentDictionary and observe flow)
//                viewModelScope.launch(Dispatchers.IO) {
//                    val wordLists =
//                        getListsUseCase.getAllListsForDictionaryForModifyWord(action.dictionaryId)
//                    withContext(Dispatchers.Main) {
//                        composeState = composeState.copy(wordLists = wordLists)
//                    }
//                }
            }

            is ModifyWordAction.PressAddNewDictionary -> {
                listener?.toAddNewDictionary()
            }

            is ModifyWordAction.AddNewList -> {
                viewModelScope.launch {
                    val validationResult = addNewListUseCase.addNewList(
                        action.title,
                        dictionaryId = composeState.dictionary.value?.id
                    )

                    when (validationResult) {
                        is Either.Failure -> {
                            // TODO add toast
                        }

                        is Either.Success -> {
                            composeState = composeState.copy(
                                isOpenAddNewListModal = false,
                                modalError = SimpleError(isError = false, text = "")
                            )
                        }
                    }

//                    withContext(Dispatchers.IO) {
//                        val id = composeState.dictionary?.id
//                        if (id != null) {
//                            val wordList =
//                                getListsUseCase.getAllListsForDictionaryForModifyWord(id)
//                            withContext(Dispatchers.Main) {
//                                composeState = composeState.copy(wordLists = wordList)
//                            }
//                        }
//
//                    }
                }
            }

            is ModifyWordAction.OnChangeDescription -> {
                composeState = composeState.copy(descriptionWord = action.value)
            }

            is ModifyWordAction.OnChangeEnglishTranscription -> {
                composeState = composeState.copy(transcriptionWord = action.value)
            }

            is ModifyWordAction.OnChangeWord -> {
                composeState = composeState.copy(
                    word = action.value,
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

            ModifyWordAction.OnPressSaveWord -> saveWord(overrideWordWithSameValue = false)
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

            is ModifyWordAction.HandleWordAlreadyExistModal -> {
                composeState = composeState.copy(isOpenModalWordAlreadyExist = false)

                when (action.action) {
                    WordAlreadyExistActions.REPLACE -> {
                        saveWord(overrideWordWithSameValue = true)
                        _localState = _localState.copy(alreadyExistWordId = null)
                    }

                    WordAlreadyExistActions.CLOSE -> {
                        _localState = _localState.copy(alreadyExistWordId = null)
                    }

                    WordAlreadyExistActions.GO_TO_WORD -> {
                        _localState.alreadyExistWordId?.let { id ->
                            launchEditMode(id)
                        }
                    }
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

    private fun onSelectList(listId: Long) {
        val selectedList = composeState.wordLists.find { it.id == listId }
        val selectedWordList =
            if (selectedList?.id == composeState.selectedWordList?.id) null else selectedList
        composeState = composeState.copy(
            selectedWordList = selectedWordList
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
                return initialState.composeState.selectedWordList == composeState.selectedWordList
            }

            val isTheSame =
                initialState.composeState.descriptionWord == composeState.descriptionWord &&
                        initialState.composeState.word == composeState.word &&
                        initialState.composeState.priorityValue == composeState.priorityValue &&
                        initialState.composeState.soundFileName == composeState.soundFileName &&
                        initialState.composeState.transcriptionWord == composeState.transcriptionWord &&

                        isWordListInfoTheSame() &&

                        initialState.hintState.hints == hintState.hints &&
                        initialState.hintState.hintWord == hintState.hintWord &&

                        initialState.translateState.translates == translateState.translates &&
                        initialState.translateState.translationWord == translateState.translationWord

//                        initialState.languageState.languageToList.find { it.isChecked } == languageState.languageToList.find { it.isChecked }

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

    private fun saveWord(overrideWordWithSameValue: Boolean) {
        val wordValidation = validateWordValue(composeState.word)
        val priorityValidation = validationPriority(composeState.priorityValue)
        val translatesValidation = validateTranslates(translateState.translates)

        val hasError =
            listOf(
                wordValidation,
                translatesValidation,
                priorityValidation,
//                selectLanguageToValidation,
            ).any { !it.successful }

        if (hasError) {
            composeState = composeState.copy(
                englishWordError = wordValidation,
                priorityError = priorityValidation
            )
            translateState = translateState.copy(error = translatesValidation)

            return
        }

        val sound = composeState.soundFileName?.let { WordAudio(it) }

        val word = ModifyWord(
            id = composeState.editableWordId ?: 0L,
            priority = composeState.priorityValue.toInt(),
            value = composeState.word.trim(),
            translates = translateState.translates,
            description = composeState.descriptionWord.trim(),
            sound = sound,
            hints = hintState.hints,
            transcription = composeState.transcriptionWord.trim(),
            createdAt = composeState.createdAt ?: getTimestamp(),
            updatedAt = getTimestamp(),
            wordListId = composeState.selectedWordList?.id,
            dictionary = composeState.dictionary.value!!, // TODO remove !!
        )


        if (!overrideWordWithSameValue && composeState.modifyMode == ModifyWordModes.MODE_ADD) {
            saveWordOrGetModal(word = word)
        } else {
            saveWordAndOverrideIfExist(word = word)
        }
    }

    private fun saveWordAndOverrideIfExist(word: ModifyWord) {
        viewModelScope.launch {
            modifyWordUseCase(
                word = word.copy(
                    id = _localState.alreadyExistWordId ?: word.id
                )
            ).apply {
                listener?.onSaveWord()
            }
        }
    }

    private fun saveWordOrGetModal(word: ModifyWord) {
        viewModelScope.launch {
            val result = modifyWordUseCase.addWordIfNotExist(word)
            when (result.status) {
                SUCCESS -> listener?.onSaveWord()
                WORD_ALREADY_EXIST -> {
                    composeState = composeState.copy(isOpenModalWordAlreadyExist = true)
                    _localState = _localState.copy(alreadyExistWordId = result.wordId)
                }
            }
        }
    }

    private suspend fun setDefaultDictionary() {
        if (composeState.dictionaryList.isEmpty()) {
            delay(100)
            setDefaultDictionary()
        } else {
//            composeState = composeState.copy(dictionary = composeState.dictionaryList[0])
            composeState.dictionary.value = composeState.dictionaryList[0]
        }
    }

    // wordValue which selected and passed into app as intent
    private fun launchAddMode(wordValue: String, listId: Long, dictionaryId: Long) {
        composeState =
            composeState.copy(modifyMode = ModifyWordModes.MODE_ADD, word = wordValue)

        viewModelScope.launch {
            val activeDictionary =
                if (dictionaryId == -1L) getActiveDictionary.getDictionaryActive() else crudDictionaryUseCase.getDictionary(
                    dictionaryId = dictionaryId
                )
            when (activeDictionary) {
                is Either.Failure -> {
//                    setDefaultDictionary()
                }

                is Either.Success -> {
                    withContext(Dispatchers.Main) {
//                        composeState = composeState.copy(dictionary = activeDictionary.value)
                        composeState.dictionary.value = activeDictionary.value
                    }

//                    withContext(Dispatchers.IO) {
//                        val wordList =
//                            getListsUseCase.getAllListsForDictionaryForModifyWord(activeDictionary.value.id)
//                        withContext(Dispatchers.Main) {
//                            composeState = composeState.copy(wordLists = wordList)
//                        }
//                    }

                }
            }
        }

        if (listId != -1L) {
            prefillListIdFromArgs(listId)
        }
        setInitialState()
    }

    private fun launchEditMode(wordId: Long) {
        val id = if (wordId == -1L) error("wordId is null") else wordId // TODO it should do usecase
        getWordById(id)
    }

    private fun prefillListIdFromArgs(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = getListsUseCase.getListById(listId)

            withContext(Dispatchers.Main) {
                composeState = composeState.copy(selectedWordList = res)
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
            val selectedWordList =
                word.wordListId?.let { getListsUseCase.getListById(word.wordListId) }
//            val wordList =
//                getListsUseCase.getAllListsForDictionaryForModifyWord(word.dictionary.id)

            withContext(Dispatchers.Main) {
                word.sound?.let { sound -> recordAudio.prepareToOpen(sound.fileName) }
                composeState = composeState.copy(
                    word = word.value,
                    transcriptionWord = word.transcription,
                    descriptionWord = word.description,
                    soundFileName = word.sound?.fileName,
                    editableWordId = word.id,
                    createdAt = word.createdAt,
                    priorityValue = word.priority.toString(),
                    selectedWordList = selectedWordList,
//                    wordLists = wordList,
                    modifyMode = ModifyWordModes.MODE_EDIT,
                )
                composeState.dictionary.value = word.dictionary

                translateState = translateState.copy(translates = word.translates)
                hintState = hintState.copy(hints = word.hints)

                setInitialState()
            }
        }
    }


    // state for track changes and show alert on goBack press if has unsaved changes
    private fun setInitialState() {
        initialState = InitialState(
            composeState = composeState,
            translateState = translateState,
            hintState = hintState,
            recordAudio = recordAudio
        )
    }

    interface Listener {
        fun onDeleteWord()
        fun onSaveWord()
        fun goBack()
        fun toAddNewDictionary()
    }
}