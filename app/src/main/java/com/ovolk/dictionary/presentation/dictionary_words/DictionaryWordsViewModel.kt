package com.ovolk.dictionary.presentation.dictionary_words

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.SetIsActiveDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.word_list.GetSearchedWordListUseCase
import com.ovolk.dictionary.domain.word_audio.WordListAudioPlayer
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarError
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarSuccess
import com.ovolk.dictionary.util.helpers.getAudioPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DictionaryWordsViewModel @Inject constructor(
    private val getSearchedWordListUseCase: GetSearchedWordListUseCase,
    private val application: Application,
    savedStateHandle: SavedStateHandle,
    private val crudDictionaryUseCase: CrudDictionaryUseCase,
    private val setIsActiveDictionaryUseCase: SetIsActiveDictionaryUseCase
) : ViewModel() {
    val dictionaryId = checkNotNull(savedStateHandle.get<Long>("dictionaryId"))
    var state by mutableStateOf(DictionaryWordsState())
        private set
    var listener: Listener? = null
    private val wordListAudioPlayer  =  WordListAudioPlayer()

    private var searchJob: Job? = null

    init {
        state = state.copy(loadingStatus = LoadingState.PENDING)
        viewModelScope.launch {
            searchDebounced("")
            state = state.copy(loadingStatus = LoadingState.SUCCESS)
        }
        viewModelScope.launch {
            crudDictionaryUseCase.getDictionaryFlow(dictionaryId).collectLatest { dictionary ->
                withContext(Dispatchers.Main) {
                    state = if (dictionary == null) {
                        state.copy(loadingStatus = LoadingState.FAILED)
                    } else {
                        state.copy(dictionary = dictionary)
                    }
                }
            }
        }
    }


    fun onAction(action: DictionaryWordsAction) {
        when (action) {
            is DictionaryWordsAction.HandleDeleteDictionaryModal -> {
                state = state.copy(isDeleteConfirmModalOpen = action.isShown)
            }

            DictionaryWordsAction.OnPressConfirmDelete -> {
                viewModelScope.launch {
                    when (val response =
                        crudDictionaryUseCase.deleteDictionaries(listOf(dictionaryId))) {
                        is Either.Failure -> {
                            GlobalSnackbarManger.showGlobalSnackbar(
                                duration = SnackbarDuration.Short,
                                data = SnackBarError(message = response.value.message),
                            )
                        }

                        is Either.Success -> {
                            listener?.goBack()
                        }
                    }
                }
            }

            DictionaryWordsAction.OnPressEditDictionary -> listener?.goToEditDictionary(dictionaryId)
            DictionaryWordsAction.OnPressTakeExam -> listener?.goToExam(dictionaryId)
            DictionaryWordsAction.MarkAsActive -> {
                viewModelScope.launch {
                    val response = setIsActiveDictionaryUseCase.setDictionaryActive(
                        dictionaryId = dictionaryId,
                        isActive = true
                    )

                    when (response) {
                        is Either.Failure -> {
                            if (response.value is FailureMessage) {
                                GlobalSnackbarManger.showGlobalSnackbar(
                                    duration = SnackbarDuration.Short,
                                    data = SnackBarError(message = response.value.message),
                                )
                            }
                        }

                        is Either.Success -> {
                            GlobalSnackbarManger.showGlobalSnackbar(
                                duration = SnackbarDuration.Short,
                                data = SnackBarSuccess(
                                    message = application.getString(
                                        R.string.dictionary_word_list_dictionary_marked_active,
                                        state.dictionary?.title
                                    )
                                ),
                            )
                        }
                    }
                }
            }

            DictionaryWordsAction.OnPressAddNewWord -> listener?.goToAddNewWord(dictionaryId)
            is DictionaryWordsAction.OnPressWordItem -> listener?.goToEditWord(action.wordId)
            is DictionaryWordsAction.OnSearchWord -> {
                state = state.copy(searchValue = action.value)
                searchDebounced(action.value)
            }

            is DictionaryWordsAction.PlayAudio -> {
                wordListAudioPlayer.playAudio(
                    onCompletionListener = action.onCompletionListener,
                    onStartListener = action.onStartListener,
                    word = action.word,
                )
            }
        }
    }


    private suspend fun searchWord(searchValue: String) {
        getSearchedWordListUseCase.getWords(searchValue, dictionaryId = dictionaryId)
            .collectLatest {
                val list = it.list
                    .run {
                        if (searchValue.isEmpty()) this else this.sortedWith(compareBy { l -> l.value.length })
                    }

                withContext(Dispatchers.Main) {
                    state = state.copy(
                        filteredWordList = list,
                        totalWordListSize = it.total,
                    )
                }
            }
    }

    private fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchWord(searchText)
        }
    }

    interface Listener {
        fun goToEditDictionary(dictionaryId: Long)
        fun goBack()
        fun goToAddNewWord(dictionaryId: Long)
        fun goToEditWord(wordId: Long)
        fun goToExam(dictionaryId: Long)
    }
}