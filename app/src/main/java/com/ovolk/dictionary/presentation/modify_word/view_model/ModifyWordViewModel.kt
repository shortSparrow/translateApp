package com.ovolk.dictionary.presentation.modify_word.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.GetActiveDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_word.GetWordItemUseCase
import com.ovolk.dictionary.presentation.modify_word.InitialState
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction
import com.ovolk.dictionary.presentation.modify_word.ModifyWordHintsAction
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.modify_word.ModifyWordTranslatesAction
import com.ovolk.dictionary.presentation.modify_word.RecordAudioAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ModifyWordViewModel @Inject constructor(
    private val getWordItemUseCase: GetWordItemUseCase,
    private val getListsUseCase: GetListsUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val crudDictionaryUseCase: CrudDictionaryUseCase,
    private val getActiveDictionaryUseCase: GetActiveDictionaryUseCase,
    val wordGlobalPart: WordGlobalPart,
    val wordTranslatesPart: WordTranslatesPart,
    private val wordHintsPart: WordHintsPart,
    private val recordAudioPart: RecordAudioPart,
) : ViewModel() {

    var listener: Listener? = null

    val globalState = wordGlobalPart.globalState
    val translateState = wordTranslatesPart.translateState
    val hintState = wordHintsPart.hintState
    val recordState = recordAudioPart.recordState

    var initialState by mutableStateOf(InitialState())
        private set

    init {
        // pass WordViewModelPart, because this class provide viewModelScope and combine all states
        wordGlobalPart(modifyWordViewModel = this)
        recordAudioPart(viewModelScope = viewModelScope, globalState = globalState)
        launchRightMode()
    }


    fun onRecordAction(action: RecordAudioAction) = recordAudioPart.onAction(action)
    fun onComposeAction(action: ModifyWordAction) = wordGlobalPart.onAction(action)
    fun onHintAction(action: ModifyWordHintsAction) = wordHintsPart.onAction(action)
    fun onTranslateAction(action: ModifyWordTranslatesAction) =
        wordTranslatesPart.onAction(action)

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
                listId = listId, // when open fragment from word list screen
                dictionaryId = dictionaryId, // when open from list screen
            )
        }
    }

    // wordValue which selected and passed into app as intent
    private fun launchAddMode(wordValue: String, listId: Long, dictionaryId: Long) {

        globalState.update {
            it.copy(
                modifyMode = ModifyWordModes.MODE_ADD,
                word = wordValue
            )
        }

        viewModelScope.launch {
            val activeDictionary =
                if (dictionaryId == -1L) {
                    getActiveDictionaryUseCase.getDictionaryActive()
                } else {
                    crudDictionaryUseCase.getDictionary(dictionaryId = dictionaryId)
                }

            when (activeDictionary) {
                is Either.Failure -> {}
                is Either.Success -> {
                    withContext(Dispatchers.Main) {
                        globalState.value.dictionary.value = activeDictionary.value
                    }
                }
            }
        }

        if (listId != -1L) {
            prefillListIdFromArgs(listId)
        }
        setInitialState()
    }

    fun launchEditMode(wordId: Long) {
        val id =
            if (wordId == -1L) error("wordId is null") else wordId // TODO it should do usecase and return some error
        getWordById(id)
    }

    private fun prefillListIdFromArgs(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = getListsUseCase.getListById(listId)

            withContext(Dispatchers.Main) {
                globalState.update {
                    it.copy(selectedWordList = res)
                }
            }
        }
    }

    private fun getWordById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val word = getWordItemUseCase(id)
            val selectedWordList =
                word.wordListId?.let { getListsUseCase.getListById(word.wordListId) }

            withContext(Dispatchers.Main) {
                word.sound?.let { sound -> recordAudioPart.prepareToOpen(sound.fileName) }
                globalState.update {
                    it.copy(
                        word = word.value,
                        transcriptionWord = word.transcription,
                        descriptionWord = word.description,
                        soundFileName = word.sound?.fileName,
                        editableWordId = word.id,
                        createdAt = word.createdAt,
                        priorityValue = word.priority.toString(),
                        selectedWordList = selectedWordList,
                        modifyMode = ModifyWordModes.MODE_EDIT,
                    )
                }
                globalState.value.dictionary.value = word.dictionary
                translateState.update { it.copy(translates = word.translates) }
                hintState.update { it.copy(hints = word.hints) }

                setInitialState()
            }
        }
    }

    // state for track changes and show alert on goBack press if has unsaved changes
    private fun setInitialState() {
        initialState = InitialState(
            composeState = globalState.value,
            translateState = translateState.value,
            hintState = hintState.value,
            recordAudio = recordState.value
        )
    }

    interface Listener {
        fun onDeleteWord()
        fun onSaveWord()
        fun goBack()
        fun toAddNewDictionary()
    }
}