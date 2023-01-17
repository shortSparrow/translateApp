package com.ovolk.dictionary.presentation.word_list

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
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.data.workers.HandleOldWordsPriority
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
class WordListViewModel @Inject constructor(
    private val getSearchedWordListUseCase: GetSearchedWordListUseCase,
    private val application: Application,
    savedStateHandle: SavedStateHandle,
    private val handleOldWordsPriority: HandleOldWordsPriority
) : ViewModel() {
    var listener: Listener? = null
    var state by mutableStateOf(WordListState())
        private set

    private var searchJob: Job? = null
    private val player = MediaPlayer()

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)


    // TODO implement infinity worker with 7 days delay for example
    init {
//        viewModelScope.launch {
//            handleOldWordsPriority.updatePriorityForBunchOldWords()
//        }
        val searchedWord: String? = savedStateHandle["searchedWord"]
        if (searchedWord != null) {
            state = state.copy(searchValue = searchedWord)
        }
        searchDebounced(searchedWord ?: "")
    }

    fun onAction(action: WordListAction) {
        when (action) {
            WordListAction.OnPressAddNewWord -> listener?.navigateToCreateNewWord()
            is WordListAction.OnPressWordItem -> listener?.navigateToExistingWord(action.wordId)
            is WordListAction.SearchWord -> {
                state = state.copy(searchValue = action.value)
                searchDebounced(action.value)
            }
            is WordListAction.PlayAudio -> playAudio(
                onStartListener = action.onStartListener,
                word = action.word,
                onCompletionListener = action.onCompletionListener,
            )
        }
    }

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
        getSearchedWordListUseCase(searchValue)
            .collectLatest {
                val list = it.list
                    .map { it.copy(translates = it.translates.filter { it.isHidden == false }) }
                    .run {
                        if (searchValue.isEmpty()) this else this.sortedWith(compareBy { l -> l.value.length })
                    }

                withContext(Dispatchers.Main) {
                    state = state.copy(
                        filteredWordList = list,
                        totalWordListSize = it.total,
                        isLoading = false
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
        fun navigateToCreateNewWord()
        fun navigateToExistingWord(wordId: Long)
    }

}