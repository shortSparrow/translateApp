package com.ovolk.dictionary.presentation.dictionary_words

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.use_case.modify_dictionary.CrudDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.modify_dictionary.SetIsActiveDictionaryUseCase
import com.ovolk.dictionary.domain.use_case.word_list.GetSearchedWordListUseCase
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
    private val savedStateHandle: SavedStateHandle,
    private val crudDictionaryUseCase: CrudDictionaryUseCase,
    private val setIsActiveDictionaryUseCase: SetIsActiveDictionaryUseCase
) : ViewModel() {
    val dictionaryId = checkNotNull(savedStateHandle.get<Long>("dictionaryId"))
    var state by mutableStateOf(DictionaryWordsState())
        private set
    var listener: Listener? = null

    private var searchJob: Job? = null
    private val player = MediaPlayer()

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

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
                    when (crudDictionaryUseCase.deleteDictionaries(listOf(dictionaryId))) {
                        is Either.Failure -> {
                            // TODO add nice snack bar with error
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
                            // TODO show nice snackbar
                        }

                        is Either.Success -> {
                            // TODO show nice snackbar
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

            is DictionaryWordsAction.PlayAudio -> playAudio(
                onStartListener = action.onStartListener,
                word = action.word,
                onCompletionListener = action.onCompletionListener,
            )
        }
    }

    // todo maybe move it to compose
    private fun playAudio(
        onStartListener: () -> Unit,
        word: WordRV,
        onCompletionListener: () -> Unit,
    ) {
        if (player.isPlaying) return
        onStartListener()

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        )

        player.apply {
            try {
                reset()
                setDataSource(getAudioPath(application, word.sound!!.fileName))
                prepare()
                start()
            } catch (e: IOException) {
                Timber.e("prepare() failed $e")
            }
        }
        player.setOnCompletionListener {
            onCompletionListener()
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                oldVolume,
                0
            )
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